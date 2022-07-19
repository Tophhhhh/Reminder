package service;

import java.util.List;

import app.Reminder;
import dao.IServiceDao;
import dao.ServiceDaoImpl;

public class ServiceImpl implements IDataService {

	private IServiceDao dao = new ServiceDaoImpl();
//	private IServiceDao dao = new ServiceMockupDao();

	@Override
	public List<Reminder> loadAll() {
		return dao.loadAll();
		
	}

	@Override
	public void create(Reminder reminder) {
		dao.createReminder(reminder);
		
	}

	@Override
	public void edit(Reminder reminder) {
		dao.update(reminder);
	}

	@Override
	public void delete(List<String> id) {
		dao.delete(id);
		
	}

	@Override
	public List<Reminder> sort(String sort, boolean isFiltered, String search) {
		return dao.sortReminder(sort, isFiltered, search);
		
	}

	@Override
	public List<Reminder> filter(String filter) {
		return dao.loadFiltered(filter);
	}

	@Override
	public void createXML(List<Reminder> reminder) {
		dao.createXml(reminder);
		
	}

	@Override
	public void createCSV(List<Reminder> reminder) {
		dao.createCsv(reminder);
		
	}

	@Override
	public boolean checkMandatory(Reminder reminder) {
		return dao.checkMandatory(reminder);
	}



}
