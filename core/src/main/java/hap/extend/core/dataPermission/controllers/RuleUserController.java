package hap.extend.core.dataPermission.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hap.extend.core.dataPermission.dto.RuleUser;
import hap.extend.core.dataPermission.service.IRuleUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yyz on 2017/2/22.
 *
 * @author yazheng.yang@hand-china.com
 */
@Controller
@RequestMapping(value = "/hap_extend/data_permission/rule_user/")
public class RuleUserController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(RuleUserController.class);

    @Autowired
    private IRuleUserService ruleUserService;

    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData getRuleUsers(HttpServletRequest request,
                                 @RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
                                 @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                 @RequestParam(name = "ruleId", defaultValue = "-1") Long ruleId,
                                 @RequestParam(name = "typeId", defaultValue = "-1") Long typeId,
                                 @RequestParam(name = "assignType", defaultValue = "") String assignType) {
        IRequest requestContext = createRequestContext(request);
        RuleUser ruleUser = new RuleUser();
        ruleUser.setTypeId(returnNullIfLessThanZero(typeId));
        ruleUser.setRuleId(returnNullIfLessThanZero(ruleId));
        ruleUser.setAssignType(returnNullIfEmpty(assignType));

        List<RuleUser> ruleUserList = ruleUserService.selectByRuleUser(requestContext, ruleUser, page, pageSize);
        return new ResponseData(ruleUserList);
    }

    /**
     * 批量创建规则-用户映射
     * @param ruleUserList
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData createRuleUsers(@RequestBody List<RuleUser> ruleUserList, BindingResult result, HttpServletRequest request){

        List<RuleUser> ruleUsers = ruleUserService.batchUpdate(createRequestContext(request), ruleUserList);
        return new ResponseData(ruleUsers);
    }

    /**
     * 批量删除规则-用户映射
     * @param ruleUserList
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData deleteRuleUsers(@RequestBody List<RuleUser> ruleUserList, BindingResult result, HttpServletRequest request) throws Exception {

        List<RuleUser> ruleUsers = ruleUserService.batchUpdate(createRequestContext(request), ruleUserList);
        return new ResponseData(ruleUsers);
    }

    /**
     * 批量更新规则-用户映射
     * @param ruleUserList
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData updateRuleUsers(@RequestBody List<RuleUser> ruleUserList, BindingResult result, HttpServletRequest request) throws Exception {
        List<RuleUser> ruleUsers = ruleUserService.batchUpdate(createRequestContext(request), ruleUserList);
        return new ResponseData(ruleUsers);
    }

    private Long returnNullIfLessThanZero(Long value){
        return null == value ? null : (value < 0 ? null : value);
    }

    private String returnNullIfEmpty(String source){
        if(null == source || source.isEmpty()){
            return null;
        }
        return source;
    }
}
