//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.pagehelper;

import com.github.pagehelper.Constant;
import com.github.pagehelper.Dialect;
import com.github.pagehelper.MSUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.SqlUtilConfig;
import com.github.pagehelper.StringUtil;
import com.github.pagehelper.parser.Parser;
import com.github.pagehelper.parser.impl.AbstractParser;
import com.github.pagehelper.sqlsource.PageDynamicSqlSource;
import com.github.pagehelper.sqlsource.PageProviderSqlSource;
import com.github.pagehelper.sqlsource.PageRawSqlSource;
import com.github.pagehelper.sqlsource.PageSqlSource;
import com.github.pagehelper.sqlsource.PageStaticSqlSource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import hap.extend.core.dataPermission.utils.DPDynamicSqlSource;
import hap.extend.core.dataPermission.utils.DPPageDynamicSqlSource;
import hap.extend.core.dataPermission.utils.NewDPDynamicSqlSource;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.session.RowBounds;

import javax.validation.constraints.NotNull;

import static hap.extend.core.dataPermission.utils.LangUtils.isNull;

public class SqlUtil implements Constant {
    private static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal();
    private static Map<String, String> PARAMS = new HashMap(5);
    private static Boolean hasRequest;
    private static Class<?> requestClass;
    private static Method getParameterMap;
    private static final Map<String, MappedStatement> msCountMap;
    private boolean offsetAsPageNum = false;
    private boolean rowBoundsWithCount = false;
    private boolean pageSizeZero = false;
    private boolean reasonable = false;
    private Parser parser;
    private boolean supportMethodsArguments = false;

    public SqlUtil(String strDialect) {
        if(strDialect != null && !"".equals(strDialect)) {
            Object exception = null;

            try {
                Dialect e = Dialect.of(strDialect);
                this.parser = AbstractParser.newParser(e);
            } catch (Exception var8) {
                exception = var8;

                try {
                    Class ex = Class.forName(strDialect);
                    if(Parser.class.isAssignableFrom(ex)) {
                        this.parser = (Parser)ex.newInstance();
                    }
                } catch (ClassNotFoundException var5) {
                    exception = var5;
                } catch (InstantiationException var6) {
                    exception = var6;
                } catch (IllegalAccessException var7) {
                    exception = var7;
                }
            }

            if(this.parser == null) {
                throw new RuntimeException((Throwable)exception);
            }
        } else {
            throw new IllegalArgumentException("Mybatis分页插件无法获取dialect参数!");
        }
    }

    public static Boolean getCOUNT() {
        Page page = getLocalPage();
        return page != null?page.getCountSignal():null;
    }

    public static <T> Page<T> getLocalPage() {
        return (Page)LOCAL_PAGE.get();
    }

    public static void setLocalPage(Page page) {
        LOCAL_PAGE.set(page);
    }

    public static void clearLocalPage() {
        LOCAL_PAGE.remove();
    }

    public static <T> Page<T> getPageFromObject(Object params) {
        MetaObject paramsObject = null;
        if(params == null) {
            throw new NullPointerException("无法获取分页查询参数!");
        } else {
            if(hasRequest.booleanValue() && requestClass.isAssignableFrom(params.getClass())) {
                try {
                    paramsObject = SystemMetaObject.forObject(getParameterMap.invoke(params, new Object[0]));
                } catch (Exception var10) {
                    ;
                }
            } else {
                paramsObject = SystemMetaObject.forObject(params);
            }

            if(paramsObject == null) {
                throw new NullPointerException("分页查询参数处理失败!");
            } else {
                Object orderBy = getParamValue(paramsObject, "orderBy", false);
                boolean hasOrderBy = false;
                if(orderBy != null && orderBy.toString().length() > 0) {
                    hasOrderBy = true;
                }

                int pageNum;
                int pageSize;
                Object _count;
                try {
                    Object page = getParamValue(paramsObject, "pageNum", !hasOrderBy);
                    _count = getParamValue(paramsObject, "pageSize", !hasOrderBy);
                    if(page == null || _count == null) {
                        Page reasonable1 = new Page();
                        reasonable1.setOrderBy(orderBy.toString());
                        reasonable1.setOrderByOnly(true);
                        return reasonable1;
                    }

                    pageNum = Integer.parseInt(String.valueOf(page));
                    pageSize = Integer.parseInt(String.valueOf(_count));
                } catch (NumberFormatException var11) {
                    throw new IllegalArgumentException("分页参数不是合法的数字类型!");
                }

                Page page1 = new Page(pageNum, pageSize);
                _count = getParamValue(paramsObject, "count", false);
                if(_count != null) {
                    page1.setCount(Boolean.valueOf(String.valueOf(_count)).booleanValue());
                }

                if(hasOrderBy) {
                    page1.setOrderBy(orderBy.toString());
                }

                Object reasonable = getParamValue(paramsObject, "reasonable", false);
                if(reasonable != null) {
                    page1.setReasonable(Boolean.valueOf(String.valueOf(reasonable)));
                }

                Object pageSizeZero = getParamValue(paramsObject, "pageSizeZero", false);
                if(pageSizeZero != null) {
                    page1.setPageSizeZero(Boolean.valueOf(String.valueOf(pageSizeZero)));
                }

                return page1;
            }
        }
    }

