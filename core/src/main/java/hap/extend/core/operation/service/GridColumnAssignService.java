package hap.extend.core.operation.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hap.extend.core.operation.dto.GridColumnAssign;

import java.util.List;

/**
 * Created by yyz on 17/4/1.
 */
public interface GridColumnAssignService  extends IBaseService<GridColumnAssign>,ProxySelf<GridColumnAssignService> {
    List<GridColumnAssign> selectByGridColumnAssign(IRequest iRequest, GridColumnAssign gridColumnAssign, int page, int pageSize);
}
