package hap.extend.core.dataPermission.dto;

import com.hand.hap.system.dto.BaseDTO;

/**
 * Created by yyz on 2017/2/20.
 *
 * @author yazheng.yang@hand-china.com
 */
public class RuleUser extends BaseDTO {
    public static final String VALUE_INCLUDE="Y";
    public static final String VALUE_EXCLUDE="N";

    private Long mapperId;
    private Long userId;
    private Long ruleId;
    private String isInclude;

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
}
