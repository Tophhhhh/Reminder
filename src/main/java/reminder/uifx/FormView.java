package reminder.uifx;

import javafx.application.Application;
import javafx.stage.Stage;

public class FormView extends Application implements Ui {

	@Override
	public void show() {
		launch("");
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		listener();
	}

	private void listener() {
		// Empty
	}
}