package hap.extend.core.dataPermission.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.extend.core.dataPermission.cache.impl.DataPermissionRuleMethodCache;
import hap.extend.core.dataPermission.dto.RuleMappermethod;
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


    @Override
    public List<RuleMappermethod> batchUpdate(IRequest request, List<RuleMappermethod> list) {
        IBaseService<RuleMappermethod> self = ((IBaseService<RuleMappermethod>) AopContext.currentProxy());
        Map<String,Set<Long>> addMap = new HashedMap();
        Map<String,Set<Long>> deleteMap = new HashedMap();
        List<RuleMappermethod> afterUpdate = new ArrayList<>();
        List<RuleMappermethod> beforeUpdate = new ArrayList<>();

        for (RuleMappermethod ruleMappermethod : list) {
            switch (ruleMappermethod.get__status()) {
                case DTOStatus.ADD:
                    self.insertSelective(request, ruleMappermethod);
                    Set<Long> valueSet = addMap.get(ruleMappermethod.getMapperMethod());
                    if(isNull(valueSet)){
                        valueSet = new HashSet<>();
                        addMap.put(ruleMappermethod.getMapperMethod(),valueSet);
                    }
                    valueSet.add(ruleMappermethod.getRuleId());
                    break;
                case DTOStatus.UPDATE:
                    RuleMappermethod condition = new RuleMappermethod();
                    condition.setMapperId(ruleMappermethod.getMapperId());
                    List<RuleMappermethod> inDb = self.select(request,condition,1,2);
                    if(isNotNull(inDb) && !inDb.isEmpty()){
                        beforeUpdate.add(inDb.get(0));
                        afterUpdate.add(ruleMappermethod);
                        if (useSelectiveUpdate()) {
                            self.updateByPrimaryKeySelective(request, ruleMappermethod);
                        } else {
                            self.updateByPrimaryKey(request, ruleMappermethod);
                        }
                    }

                    break;
                case DTOStatus.DELETE:
                    self.deleteByPrimaryKey(ruleMappermethod);
                    Set<Long> set = deleteMap.get(ruleMappermethod.getMapperMethod());
                    if(isNull(set)){
                        set = new HashSet<>();
                        deleteMap.put(ruleMappermethod.getMapperMethod(),set);
                    }
                    set.add(ruleMappermethod.getRuleId());
                    break;
                default:
                    break;
            }
        }
        //avoid transaction problem:fear rollback
        addMap.forEach((key,value)->ruleMethodCache.addValuesToKey(CacheUtils.getMappermethodRulesKey(key),value.toArray(new Long[value.size()])));
        deleteMap.forEach((key,value)->ruleMethodCache.removeValuesFromKey(CacheUtils.getMappermethodRulesKey(key),value.toArray(new Long[value.size()])));
        beforeUpdate.forEach(ruleMappermethod -> ruleMethodCache.removeValuesFromKey(CacheUtils.getMappermethodRulesKey(ruleMappermethod.getMapperMethod()),ruleMappermethod.getRuleId()));
        afterUpdate.forEach(ruleMappermethod -> ruleMethodCache.addValuesToKey(CacheUtils.getMappermethodRulesKey(ruleMappermethod.getMapperMethod()),ruleMappermethod.getRuleId()));
        return list;
    }

    @Override
    public List<RuleMappermethod> selectByRuleMappermethod(IRequest iRequest, RuleMappermethod ruleMappermethod, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return ruleMappermethodMapper.selectByRuleMappermethod(ruleMappermethod);
    }
}
