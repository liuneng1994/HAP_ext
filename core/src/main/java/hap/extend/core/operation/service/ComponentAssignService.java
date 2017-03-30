package hap.extend.core.operation.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hap.extend.core.operation.dto.ComponentAssign;

import java.util.List;

/**
 * Created by yyz on 17/3/24.
 */
public interface ComponentAssignService  extends IBaseService<ComponentAssign>,ProxySelf<ComponentAssignService> {
    List<ComponentAssign> selectByComponentAssign(IRequest requestContext, ComponentAssign componentAssign, int page, int pageSize);

}
