package hap.extend.core.operation.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hap.extend.core.operation.dto.JsAssign;

import java.util.List;

/**
 * Created by yyz on 2017/3/13.
 *
 * @author yazheng.yang@hand-china.com
 */
public interface JsAssignService extends IBaseService<JsAssign>,ProxySelf<JsAssignService> {
    public List<JsAssign> selectByJsAssign(IRequest requestContext, JsAssign jsAssign, int page, int pageSize);
}
