package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author yotta2
 *
 */
public class DBConnection {
	private static final String DB_URL_FMT = "jdbc:mysql://%s";
	private static final String USE_DB_CMD_FMT = "USE %s";

	public DBConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(String.format(DB_URL_FMT, MyDBInfo.MYSQL_DATABASE_SERVER), MyDBInfo.MYSQL_USERNAME, MyDBInfo.MYSQL_PASSWORD);
			stmt = conn.createStatement();
			stmt.executeUpdate(String.format(USE_DB_CMD_FMT, MyDBInfo.MYSQL_DATABASE_NAME));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public Statement getStatement() {
		return stmt;
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private Connection conn;
	private Statement stmt;
}
