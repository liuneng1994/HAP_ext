package hap.extend.core.dataPermission.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hap.extend.core.dataPermission.dto.RuleMappermethod;
import hap.extend.core.dataPermission.service.IRuleMappermethodService;
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
@RequestMapping(value = "/hap_extend/data_permission/method_rule/")
public class RuleMappermethodController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(RuleMappermethodController.class);

    @Autowired
    private IRuleMappermethodService ruleMappermethodService;

    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData getMappermethodRules(HttpServletRequest request,
                                 @RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
                                 @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                 @RequestParam(name = "ruleId", defaultValue = "-1") Long ruleId,
                                 @RequestParam(name = "mapperMethod", defaultValue = "") String mapperMethod) {
        IRequest requestContext = createRequestContext(request);
        RuleMappermethod ruleMappermethod = new RuleMappermethod();
        ruleMappermethod.setRuleId(returnNullIfLessThanZero(ruleId));
        ruleMappermethod.setMapperMethod(returnNullIfEmpty(mapperMethod));

        List<RuleMappermethod> ruleUserList = ruleMappermethodService.selectByRuleMappermethod(requestContext, ruleMappermethod, page, pageSize);
        return new ResponseData(ruleUserList);
    }

    /**
     * 批量创建mapper方法-规则的映射
     * @param ruleUserList
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData createRules(@RequestBody List<RuleMappermethod> ruleUserList, BindingResult result, HttpServletRequest request){

        List<RuleMappermethod> ruleUsers = ruleMappermethodService.batchUpdate(createRequestContext(request), ruleUserList);
        return new ResponseData(ruleUsers);
    }

    /**
     * 批量删除mapper方法-规则的映射
     * @param ruleUserList
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData deleteRules(@RequestBody List<RuleMappermethod> ruleUserList, BindingResult result, HttpServletRequest request) throws Exception {

        List<RuleMappermethod> ruleUsers = ruleMappermethodService.batchUpdate(createRequestContext(request), ruleUserList);
        return new ResponseData(ruleUsers);
    }

    private String returnNullIfEmpty(String source){
        if(null == source || source.isEmpty()){
            return null;
        }
        return source;
    }

    private Long returnNullIfLessThanZero(Long value){
        return null == value ? null : (value < 0 ? null : value);
    }
}
