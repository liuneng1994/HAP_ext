package hap.extend.core.operation.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hap.extend.core.operation.dto.GridColumnAssign;
import hap.extend.core.operation.service.GridColumnAssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static hap.extend.core.operation.utils.LangUtil.returnNullIfStrEmpty;

/**
 * Created by yyz on 17/4/1.
 */
@Controller
@RequestMapping(value = "/hap_extend/operation_permission/grid_column_assign/")
public class GridColumnAssignController extends BaseController {
    @Autowired
    private GridColumnAssignService gridColumnAssignService;



    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(HttpServletRequest request,
                              @RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                              @RequestParam(name = "columnName", defaultValue = "") String columnName,
                              @RequestParam(name = "cpnAssignId", defaultValue = "-1") Long cpnAssignId) {
        IRequest requestContext = createRequestContext(request);
        GridColumnAssign gridColumnAssign = new GridColumnAssign();
        gridColumnAssign.setColumnName(returnNullIfStrEmpty(columnName));
        gridColumnAssign.setCpnAssignId(cpnAssignId);

        List<GridColumnAssign> gridColumnAssignList = gridColumnAssignService.select(requestContext, gridColumnAssign, page, pageSize);
        return new ResponseData(gridColumnAssignList);
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData submit(@RequestBody List<GridColumnAssign> gridColumnAssignList, BindingResult result, HttpServletRequest request){

        List<GridColumnAssign> gridColumnAssigns = gridColumnAssignService.batchUpdate(createRequestContext(request), gridColumnAssignList);
        return new ResponseData(gridColumnAssigns);
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData remove(@RequestBody List<GridColumnAssign> gridColumnAssignList, BindingResult result, HttpServletRequest request) throws Exception {

        List<GridColumnAssign> gridColumnAssigns = gridColumnAssignService.batchUpdate(createRequestContext(request), gridColumnAssignList);
        return new ResponseData(gridColumnAssigns);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData update(@RequestBody List<GridColumnAssign> gridColumnAssignList, BindingResult result, HttpServletRequest request) throws Exception {
        List<GridColumnAssign> gridColumnAssigns = gridColumnAssignService.batchUpdate(createRequestContext(request), gridColumnAssignList);
        return new ResponseData(gridColumnAssigns);
    }
}
