package hap.extend.core.operation.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyz on 2017/3/8.
 *
 * @author yazheng.yang@hand-china.com
 */
@Controller
@RequestMapping(value = "/hap_extend/operation_permission/")
public class OperationPermissionController extends BaseController {

    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData getRules(HttpServletRequest request,HttpServletResponse response,
                                 @RequestParam(name = "filePath", defaultValue = "") String filePath) {
        IRequest requestContext = createRequestContext(request);
        List<BaseDTO> list = new ArrayList<>();
        BaseDTO dataCarrier = new BaseDTO();
        String s = "function disableCpn(cpn_id) {\n" +
                "    $(\"#\"+cpn_id).attr(\"disabled\",true);\n" +
                "}\ndisableCpn(\"btn_test\");";
        dataCarrier.setAttribute1(s);
        list.add(dataCarrier);
        return new ResponseData(list);
    }

}
