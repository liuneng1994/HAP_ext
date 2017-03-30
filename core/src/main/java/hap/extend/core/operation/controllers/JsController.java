package hap.extend.core.operation.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hap.extend.core.operation.dto.Js;
import hap.extend.core.operation.service.JsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yyz on 2017/3/13.
 *
 * @author yazheng.yang@hand-china.com
 */
@Controller
@RequestMapping(value = "/hap_extend/operation_permission/js/")
public class JsController extends BaseController {
    @Autowired
    private JsService jsService;

    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(HttpServletRequest request,
                                 @RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
                                 @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                 @RequestParam(name = "jsName", defaultValue = "") String jsName,
                                 @RequestParam(name = "resourceId", defaultValue = "-1") Long resourceId,
                                 @RequestParam(name = "description", defaultValue = "") String description) {
        IRequest requestContext = createRequestContext(request);
        Js js = new Js();
        js.setJsName(jsName);
        js.setResourceId(resourceId);
        js.setDescription(description);

        List<Js> ruleList = jsService.select(requestContext, js, page, pageSize);
        return new ResponseData(ruleList);
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData submit(@RequestBody List<Js> ruleList, BindingResult result, HttpServletRequest request){

        List<Js> rules = jsService.batchUpdate(createRequestContext(request), ruleList);
        return new ResponseData(rules);
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData remove(@RequestBody List<Js> ruleList, BindingResult result, HttpServletRequest request) throws Exception {

        List<Js> rules = jsService.batchUpdate(createRequestContext(request), ruleList);
        return new ResponseData(rules);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData update(@RequestBody List<Js> ruleList, BindingResult result, HttpServletRequest request) throws Exception {
        List<Js> rules = jsService.batchUpdate(createRequestContext(request), ruleList);
        return new ResponseData(rules);
    }
}
