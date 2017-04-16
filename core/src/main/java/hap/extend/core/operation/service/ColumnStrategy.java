package hap.extend.core.operation.service;

import hap.extend.core.operation.dto.Component;

/**
 * Created by yyz on 17/3/30.
 */
public interface ColumnStrategy {
    //对于不可编辑的列统一收集完毕之后再调用函数
    //考虑到生成的js代码量，也可以统一收集那些需要隐藏的列
    void handle(StringBuilder stringBuilder, Component component, Long cpnAssignId);
}
