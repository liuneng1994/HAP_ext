package hap.extend.core.switchUser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.SelectUtils;

/**
 * Created by yyz on 2017/2/8.
 *
 * @author yazheng.yang@hand-china.com
 */
public class Test {
    public static void main(String[] args) throws JSQLParserException {
        Expression expr = CCJSqlParserUtil.parseExpression("hh=1");
//        AndExpression expression = (AndExpression) CCJSqlParserUtil.parseCondExpression("hh=2");

        Select select = (Select) CCJSqlParserUtil.parse("SELECT * FROM   tab1");

//        SelectUtils.addExpression(select,expression);

        System.out.println(select.toString());
        Expression where = ((PlainSelect) select.getSelectBody()).getWhere();//如果没有where，那么返回null
        ((PlainSelect) select.getSelectBody()).setWhere(CCJSqlParserUtil.parseCondExpression("hh=1 and hehe=1"));
//        System.out.println(where.toString());
        System.out.println(select.toString());
    }
}