    public static Object getParamValue(MetaObject paramsObject, String paramName, boolean required) {
        Object value = null;
        if(paramsObject.hasGetter((String)PARAMS.get(paramName))) {
            value = paramsObject.getValue((String)PARAMS.get(paramName));
        }

        if(value != null && value.getClass().isArray()) {
            Object[] values = (Object[])((Object[])value);
            if(values.length == 0) {
                value = null;
            } else {
                value = values[0];
            }
        }

        if(required && value == null) {
            throw new RuntimeException("分页查询缺少必要的参数:" + (String)PARAMS.get(paramName));
        } else {
            return value;
        }
    }

    public boolean isPageSqlSource(MappedStatement ms) {
        return ms.getSqlSource() instanceof PageSqlSource;
    }

    /** @deprecated */
    @Deprecated
    public static void testSql(String dialect, String originalSql) {
        testSql(Dialect.of(dialect), originalSql);
    }

    /** @deprecated */
    @Deprecated
    public static void testSql(Dialect dialect, String originalSql) {
        Parser parser = AbstractParser.newParser(dialect);
        if(dialect == Dialect.sqlserver) {
            setLocalPage(new Page(1, 10));
        }

        String countSql = parser.getCountSql(originalSql);
        System.out.println(countSql);
        String pageSql = parser.getPageSql(originalSql);
        System.out.println(pageSql);
        if(dialect == Dialect.sqlserver) {
            clearLocalPage();
        }

    }

    public void processMappedStatement(@NotNull MappedStatement ms) throws Throwable {
        SqlSource sqlSource = ms.getSqlSource();
        MetaObject msObject = SystemMetaObject.forObject(ms);
        Object pageSqlSource = null;
        if(sqlSource instanceof StaticSqlSource) {
            pageSqlSource = new PageStaticSqlSource((StaticSqlSource)sqlSource);
        } else if(sqlSource instanceof RawSqlSource) {
            pageSqlSource = new PageRawSqlSource((RawSqlSource)sqlSource);
        } else if(sqlSource instanceof ProviderSqlSource) {
            pageSqlSource = new PageProviderSqlSource((ProviderSqlSource)sqlSource);
        } else {
            if(!(sqlSource instanceof DynamicSqlSource)) {
                throw new RuntimeException("无法处理该类型[" + sqlSource.getClass() + "]的SqlSource");
            }
            if( sqlSource instanceof DPDynamicSqlSource){
                DPDynamicSqlSource temp = (DPDynamicSqlSource)sqlSource;
//                pageSqlSource = new DPPageDynamicSqlSource(temp.getConfiguration(),temp.getRootSqlNode(),temp.getTlOfConditionSql(),temp.getTlOfIsCountFlag());
                pageSqlSource = new NewDPDynamicSqlSource(temp.getConfiguration(),temp.getRootSqlNode(),
                        isNull(temp.getTlOfConditionSql())?"":temp.getTlOfConditionSql().get());
            }else {
                pageSqlSource = new PageDynamicSqlSource((DynamicSqlSource)sqlSource);
            }
        }

        msObject.setValue("sqlSource", pageSqlSource);
        msCountMap.put(ms.getId(), MSUtils.newCountMappedStatement(ms));
    }

