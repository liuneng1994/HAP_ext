package hap.extend.core.operation.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.extend.core.operation.dto.Js;
import hap.extend.core.operation.mapper.JsMapper;
import hap.extend.core.operation.service.JsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yyz on 2017/3/13.
 *
 * @author yazheng.yang@hand-china.com
 */
@Service
public class JsServiceImpl extends BaseServiceImpl<Js> implements JsService {
    @Autowired
    private JsMapper jsMapper;

//    @Override
//    public List<Js> selectPageJsByRole(IRequest request, Long resourceId, Long roleId) {
//        return jsMapper.selectPageJsByRole(resourceId,roleId);
//    }
//
//    @Override
//    public List<Js> selectPageJsByUser(IRequest request, Long resourceId, Long userId) {
//        return jsMapper.selectPageJsByUser(resourceId,userId);
//    }
}
