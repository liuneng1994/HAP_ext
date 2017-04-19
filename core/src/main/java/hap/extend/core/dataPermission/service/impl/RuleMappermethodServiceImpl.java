package hap.extend.core.dataPermission.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.extend.core.dataPermission.cache.impl.DataPermissionRuleMethodCache;
import hap.extend.core.dataPermission.dto.MapperMethod;
import hap.extend.core.dataPermission.dto.RuleMappermethod;
import hap.extend.core.dataPermission.mapper.MapperMethodMapper;
import hap.extend.core.dataPermission.mapper.RuleMappermethodMapper;
import hap.extend.core.dataPermission.service.IRuleMappermethodService;
import hap.extend.core.dataPermission.utils.CacheUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static hap.extend.core.dataPermission.utils.LangUtils.isNotNull;
import static hap.extend.core.dataPermission.utils.LangUtils.isNull;

import java.util.*;

/**
 * Created by yyz on 2017/2/22.
 *
 * @author yazheng.yang@hand-china.com
 */
@Service
public class RuleMappermethodServiceImpl extends BaseServiceImpl<RuleMappermethod> implements IRuleMappermethodService {
    @Autowired
    private DataPermissionRuleMethodCache ruleMethodCache;
    @Autowired
    private RuleMappermethodMapper ruleMappermethodMapper;
    @Autowired
    private MapperMethodMapper mapperMethodMapper;


    @Override
    public List<RuleMappermethod> batchUpdateCacheAndDb(IRequest request, List<RuleMappermethod> list) {
        IBaseService<RuleMappermethod> self = ((IBaseService<RuleMappermethod>) AopContext.currentProxy());
        Map<String,Set<Long>> addMap = new HashedMap();
        Map<String,Set<Long>> deleteMap = new HashedMap();

        for (RuleMappermethod ruleMappermethod : list) {
            MapperMethod condition_MapperMethod = new MapperMethod();
            condition_MapperMethod.setHeaderId(ruleMappermethod.getHeaderId());
            List<MapperMethod> mapperMethods = mapperMethodMapper.select(condition_MapperMethod);
            if(isNotNull(mapperMethods) && !mapperMethods.isEmpty()){
                Set<Long> deleteSet = deleteMap.get(mapperMethods.get(0).getSqlId());
                Set<Long> addSet = addMap.get(mapperMethods.get(0).getSqlId());
                switch (ruleMappermethod.get__status()) {
                    case DTOStatus.ADD:
                        self.insertSelective(request, ruleMappermethod);
                        if(isNull(addSet)){
                            addSet = new HashSet<>();
                            addMap.put(mapperMethods.get(0).getSqlId(),addSet);
                        }
                        addSet.add(ruleMappermethod.getRuleId());
                        break;
                    case DTOStatus.UPDATE:

                        RuleMappermethod condition = new RuleMappermethod();
                        condition.setLineId(ruleMappermethod.getLineId());
                        List<RuleMappermethod> inDb = self.select(request,condition,1,2);
                        if(isNotNull(inDb) && !inDb.isEmpty()){
                            boolean oldEnable = RuleMappermethod.isEnable(inDb.get(0).getEnableFlag());
                            boolean newEnable = RuleMappermethod.isEnable(ruleMappermethod.getEnableFlag());
                            if(oldEnable){
                                if(isNull(deleteSet)){
                                    deleteSet = new HashSet<>();
                                    deleteMap.put(mapperMethods.get(0).getSqlId(),deleteSet);
                                }
                                deleteSet.add(inDb.get(0).getRuleId());
                            }
                            if(newEnable){
                                if(isNull(addSet)){
                                    addSet = new HashSet<>();
                                    addMap.put(mapperMethods.get(0).getSqlId(),addSet);
                                }
                                addSet.add(ruleMappermethod.getRuleId());
                            }

                            if (useSelectiveUpdate()) {
                                self.updateByPrimaryKeySelective(request, ruleMappermethod);
                            } else {
                                self.updateByPrimaryKey(request, ruleMappermethod);
                            }
                        }
                        break;
                    case DTOStatus.DELETE:
                        self.deleteByPrimaryKey(ruleMappermethod);
                        if(isNull(deleteSet)){
                            deleteSet = new HashSet<>();
                            deleteMap.put(mapperMethods.get(0).getSqlId(),deleteSet);
                        }
                        deleteSet.add(ruleMappermethod.getRuleId());
                        break;
                    default:
                        break;
                }
            }
        }
        //avoid transaction problem:fear rollback
        //delete first and add new value
        deleteMap.forEach((key,value)->ruleMethodCache.removeValuesFromKey(CacheUtils.getMappermethodRulesKey(key),value.toArray(new Long[value.size()])));
        addMap.forEach((key,value)->ruleMethodCache.addValuesToKey(CacheUtils.getMappermethodRulesKey(key),value.toArray(new Long[value.size()])));

        return list;
    }

    @Override
    public List<RuleMappermethod> selectByRuleMappermethod(IRequest iRequest, RuleMappermethod ruleMappermethod, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return ruleMappermethodMapper.selectByRuleMappermethod(ruleMappermethod);
    }
}
