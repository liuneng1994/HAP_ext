package hap.extend.core.dataPermission.dto;

import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by yyz on 2017/2/28.
 *
 * @author yazheng.yang@hand-china.com
 */
@Table(name = "HCOM_SQL_RULE_HEADER")
public class MapperMethod extends BaseDTO{
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "HEADER_ID")
    private Long headerId;

    /** mapper里面查询方法对应的全路径，如：com.hand.hap.function.mapper.ResourceMapper.select*/
    @Column(name = "SQLID_CODE",nullable = false)
    @Condition(operator = LIKE)
    private String sqlId;

    @Column(name = "DESCRIPTION")
    @Condition(operator = LIKE)
    private String description;


    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getSqlId() {
        return sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
