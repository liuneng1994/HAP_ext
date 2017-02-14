package HapSwitchUser.core.switchUser.controllers;

import com.hand.hap.account.dto.User;
import com.hand.hap.account.service.IRole;
import com.hand.hap.account.service.IRoleService;
import com.hand.hap.account.service.IUserService;
import com.hand.hap.activiti.service.IActivitiEntityService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by yyz on 2017/2/8.
 *
 * @author yazheng.yang@hand-china.com
 */
@Controller
public class SwitchUserController extends BaseController{
    public static final String FIELD_SOURCE_USER_ID="switch_user_userId";
    public static final String FIELD_SOURCE_USER_NAME="switch_user_userName";
    public static final String FIELD_USER_NAME="userName";

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IActivitiEntityService activitiEntityService;
    @Autowired
    private IUserService userService;


    @RequestMapping(value = "/switch_user/switch_to", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData switchTargetUser(final HttpServletRequest request,
                                         final HttpServletResponse response,
                                         @RequestParam(name = "targetUserId", defaultValue = "-1") Long targetUserId) {
        Assert.isTrue(targetUserId>0,"illegal userId");
        IRequest requestContext = createRequestContext(request);
        HttpSession session = request.getSession();
        Long oldUserId = (Long) session.getAttribute(IRequest.FIELD_USER_ID);
        User oldUser = fetchUserById(oldUserId,requestContext);
        String oldUserName = oldUser.getUserName();
        User newUser = fetchUserById(targetUserId,requestContext);
        switchToTarget(targetUserId,newUser.getUserName(), session,requestContext);
        //set source user to session
        session.setAttribute(FIELD_SOURCE_USER_ID,oldUserId);
        session.setAttribute(FIELD_SOURCE_USER_NAME,oldUserName);
        //redirect to home page
        return new ResponseData(true);
    }

    @RequestMapping(value = "/switch_user/reset", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData switchToSource(final HttpServletRequest request,
                                  final HttpServletResponse response) {

        IRequest requestContext = createRequestContext(request);
        HttpSession session = request.getSession();
        Long oldUserId = (Long) session.getAttribute(FIELD_SOURCE_USER_ID);
        String oldUserName = (String) session.getAttribute(FIELD_SOURCE_USER_NAME);
        Assert.isTrue(oldUserId>0,"illegal userId");
        Assert.notNull(oldUserName);
        switchToTarget(oldUserId,oldUserName, session,requestContext);

        //remove source user from session
        session.removeAttribute(FIELD_SOURCE_USER_ID);
        session.removeAttribute(FIELD_SOURCE_USER_NAME);
        //redirect to home page
        return new ResponseData(true);
    }


    private void switchToTarget(Long userId, String userName, HttpSession session, IRequest requestContext){
        Assert.notNull(userId);
        Assert.notNull(session);

        //fetch roleId of new user
        User user = new User();
        user.setUserId(userId);
        List<IRole> roleList = roleService.selectRolesByUser(requestContext,user);
        Assert.notNull(roleList,"illegal userId");
        Assert.notEmpty(roleList,"illegal userId");
        final int size = roleList.size();
        Long[] userIds = new Long[size];
        int index = 0;
        for(IRole role : roleList){
            userIds[index++] = role.getRoleId();
        }
        //fetch employee code of new user
        UserEntity employee = activitiEntityService.getEmployee(userName);
        Assert.notNull(employee);
        session.setAttribute(IRequest.FIELD_USER_ID,new Long(userId));
        session.setAttribute(IRequest.FIELD_ROLE_ID,userIds[0]);
        session.setAttribute(IRequest.FIELD_ALL_ROLE_ID,userIds);
        session.setAttribute(IRequest.EMP_CODE,employee.getId());
        session.setAttribute(FIELD_USER_NAME,userName);
    }

    private User fetchUserById(Long userId, IRequest requestContext){
        User user = new User();
        user.setUserId(userId);
        List<User> usersInDb = userService.select(requestContext, user, 1, 10);
        Assert.notNull(usersInDb);
        Assert.notEmpty(usersInDb);
        return usersInDb.get(0);
    }
}