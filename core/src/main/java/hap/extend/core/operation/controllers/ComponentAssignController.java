package hap.extend.core.operation.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hap.extend.core.operation.dto.ComponentAssign;
import hap.extend.core.operation.service.ComponentAssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yyz on 17/3/24.
 */
@Controller
@RequestMapping(value = "/hap_extend/operation_permission/cpn_assign/")
public class ComponentAssignController extends BaseController {
    @Autowired
    private ComponentAssignService componentAssignService;


    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(HttpServletRequest request,
                              @RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                              @RequestParam(name = "assignId", defaultValue = "") Long assignId,
                              @RequestParam(name = "description", defaultValue = "") String description) {
        IRequest requestContext = createRequestContext(request);
        ComponentAssign componentAssign = new ComponentAssign();
        componentAssign.setAssignId(assignId);
        componentAssign.setDescription(description);

        List<ComponentAssign> componentAssignList = componentAssignService.select(requestContext, componentAssign, page, pageSize);
        return new ResponseData(componentAssignList);
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData submit(@RequestBody List<ComponentAssign> componentAssignList, BindingResult result, HttpServletRequest request){

        List<ComponentAssign> componentAssigns = componentAssignService.batchUpdate(createRequestContext(request), componentAssignList);
        return new ResponseData(componentAssigns);
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData remove(@RequestBody List<ComponentAssign> componentAssignList, BindingResult result, HttpServletRequest request) throws Exception {

        List<ComponentAssign> componentAssigns = componentAssignService.batchUpdate(createRequestContext(request), componentAssignList);
        return new ResponseData(componentAssigns);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData update(@RequestBody List<ComponentAssign> componentAssignList, BindingResult result, HttpServletRequest request) throws Exception {
        List<ComponentAssign> componentAssigns = componentAssignService.batchUpdate(createRequestContext(request), componentAssignList);
        return new ResponseData(componentAssigns);
    }
}
