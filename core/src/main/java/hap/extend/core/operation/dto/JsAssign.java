package hap.extend.core.operation.dto;

import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;

/**
 * assign rule js code to each permission type.<br><br>
 *
 * Created by yyz on 2017/3/10.
 *
 * @author yazheng.yang@hand-china.com
 */
@Table(name = "HCOM_RES_JS_ASSIGN")
public class JsAssign extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "JS_ASSIGN_ID")
    private Long jsAssignId;

    @Column(name = "ASSIGN_ID", nullable = false)
    @Condition
    private Long assignId;

    @Column(name = "JS_ID",nullable = false)
    @Condition
    private Long jsId;

    @Transient
    private String jsName;

    @Column(name = "DESCRIPTION")
    @Condition(operator = LIKE)
    private String description;

    @Column(name = "ENABLE_FLAG",nullable = false)
    @Condition
    private String enableFlag;

    public Long getJsAssignId() {
        return jsAssignId;
    }

    public void setJsAssignId(Long jsAssignId) {
        this.jsAssignId = jsAssignId;
    }

    public Long getAssignId() {
        return assignId;
    }

    public void setAssignId(Long assignId) {
        this.assignId = assignId;
    }

    public Long getJsId() {
        return jsId;
    }

    public void setJsId(Long jsId) {
        this.jsId = jsId;
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

    public String getJsName() {
        return jsName;
    }

    public void setJsName(String jsName) {
        this.jsName = jsName;
    }
}
