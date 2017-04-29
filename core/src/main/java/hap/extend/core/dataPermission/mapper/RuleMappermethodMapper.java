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
    /** 查询所有映射关系，并将头行结构中头部的SQL ID放进去*/
    List<RuleMappermethod> selectAllMapping();

    List<RuleMappermethod> selectByRuleMappermethod(RuleMappermethod ruleMappermethod);
}
