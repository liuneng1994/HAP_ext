package hap.extend.core.dataPermission.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hap.extend.core.dataPermission.dto.MapperMethod;
import hap.extend.core.dataPermission.service.IMapperMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yyz on 2017/2/28.
 *
 * @author yazheng.yang@hand-china.com
 */
@Controller
@RequestMapping(value = "/hap_extend/data_permission/method_rule_header/")
public class MapperMethodController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(MapperMethodController.class);
    @Autowired
    private IMapperMethodService mapperMethodService;

    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData getMapperMethods(HttpServletRequest request,
                                 @RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
                                 @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                 @RequestParam(name = "sqlId", defaultValue = "") String sqlId,
                                 @RequestParam(name = "description", defaultValue = "") String description) {
        IRequest requestContext = createRequestContext(request);
        MapperMethod mapperMethod = new MapperMethod();
        mapperMethod.setSqlId(returnNullIfEmpty(sqlId));
        mapperMethod.setDescription(returnNullIfEmpty(description));
        List<MapperMethod> mapperMethodList = mapperMethodService.select(requestContext, mapperMethod, page, pageSize);
        return new ResponseData(mapperMethodList);
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData createMapperMethods(@RequestBody List<MapperMethod> mapperMethodList, BindingResult result, HttpServletRequest request){
        return new ResponseData(mapperMethodService.batchUpdate(createRequestContext(request), mapperMethodList));
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData deleteMapperMethods(@RequestBody List<MapperMethod> mapperMethodList, BindingResult result, HttpServletRequest request) throws Exception {
        return new ResponseData(mapperMethodService.batchUpdate(createRequestContext(request), mapperMethodList));
    }

    @RequestMapping(value = "update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseData updateMapperMethods(@RequestBody List<MapperMethod> mapperMethodList, BindingResult result, HttpServletRequest request) throws Exception {
        return new ResponseData(mapperMethodService.batchUpdate(createRequestContext(request), mapperMethodList));
    }

    private String returnNullIfEmpty(String source){
        if(null == source || source.isEmpty()){
            return null;
        }
        return source;
    }
}
