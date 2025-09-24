package murach.sql;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class SqlGatewayServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=murach;encrypt=false";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "12345";
    
    // Load driver khi servlet khởi tạo
    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("✓ SQL Server JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            throw new ServletException("Failed to load SQL Server JDBC Driver", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        
        String sqlStatement = request.getParameter("sqlStatement");
        String sqlResult = "";
        Connection connection = null;

        try {
            // Test kết nối
            System.out.println("Attempting to connect to: " + DB_URL);
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.println("✓ Database connection established");
            
            if (sqlStatement == null || sqlStatement.trim().isEmpty()) {
                sqlResult = "<p style='color:red'>Please enter an SQL statement.</p>";
            } else {
                Statement statement = connection.createStatement();
                sqlStatement = sqlStatement.trim();
                
                if (sqlStatement.length() >= 6) {
                    String sqlType = sqlStatement.substring(0, 6).toUpperCase();
                    
                    if (sqlType.equals("SELECT")) {
                        ResultSet resultSet = statement.executeQuery(sqlStatement);
                        sqlResult = SQLUtil.getHtmlTable(resultSet);
                        resultSet.close();
                        System.out.println("✓ SELECT query executed successfully");
                    } else {
                        int i = statement.executeUpdate(sqlStatement);
                        if (i == 0) {
                            sqlResult = "<p>The statement executed successfully.</p>";
                        } else {
                            sqlResult = "<p>The statement executed successfully.<br>"
                                    + i + " row(s) affected.</p>";
                        }
                        System.out.println("✓ UPDATE/INSERT/DELETE executed successfully");
                    }
                }
                statement.close();
            }
        } catch (SQLException e) {
            sqlResult = "<p style='color:red'>Error executing the SQL statement: <br>"
                    + e.getMessage() + "</p>";
            System.err.println("✗ SQL Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("✓ Database connection closed");
                } catch (SQLException e) {
                    System.err.println("✗ Error closing connection: " + e.getMessage());
                }
            }
        }

        request.setAttribute("sqlResult", sqlResult);
        request.setAttribute("sqlStatement", sqlStatement != null ? sqlStatement : "");
        
        String url = "/index.jsp";
        getServletContext().getRequestDispatcher(url).forward(request, response);
    }
}