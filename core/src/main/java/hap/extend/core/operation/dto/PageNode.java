package hap.extend.core.operation.dto;

import com.hand.hap.system.dto.BaseDTO;

import static hap.extend.core.operation.utils.LangUtil.isNull;

/**
 * just for displaying the config page level structure.
 *
 * </br>
 * Created by yyz on 2017/3/17.
 *
 * @author yazheng.yang@hand-china.com
 */
public class PageNode extends BaseDTO {
    private Long functionId;
    private String functionName;
    private Long resourceId;
    private String resourceName;
    private String url;

    /** designed for listView specially*/
    private Long id;
    private Long parentId;
    private String text;
    /** queue */
    private Long sequence;


    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {
        if(!obj.getClass().equals(this.getClass())){
            return false;
        }
        PageNode o = (PageNode) obj;
        return isNull(functionId)?false:this.getId().equals(o.getId());
    }

    @Override
    public int hashCode() {
        return isNull(id)?-1:id.intValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
