package com.examples.jdbc;

import org.springframework.lang.Nullable;

import java.sql.*;
import java.util.*;

public class JdbcDemo {
    public void query() {
        try {
            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection("jdbc:postgresql://10.41.49.243:7092/logtransmit_db", "logtransmit_db_user", "d8KuRwbn");

            // 关闭自动提交，后面需要手动调用 commit() 方法
            conn.setAutoCommit(false);
            // 配置隔离级别
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            Statement stmt = conn.createStatement();

            stmt.execute("select * from user_info");

            ResultSet resultSet = stmt.getResultSet();
            List<Map<String, Object>> maps = extractResult(resultSet);

            System.out.println(maps);

            // 提交事务才会生效
            conn.commit();

            stmt.execute("update user_info set organization_id = 'hkvs'");
            resultSet = stmt.getResultSet();
            List<Map<String, Object>> maps2 = extractResult(resultSet);

            System.out.println(maps2);

            // 提交事务才会生效
            conn.commit();

            // 关闭连接
            close(stmt, conn);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // 关闭连接
    private void close(@Nullable AutoCloseable... autoCloseables) {
        if (Objects.isNull(autoCloseables)) {
            return;
        }

        for (AutoCloseable autoCloseable : autoCloseables) {
            if (Objects.nonNull(autoCloseable)) {
                try {
                    autoCloseable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 提取 resultSet 数据
     * @param resultSet
     * @return
     */
    public static List<Map<String, Object>> extractResult(ResultSet resultSet) {
        if (Objects.isNull(resultSet)) {
            return List.of();
        }

        List<Map<String, Object>> tableData = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> rowData = new HashMap<>();
                // 提取行数据
                for (int i = 1; i <= columnCount; i++) {
                    String columnLabel = metaData.getColumnLabel(i);
                    int columnType = metaData.getColumnType(i); // 根据 type 获取 java 类型？
                    String value = resultSet.getString(i);
                    rowData.put(columnLabel, value);
                }
                tableData.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableData;
    }
}
