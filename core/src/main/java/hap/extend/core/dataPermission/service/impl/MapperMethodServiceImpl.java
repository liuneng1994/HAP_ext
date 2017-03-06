package hap.extend.core.dataPermission.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.extend.core.dataPermission.cache.impl.DataPermissionRuleMethodCache;
import hap.extend.core.dataPermission.dto.MapperMethod;
import hap.extend.core.dataPermission.mapper.MapperMethodMapper;
import hap.extend.core.dataPermission.service.IMapperMethodService;
import hap.extend.core.dataPermission.utils.CacheUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static hap.extend.core.dataPermission.utils.LangUtils.isNotNull;

/**
 * Created by yyz on 2017/2/28.
 *
 * @author yazheng.yang@hand-china.com
 */
@Service
public class MapperMethodServiceImpl  extends BaseServiceImpl<MapperMethod> implements IMapperMethodService {
    @Autowired
    private DataPermissionRuleMethodCache ruleMethodCache;

    @Override
    public List<MapperMethod> batchUpdate(IRequest request, List<MapperMethod> list) {
        IBaseService<MapperMethod> self = ((IBaseService<MapperMethod>) AopContext.currentProxy());
        List<MapperMethod> deleteList = new ArrayList<>();
        Map<String,Long[]> addMap = new HashedMap();
        for (MapperMethod mapperMethod : list) {
            switch (mapperMethod.get__status()) {
                case DTOStatus.ADD:
                    self.insertSelective(request, mapperMethod);
                    break;
                case DTOStatus.UPDATE:
                    //TODO 增加unique数据库约束 规则ID+ID+类别
                    MapperMethod condition = new MapperMethod();
                    condition.setHeaderId(mapperMethod.getHeaderId());
                    List<MapperMethod> mapperMethodsInDB = self.select(request, condition, 1, 2);
                    if(isNotNull(mapperMethodsInDB) && !mapperMethodsInDB.isEmpty()){
                        if(!mapperMethodsInDB.get(0).getSqlId().equals(mapperMethod.getSqlId())){
                            deleteList.add(mapperMethodsInDB.get(0));
                            Long[] value = ruleMethodCache.getValue(CacheUtils.getMappermethodRulesKey(mapperMethodsInDB.get(0).getSqlId()));
                            if(isNotNull(value)){
                                addMap.put(CacheUtils.getMappermethodRulesKey(mapperMethod.getSqlId()), value);
                            }
                        }
                        if (useSelectiveUpdate()) {
                            self.updateByPrimaryKeySelective(request, mapperMethod);
                        } else {
                            self.updateByPrimaryKey(request, mapperMethod);
                        }
                    }
                    break;
                case DTOStatus.DELETE:
                    deleteList.add(mapperMethod);
                    self.deleteByPrimaryKey(mapperMethod);
                    break;
                default:
                    break;
            }
        }
        //to avoid transaction rollback
        deleteList.forEach(mapperMethod -> ruleMethodCache.remove(CacheUtils.getMappermethodRulesKey(mapperMethod.getSqlId())));
        addMap.forEach((key,value)->ruleMethodCache.setValue(key,value));
        return list;
    }
}
