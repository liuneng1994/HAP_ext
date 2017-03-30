package hap.extend.core.operation.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hap.extend.core.operation.dto.JsAssign;
import hap.extend.core.operation.service.JsAssignService;
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
@RequestMapping(value = "/hap_extend/operation_permission/js_assign/")
public class JsAssignController extends BaseController {
    @Autowired
    private JsAssignService jsAssignService;

    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(HttpServletRequest request,
                              @RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                              @RequestParam(name = "assignId", defaultValue = "") Long assignId,
                              @RequestParam(name = "description", defaultValue = "") String description) {
        IRequest requestContext = createRequestContext(request);
        JsAssign jsAssign = new JsAssign();
        jsAssign.setAssignId(assignId);
        jsAssign.setDescription(description);

        List<JsAssign> assignList = jsAssignService.selectByJsAssign(requestContext, jsAssign, page, pageSize);
        return new ResponseData(assignList);
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData submit(@RequestBody List<JsAssign> assignList, BindingResult result, HttpServletRequest request){

        List<JsAssign> assigns = jsAssignService.batchUpdate(createRequestContext(request), assignList);
        return new ResponseData(assigns);
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData remove(@RequestBody List<JsAssign> assignList, BindingResult result, HttpServletRequest request) throws Exception {

        List<JsAssign> assigns = jsAssignService.batchUpdate(createRequestContext(request), assignList);
        return new ResponseData(assigns);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData update(@RequestBody List<JsAssign> assignList, BindingResult result, HttpServletRequest request) throws Exception {
        List<JsAssign> assigns = jsAssignService.batchUpdate(createRequestContext(request), assignList);
        return new ResponseData(assigns);
    }
}
