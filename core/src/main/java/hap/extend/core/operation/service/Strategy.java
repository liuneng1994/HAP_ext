package hap.extend.core.operation.service;

import hap.extend.core.operation.dto.ComponentAssign;

import java.util.List;

/**
 * Created by yyz on 17/3/28.
 */
public interface Strategy {
    void handle(List<ComponentAssign> componentAssigns, StringBuilder stringBuilder);
}
