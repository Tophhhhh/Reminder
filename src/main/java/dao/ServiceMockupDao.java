package dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import app.Priority;
import app.Reminder;

public class ServiceMockupDao implements IServiceDao {

	@Override
	public List<Reminder> loadAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reminder> loadFiltered(String index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createReminder(Reminder newReminder) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createCsv(List<Reminder> reminder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createXml(List<Reminder> xml) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkMandatory(Reminder reminder) {
		// TODO Auto-generated method stub
		return false;
	}
}
