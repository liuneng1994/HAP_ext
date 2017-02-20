package hap.extend.core.dataPermission.interceptor;


import java.sql.Connection;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

@Intercepts(@Signature(type=StatementHandler.class,method="prepare",args={Connection.class}))
public class OraclePagingInterceptor extends AbstractPagingInterceptor {

    @Override
    protected String getSelectTotalSql(String targetSql) {
        String sql = targetSql.toLowerCase();
        StringBuilder sqlBuilder = new StringBuilder(sql);

        int orderByPos = 0;
        if((orderByPos = sqlBuilder.lastIndexOf(ORDER_BY)) != -1) {
            sqlBuilder.delete(orderByPos, sqlBuilder.length());
        }
        sqlBuilder.insert(0, "select count(1) as _count from ( ").append(" )");

        return sqlBuilder.toString();
    }

    @Override
    protected String getSelectPagingSql(String targetSql, PagingBounds bounds) {
        String sql = targetSql.toLowerCase();
        StringBuilder sqlBuilder = new StringBuilder(sql);

        sqlBuilder.insert(0, "select * from ( select table_alias.*, rownum mybatis_rowNo from (");
        sqlBuilder.append(") ");
        sqlBuilder.append("table_alias where rownum <= " + bounds.getSelectCount()).append(")");
        sqlBuilder.append("where mybatis_rowNo >= " + (bounds.getOffset() + 1));

        return sqlBuilder.toString();
    }

}
