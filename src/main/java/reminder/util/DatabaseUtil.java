package reminder.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
	
	private static Connection con;
	private static Statement stmnt;

	public static void connection() {
		try {
			File file = new File("src/database/database.db");
			if (!file.exists()) {
				file.createNewFile();
			}
			String sql = "jdbc:sqlite:" + file.getPath();
			con = DriverManager.getConnection(sql);
			con.setAutoCommit(true);
			stmnt = con.createStatement();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeConnection() {
		try {
			if (stmnt != null) {
				stmnt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Needed a solution to convert data to a Pojo without hardcode
	 * @param <T>
	 * 
	 * @param sql
	 */
	public static <T> void executeStatement(String sql, Class<T> clazz) {
		try {
			ResultSet rs = stmnt.executeQuery(sql);
			for(Field f : clazz.getDeclaredFields()) {
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateStatement(String sql) {
		try {
			stmnt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}