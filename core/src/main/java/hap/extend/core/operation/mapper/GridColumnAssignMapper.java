package hap.extend.core.operation.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hap.extend.core.operation.dto.GridColumnAssign;

import java.util.List;

/**
 * Created by yyz on 17/4/1.
 */
public interface GridColumnAssignMapper extends Mapper<GridColumnAssign> {
    List<GridColumnAssign> selectByGridColumnAssign(GridColumnAssign gridColumnAssign);
}
