package reminder.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import reminder.app.Priority;
import reminder.model.Reminder;

public class ListView implements Ui {

	private Controller ctrl;

	private Display display;
	private Shell shell;
	private Composite comp;

	// TollItems
	private ToolItem create;
	private ToolItem loadAll;
	private ToolItem update;
	private ToolItem delete;
	private ToolItem csvExport;
	private ToolItem xmlExport;
	private ToolItem save;

	private Button searchButton;
	private Text searchBar;

	private Table table;

	private MusicLoader ml;

	public ListView() {
		display = new Display();

		shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setText("Reminder");
		shell.setImage(new Image(display, "src/main/resources/icon/alarm.png"));
		shell.setLayout(new GridLayout(1, false));
		shell.setSize(1080, 860);

		comp = new Composite(shell, SWT.NONE);
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		comp.setLayout(new GridLayout(3, false));

		ctrl = new Controller(shell);
	}

	public void show() {
		toolbar();
		listView();
		listener();
		formView();
		reminderThread();

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private void toolbar() {
		ToolBar toolBar = new ToolBar(comp, SWT.FLAT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 0));

		create = new ToolItem(toolBar, SWT.PUSH);
		create.setImage(new Image(display, "src/main/resources/icon/Add.png"));
		create.setText("Erstellen");

		loadAll = new ToolItem(toolBar, SWT.PUSH);
		loadAll.setImage(new Image(display, "src/main/resources/icon/load.png"));
		loadAll.setText("Neu Laden");

		save = new ToolItem(toolBar, SWT.PUSH);
		save.setImage(new Image(display, "src/main/resources/icon/save.png"));
		save.setText("Speichern");

		update = new ToolItem(toolBar, SWT.PUSH);
		update.setImage(new Image(display, "src/main/resources/icon/change.png"));
		update.setText("Ändern");

		delete = new ToolItem(toolBar, SWT.PUSH);
		delete.setImage(new Image(display, "src/main/resources/icon/delete.png"));
		delete.setText("Löschen");

		csvExport = new ToolItem(toolBar, SWT.PUSH);
		csvExport.setImage(new Image(display, "src/main/resources/icon/export.png"));
		csvExport.setText("CSV");

		xmlExport = new ToolItem(toolBar, SWT.PUSH);
		xmlExport.setImage(new Image(display, "src/main/resources/icon/export.png"));
		xmlExport.setText("XML");

		Composite searchComp = new Composite(comp, SWT.NONE);
		searchComp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		searchComp.setLayout(new GridLayout(5, false));

		Label search = new Label(searchComp, SWT.NONE);
		search.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 5, 0));
		search.setText("Suche: ");

		searchButton = new Button(searchComp, SWT.PUSH);
		searchButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 3, 0));
		searchButton.setText("Suchen");

		searchBar = new Text(searchComp, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		searchBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 0));
	}

	private void listView() {
		Map<String, Integer> column = new LinkedHashMap<String, Integer>();
		column.put("ID", 0);
		column.put("Thema", 100);
		column.put("Priorität", 70);
		column.put("Kommentar", 200);
		column.put("Ort", 100);
		column.put("Datum", 100);
		column.put("Uhrzeit", 70);
		column.put("Sound", 50);

		Group gListView = new Group(comp, SWT.NONE);
		gListView.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		gListView.setLayout(new GridLayout(1, false));
		gListView.setText("Reminder");

		table = new Table(gListView, SWT.MULTI | SWT.FULL_SELECTION | SWT.V_SCROLL);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 0);
		gd.heightHint = 20 * table.getItemHeight();
		table.setLayoutData(gd);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		for (String i : column.keySet()) {
			TableColumn tC = new TableColumn(table, SWT.LEAD);
			tC.setText(i);
			tC.setWidth(column.get(i));
			tC.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					fillTable(ctrl.sort(tC.getText(), searchBar.getText().trim().length() > 0, searchBar.getText()));
				}
			});
		}
		fillTable(ctrl.loadAll());
	}

	private void fillTable(List<Reminder> temp) {
		table.removeAll();
		List<Reminder> remind = temp;

		Color red = new Color(null, 255, 0, 0);
		Color yellow = new Color(null, 255, 255, 0);
		Color green = new Color(null, 0, 255, 0);

		for (Reminder r : remind) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(0, r.getID());
			tableItem.setText(1, r.getTopic());
			tableItem.setText(2, r.prio.toString());
			tableItem.setBackground(2,
					r.getPrio() == Priority.HIGH ? red : r.getPrio() == Priority.MEDIUM ? yellow : green);
			tableItem.setText(3, r.getComment());
			tableItem.setText(4, r.getPlace());
			tableItem.setText(5, new SimpleDateFormat("dd.MM.yyyy").format(r.getDatetime()));
			tableItem.setText(6, new SimpleDateFormat("HH:mm").format(r.getDatetime()));
			tableItem.setText(7, String.valueOf(r.isSound()));
		}
	}

	private void listener() {
		// set listener
		create.addListener(SWT.Selection, e -> {
			ctrl.create();
		});

		loadAll.addListener(SWT.Selection, e -> {
			searchBar.setText("");
			fillTable(ctrl.loadAll());
		});

		update.addListener(SWT.Selection, e -> {
			ctrl.update();
		});

		delete.addListener(SWT.Selection, e -> {
			List<String> dummy = new ArrayList<>();
			for (TableItem ti : table.getSelection()) {
				dummy.add(ti.getText());
			}
			ctrl.delete(dummy);
			fillTable(ctrl.loadAll());
		});

		save.addListener(SWT.Selection, e -> {
			if(ctrl.save()) {
				fillTable(ctrl.loadAll());
			}else {
				MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
				mb.setText("WARNING");
				mb.setMessage("Bitte Pflichtfelder ausfüllen!");
				mb.open();
			}
		});

		csvExport.addListener(SWT.Selection, e -> {
			List<Reminder> rList = new ArrayList<>();
			for (TableItem t : table.getSelection()) {
				Reminder dummy = stringToReminder(t);
				rList.add(dummy);
			}
			ctrl.csvExport(rList);
		});

		xmlExport.addListener(SWT.Selection, e -> {
			List<Reminder> rList = new ArrayList<>();
			for (TableItem t : table.getSelection()) {
				Reminder dummy = stringToReminder(t);
				rList.add(dummy);
			}
			ctrl.xmlExport(rList);
		});

		searchButton.addListener(SWT.Selection, e -> {
			fillTable(ctrl.filter(searchBar.getText().trim()));
		});

		table.addListener(SWT.MouseDoubleClick, e -> {
			try {
				TableItem ti = table.getItem(table.getSelectionIndex());
				Reminder dummy = stringToReminder(ti);
				ctrl.selected(dummy);
			} catch (IllegalArgumentException ex) {
				MessageBox mb = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
				mb.setText("Information");
				mb.setMessage("Klick auf ein Eintrag!");
				mb.open();
			}
		});
	}

	private Reminder stringToReminder(TableItem ti) {
		Reminder dummy = new Reminder();
		String datetime = ti.getText(5) + " " + ti.getText(6);
		try {
			dummy.setUid(ti.getText(0));
			dummy.setTopic(ti.getText(1));
			dummy.setPrio(Priority.valueOf(ti.getText(2)));
			dummy.setComment(ti.getText(3));
			dummy.setPlace(ti.getText(4));
			dummy.setDatetime(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(datetime));
			dummy.setSound(Boolean.parseBoolean(ti.getText(7)));
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return dummy;
	}

	private void formView() {
		Composite cForm = new Composite(comp, SWT.NONE);
		cForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		cForm.setLayout(new GridLayout(1, false));
		ctrl.open(cForm);
	}

	private void reminderThread() {
		shell.getDisplay().timerExec((20 * 1000), new Runnable() {

			@Override
			public void run() {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date dateNow = new Date();

				List<Reminder> reminderlist = ctrl.loadAll();
				for (Reminder i : reminderlist) {
					if (sdf.format(dateNow).equals(sdf.format(i.getDatetime()))) {
						StringBuilder sb = new StringBuilder();
						sb.append("Folgender Termin steht an: \n");
						sb.append("Priorität : " + i.getPrio() + "\n");
						sb.append("Thema : " + i.getTopic() + "\n");
						sb.append("Kommentar : " + i.getComment() + "\n");
						sb.append("Ort: " + i.getPlace() + "\n");

						if (i.isSound()) {
							ml = new MusicLoader();
							ml.load();
							ml.play(MusicLoader.sound);
						}

						MessageBox mb = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
						mb.setText("ERINNERUNG!! " + sdf.format(i.getDatetime()));
						mb.setMessage(sb.toString());
						
						if(32 == mb.open() && ml != null) {
							ml.stop();
						}
					}
				}
				reminderThread();
			}
		});
	}
}
