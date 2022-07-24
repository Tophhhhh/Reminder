package reminder.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import reminder.annotation.Mandatory;
import reminder.database.Database;
import reminder.model.Reminder;

public class ServiceDaoImpl2 implements IServiceDao {

	public ServiceDaoImpl2() {
		Database.connection();
	}

	@Override
	public List<Reminder> loadAll() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM reminder");
		return null;
	}

	@Override
	public List<Reminder> loadFiltered(String index) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM reminder where like = '%" + index + "%'");
		return null;
	}

	@Override
	public void createReminder(Reminder newReminder) {
		StringBuilder sb = new StringBuilder();
		sb.append("Insert into reminder values(");
	}

	@Override
	public void copy(List<Reminder> reminder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(List<String> id) {
		// TODO Auto-generated method stub
		
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
