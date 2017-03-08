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

import java.util.ArrayList;
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
        List<Rule> deleteList = new ArrayList<>();
        List<Rule> addList = new ArrayList<>();
        for (Rule rule : list) {
            switch (rule.get__status()) {
                case DTOStatus.ADD:
                    self.insertSelective(request, rule);
                    addList.add(rule);
                    break;
                case DTOStatus.UPDATE:
                    deleteList.add(rule);
                    addList.add(rule);
                    if (useSelectiveUpdate()) {
                        self.updateByPrimaryKeySelective(request, rule);
                    } else {
                        self.updateByPrimaryKey(request, rule);
                    }
                    break;
                case DTOStatus.DELETE:
                    deleteList.add(rule);
                    self.deleteByPrimaryKey(rule);
                    break;
                default:
                    break;
            }
        }
        //avoid transaction problem:fear rollback
        deleteList.forEach(rule -> {
            ruleCache.remove(CacheUtils.getRuleKey(rule.getRuleId().toString(),true));
            ruleCache.remove(CacheUtils.getRuleKey(rule.getRuleId().toString(),false));
        });
        addList.forEach(rule -> ruleCache.setValue(CacheUtils.getRuleKey(rule.getRuleId().toString(),Rule.isIncludeType(rule.getIsIncludeType())),rule.getRuleSql()));

        return list;
    }
}
