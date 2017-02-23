package hap.extend.core.dataPermission.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hap.extend.core.dataPermission.dto.RuleUser;

import java.util.List;

/**
 * Created by yyz on 2017/2/22.
 *
 * @author yazheng.yang@hand-china.com
 */
public interface IRuleUserService extends IBaseService<RuleUser>, ProxySelf<IRuleUserService> {
    List<RuleUser> selectByRuleUser(IRequest iRequest, RuleUser ruleUser, int i, int i1);
}
