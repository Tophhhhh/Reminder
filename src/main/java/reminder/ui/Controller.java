package reminder.ui;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import reminder.model.Reminder;
import reminder.service.IDataService;
import reminder.service.ServiceImpl;

public class Controller {

	private IDataService service;
	private FormView form;
	private int update = 0;

	public Controller(Shell shell) {
		service = new ServiceImpl();
		this.form = new FormView();
	}

	public List<Reminder> loadAll() {
		return service.loadAll();
	}

	public void open(Composite comp) {
		form.showForm(comp);
		form.setEnabled(false);
	}

	public void create() {
		form.setEnabled(true);
		form.create();
	}

	public void update() {
		if(update == 0) {
			form.setEnabled(true);
			update++;
		}else {
			form.setEnabled(false);
			update = 0;
		}
	}

	public boolean save() {
		Reminder reminder = form.save();
		if(service.checkMandatory(reminder)) {
			service.create(reminder);
			form.setEnabled(false);
			form.empty();
			return true;
		}
		return false;
	}

	public List<Reminder> filter(String filter) {
		return service.filter(filter);
	}

	public void delete(List<String> id) {
		service.delete(id);
	}

	public void csvExport(List<Reminder> reminder) {
		service.createCSV(reminder);
	}

	public void xmlExport(List<Reminder> reminder) {
		service.createXML(reminder);
	}

	public void selected(Reminder dummy) {
		form.selectedItem(dummy);
	}

	public List<Reminder> sort(String sort, boolean isFiltered, String search) {
		return service.sort(sort, isFiltered, search);
	}

}