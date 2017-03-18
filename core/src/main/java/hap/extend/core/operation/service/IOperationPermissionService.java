package hap.extend.core.operation.service;

import com.hand.hap.core.IRequest;
import hap.extend.core.operation.dto.PageNode;

import java.util.List;

/**
 * Created by yyz on 2017/3/8.
 *
 * @author yazheng.yang@hand-china.com
 */
public interface IOperationPermissionService {
    public String fetchApplyRules(String uriStr, IRequest requestContext);
    public List<PageNode> fetchAllPageNodes(IRequest request);
}
