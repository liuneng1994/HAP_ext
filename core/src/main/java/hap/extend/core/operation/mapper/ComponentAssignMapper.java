package hap.extend.core.operation.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hap.extend.core.operation.dto.ComponentAssign;

import java.util.List;

/**
 * Created by yyz on 17/3/24.
 */
public interface ComponentAssignMapper extends Mapper<ComponentAssign> {

    List<ComponentAssign> selectByComponentAssign(ComponentAssign componentAssign);
}
