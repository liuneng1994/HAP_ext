package hap.extend.core.operation.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hap.extend.core.operation.dto.GridColumn;
import hap.extend.core.operation.service.GridColumnService;
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
@RequestMapping(value = "/hap_extend/operation_permission/grid_column/")
public class GridColumnController extends BaseController {
    @Autowired
    private GridColumnService gridColumnService;



    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(HttpServletRequest request,
                              @RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                              @RequestParam(name = "columnName", defaultValue = "") String columnName,
                              @RequestParam(name = "componentId", defaultValue = "-1") Long componentId) {
        IRequest requestContext = createRequestContext(request);
        GridColumn gridColumn = new GridColumn();
        gridColumn.setName(returnNullIfStrEmpty(columnName));
        gridColumn.setComponentId(componentId);

        List<GridColumn> gridColumnList = gridColumnService.select(requestContext, gridColumn, page, pageSize);
        return new ResponseData(gridColumnList);
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData submit(@RequestBody List<GridColumn> gridColumnList, BindingResult result, HttpServletRequest request){

        List<GridColumn> gridColumns = gridColumnService.batchUpdate(createRequestContext(request), gridColumnList);
        return new ResponseData(gridColumns);
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData remove(@RequestBody List<GridColumn> gridColumnList, BindingResult result, HttpServletRequest request) throws Exception {

        List<GridColumn> gridColumns = gridColumnService.batchUpdate(createRequestContext(request), gridColumnList);
        return new ResponseData(gridColumns);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData update(@RequestBody List<GridColumn> gridColumnList, BindingResult result, HttpServletRequest request) throws Exception {
        List<GridColumn> gridColumns = gridColumnService.batchUpdate(createRequestContext(request), gridColumnList);
        return new ResponseData(gridColumns);
    }
}
