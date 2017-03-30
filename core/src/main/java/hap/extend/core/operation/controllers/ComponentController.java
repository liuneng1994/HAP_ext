package hap.extend.core.operation.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hap.extend.core.operation.dto.Component;
import hap.extend.core.operation.service.ComponentService;
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
@RequestMapping(value = "/hap_extend/operation_permission/cpn/")
public class ComponentController extends BaseController {
    @Autowired
    private ComponentService componentService;


    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(HttpServletRequest request,
                              @RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                              @RequestParam(name = "componentName", defaultValue = "") String componentName,
                              @RequestParam(name = "resourceId", defaultValue = "-1") Long resourceId,
                              @RequestParam(name = "description", defaultValue = "") String description) {
        IRequest requestContext = createRequestContext(request);
        Component component = new Component();
        component.setComponentName(componentName);
        component.setResourceId(resourceId);
        component.setDescription(description);

        List<Component> componentList = componentService.select(requestContext, component, page, pageSize);
        return new ResponseData(componentList);
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData submit(@RequestBody List<Component> componentList, BindingResult result, HttpServletRequest request){

        List<Component> components = componentService.batchUpdate(createRequestContext(request), componentList);
        return new ResponseData(components);
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData remove(@RequestBody List<Component> componentList, BindingResult result, HttpServletRequest request) throws Exception {

        List<Component> components = componentService.batchUpdate(createRequestContext(request), componentList);
        return new ResponseData(components);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData update(@RequestBody List<Component> componentList, BindingResult result, HttpServletRequest request) throws Exception {
        List<Component> components = componentService.batchUpdate(createRequestContext(request), componentList);
        return new ResponseData(components);
    }
}
