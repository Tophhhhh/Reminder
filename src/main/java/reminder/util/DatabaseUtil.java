package reminder.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reminder.app.Priority;
import reminder.model.Reminder;

public class DatabaseUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);
	
	private static Connection con;
	private static Statement stmnt;

	public static void connection() {
		try {
			File file = new File("src/main/resources/database/Database.db");
			if (!file.exists()) {
				file.createNewFile();
			}
			String sql = "jdbc:sqlite:" + file.getPath();
			con = DriverManager.getConnection(sql);
			con.setAutoCommit(true);
			stmnt = con.createStatement();
		} catch (IOException | SQLException e) {
			LOGGER.error(e.getMessage(), e);
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
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Needed a solution to convert data to a Pojo without hardcode
	 * 
	 * @param <T>
	 * 
	 * @param sql
	 */
	public static <T> void executeStatement(String sql, Class<T> clazz) {
		try {
			ResultSet rs = stmnt.executeQuery(sql);
			for (Field f : clazz.getDeclaredFields()) {
				f.getType(); // return type
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public static List<Reminder> executeStatementReminder(String sql) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			List<Reminder> result = new ArrayList<>();
			ResultSet rs = stmnt.executeQuery(sql);
			while (rs.next()) {
				result.add(
						new Reminder(
								rs.getLong("id"), 
								rs.getString("topic"), 
								rs.getString("comment"),
								rs.getBoolean("sound"), 
								rs.getString("place"), 
								Priority.valueOf(rs.getString("Priority")),
								sdf.parse(rs.getString("date"))));
			}
			return result;
		} catch (SQLException | ParseException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	public static void updateStatement(String sql) {
		try {
			stmnt.executeUpdate(sql);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}