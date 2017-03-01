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
@Table(name = "HCOM_SQL_RULE_LINES")
public class RuleMappermethod extends BaseDTO {
    @Transient
    public static final String VALUE_YES = "Y";
    @Transient
    public static final String VALUE_NO = "N";

    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "LINE_ID")
    private Long lineId;

    @Column(name = "HEADER_ID",nullable = false)
    private Long headerId;

    @Column(name = "RULE_ID")
    @Condition
    private Long ruleId;

    @Column(name = "ENABLE_FLAG")
    @Condition
    private String enableFlag;

    @Transient
    private String ruleName;
    @Transient
    private String sqlId;

    public static boolean isEnable(String enableFlagStr){
        return VALUE_YES.equals(enableFlagStr);
    }
    public static boolean isDisable(String enableFlagStr){
        return VALUE_NO.equals(enableFlagStr);
    }


    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getSqlId() {
        return sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }
}