    public Page getPage(Object[] args) {
        Page page = getLocalPage();
        if(page == null || page.isOrderByOnly()) {
            Page oldPage = page;
            if((args[2] == null || args[2] == RowBounds.DEFAULT) && page != null) {
                return page;
            }

            if(args[2] instanceof RowBounds && args[2] != RowBounds.DEFAULT) {
                RowBounds e = (RowBounds)args[2];
                if(this.offsetAsPageNum) {
                    page = new Page(e.getOffset(), e.getLimit(), this.rowBoundsWithCount);
                } else {
                    page = new Page(new int[]{e.getOffset(), e.getLimit()}, this.rowBoundsWithCount);
                    page.setReasonable(Boolean.valueOf(false));
                }
            } else {
                try {
                    page = getPageFromObject(args[1]);
                } catch (Exception var5) {
                    return null;
                }
            }

            if(oldPage != null) {
                page.setOrderBy(oldPage.getOrderBy());
            }

            setLocalPage(page);
        }

        if(page.getReasonable() == null) {
            page.setReasonable(Boolean.valueOf(this.reasonable));
        }

        if(page.getPageSizeZero() == null) {
            page.setPageSizeZero(Boolean.valueOf(this.pageSizeZero));
        }

        return page;
    }

    public Object processPage(Invocation invocation) throws Throwable {
        Object var3;
        try {
            Object result = this._processPage(invocation);
            var3 = result;
        } finally {
            clearLocalPage();
        }

        return var3;
    }

    private Object _processPage(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        Page page = null;
        if(this.supportMethodsArguments) {
            page = this.getPage(args);
        }

        RowBounds rowBounds = (RowBounds)args[2];
        if(this.supportMethodsArguments && page == null || !this.supportMethodsArguments && getLocalPage() == null && rowBounds == RowBounds.DEFAULT) {
            return invocation.proceed();
        } else {
            if(!this.supportMethodsArguments && page == null) {
                page = this.getPage(args);
            }

            return this.doProcessPage(invocation, page, args);
        }
    }

    private boolean isQueryOnly(Page page) {
        return page.isOrderByOnly() || page.getPageSizeZero() != null && page.getPageSizeZero().booleanValue() && page.getPageSize() == 0;
    }

    private Page doQueryOnly(Page page, Invocation invocation) throws Throwable {
        page.setCountSignal((Boolean)null);
        Object result = invocation.proceed();
        page.addAll((List)result);
        page.setPageNum(1);
        page.setPageSize(page.size());
        page.setTotal((long)page.size());
        return page;
    }

    public void processStatement(@NotNull MappedStatement ms) throws Throwable {
        SqlSource sqlSource = ms.getSqlSource();
        MetaObject msObject = SystemMetaObject.forObject(ms);

        if(sqlSource instanceof NewDPDynamicSqlSource) {
            NewDPDynamicSqlSource temp = (NewDPDynamicSqlSource)sqlSource;
            ThreadLocal<String> tlOfConditionSql = temp.getTlOfConditionSql();
            String conditionSql = "";
            if(!isNull(tlOfConditionSql) || !isNull(tlOfConditionSql.get())){
                conditionSql = tlOfConditionSql.get();
            }
            NewDPDynamicSqlSource newDPDynamicSqlSource = new NewDPDynamicSqlSource(temp.getConfiguration(),temp.getRootSqlNode(),conditionSql);
            msObject.setValue("sqlSource", newDPDynamicSqlSource);
            msCountMap.put(ms.getId(), MSUtils.newCountMappedStatement(ms));
        }
    }

