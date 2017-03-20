package hap.extend.core.operation.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.function.dto.Function;
import com.hand.hap.function.dto.FunctionResource;
import com.hand.hap.function.dto.Resource;
import com.hand.hap.function.mapper.FunctionMapper;
import com.hand.hap.function.mapper.FunctionResourceMapper;
import com.hand.hap.function.mapper.ResourceMapper;
import com.hand.hap.function.service.IResourceService;
import hap.extend.core.operation.dto.Js;
import hap.extend.core.operation.dto.JsAssign;
import hap.extend.core.operation.dto.PageNode;
import hap.extend.core.operation.dto.PermissionType;
import hap.extend.core.operation.mapper.JsAssignMapper;
import hap.extend.core.operation.mapper.JsMapper;
import hap.extend.core.operation.mapper.PageNodeMapper;
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

    @Autowired
    private PageNodeMapper pageNodeMapper;
    @Autowired
    private FunctionMapper functionMapper;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private FunctionResourceMapper functionResourceMapper;

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

    @Override
    public List<PageNode> fetchAllPageNodes(IRequest request) {
        List<Function> functions = functionMapper.selectAll();
        List<PageNode> pageNodes = new ArrayList<>();
        List<PageNode> topLevelNodes = new ArrayList<>();
//        functions.parallelStream().forEach(fun->{
        functions.forEach(fun->{
            PageNode pageNode = new PageNode();
            pageNode.setFunctionId(fun.getFunctionId());
            pageNode.setFunctionName(fun.getFunctionName());
            pageNode.setResourceId(fun.getResourceId());

            pageNode.setId(fun.getFunctionId());
            if(isNull(fun.getParentFunctionId())){//top level
                pageNode.setSequence(fun.getFunctionSequence());
                topLevelNodes.add(pageNode);
                pageNode.setParentId(null);
            }else {
                pageNodes.add(pageNode);
                pageNode.setParentId(fun.getParentFunctionId());
            }
            pageNode.setText(fun.getFunctionName());

            //init resource message
            if(isNotNull(fun.getResourceId())){
                //fill resource msg from DB
                Resource resource = resourceMapper.selectByPrimaryKey(fun.getResourceId());
                if(isNotNull(resource)){
                    pageNode.setUrl(resource.getUrl());
                    pageNode.setResourceName(resource.getName());
                }
            }
            //find if exist resources of this function
            FunctionResource functionResource = new FunctionResource();
            functionResource.setFunctionId(fun.getFunctionId());
            List<FunctionResource> functionResources = functionResourceMapper.select(functionResource);
            if(isNotNull(functionResources)){
                Long base = 100000*(isNull(pageNode.getParentId())? 0:pageNode.getParentId());
//                functionResources.parallelStream().forEach(rs->{
                functionResources.forEach(rs->{
                    //filter self
                    if(!rs.getResourceId().equals(fun.getResourceId())){
                        Resource resource_f = resourceMapper.selectByPrimaryKey(rs.getResourceId());
                        if(isNotNull(resource_f)){
                            PageNode child = new PageNode();
                            child.setResourceId(rs.getResourceId());
                            child.setParentId(pageNode.getId());
                            child.setId(base+rs.getResourceId());
                            child.setUrl(resource_f.getUrl());
                            child.setResourceName(resource_f.getName());
                            child.setText(resource_f.getName());
                            pageNodes.add(child);
                        }
                    }
                });
            }
        });
        List<PageNode> results = topLevelNodes.parallelStream().sorted((n1, n2) -> Long.compare(n1.getSequence(), n2.getSequence())).collect(Collectors.toList());
        results.addAll(pageNodes);
        return results;
    }
}
