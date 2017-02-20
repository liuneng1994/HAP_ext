package hap.extend.core.dataPermission.dto;

import com.hand.hap.system.dto.BaseDTO;

/**
 * Created by yyz on 2017/2/20.
 *
 * @author yazheng.yang@hand-china.com
 */
public class Rule extends BaseDTO {
    private Long ruleId;
    private String ruleSql;

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
}
