package hap.extend.core.operation.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.extend.core.operation.dto.ComponentAssign;
import hap.extend.core.operation.mapper.ComponentAssignMapper;
import hap.extend.core.operation.service.ComponentAssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yyz on 17/3/24.
 */
@Service
public class ComponentAssignServiceImpl extends BaseServiceImpl<ComponentAssign> implements ComponentAssignService {
    @Autowired
    private ComponentAssignMapper componentAssignMapper;

    @Override
    public List<ComponentAssign> selectByComponentAssign(IRequest requestContext, ComponentAssign componentAssign, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return componentAssignMapper.selectByComponentAssign(componentAssign);
    }
}
