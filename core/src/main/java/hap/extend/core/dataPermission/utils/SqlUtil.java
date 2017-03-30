package hap.extend.core.dataPermission.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static Pattern limitOffsetPattern = Pattern.compile("LIMIT \\? OFFSET \\?");
    public static Pattern limitPattern = Pattern.compile("LIMIT \\?,\\?");

    /**
     * generate new sql with additional Condition
     * @param oldSql
     * @param conditionSql
     * @return new sql with additional Condition
     * @throws JSQLParserException
     */
    public static String addConditionToSql(final String oldSql, final String conditionSql) throws JSQLParserException {
        if(isNull(conditionSql)){
            return oldSql;
        }
        String newSql = oldSql;

        if(ConfigUtil.enableWrapSqlWithSelectAll && "MYSQL".equalsIgnoreCase(ConfigUtil.dbType)){
            String source = oldSql.toUpperCase();
            boolean flag = false;
            String tempSql = oldSql;

            if(source.endsWith("LIMIT ? OFFSET ?")){
                int lastIndex = source.lastIndexOf("LIMIT ? OFFSET ?");
                tempSql = oldSql.substring(0,lastIndex);
                flag = true;
            }
            if(source.endsWith("LIMIT ?,?")){
                int lastIndex = source.lastIndexOf("LIMIT ?,?");
                tempSql = oldSql.substring(0,lastIndex);
                flag = true;
            }

            if(flag){
                newSql = "SELECT HAP_DP_TEMP.* FROM ("+ tempSql+") HAP_DP_TEMP LIMIT ?,?";
            }else {
                newSql = "SELECT HAP_DP_TEMP.* FROM ("+ tempSql+") HAP_DP_TEMP";
            }
        }
        else if(!ConfigUtil.enableWrapSqlWithSelectAll && "ORACLE".equalsIgnoreCase(ConfigUtil.dbType)){
            Select oldSelect = (Select) CCJSqlParserUtil.parse(newSql);
            String oldSqlStrGenerated = oldSelect.toString();
            Expression pageWhere = ((PlainSelect) oldSelect.getSelectBody()).getWhere();
            if(isNotNull(pageWhere) && "row_id > ?".equalsIgnoreCase(pageWhere.toString())){
                FromItem fromItem = ((PlainSelect) oldSelect.getSelectBody()).getFromItem();
                if(isNotNull(fromItem)){
                    String temp_1 = fromItem.toString().substring(1,fromItem.toString().length()-1);
                    Select nestedSelect = (Select) CCJSqlParserUtil.parse(temp_1);
                    if(isNotNull(nestedSelect)){
                        FromItem sourceSqlFromIem = ((PlainSelect) nestedSelect.getSelectBody()).getFromItem();
                        int lastIndex = sourceSqlFromIem.toString().lastIndexOf(")");
                        String sourceSql = sourceSqlFromIem.toString().substring(1,lastIndex);
                        Select sourceSelect = (Select) CCJSqlParserUtil.parse(sourceSql);
                        Expression where = ((PlainSelect) sourceSelect.getSelectBody()).getWhere();
                        Expression expression = null;
                        if(isNull(where) || where.toString().length() < 1){
                            expression = CCJSqlParserUtil.parseCondExpression(conditionSql);
                        }else {
                            expression = CCJSqlParserUtil.parseCondExpression(where.toString() + " " + FIELD_SQL_AND + " " + conditionSql);
                        }
                        ((PlainSelect) sourceSelect.getSelectBody()).setWhere(expression);

                        oldSqlStrGenerated = oldSqlStrGenerated.replace(sourceSql,sourceSelect.toString());

                        logger.info("\nsql with data permission:\n{}\n",oldSqlStrGenerated);
                        return oldSqlStrGenerated;
                    }
                }
            }
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
        logger.info("\nsql with data permission:\n{}\n",select.toString());
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
}
