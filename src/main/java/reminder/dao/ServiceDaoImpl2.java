package reminder.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import reminder.annotation.Mandatory;
import reminder.model.Reminder;
import reminder.util.DatabaseUtil;

public class ServiceDaoImpl2 implements IServiceDao {

	public ServiceDaoImpl2() {
		DatabaseUtil.connection();
	}
	
	@Override
	public void onClose() {
		DatabaseUtil.closeConnection();
		System.out.println("Connection wurde geschlossen!");
	}

	@Override
	public List<Reminder> loadAll() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM reminder");
//		DatabaseUtil.executeStatement(sb.toString(), Reminder.class);
		return DatabaseUtil.executeStatementReminder(sql.toString());
	}

	@Override
	public List<Reminder> loadFiltered(String key) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM reminder WHERE topic LIKE '" + key + "'");
//		DatabaseUtil.executeStatement(sb.toString(), Reminder.class);
		return DatabaseUtil.executeStatementReminder(sql.toString());
	}

	@Override
	public void createReminder(Reminder r) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO reminder(topic,comment,sound,place,date,priority) VALUES(");
		sql.append("'" + r.getTopic() + "',");
		sql.append("'" + r.getComment() + "',");
		sql.append((r.isSound() ? 1 : 0)  + ",");
		sql.append("'" + r.getPlace() + "',");
		sql.append("'" + sdf.format(r.getDatetime()) + "',");
		sql.append("'" + r.getPrio().name() + "');");
		DatabaseUtil.updateStatement(sql.toString());
	}

	@Override
	public void copy(List<Reminder> reminder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete(List<String> id) {
		StringJoiner sj = new StringJoiner(",");
		for(String s : id) {
			sj.add(s);
		}
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM reminder WHERE id IN (" + sj.toString() + ");");
		DatabaseUtil.updateStatement(sql.toString());
	}

	@Override
	public void update(Reminder currentReminder) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Reminder> sortReminder(String sort, boolean isFiltered, String search) {
		// sort
		return null;
	}

	@Override
	public void createCsv(List<Reminder> reminder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void createXml(List<Reminder> xml) {
		// Empty 
	}
	
	public boolean checkMandatory(Reminder reminder) {
		boolean isMandatory = false;
		for (Field f : reminder.getClass().getDeclaredFields()) {
			Mandatory mandatory = f.getAnnotation(Mandatory.class);
			if (mandatory != null && mandatory.isMandatory()) {
				try {
					String getter = "get" + String.valueOf(f.getName().charAt(0)).toUpperCase()
							+ f.getName().substring(1);
					Method m = reminder.getClass().getMethod(getter);
					Object value = m.invoke(reminder);
					if (value instanceof Integer) {
						isMandatory = ((Integer) value) > 0;
					} else if (value instanceof String) {
						isMandatory = !((String) value).trim().isEmpty();
					} else if (value instanceof Date) {
						isMandatory = !((Date) value).toString().isEmpty();
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				if (!isMandatory) {
					return isMandatory;
				}
			}
		}
		return isMandatory;
	}
}
