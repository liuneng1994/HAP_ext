package hap.extend.core.operation.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hap.extend.core.operation.dto.PermissionType;
import hap.extend.core.operation.service.PermissionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static hap.extend.core.operation.utils.LangUtil.returnNullIfStrEmpty;

/**
 * Created by yyz on 2017/3/13.
 *
 * @author yazheng.yang@hand-china.com
 */
@Controller
@RequestMapping(value = "/hap_extend/operation_permission/permission_type/")
public class PermissionTypeController extends BaseController {
    @Autowired
    private PermissionTypeService permissionTypeService;

    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(HttpServletRequest request,
                              @RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                              @RequestParam(name = "assignType", defaultValue = "") String assignType,
                              @RequestParam(name = "resourceId", defaultValue = "-1") Long resourceId,
                              @RequestParam(name = "description", defaultValue = "") String description) {
        IRequest requestContext = createRequestContext(request);
        PermissionType permissionType = new PermissionType();
        permissionType.setResourceId(resourceId);
        permissionType.setDescription(returnNullIfStrEmpty(description));
        permissionType.setAssignType(returnNullIfStrEmpty(assignType));

        List<PermissionType> permissionTypeList = permissionTypeService.selectByPermissionType(requestContext, permissionType, page, pageSize);
        return new ResponseData(permissionTypeList);
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData submit(@RequestBody List<PermissionType> permissionTypeList, BindingResult result, HttpServletRequest request){

        List<PermissionType> rules = permissionTypeService.batchUpdate(createRequestContext(request), permissionTypeList);
        return new ResponseData(rules);
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData remove(@RequestBody List<PermissionType> permissionTypeList, BindingResult result, HttpServletRequest request) throws Exception {

        List<PermissionType> rules = permissionTypeService.batchUpdate(createRequestContext(request), permissionTypeList);
        return new ResponseData(rules);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData update(@RequestBody List<PermissionType> permissionTypeList, BindingResult result, HttpServletRequest request) throws Exception {
        List<PermissionType> rules = permissionTypeService.batchUpdate(createRequestContext(request), permissionTypeList);
        return new ResponseData(rules);
    }
}
