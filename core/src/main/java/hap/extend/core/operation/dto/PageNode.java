package hap.extend.core.operation.dto;

import com.hand.hap.system.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

import static hap.extend.core.operation.utils.LangUtil.isNotNull;
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
    private List<PageNode> children;
    private List<PageNode> children_temp1;
    /** FunctionResource list*/
    private List<PageNode> children_temp2;
    private Long parentFunctionId;

    /** designed for listView specially*/
    private Long id;
    private Long parentId;


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

    public List<PageNode> getChildren() {
        return children;
    }

    public void setChildren(List<PageNode> children) {
        this.children = children;
    }

    public List<PageNode> getChildren_temp1() {
        return children_temp1;
    }

    public void setChildren_temp1(List<PageNode> children_temp1) {
        this.children_temp1 = children_temp1;
    }

    public List<PageNode> getChildren_temp2() {
        return children_temp2;
    }

    public void setChildren_temp2(List<PageNode> children_temp2) {
        this.children_temp2 = children_temp2;
    }

    public void mergeChildren(){
        List<PageNode> pageNodes = new ArrayList<>();

        if(isNotNull(children_temp1) && !children_temp1.isEmpty()){
            pageNodes.addAll(children_temp1);
        }
        if(isNotNull(children_temp2) && !children_temp2.isEmpty()){
            pageNodes.addAll(children_temp2);
        }
        children = pageNodes;
    }

    @Override
    public boolean equals(Object obj) {
        if(!obj.getClass().equals(this.getClass())){
            return false;
        }
        PageNode o = (PageNode) obj;
        return isNull(functionId)?false:this.getFunctionId().equals(o.getFunctionId());
    }

    @Override
    public int hashCode() {
        return isNull(functionId)?-1:functionId.intValue();
    }

    public Long getParentFunctionId() {
        return parentFunctionId;
    }

    public void setParentFunctionId(Long parentFunctionId) {
        this.parentFunctionId = parentFunctionId;
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
}
