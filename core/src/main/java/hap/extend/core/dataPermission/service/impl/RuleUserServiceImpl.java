package hap.extend.core.dataPermission.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.extend.core.dataPermission.cache.impl.DataPermissionRuleUserCache;
import hap.extend.core.dataPermission.dto.RuleUser;
import hap.extend.core.dataPermission.mapper.RuleUserMapper;
import hap.extend.core.dataPermission.service.IRuleUserService;
import hap.extend.core.dataPermission.utils.CacheUtils;
import hap.extend.core.dataPermission.utils.Constant;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static hap.extend.core.dataPermission.utils.LangUtils.isNotNull;

import java.util.List;

/**
 * Created by yyz on 2017/2/22.
 *
 * @author yazheng.yang@hand-china.com
 */
@Service
public class RuleUserServiceImpl extends BaseServiceImpl<RuleUser> implements IRuleUserService {
    @Autowired
    private DataPermissionRuleUserCache ruleUserCache;
    @Autowired
    private RuleUserMapper ruleUserMapper;

    @Override
    public List<RuleUser> selectByRuleUser(IRequest request, RuleUser condition, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        return ruleUserMapper.selectByRuleUser(condition);
    }

    @Override
    public List<RuleUser> batchUpdate(IRequest request, List<RuleUser> list) {
        IBaseService<RuleUser> self = ((IBaseService<RuleUser>) AopContext.currentProxy());
        for (RuleUser ruleUser : list) {
            switch (ruleUser.get__status()) {
                case DTOStatus.ADD:
                    self.insertSelective(request, ruleUser);
                    break;
                case DTOStatus.UPDATE:
                    if (useSelectiveUpdate()) {
                        self.updateByPrimaryKeySelective(request, ruleUser);
                    } else {
                        self.updateByPrimaryKey(request, ruleUser);
                    }
                    break;
                case DTOStatus.DELETE:
                    self.deleteByPrimaryKey(ruleUser);
                    break;
                default:
                    break;
            }
        }
        //avoid transaction problem:fear rollback
        for (RuleUser ruleUser : list) {
            switch (ruleUser.get__status()) {
                case DTOStatus.ADD:
                    ruleUserCache.setValue(CacheUtils.getRuleUserKey(ruleUser.getRuleId().toString(),
                            ruleUser.getUserId().toString(),
                            RuleUser.isInclude(ruleUser.getIsInclude())), Constant.VALUE_RULE_USER);
                    break;
                case DTOStatus.UPDATE:
                    RuleUser condition = new RuleUser();
                    condition.setMapperId(ruleUser.getMapperId());
                    List<RuleUser> ruleUsersInDb = self.select(request, condition, 1, 2);
                    if(isNotNull(ruleUsersInDb) && !ruleUsersInDb.isEmpty()){
                        ruleUserCache.remove(CacheUtils.getRuleUserKey(ruleUsersInDb.get(0).getRuleId().toString(),
                                ruleUsersInDb.get(0).getUserId().toString(),
                                RuleUser.isInclude(ruleUsersInDb.get(0).getIsInclude())));
                        ruleUserCache.setValue(CacheUtils.getRuleUserKey(ruleUser.getRuleId().toString(),
                                ruleUser.getUserId().toString(),
                                RuleUser.isInclude(ruleUser.getIsInclude())), Constant.VALUE_RULE_USER);
                    }
                    break;
                case DTOStatus.DELETE:
                    ruleUserCache.remove(CacheUtils.getRuleUserKey(ruleUser.getRuleId().toString(),
                            ruleUser.getUserId().toString(),
                            RuleUser.isInclude(ruleUser.getIsInclude())));
                    break;
                default:
                    break;
            }
        }
        return list;
    }
}
