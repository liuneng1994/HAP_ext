package hap.extend.core.dataPermission.dto;

import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;

/**
 * HAP扩展功能之数据屏蔽：规则与mapper方法的映射关系
 * <br>
 * Created by yyz on 2017/2/20.
 *
 * @author yazheng.yang@hand-china.com
 */
@Table(name = "hapextend_rule_mappermethod")
public class RuleMappermethod extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "mapping_id")
    private Long mapperId;
    @Column(name = "rule_id")
    @Condition
    private Long ruleId;
    @Column(name = "mapper_method")
    @Condition(operator = LIKE)
    private String mapperMethod;

    @Transient
    private String ruleName;

    public Long getMapperId() {
        return mapperId;
    }

    public void setMapperId(Long mapperId) {
        this.mapperId = mapperId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getMapperMethod() {
        return mapperMethod;
    }

    public void setMapperMethod(String mapperMethod) {
        this.mapperMethod = mapperMethod;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}