    private Page doProcessPage(Invocation invocation, Page page, Object[] args) throws Throwable {
        RowBounds rowBounds = (RowBounds)args[2];
        MappedStatement ms = (MappedStatement)args[0];
        if(!this.isPageSqlSource(ms)) {
            this.processMappedStatement(ms);
        }else {
            this.processStatement(ms);
        }

        ((PageSqlSource)ms.getSqlSource()).setParser(this.parser);

        Page boundSql;
        try {
            args[2] = RowBounds.DEFAULT;
            if(!this.isQueryOnly(page)) {
                if(page.isCount()) {
                    page.setCountSignal(Boolean.TRUE);
                    args[0] = msCountMap.get(ms.getId());
                    Object boundSql1 = invocation.proceed();
                    args[0] = ms;
                    page.setTotal((long)((Integer)((List)boundSql1).get(0)).intValue());
                    if(page.getTotal() == 0L) {
                        Page result = page;
                        return result;
                    }
                } else {
                    page.setTotal(-1L);
                }

                if(page.getPageSize() > 0 && (rowBounds == RowBounds.DEFAULT && page.getPageNum() > 0 || rowBounds != RowBounds.DEFAULT)) {
                    page.setCountSignal((Boolean)null);
                    BoundSql boundSql2 = ms.getBoundSql(args[1]);
                    args[1] = this.parser.setPageParameter(ms, args[1], boundSql2, page);
                    page.setCountSignal(Boolean.FALSE);
                    Object result1 = invocation.proceed();
                    page.addAll((List)result1);
                }

                return page;
            }

            boundSql = this.doQueryOnly(page, invocation);
        } finally {
            ((PageSqlSource)ms.getSqlSource()).removeParser();
        }

        return boundSql;
    }

    public void setOffsetAsPageNum(boolean offsetAsPageNum) {
        this.offsetAsPageNum = offsetAsPageNum;
    }

    public void setRowBoundsWithCount(boolean rowBoundsWithCount) {
        this.rowBoundsWithCount = rowBoundsWithCount;
    }

    public void setPageSizeZero(boolean pageSizeZero) {
        this.pageSizeZero = pageSizeZero;
    }

    public void setReasonable(boolean reasonable) {
        this.reasonable = reasonable;
    }

    public void setSupportMethodsArguments(boolean supportMethodsArguments) {
        this.supportMethodsArguments = supportMethodsArguments;
    }

    public static void setParams(String params) {
        PARAMS.put("pageNum", "pageNum");
        PARAMS.put("pageSize", "pageSize");
        PARAMS.put("count", "countSql");
        PARAMS.put("orderBy", "orderBy");
        PARAMS.put("reasonable", "reasonable");
        PARAMS.put("pageSizeZero", "pageSizeZero");
        if(StringUtil.isNotEmpty(params)) {
            String[] ps = params.split("[;|,|&]");
            String[] var2 = ps;
            int var3 = ps.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String s = var2[var4];
                String[] ss = s.split("[=|:]");
                if(ss.length == 2) {
                    PARAMS.put(ss[0], ss[1]);
                }
            }
        }

    }

    public void setProperties(Properties p) {
        String offsetAsPageNum = p.getProperty("offsetAsPageNum");
        this.offsetAsPageNum = Boolean.parseBoolean(offsetAsPageNum);
        String rowBoundsWithCount = p.getProperty("rowBoundsWithCount");
        this.rowBoundsWithCount = Boolean.parseBoolean(rowBoundsWithCount);
        String pageSizeZero = p.getProperty("pageSizeZero");
        this.pageSizeZero = Boolean.parseBoolean(pageSizeZero);
        String reasonable = p.getProperty("reasonable");
        this.reasonable = Boolean.parseBoolean(reasonable);
        String supportMethodsArguments = p.getProperty("supportMethodsArguments");
        this.supportMethodsArguments = Boolean.parseBoolean(supportMethodsArguments);
        setParams(p.getProperty("params"));
    }

    public void setSqlUtilConfig(SqlUtilConfig config) {
        this.offsetAsPageNum = config.isOffsetAsPageNum();
        this.rowBoundsWithCount = config.isRowBoundsWithCount();
        this.pageSizeZero = config.isPageSizeZero();
        this.reasonable = config.isReasonable();
        this.supportMethodsArguments = config.isSupportMethodsArguments();
        setParams(config.getParams());
    }

    static {
        try {
            requestClass = Class.forName("javax.servlet.ServletRequest");
            getParameterMap = requestClass.getMethod("getParameterMap", new Class[0]);
            hasRequest = Boolean.valueOf(true);
        } catch (Throwable var1) {
            hasRequest = Boolean.valueOf(false);
        }

        msCountMap = new ConcurrentHashMap();
    }
}
