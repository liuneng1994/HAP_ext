package hap.extend.core.operation.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.extend.core.operation.dto.Js;
import hap.extend.core.operation.dto.JsAssign;
import hap.extend.core.operation.mapper.JsAssignMapper;
import hap.extend.core.operation.mapper.JsMapper;
import hap.extend.core.operation.service.JsAssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yyz on 2017/3/13.
 *
 * @author yazheng.yang@hand-china.com
 */
@Service
public class JsAssignServiceImpl extends BaseServiceImpl<JsAssign> implements JsAssignService {
    @Autowired
    private JsAssignMapper jsAssignMapper;
    @Autowired
    private JsMapper jsMapper;

    @Override
    public List<JsAssign> selectByJsAssign(IRequest requestContext, JsAssign jsAssign, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<JsAssign> jsAssigns = jsAssignMapper.select(jsAssign);
        jsAssigns.parallelStream().forEach(assign->{
            Js js = new Js();
            js.setJsId(assign.getJsId());
            List<Js> jses = jsMapper.select(js);
            if(jses==null || jses.isEmpty()){
                assign.setJsName("");
            }else {
                assign.setJsName(jses.get(0).getJsName());
            }
        });
        return jsAssigns;
    }
}
