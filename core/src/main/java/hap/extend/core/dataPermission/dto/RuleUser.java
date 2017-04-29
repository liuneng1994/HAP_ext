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
@Table(name = "HCOM_DATA_RULE_ASSIGN")
public class RuleUser extends BaseDTO {
    @Transient
    public static final String TYPE_USER="USER";
    @Transient
    public static final String TYPE_ROLE="ROLE";

    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "ASSIGN_ID")
    private Long assignId;

    /** 类别，目前有：用户和角色*/
    @Column(name = "ASSIGN_TYPE")
    @Condition
    private String assignType;

    /** 类别具体值，可以是用户id、角色id*/
    @Column(name = "TYPE_ID")
    @Condition
    private Long typeId;

    @Column(name = "RULE_ID")
    @Condition
    private Long ruleId;
    /** 类别值对应的名称，如用户名、角色名称 */
    @Transient
    private String typeIdName;

    public static boolean isUserType(String type){
        return TYPE_USER.equals(type);
    }
    public static boolean isRoleType(String type){
        return TYPE_ROLE.equals(type);
    }

    public Long getAssignId() {
        return assignId;
    }

    public void setAssignId(Long assignId) {
        this.assignId = assignId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getAssignType() {
        return assignType;
    }

    public void setAssignType(String assignType) {
        this.assignType = assignType;
    }

    public String getTypeIdName() {
        return typeIdName;
    }

    public void setTypeIdName(String typeIdName) {
        this.typeIdName = typeIdName;
    }
}
