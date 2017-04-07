package hap.extend.core.operation.service.impl;

import hap.extend.core.operation.dto.Component;
import hap.extend.core.operation.dto.GridColumnAssign;
import hap.extend.core.operation.mapper.GridColumnAssignMapper;
import hap.extend.core.operation.service.ColumnStrategy;
import hap.extend.core.operation.utils.OPConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static hap.extend.core.operation.utils.LangUtil.isNotNull;
import static hap.extend.core.operation.utils.OPConstUtil.isGridgridType;

/**
 * Created by yyz on 17/4/6.
 */
@Service
public class ColumnStrategyImpl implements ColumnStrategy {
    @Autowired
    private GridColumnAssignMapper gridColumnAssignMapper;

    @Override
    public void handle(StringBuilder stringBuilder, Component component, Long cpnAssignId) {
        if(isGridgridType(component.getLevel(),component.getComponentType())){
            GridColumnAssign columnAssign = new GridColumnAssign();
            columnAssign.setCpnAssignId(cpnAssignId);
            columnAssign.setEnableFlag(OPConstUtil.VALUE_YES);
            List<GridColumnAssign> gridColumnAssignList = gridColumnAssignMapper.selectByGridColumnAssign(columnAssign);
            if(isNotNull(gridColumnAssignList) || !gridColumnAssignList.isEmpty()){
                Set<Long> forbidEditColumnIndexs = new HashSet<>();
                Set<Long> hideColumnIndexs = new HashSet<>();

                gridColumnAssignList.forEach(assign->{
                    if(OPConstUtil.VALUE_YES.equals(assign.getReadonly())){
                        forbidEditColumnIndexs.add(assign.getColumnIndex());
                    }
                    if(OPConstUtil.VALUE_NO.equals(assign.getDisplay())){
                        hideColumnIndexs.add(assign.getColumnIndex());
                    }
                });
                String noEditIndex = null;
                String hideIndex = null;
                if(!forbidEditColumnIndexs.isEmpty()){
                    noEditIndex = forbidEditColumnIndexs.toString();
                }
                if(!hideColumnIndexs.isEmpty()){
                    hideIndex = hideColumnIndexs.toString();
                }
                stringBuilder.append(OPConstUtil.generateColumnJsCode(component.getHtmlTagAttr(), component.getHtmlTagAttrVal(), hideIndex, noEditIndex));
            }
        }
    }
}
