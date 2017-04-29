package hap.extend.core.dataPermission.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.core.annotation.StdWho;
import com.hand.hap.system.service.IBaseService;
import hap.extend.core.dataPermission.dto.Rule;

import java.util.List;

/**
 * Created by yyz on 2017/2/22.
 *
 * @author yazheng.yang@hand-china.com
 */
public interface IRuleService extends IBaseService<Rule>, ProxySelf<IRuleService>{
    List<Rule> batchUpdateCacheAndDb(IRequest iRequest, @StdWho List<Rule> list);
}
