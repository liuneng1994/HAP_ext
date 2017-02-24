/*
 * #{copyright}#
 */
package com.lkkhpg.dsis.platform.vpd.parser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import com.lkkhpg.dsis.platform.vpd.dto.SysVpdDetails;
import com.lkkhpg.dsis.platform.vpd.exception.VpdParseErrorException;

/**
 * VPD表达式构建.
 * @author chenjingxiong
 */
public class VpdExpressionBuilder {

    private static final String PATTERN_LONG_VALUE = "^-?[1-9]\\d*$";

    private static final String PATTERN_DOUBLE_VALUE = "[-]*[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$";

    private static final String PATTERN_DATE_VALUE = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";

    private static final String PATTERN_STRING_VALUE = "'.+'";

    private static final String PATTERN_EXPRESSION_VALUE = "\\$\\{request\\..+\\}";

    private static final String PATTERN_ALIAS_VALUE = "\\$\\{alias\\}";

    private static final String PATTERN_EXPRESSION_LIST = "\\(([^,]+,)+[^,]*\\)";

    private static final String QUATATION_MARK = "'";

    private static final String LIST_SPLITOR = ",";

    private static final int INDEX_OF_EXPRESSION = 10;

    private static final int INDEX_OF_ALIAS = 8;

    public Expression buildExpression(String alias, SysVpdDetails vpdConfigDetail) throws VpdParseErrorException {
        return configDetailsToExpression(alias, vpdConfigDetail);
    }

    private Expression configDetailsToExpression(String alias, SysVpdDetails vpdConfigDetail)
            throws VpdParseErrorException {
        Expression expression = null;
        String operation = vpdConfigDetail.getOperation();

        switch (operation) {
        case SysVpdDetails.OPERATION_EQUALS:
            expression = processEquals(alias, vpdConfigDetail);
            break;
        case SysVpdDetails.OPERATION_IN:
            expression = processInExpression(alias, vpdConfigDetail);
            break;
        case SysVpdDetails.OPERATION_NOT_EQUALS:
            expression = processNotEquals(alias, vpdConfigDetail);
            break;
        case SysVpdDetails.OPERATION_GT:
            expression = processGreaterThan(alias, vpdConfigDetail);
            break;
        case SysVpdDetails.OPERATION_GE:
            expression = processGreaterAndEquals(alias, vpdConfigDetail);
            break;
        case SysVpdDetails.OPERATION_LT:
            expression = processMinorThan(alias, vpdConfigDetail);
            break;
        case SysVpdDetails.OPERATION_LE:
            expression = processMinorAndEquals(alias, vpdConfigDetail);
            break;
        default:
            break;
        }

        return expression;
    }

    private Expression processMinorThan(String alias, SysVpdDetails vpdConfigDetail) throws VpdParseErrorException {
        MinorThan minorThan = new MinorThan();
        String columnName = vpdConfigDetail.getColumnName();

        Column column = new Column(new Table(alias), columnName);
        minorThan.setLeftExpression(column);

        Expression jsqlParserExp = toJsqlparserExpression(alias, vpdConfigDetail.getExpression());
        minorThan.setRightExpression(jsqlParserExp);

        return minorThan;
    }

    private Expression processMinorAndEquals(String alias, SysVpdDetails vpdConfigDetail)
            throws VpdParseErrorException {
        MinorThanEquals minorThanEquals = new MinorThanEquals();
        String columnName = vpdConfigDetail.getColumnName();

        Column column = new Column(new Table(alias), columnName);
        minorThanEquals.setLeftExpression(column);

        Expression jsqlParserExp = toJsqlparserExpression(alias, vpdConfigDetail.getExpression());
        minorThanEquals.setRightExpression(jsqlParserExp);

        return minorThanEquals;
    }

    private Expression processGreaterThan(String alias, SysVpdDetails vpdConfigDetail) throws VpdParseErrorException {
        GreaterThan greaterThan = new GreaterThan();
        String columnName = vpdConfigDetail.getColumnName();

        Column column = new Column(new Table(alias), columnName);
        greaterThan.setLeftExpression(column);

        Expression jsqlParserExp = toJsqlparserExpression(alias, vpdConfigDetail.getExpression());
        greaterThan.setRightExpression(jsqlParserExp);

        return greaterThan;
    }

    private Expression processGreaterAndEquals(String alias, SysVpdDetails vpdConfigDetail)
            throws VpdParseErrorException {
        GreaterThanEquals greaterThanEquals = new GreaterThanEquals();
        String columnName = vpdConfigDetail.getColumnName();

        Column column = new Column(new Table(alias), columnName);
        greaterThanEquals.setLeftExpression(column);

        Expression jsqlParserExp = toJsqlparserExpression(alias, vpdConfigDetail.getExpression());
        greaterThanEquals.setRightExpression(jsqlParserExp);

        return greaterThanEquals;
    }

    private Expression processEquals(String alias, SysVpdDetails vpdConfigDetail) throws VpdParseErrorException {
        EqualsTo equalsTo = new EqualsTo();
        String columnName = vpdConfigDetail.getColumnName();

        Column column = new Column(new Table(alias), columnName);
        equalsTo.setLeftExpression(column);

        Expression jsqlParserExp = toJsqlparserExpression(alias, vpdConfigDetail.getExpression());
        equalsTo.setRightExpression(jsqlParserExp);

        return equalsTo;
    }

