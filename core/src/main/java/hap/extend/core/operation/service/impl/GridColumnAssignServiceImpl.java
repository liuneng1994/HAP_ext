package hap.extend.core.operation.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.extend.core.operation.dto.GridColumnAssign;
import hap.extend.core.operation.mapper.GridColumnAssignMapper;
import hap.extend.core.operation.service.GridColumnAssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yyz on 17/4/1.
 */
@Service
public class GridColumnAssignServiceImpl  extends BaseServiceImpl<GridColumnAssign> implements GridColumnAssignService {

    @Autowired
    private GridColumnAssignMapper gridColumnAssignMapper;

    @Override
    public List<GridColumnAssign> selectByGridColumnAssign(IRequest iRequest, GridColumnAssign gridColumnAssign, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return gridColumnAssignMapper.selectByGridColumnAssign(gridColumnAssign);
    }
}
