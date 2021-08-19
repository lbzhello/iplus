package com.examples.jdbc;

import org.junit.jupiter.api.Test;

public class JdbcTest {
    @Test
    public void jdbcTest() {
        JdbcDemo jdbcDemo = new JdbcDemo();
        jdbcDemo.query();
    }
}
