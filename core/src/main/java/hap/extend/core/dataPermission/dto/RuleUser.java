package hap.extend.core.dataPermission.dto;

import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;

/**
 * HAP扩展功能之数据屏蔽：规则与用户的映射关系
 * <br>
 * Created by yyz on 2017/2/20.
 *
 * @author yazheng.yang@hand-china.com
 */
@Table(name = "hapextend_rule_user")
public class RuleUser extends BaseDTO {
    @Transient
    public static final String VALUE_INCLUDE="Y";
    @Transient
    public static final String VALUE_EXCLUDE="N";

    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "mapping_id")
    private Long mapperId;
    @Column(name = "user_id")
    @Condition
    private Long userId;
    @Column(name = "rule_id")
    @Condition
    private Long ruleId;
    @Column(name = "is_include")
    private String isInclude;

    @Transient
    private String userName;
    @Transient
    private String ruleName;

    public static boolean isInclude(String isIncludeStr){
        return VALUE_INCLUDE.equals(isIncludeStr);
    }
    public static boolean isExclude(String isExcludeStr){
        return VALUE_EXCLUDE.equals(isExcludeStr);
    }

    public Long getMapperId() {
        return mapperId;
    }

    public void setMapperId(Long mapperId) {
        this.mapperId = mapperId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getIsInclude() {
        return isInclude;
    }

    public void setIsInclude(String isInclude) {
        this.isInclude = isInclude;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}
