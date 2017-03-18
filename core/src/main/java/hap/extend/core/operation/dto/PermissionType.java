package hap.extend.core.operation.dto;

import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;

/**
 * permission type.
 * <br><br>
 * Created by yyz on 2017/3/10.
 *
 * @author yazheng.yang@hand-china.com
 */
@Table(name = "HCOM_RES_PRIV_ASSIGN")
public class PermissionType extends BaseDTO {
    @Transient
    public static final String VALUE_USER_TYPE="USER";
    public static final String VALUE_ROLE_TYPE="ROLE";
    public static final String VALUE_GLOBAL_TYPE="GLOBAL";

    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "ASSIGN_ID")
    private Long assignId;

    @Column(name = "RESOURCE_ID",nullable = false)
    @Condition
    private Long resourceId;

    /** ROLE/USER/GLOBAL*/
    @Column(name = "ASSIGN_TYPE",nullable = false)
    @Condition(operator = LIKE)
    private String assignType;

    @Column(name = "TYPE_ID",nullable = false)
    private Long assignValue;

    @Transient
    private String assignValueName;

    @Column(name = "DESCRIPTION")
    @Condition(operator = LIKE)
    private String description;

    @Column(name = "ENABLE_FLAG",nullable = false)
    @Condition
    private String enableFlag;

    public Long getAssignId() {
        return assignId;
    }

    public void setAssignId(Long assignId) {
        this.assignId = assignId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getAssignType() {
        return assignType;
    }

    public void setAssignType(String assignType) {
        this.assignType = assignType;
    }

    public Long getAssignValue() {
        return assignValue;
    }

    public void setAssignValue(Long assignValue) {
        this.assignValue = assignValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getAssignValueName() {
        return assignValueName;
    }

    public void setAssignValueName(String assignValueName) {
        this.assignValueName = assignValueName;
    }

    public static boolean isUserType(String type){
        return VALUE_USER_TYPE.equals(type);
    }
    public static boolean isRoleType(String type){
        return VALUE_ROLE_TYPE.equals(type);
    }
}
