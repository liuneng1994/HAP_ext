package hap.extend.core.operation.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.account.dto.Role;
import com.hand.hap.account.dto.User;
import com.hand.hap.account.mapper.RoleMapper;
import com.hand.hap.account.mapper.UserMapper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.extend.core.operation.dto.PermissionType;
import hap.extend.core.operation.mapper.PermissionTypeMapper;
import hap.extend.core.operation.service.PermissionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static hap.extend.core.dataPermission.utils.LangUtils.isNotNull;

/**
 * Created by yyz on 2017/3/13.
 *
 * @author yazheng.yang@hand-china.com
 */
@Service
public class PermissionTypeServiceImpl extends BaseServiceImpl<PermissionType> implements PermissionTypeService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionTypeMapper permissionTypeMapper;

    @Override
    public List<PermissionType> selectByPermissionType(IRequest request, PermissionType condition, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PermissionType> types = permissionTypeMapper.select(condition);
        List<User> users = userMapper.selectAll();
        List<Role> roles = roleMapper.selectAll();
        types.parallelStream().forEach(type -> {
            if(PermissionType.isUserType(type.getAssignType())){
                List<User> result = users.parallelStream().filter(user -> type.getAssignValue().equals(user.getUserId())).collect(Collectors.toList());
                if(isNotNull(result) && !result.isEmpty()){
                    type.setAssignValueName(result.get(0).getUserName());
                }else {
                    type.setAssignValueName("");
                }
            }else if(PermissionType.isRoleType(type.getAssignType())){
                List<Role> result = roles.parallelStream().filter(role -> type.getAssignValue().equals(role.getRoleId())).collect(Collectors.toList());
                if(isNotNull(result) && !result.isEmpty()){
                    type.setAssignValueName(result.get(0).getRoleName());
                }else {
                    type.setAssignValueName("");
                }
            }
        });
        return types;
    }
}
