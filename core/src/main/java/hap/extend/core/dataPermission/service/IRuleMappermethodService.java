package hap.extend.core.dataPermission.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hap.extend.core.dataPermission.dto.RuleMappermethod;

import java.util.List;

/**
 * Created by yyz on 2017/2/22.
 *
 * @author yazheng.yang@hand-china.com
 */
public interface IRuleMappermethodService  extends IBaseService<RuleMappermethod>, ProxySelf<IRuleService> {
    List<RuleMappermethod> selectByRuleMappermethod(IRequest iRequest, RuleMappermethod ruleMappermethod, int pageNum, int pageSize);
}
