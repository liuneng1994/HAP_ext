package hap.extend.core.operation.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.function.dto.Resource;
import com.hand.hap.function.service.IResourceService;
import hap.extend.core.operation.dto.Js;
import hap.extend.core.operation.dto.JsAssign;
import hap.extend.core.operation.dto.PermissionType;
import hap.extend.core.operation.mapper.JsAssignMapper;
import hap.extend.core.operation.mapper.JsMapper;
import hap.extend.core.operation.mapper.PermissionTypeMapper;
import hap.extend.core.operation.service.IOperationPermissionService;

import static hap.extend.core.operation.utils.LangUtil.isNotNull;
import static hap.extend.core.operation.utils.LangUtil.isNull;

import hap.extend.core.operation.utils.OPConstUtil;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by yyz on 2017/3/8.
 *
 * @author yazheng.yang@hand-china.com
 */
@Service
public class OperationPermissionServiceImpl implements IOperationPermissionService {
    @Autowired
    private IResourceService resourceService;
    @Autowired
    private PermissionTypeMapper permissionTypeMapper;
    @Autowired
    private JsAssignMapper jsAssignMapper;
    @Autowired
    private JsMapper jsMapper;

    @Override
    public String fetchApplyRules(String uriStr, IRequest requestContext) {
        Resource resource = resourceService.selectResourceByUrl(uriStr);
        if(isNull(resource) || isNull(resource.getResourceId())){
            return "";
        }

        List<Pair<String,Long>> typeList = new ArrayList<>();
        typeList.add(new Pair<String, Long>(OPConstUtil.VALUE_GLOBAL_TYPE,null));
        typeList.add(new Pair<>(OPConstUtil.VALUE_ROLE_TYPE,requestContext.getRoleId()));
        typeList.add(new Pair<>(OPConstUtil.VALUE_USER_TYPE,requestContext.getUserId()));

        //collect assign ids
        Set<Long> assignIds = new HashSet<>();
        for(Pair<String,Long> pair : typeList){
            PermissionType pt = new PermissionType();
            pt.setResourceId(resource.getResourceId());
            pt.setAssignType(pair.getKey());
            pt.setAssignValue(pair.getValue());
            pt.setEnableFlag(OPConstUtil.VALUE_YES);
            List<PermissionType> pts = permissionTypeMapper.select(pt);
            if(isNull(pts) || pts.isEmpty()){
                continue;
            }
            assignIds.addAll(pts.stream().map(type -> type.getAssignId()).collect(Collectors.toSet()));
        }
        Set<Long> jsIds = new HashSet<>();
        for(Long assignId : assignIds){
            JsAssign jsAssign = new JsAssign();
            jsAssign.setAssignId(assignId);
            List<JsAssign> assigns = jsAssignMapper.select(jsAssign);
            jsIds.addAll(assigns.stream().map(assign->assign.getJsId()).collect(Collectors.toSet()));
        }

        List<Js> results = new ArrayList<>();
        jsIds.forEach(jsId->{
            Js js = new Js();
            js.setJsId(jsId);
            js.setEnableFlag(OPConstUtil.VALUE_YES);
            List<Js> jses = jsMapper.select(js);
            if(isNotNull(jses) && !jses.isEmpty()){
                results.add(jses.get(0));
            }
        });
        //TODO 判断是否需要应用组件规则（这部分对应于页面中不用编写js代码即可进行控制，目前暂不实现）

        //构建 final js
        StringBuilder sb = new StringBuilder();
        sb.append("function HAP_EXT_OPM_appliedOP(){\n");
        results.forEach(js -> {
            sb.append("//applied:"+js.getJsName()+"\n");
            sb.append(js.getJsScript()+"\n");
        });
        sb.append("\n}\n HAP_EXT_OPM_appliedOP();");
        return sb.toString();
    }
}
