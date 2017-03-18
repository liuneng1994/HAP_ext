package hap.extend.core.operation.service;

import com.hand.hap.core.IRequest;

/**
 * Created by yyz on 2017/3/8.
 *
 * @author yazheng.yang@hand-china.com
 */
public interface IOperationPermissionService {
    public String fetchApplyRules(String uriStr, IRequest requestContext);
}
