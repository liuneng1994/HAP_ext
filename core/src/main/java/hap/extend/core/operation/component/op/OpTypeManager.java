package hap.extend.core.operation.component.op;

import hap.extend.core.operation.component.op.mapper.OpTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by liuneng on 2017/2/28.
 */
@Component
public class OpTypeManager {
    private Map<String,OpType> opTypes;
    @Autowired
    private OpTypeMapper opTypeMapper;

    @PostConstruct
    public void init() {
        List<OpType> opTypeList = opTypeMapper.selectAll();
        opTypes = new HashMap<>();
        opTypeList.forEach(opType -> opTypes.put(opType.getName(),opType));
    }

    public boolean hasOpType(String name) {
        return opTypes.containsKey(name);
    }

}
