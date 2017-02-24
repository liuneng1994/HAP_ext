/*
 * #{copyright}#
 */
package com.lkkhpg.dsis.platform.vpd.parser.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.hand.hap.cache.Cache;
import com.hand.hap.cache.CacheManager;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.lkkhpg.dsis.platform.vpd.dto.SysVpdAssign;
import com.lkkhpg.dsis.platform.vpd.dto.SysVpdConfig;
import com.lkkhpg.dsis.platform.vpd.dto.SysVpdDetails;
import com.lkkhpg.dsis.platform.vpd.exception.VpdParseErrorException;
import com.lkkhpg.dsis.platform.vpd.parser.IVpdParser;

/**
 * VPD解析器实现.
 * @author chenjingxiong
 */
public class VpdParser implements IVpdParser, ApplicationContextAware {

    private static final String VPD_CACHE_NAME = "vpd";

    private static List<SysVpdConfig> emptyList = new ArrayList<>(0);

    private ApplicationContext applicationContext;

    private VpdExpressionBuilder vpdExpressionBuilder = new VpdExpressionBuilder();

    private List<SysVpdConfig> getVpdConfigsFromCache(String mapperId) {
        Cache<SysVpdAssign> cache = getCacheManager().getCache(VPD_CACHE_NAME);
        if (cache == null) {
            return emptyList;
        }
        SysVpdAssign sysVpdAssign = cache.getValue(mapperId);
        if (sysVpdAssign == null) {
            return emptyList;
        }
        List<SysVpdConfig> sysVpdConfigSet = sysVpdAssign.getVpdConfigs();
        if (sysVpdConfigSet == null) {
            return emptyList;
        }
        return sysVpdConfigSet;
    }

    public CacheManager getCacheManager() {
        CacheManager cacheManager = (CacheManager) applicationContext.getBean("cacheManager");
        return cacheManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see IVpdParser#parseAndBuild(java.lang.String)
     */
    @Override
    public String parseAndBuild(String mapperId, String sql) throws JSQLParserException, VpdParseErrorException {
        // 解析SQL
        CCJSqlParserManager pm = new CCJSqlParserManager();
        Statement statement = pm.parse(new StringReader(sql));

        if (statement instanceof Select) {
            Select select = (Select) statement;
            List<SysVpdConfig> vpdConfigs = getVpdConfigsFromCache(mapperId);
            if (vpdConfigs != null && vpdConfigs.size() > 0) {
                for (SysVpdConfig vpdConfig : vpdConfigs) {
                    processSelect(select, vpdConfig);
                }
            }
            sql = select.toString();
        } else {
            throw new VpdParseErrorException();
        }
        return sql;
    }

    private void processSelect(Select select, SysVpdConfig vpdConfig) throws VpdParseErrorException {
        SelectBody selectBody = select.getSelectBody();
        processSelectBody(selectBody, vpdConfig);
    }

    private void processSelectBody(SelectBody selectBody, SysVpdConfig vpdConfig) throws VpdParseErrorException {
        String tableName = vpdConfig.getTableName().toLowerCase();
        if (selectBody instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) selectBody;
            FromItem fromItem = plainSelect.getFromItem();
            // FROM
            if (fromItem instanceof Table) {
                Table table = (Table) fromItem;
                if (tableName.equals(table.getName().toLowerCase())) {
                    Alias alias = table.getAlias();

                    List<SysVpdDetails> vpdConfigDetails = vpdConfig.getVpdDetails();
                    for (SysVpdDetails vpdConfigDetail : vpdConfigDetails) {
                        Expression ex = vpdExpressionBuilder.buildExpression(alias.getName(), vpdConfigDetail);
                        Expression where = plainSelect.getWhere();
                        if (where == null) {
                            EqualsTo equalsTo = new EqualsTo();
                            LongValue one = new LongValue(1);
                            equalsTo.setLeftExpression(one);
                            equalsTo.setRightExpression(one);
                            where = equalsTo;
                        }
                        Expression and = new AndExpression(ex, where);
                        plainSelect.setWhere(and);
                    }
                }
            } else if (fromItem instanceof SubJoin) {
                SubJoin subJoin = (SubJoin) fromItem;
                Join join = subJoin.getJoin();
                processJoin(join, vpdConfig);
            } else if (fromItem instanceof SubSelect) {
                SubSelect subSelect = (SubSelect) fromItem;
                SelectBody subSelectBody = subSelect.getSelectBody();
                processSelectBody(subSelectBody, vpdConfig);
            }

            // JOINS
            List<Join> joins = plainSelect.getJoins();
            if (joins != null) {
                for (Join join : joins) {
                    processJoin(join, vpdConfig);
                }
            }
            // SELECT ITEMS
            List<SelectItem> selectItems = plainSelect.getSelectItems();
            for (SelectItem selectItem : selectItems) {
                processSelectItem(selectItem, vpdConfig);
            }

            // Where
            // where语句加select子句？
            // TODO:

        }
    }

    private void processSelectItem(SelectItem selectItem, SysVpdConfig vpdConfig) throws VpdParseErrorException {
        if (selectItem instanceof SelectExpressionItem) {
            SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
            Expression exp = selectExpressionItem.getExpression();
            if (exp instanceof SubSelect) {
                SubSelect subSelect = (SubSelect) exp;
                SelectBody subSelectBody = subSelect.getSelectBody();
                processSelectBody(subSelectBody, vpdConfig);
            }
        }
    }

    private void processJoin(Join join, SysVpdConfig vpdConfig) throws VpdParseErrorException {
        String tableName = vpdConfig.getTableName().toLowerCase();

        FromItem fromItem = join.getRightItem();
        // FROM
        if (fromItem instanceof Table) {
            Table table = (Table) fromItem;
            if (tableName.equals(table.getName().toLowerCase())) {
                Alias alias = table.getAlias();
                List<SysVpdDetails> vpdConfigDetails = vpdConfig.getVpdDetails();
                for (SysVpdDetails vpdConfigDetail : vpdConfigDetails) {
                    Expression ex = vpdExpressionBuilder.buildExpression(alias.getName(), vpdConfigDetail);
                    Expression where = join.getOnExpression();
                    if (where == null) {
                        EqualsTo equalsTo = new EqualsTo();
                        LongValue one = new LongValue(1);
                        equalsTo.setLeftExpression(one);
                        equalsTo.setRightExpression(one);
                        where = equalsTo;
                    }
                    Expression and = new AndExpression(ex, where);
                    join.setOnExpression(and);
                }
            }
        } else if (fromItem instanceof SubJoin) {
            SubJoin subJoinItem = (SubJoin) fromItem;
            Join subjoin = subJoinItem.getJoin();
            processJoin(subjoin, vpdConfig);
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            SelectBody subSelectBody = subSelect.getSelectBody();
            processSelectBody(subSelectBody, vpdConfig);
        }
    }

    @Override
    public boolean canParse(String mapperId) {
        return getVpdConfigsFromCache(mapperId).size() > 0;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
