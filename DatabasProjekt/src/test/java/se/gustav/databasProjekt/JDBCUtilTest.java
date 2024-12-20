package se.gustav.databasProjekt;

import org.junit.Test;
import se.gustav.databasProjekt.JDBCUtil;

import static org.junit.Assert.*;
import java.sql.Connection;

public class JDBCUtilTest {

    @Test
    public void testDatabaseConnection() {
        try (Connection conn = JDBCUtil.getConnection()) {
            String dbName = JDBCUtil.getDatabaseProductName(conn);
            System.out.println("Successfully connected to: " + dbName);
            assertNotNull("Database connection should not be null", conn);
            assertEquals("Should be connected to H2", "H2", dbName);
        } catch (Exception e) {
            fail("Database connection failed: " + e.getMessage());
        }
    }
}