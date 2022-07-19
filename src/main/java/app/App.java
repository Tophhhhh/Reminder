package app;

import service.IDataService;
import service.ServiceImpl;
import ui.ListView;
import ui.Ui;

public class App {
	
	private static App INSTANCE;
	private IDataService service;
	private Ui ui;
	
	public static App getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new App();
		}
		return INSTANCE;
	}
	
	public void init(String[] args) {
		service = new ServiceImpl();
		ui = new ListView();
		ui.show();
	}
}
