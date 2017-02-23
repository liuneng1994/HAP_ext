package hap.extend.core.dataPermission.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.extend.core.dataPermission.cache.impl.DataPermissionRuleCache;
import hap.extend.core.dataPermission.dto.Rule;
import hap.extend.core.dataPermission.service.IRuleService;
import hap.extend.core.dataPermission.utils.CacheUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yyz on 2017/2/22.
 *
 * @author yazheng.yang@hand-china.com
 */
@Service
public class RuleServiceImpl extends BaseServiceImpl<Rule> implements IRuleService {
    @Autowired
    private DataPermissionRuleCache ruleCache;

    @Override
    public List<Rule> batchUpdate(IRequest request, List<Rule> list) {
        IBaseService<Rule> self = ((IBaseService<Rule>) AopContext.currentProxy());
        for (Rule rule : list) {
            switch (rule.get__status()) {
                case DTOStatus.ADD:
                    self.insertSelective(request, rule);
                    break;
                case DTOStatus.UPDATE:
                    if (useSelectiveUpdate()) {
                        self.updateByPrimaryKeySelective(request, rule);
                    } else {
                        self.updateByPrimaryKey(request, rule);
                    }
                    break;
                case DTOStatus.DELETE:
                    self.deleteByPrimaryKey(rule);
                    break;
                default:
                    break;
            }
        }
        //avoid transaction problem:fear rollback
        for (Rule rule : list) {
            switch (rule.get__status()) {
                case DTOStatus.ADD:
                case DTOStatus.UPDATE:
                    ruleCache.setValue(CacheUtils.getRuleKey(rule.getRuleId().toString()),rule.getRuleSql());
                    break;
                case DTOStatus.DELETE:
                    ruleCache.remove(CacheUtils.getRuleKey(rule.getRuleId().toString()));
                    break;
                default:
                    break;
            }
        }
        return list;
    }
}
