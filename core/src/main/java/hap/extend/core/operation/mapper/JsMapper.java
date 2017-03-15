package hap.extend.core.operation.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hap.extend.core.operation.dto.Js;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yyz on 2017/3/10.
 *
 * @author yazheng.yang@hand-china.com
 */
public interface JsMapper extends Mapper<Js> {
    List<Js> selectPageJsByRole(@Param("resourceId") Long resourceId,@Param("roleId") Long roleId);
    List<Js> selectPageJsByUser(@Param("resourceId") Long resourceId,@Param("userId") Long userId);
}
