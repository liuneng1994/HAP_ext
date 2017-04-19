package hap.extend.core.dataPermission.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hap.extend.core.dataPermission.cache.impl.DataPermissionRuleCache;
import hap.extend.core.dataPermission.dto.Rule;
import hap.extend.core.dataPermission.service.IRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import static hap.extend.core.dataPermission.utils.LangUtils.isNull;
import java.util.List;

/**
 * Created by yyz on 2017/2/22.
 *
 * @author yazheng.yang@hand-china.com
 */
@Controller
@RequestMapping(value = "/hap_extend/data_permission/rule/")
public class RuleController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(RuleController.class);

    @Autowired
    private IRuleService ruleService;

    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData getRules(HttpServletRequest request,
                                  @RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                  @RequestParam(name = "ruleName", defaultValue = "") String ruleName,
                                  @RequestParam(name = "description", defaultValue = "") String description) {
        IRequest requestContext = createRequestContext(request);
        Rule rule = new Rule();
        rule.setRuleName(returnNullIfEmpty(ruleName));
        rule.setDescription(returnNullIfEmpty(description));

        List<Rule> ruleList = ruleService.select(requestContext, rule, page, pageSize);
        return new ResponseData(ruleList);
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData createRules(@RequestBody List<Rule> ruleList, BindingResult result, HttpServletRequest request){

        List<Rule> rules = ruleService.batchUpdateCacheAndDb(createRequestContext(request), ruleList);
        return new ResponseData(rules);
    }

    /**
     * 批量删除规则
     * @param ruleList
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData deleteRules(@RequestBody List<Rule> ruleList, BindingResult result, HttpServletRequest request) throws Exception {

        List<Rule> rules = ruleService.batchUpdateCacheAndDb(createRequestContext(request), ruleList);
        return new ResponseData(rules);
    }

    /**
     * 批量更新规则
     * @param ruleList
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData updateRules(@RequestBody List<Rule> ruleList, BindingResult result, HttpServletRequest request) throws Exception {
        List<Rule> rules = ruleService.batchUpdateCacheAndDb(createRequestContext(request), ruleList);
        return new ResponseData(rules);
    }

    private String returnNullIfEmpty(String source){
        if(null == source || source.isEmpty()){
            return null;
        }
        return source;
    }
}
