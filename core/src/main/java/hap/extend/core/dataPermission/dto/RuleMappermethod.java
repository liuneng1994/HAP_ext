package hap.extend.core.dataPermission.dto;

import com.hand.hap.system.dto.BaseDTO;

/**
 * Created by yyz on 2017/2/20.
 *
 * @author yazheng.yang@hand-china.com
 */
public class RuleMappermethod extends BaseDTO {
    private Long mapperId;
    private Long ruleId;
    private String mapperMethod;

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
}
