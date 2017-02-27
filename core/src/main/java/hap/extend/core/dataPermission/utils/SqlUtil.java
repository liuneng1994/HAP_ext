package hap.extend.core.dataPermission.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static hap.extend.core.dataPermission.utils.LangUtils.isNull;

/**
 * Created by yyz on 2017/2/24.
 *
 * @author yazheng.yang@hand-china.com
 */
public final class SqlUtil {
    public static Logger logger = LoggerFactory.getLogger(SqlUtil.class);

    public static final String FIELD_SQL_AND = "AND";

    /**
     * generate new sql with additional Condition
     * @param oldSql
     * @param conditionSql
     * @return new sql with additional Condition
     * @throws JSQLParserException
     */
    public static String addConditionToSql(final String oldSql, final String conditionSql) throws JSQLParserException {
        Select select = (Select) CCJSqlParserUtil.parse(oldSql);
        Expression where = ((PlainSelect) select.getSelectBody()).getWhere();
        Expression expression = null;
        if(isNull(where) || where.toString().length() < 1){
            expression = CCJSqlParserUtil.parseCondExpression(conditionSql);
        }else {
            expression = CCJSqlParserUtil.parseCondExpression(where.toString() + " " + FIELD_SQL_AND + " " + conditionSql);
        }
        ((PlainSelect) select.getSelectBody()).setWhere(expression);
        logger.info("\n\nsql with data permission:\n{}\n\n",select.toString());
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
        Pattern p = Pattern.compile("LIMIT \\? OFFSET \\?");
        Matcher m = p.matcher(source);
        return m.replaceAll("LIMIT \\?,\\?");
    }
}
