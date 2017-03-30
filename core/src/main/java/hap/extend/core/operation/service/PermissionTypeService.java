package hap.extend.core.operation.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hap.extend.core.operation.dto.PermissionType;

import java.util.List;

/**
 * Created by yyz on 2017/3/13.
 *
 * @author yazheng.yang@hand-china.com
 */
public interface PermissionTypeService extends IBaseService<PermissionType>,ProxySelf<PermissionTypeService> {
    public List<PermissionType> selectByPermissionType(IRequest request, PermissionType condition, int pageNum, int pageSize);
}
