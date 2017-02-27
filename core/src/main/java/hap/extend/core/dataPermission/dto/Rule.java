package hap.extend.core.dataPermission.dto;

import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;

/**
 * HAP扩展功能之数据屏蔽：权限规则
 * <br>
 * Created by yyz on 2017/2/20.
 *
 * @author yazheng.yang@hand-china.com
 */
@Table(name = "hapextend_rules")
public class Rule extends BaseDTO {
    @Transient
    public static final String VALUE_YES = "Y";
    @Transient
    public static final String VALUE_NO = "N";
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "rule_id")
    private Long ruleId;
    @Column(name = "rule_sql", nullable = false)
    @Condition(operator = LIKE)
    private String ruleSql;
    @Column(name = "rule_name", nullable = false)
    @Condition(operator = LIKE)
    private String ruleName;
//用于判断当前规则是用于排斥exclude列表中的用户，还是包含include列表中的用户
//当为排斥型的时候，将查看当前用户是否在排斥列表，如果在，将跳过应用规则；否则，应用规则。
//当为包含型的时候，将查看包含列表中是否有当前用户，如果有，应用规则；否则，不使用规则
    @Column(name = "is_include_type", nullable = false)
    @Condition
    private String isIncludeType;

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleSql() {
        return ruleSql;
    }

    public void setRuleSql(String ruleSql) {
        this.ruleSql = ruleSql;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getIsIncludeType() {
        return isIncludeType;
    }

    public void setIsIncludeType(String isIncludeType) {
        this.isIncludeType = isIncludeType;
    }

    public static boolean isIncludeType(String type){
        return VALUE_YES.equals(type);
    }
}
