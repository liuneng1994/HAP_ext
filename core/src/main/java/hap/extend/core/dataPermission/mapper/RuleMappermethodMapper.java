package hap.extend.core.dataPermission.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hap.extend.core.dataPermission.dto.RuleMappermethod;

import java.util.List;

/**
 * Created by yyz on 2017/2/20.
 *
 * @author yazheng.yang@hand-china.com
 */
public interface RuleMappermethodMapper extends Mapper<RuleMappermethod> {
    List<RuleMappermethod> selectByRuleMappermethod(RuleMappermethod ruleMappermethod);
}
