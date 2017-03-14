package hap.extend.core.operation.dto;

import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * js code of each page.one page can own several js codes<br><br>
 * Created by yyz on 2017/3/10.
 * @author yazheng.yang@hand-china.com
 */
@Table(name = "HCOM_RES_JS")
public class Js extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "JS_ID")
    private Long jsId;

    @Column(name = "RESOURCE_ID",nullable = false)
    @Condition
    private Long resourceId;

    @Column(name = "JS_NAME",nullable = false)
    @Condition(operator = LIKE)
    private String jsName;

    @Column(name = "DESCRIPTION")
    @Condition(operator = LIKE)
    private String description;

    @Column(name = "JS_SCRIPT")
    @Condition(operator = LIKE)
    private String jsScript;
//FIXME 这里设计得有问题——既然在js分配中使用了是否启用，那么这里的是否启用就没有意义
    @Column(name = "ENABLE_FLAG",nullable = false)
    @Condition
    private String enableFlag;


    public Long getJsId() {
        return jsId;
    }

    public void setJsId(Long jsId) {
        this.jsId = jsId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getJsName() {
        return jsName;
    }

    public void setJsName(String jsName) {
        this.jsName = jsName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJsScript() {
        return jsScript;
    }

    public void setJsScript(String jsScript) {
        this.jsScript = jsScript;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
