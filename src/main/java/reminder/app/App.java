package reminder.app;

import reminder.service.ServiceImpl;
import reminder.ui.ListView;
import reminder.ui.Ui;

public class App {
	
	private static App INSTANCE;
	private Ui ui;
	
	public static App getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new App();
		}
		return INSTANCE;
	}
	
	public void init(String[] args) {
		new ServiceImpl();
		ui = new ListView();
		ui.show();
	}
}