    private Expression processNotEquals(String alias, SysVpdDetails vpdConfigDetail) throws VpdParseErrorException {
        NotEqualsTo notEqualsTo = new NotEqualsTo();
        String columnName = vpdConfigDetail.getColumnName();

        Column column = new Column(new Table(alias), columnName);
        notEqualsTo.setLeftExpression(column);

        Expression jsqlParserExp = toJsqlparserExpression(alias, vpdConfigDetail.getExpression());
        notEqualsTo.setRightExpression(jsqlParserExp);

        return notEqualsTo;
    }

    private Expression processInExpression(String alias, SysVpdDetails vpdConfigDetail) throws VpdParseErrorException {
        InExpression inExpression = new InExpression();
        String columnName = vpdConfigDetail.getColumnName();

        Column column = new Column(new Table(alias), columnName);
        inExpression.setLeftExpression(column);

        ExpressionList jsqlParserExpList = toJsqlparserExpressionList(alias, vpdConfigDetail.getExpression());
        inExpression.setRightItemsList(jsqlParserExpList);

        return inExpression;
    }

    private ExpressionList toJsqlparserExpressionList(String alias, String expression) throws VpdParseErrorException {
        ExpressionList expressionList = new ExpressionList();
        List<Expression> expressionArrayList = new ArrayList<>();
        if (Pattern.matches(PATTERN_EXPRESSION_LIST, expression)) {
            // 表达式${request.attributeName}
            Matcher matcher = Pattern.compile(PATTERN_EXPRESSION_LIST).matcher(expression);
            if (matcher.find()) {
                String expressionValue = matcher.group();
                expressionValue = expressionValue.substring(1, expressionValue.length() - 1);

                String[] expressionArray = expressionValue.split(LIST_SPLITOR);
                for (String expressionStr : expressionArray) {
                    Expression jsqlParserExp = toJsqlparserExpression(alias, expressionStr);
                    expressionArrayList.add(jsqlParserExp);
                }
                expressionList.setExpressions(expressionArrayList);
            }
        }
        return expressionList;
    }

    /**
     * @param alias
     * @param expression
     * @return
     * @throws VpdParseErrorException
     *             表达式支持类型 'XXX' 字符串 123 数字 NULL 空 '2015-10-12' 日期
     *             to_date('xxxx','yyyyy') function ${request.attributeName}
     *             ${alias}.xxxx
     */
    private Expression toJsqlparserExpression(String alias, String expression) throws VpdParseErrorException {
        IRequest request = RequestHelper.getCurrentRequest();

        if (expression != null && "null".equals(expression.toLowerCase())) {
            // 空值
            return new NullValue();
        } else if (Pattern.matches(PATTERN_EXPRESSION_VALUE, expression)) {
            // 表达式${request.attributeName}
            Matcher matcher = Pattern.compile(PATTERN_EXPRESSION_VALUE).matcher(expression);
            if (matcher.find()) {
                String expressionValue = matcher.group();
                expressionValue = expressionValue.substring(INDEX_OF_EXPRESSION, expressionValue.length() - 1);
                // 获取上下文值
                Object obj = request.getAttribute(expressionValue);
                if (obj == null) {
                    return new NullValue();
                } else if (obj instanceof String) {
                    String stringValue = QUATATION_MARK + obj.toString() + QUATATION_MARK;
                    return new StringValue(stringValue);
                } else if (obj instanceof Long) {
                    return new LongValue((Long) obj);
                } else if (obj instanceof Double) {
                    return new DoubleValue(obj.toString());
                }
            }
        } else if (Pattern.matches(PATTERN_ALIAS_VALUE, expression)) {
            // 表达式${alias}.columnName，该表的某些字段
            Matcher matcher = Pattern.compile(PATTERN_ALIAS_VALUE).matcher(expression);
            if (matcher.find()) {
                String columnName = matcher.group();
                columnName.substring(INDEX_OF_ALIAS);
                return new Column(new Table(alias), columnName);
            }
        } else if (Pattern.matches(PATTERN_DATE_VALUE, expression)) {
            // 日期格式
            Matcher matcher = Pattern.compile(PATTERN_DATE_VALUE).matcher(expression);
            if (matcher.find()) {
                String date = matcher.group();
                if (!date.startsWith(QUATATION_MARK)) {
                    date = QUATATION_MARK + date + QUATATION_MARK;
                }

                return new DateValue(date);
            }

        } else if (Pattern.matches(PATTERN_STRING_VALUE, expression)) {
            // 字符串格式
            Matcher matcher = Pattern.compile(PATTERN_STRING_VALUE).matcher(expression);
            if (matcher.find()) {
                String stringValue = matcher.group();
                return new StringValue(stringValue);
            }
        } else if (Pattern.matches(PATTERN_DOUBLE_VALUE, expression)) {
            // 浮点数格式
            Matcher matcher = Pattern.compile(PATTERN_DOUBLE_VALUE).matcher(expression);
            if (matcher.find()) {
                String doubleValue = matcher.group();
                return new DoubleValue(doubleValue);
            }
        } else if (Pattern.matches(PATTERN_LONG_VALUE, expression)) {
            // 整数格式
            Matcher matcher = Pattern.compile(PATTERN_LONG_VALUE).matcher(expression);
            if (matcher.find()) {
                String longValue = matcher.group();
                return new LongValue(longValue);
            }
        }
        throw new VpdParseErrorException(expression);
    }

}
