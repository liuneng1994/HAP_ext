package hap.extend.core.dataPermission.dto;

import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by yyz on 2017/2/17.
 *
 * @author yazheng.yang@hand-china.com
 */
@Table(name = "hap_extend_user_method_rule_mapping")
public class DataPermissionUserMethodRule extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "mapping_id")
    private Long mappingId;
    @Column(name = "user_id",nullable = false)
    private Long userId;
    @Column(name = "rule_id",nullable = false)
    private Long ruleId;

    public Long getMappingId() {
        return mappingId;
    }

    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
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
}
