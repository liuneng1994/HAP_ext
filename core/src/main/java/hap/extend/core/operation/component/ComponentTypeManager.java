package hap.extend.core.operation.component;

import hap.extend.core.operation.component.mapper.ComponentTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liuneng on 2017/2/28.
 */
@Component
public class ComponentTypeManager {
    @Autowired
    private ComponentTypeMapper htmlComponentMapper;
    
}
