package hap.extend.core.operation.dto;

import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by yyz on 17/4/1.
 */
@Table(name = "HCOM_RES_GRID_COLUMN")
public class GridColumn extends BaseDTO {

    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "CPN_COLUMN_ID")
    private Long columnId;

    @Column(name = "COMPONENT_ID",nullable = false)
    @Condition
    private Long componentId;

    @Column(name = "COLUMN_NAME",nullable = false)
    @Condition(operator = LIKE)
    private String name;

    @Column(name = "COLUMN_INDEX",nullable = false)
    private Long index;


    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }
}
