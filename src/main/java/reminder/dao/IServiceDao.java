package reminder.dao;

import java.util.List;

import reminder.model.Reminder;

public interface IServiceDao {
	
	public List<Reminder> loadAll();
	
	public List<Reminder> loadFiltered(String index);
	
	public void createReminder(Reminder newReminder);
	
	public void copy(List<Reminder> reminder);
	
	public void delete(List<String> id);
	
	public void update(Reminder currentReminder);
	
	public List<Reminder> sortReminder(String sort, boolean isFiltered, String search);
	
	public void createCsv(List<Reminder> reminder);
	
	public void createXml(List<Reminder> xml);
	
	public boolean checkMandatory(Reminder reminder);
	
	public void onClose();
}
