package hap.extend.core.operation.service.impl;

import hap.extend.core.operation.dto.Component;
import hap.extend.core.operation.dto.ComponentAssign;
import hap.extend.core.operation.mapper.ComponentMapper;
import hap.extend.core.operation.service.ColumnStrategy;
import hap.extend.core.operation.service.Strategy;
import hap.extend.core.operation.utils.OPConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static hap.extend.core.operation.utils.LangUtil.isNotNull;
import static hap.extend.core.operation.utils.LangUtil.isNull;
import static hap.extend.core.operation.utils.OPConstUtil.generateJsCode;

/**
 * Created by yyz on 17/3/28.
 */
@Service
public class StrategyImpl implements Strategy {
    @Autowired
    private ComponentMapper componentMapper;
    @Autowired
    private ColumnStrategy columnStrategy;

    @Override
    public void handle(List<ComponentAssign> componentAssigns, StringBuilder stringBuilder) {
        if(isNull(componentAssigns) || isNull(stringBuilder) || componentAssigns.isEmpty()){
            return;
        }
        componentAssigns.forEach(assign -> {
            Component component = componentMapper.selectByPrimaryKey(assign.getComponentId());
            if(isNotNull(component)){
                stringBuilder.append(generateJsCode(component.getLevel(),component.getComponentType(), OPConstUtil.CPN_CTR_FUN_TYPE_DISPLAY,component.getHtmlTagAttr(),component.getHtmlTagAttrVal(),assign.getDisplay()));
                stringBuilder.append(generateJsCode(component.getLevel(),component.getComponentType(), OPConstUtil.CPN_CTR_FUN_TYPE_REQUIRED,component.getHtmlTagAttr(),component.getHtmlTagAttrVal(),assign.getRequire()));
                stringBuilder.append(generateJsCode(component.getLevel(),component.getComponentType(), OPConstUtil.CPN_CTR_FUN_TYPE_READONLY,component.getHtmlTagAttr(),component.getHtmlTagAttrVal(),assign.getReadonly()));
                stringBuilder.append(generateJsCode(component.getLevel(),component.getComponentType(), OPConstUtil.CPN_CTR_FUN_TYPE_DISABLE,component.getHtmlTagAttr(),component.getHtmlTagAttrVal(),assign.getDisable()));
                // apply column strategy here
                columnStrategy.handle(stringBuilder,component,assign.getCpnAssignId());
            }
        });
    }
}
