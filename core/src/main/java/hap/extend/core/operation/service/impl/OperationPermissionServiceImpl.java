package hap.extend.core.operation.service.impl;

import com.hand.hap.core.IRequest;
import hap.extend.core.operation.service.IOperationPermissionService;
import org.springframework.stereotype.Service;

/**
 * Created by yyz on 2017/3/8.
 *
 * @author yazheng.yang@hand-china.com
 */
@Service
public class OperationPermissionServiceImpl implements IOperationPermissionService {
    @Override
    public String fetchApplyRules(String filePath, IRequest requestContext) {
        //TODO 解析页面，抽取出页面中的所有组件以及每个组件对应的id集合
        //TODO 从数据库中获取这些组件的<组件，js，优先级>，根据用户id、角色id、组件名称查询
        //TODO 构建 final js
        return null;
    }
}
