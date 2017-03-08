package hap.extend.core.dataPermission.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.account.dto.Role;
import com.hand.hap.account.dto.User;
import com.hand.hap.account.mapper.RoleMapper;
import com.hand.hap.account.mapper.UserMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RuleUser> selectByRuleUser(IRequest request, RuleUser condition, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<RuleUser> ruleUsers = ruleUserMapper.select(condition);
        List<User> users = userMapper.selectAll();
        List<Role> roles = roleMapper.selectAll();
        ruleUsers.parallelStream().forEach(ruleUser -> {
            if(RuleUser.isUserType(ruleUser.getAssignType())){
                List<User> result = users.parallelStream().filter(user -> ruleUser.getTypeId().equals(user.getUserId())).collect(Collectors.toList());
                if(isNotNull(result) && !result.isEmpty()){
                    ruleUser.setTypeIdName(result.get(0).getUserName());
                }else {
                    ruleUser.setTypeIdName("");
                }
            }else if(RuleUser.isRoleType(ruleUser.getAssignType())){
                List<Role> result = roles.parallelStream().filter(role -> ruleUser.getTypeId().equals(role.getRoleId())).collect(Collectors.toList());
                if(isNotNull(result) && !result.isEmpty()){
                    ruleUser.setTypeIdName(result.get(0).getRoleName());
                }else {
                    ruleUser.setTypeIdName("");
                }
            }
        });
        return ruleUsers;
    }

    @Override
    public List<RuleUser> batchUpdate(IRequest request, List<RuleUser> list) {
        IBaseService<RuleUser> self = ((IBaseService<RuleUser>) AopContext.currentProxy());
        List<RuleUser> addList = new ArrayList<>();
        List<RuleUser> beforeUpdateList = new ArrayList<>();
        List<RuleUser> afterUpdateList = new ArrayList<>();
        List<RuleUser> deleteList = new ArrayList<>();
        for (RuleUser ruleUser : list) {
            switch (ruleUser.get__status()) {
                case DTOStatus.ADD:
                    self.insertSelective(request, ruleUser);
                    addList.add(ruleUser);
                    break;
                case DTOStatus.UPDATE:
                    RuleUser condition = new RuleUser();
                    condition.setAssignId(ruleUser.getAssignId());
                    List<RuleUser> ruleUsersInDb = self.select(request, condition, 1, 2);
                    if(isNotNull(ruleUsersInDb) && !ruleUsersInDb.isEmpty()){
                        beforeUpdateList.add(ruleUsersInDb.get(0));
                        afterUpdateList.add(ruleUser);
                        if (useSelectiveUpdate()) {
                            self.updateByPrimaryKeySelective(request, ruleUser);
                        } else {
                            self.updateByPrimaryKey(request, ruleUser);
                        }
                    }
                    break;
                case DTOStatus.DELETE:
                    deleteList.add(ruleUser);
                    self.deleteByPrimaryKey(ruleUser);
                    break;
                default:
                    break;
            }
        }
        //avoid transaction problem:fear rollback
        addList.forEach(ruleUser -> ruleUserCache.setValue(CacheUtils.getRuleUserKey(ruleUser.getRuleId().toString(),
                ruleUser.getAssignType(),
                ruleUser.getTypeId().toString()), Constant.VALUE_RULE_USER));
        beforeUpdateList.forEach(ruleUser -> ruleUserCache.remove(CacheUtils.getRuleUserKey(ruleUser.getRuleId().toString(),
                ruleUser.getAssignType(),
                ruleUser.getTypeId().toString())));
        afterUpdateList.forEach(ruleUser -> ruleUserCache.setValue(CacheUtils.getRuleUserKey(ruleUser.getRuleId().toString(),
                ruleUser.getAssignType(),
                ruleUser.getTypeId().toString()), Constant.VALUE_RULE_USER));
        deleteList.forEach(ruleUser -> ruleUserCache.remove(CacheUtils.getRuleUserKey(ruleUser.getRuleId().toString(),
                ruleUser.getAssignType(),
                ruleUser.getTypeId().toString())));

        return list;
    }
}
