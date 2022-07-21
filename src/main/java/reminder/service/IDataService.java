package reminder.service;

import java.util.List;

import reminder.model.Reminder;

public interface IDataService {

	public List<Reminder> loadAll();
	
	public void create(Reminder reminder);

	public void edit(Reminder reminder);

	public void delete(List<String> id);
	
	public List<Reminder> sort(String sort, boolean isFiltered, String search);
	
	public List<Reminder> filter(String filter);

	public void createXML(List<Reminder> reminder);

	public void createCSV(List<Reminder> reminder);
	
	public boolean checkMandatory(Reminder reminder);
}
