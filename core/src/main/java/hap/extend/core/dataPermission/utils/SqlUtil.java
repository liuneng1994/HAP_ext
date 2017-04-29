package hap.extend.core.dataPermission.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hap.extend.core.dataPermission.utils.LangUtils.isNotNull;
import static hap.extend.core.dataPermission.utils.LangUtils.isNull;

/**
 * Created by yyz on 2017/2/24.
 *
 * @author yazheng.yang@hand-china.com
 */
public final class SqlUtil {
    public static Logger logger = LoggerFactory.getLogger(SqlUtil.class);

    public static final String FIELD_SQL_AND = "AND";
    public static final Pattern limitOffsetPattern = Pattern.compile("LIMIT \\? OFFSET \\?");
    public static final Pattern limitPattern = Pattern.compile("LIMIT \\?,\\?");
    public static final String WRAP_SELECT_ALL_SQL_TEMPLATE_1 = "SELECT * FROM (%s) DATA_PMS_TEMP WHERE %s";
    public static final String WRAP_SELECT_ALL_SQL_TEMPLATE_2 = "SELECT * FROM (%s) DATA_PMS_TEMP ";

    /**
     * generate new sql with additional Condition
     * @param oldSql
     * @param conditionSql
     * @return new sql with additional Condition
     * @throws JSQLParserException
     */
    public static String addConditionToSql(final String oldSql, final String conditionSql, final boolean isCountMethod) throws JSQLParserException {
        if(isNull(conditionSql)){
            return oldSql;
        }
        String newSql = oldSql.toUpperCase();

        /** add to enable wrap select * from() outside source sql*/
        if(ConfigUtil.enableWrapSqlWithSelectAll){
            return wrapSqlWithSelectAll(oldSql,conditionSql);
        }
        /** add end*/


        if("".equals(conditionSql)){
            return oldSql;
        }

        Select select = (Select) CCJSqlParserUtil.parse(newSql);
        Expression where = ((PlainSelect) select.getSelectBody()).getWhere();
        Expression expression = null;
        if(isNull(where) || where.toString().length() < 1){
            expression = CCJSqlParserUtil.parseCondExpression(conditionSql);
        }else {
            expression = CCJSqlParserUtil.parseCondExpression(where.toString() + " " + FIELD_SQL_AND + " " + conditionSql);
        }
        ((PlainSelect) select.getSelectBody()).setWhere(expression);
//        logger.info("\nsql with data permission:\n{}\n",select.toString());
        return select.toString();
    }

    public static String replaceLimit(String sql){
        if(isNull(sql)){
            return sql;
        }
        String source = sql.toUpperCase();
        if(!source.contains("LIMIT")){
            return sql;
        }

        Matcher m = limitOffsetPattern.matcher(source);
        return m.replaceAll("LIMIT \\?,\\?");
    }


    /**
     * generate new sql with additional Condition
     * @param oldSql
     * @param conditionSql
     * @return new sql with additional Condition
     * @throws JSQLParserException
     */
    public static String addConditionToSql_0(final String oldSql, final String conditionSql) throws JSQLParserException {
        if(isNull(conditionSql) || isNull(oldSql)){
            return oldSql;
        }

        /** add to enable wrap select * from() outside source sql*/
        if(ConfigUtil.enableWrapSqlWithSelectAll){
            return wrapSqlWithSelectAll(oldSql,conditionSql);
        }
        /** add end*/


        if("".equals(conditionSql)){
            return oldSql;
        }
        String newSql = oldSql.toUpperCase();

        Select select = (Select) CCJSqlParserUtil.parse(newSql);
        Expression where = ((PlainSelect) select.getSelectBody()).getWhere();
        Expression expression = null;
        if(isNull(where) || where.toString().length() < 1){
            expression = CCJSqlParserUtil.parseCondExpression(conditionSql);
        }else {
            expression = CCJSqlParserUtil.parseCondExpression(where.toString() + " " + FIELD_SQL_AND + " " + conditionSql);
        }
        ((PlainSelect) select.getSelectBody()).setWhere(expression);
//        logger.info("\nsql with data permission:\n{}\n",select.toString());
        return select.toString();
    }

    public static String wrapSqlWithSelectAll(final String oldSql, final String conditionSql){
        if(isNull(conditionSql) || isNull(oldSql)){
            return oldSql;
        }
        if(!"".equals(conditionSql.trim())){
            return String.format(WRAP_SELECT_ALL_SQL_TEMPLATE_1,oldSql,conditionSql);
        }else {
            return String.format(WRAP_SELECT_ALL_SQL_TEMPLATE_2,oldSql);
        }
    }

    public static String removeLimitOffset(String sourceSql){
        return sourceSql.replaceAll("LIMIT \\? OFFSET \\?","");
    }
}
