package ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import app.Priority;
import app.Reminder;

public class FormView {

	private Text uid;
	private Text topic;
	private Text comment;
	private Text place;
	private DateTime dateTime;
	private DateTime time;
	private Combo cPrio;
	private Button sound;

	public void showForm(Composite comp) {
		Group group = new Group(comp, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		group.setLayout(new GridLayout(3, false));

		// - - R O W O N E

		Label lUid = new Label(group, SWT.NONE);
		lUid.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 0));
		lUid.setText("UUID");
		
		Label lTopic = new Label(group, SWT.NONE);
		lTopic.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 2, 0));
		lTopic.setText("Thema");

		uid = new Text(group, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		uid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 0));
		uid.setEnabled(false);

		topic = new Text(group, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		topic.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 0));
		topic.setTextLimit(50);

		Label lComment = new Label(group, SWT.NONE);
		lComment.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 3, 0));
		lComment.setText("Kommentar");

		comment = new Text(group, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		comment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 0));

		// - - R O W T W O

		Label lPlace = new Label(group, SWT.NONE);
		lPlace.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 3, 0));
		lPlace.setText("Ort");

		place = new Text(group, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		place.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 0));

		// - - R O W T H R E E

		Label lDate = new Label(group, SWT.NONE);
		lDate.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 0));
		lDate.setText("Datum");

		Label lTime = new Label(group, SWT.NONE);
		lTime.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 2, 0));
		lTime.setText("Uhrzeit");

		dateTime = new DateTime(group, SWT.DROP_DOWN);
		dateTime.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 1, 0));

		time = new DateTime(group, SWT.TIME);
		time.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 2, 0));

		// -- R O W F O U R

		String[] prio = { Priority.LOW.toString(), Priority.MEDIUM.toString(), Priority.HIGH.toString() };

		Label lPrio = new Label(group, SWT.NONE);
		lPrio.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 3, 0));
		lPrio.setText("Priorität");

		cPrio = new Combo(group, SWT.DROP_DOWN);
		cPrio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 0));
		cPrio.setItems(prio);
		cPrio.select(0);

		sound = new Button(group, SWT.CHECK);
		sound.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 0));
		sound.setText("Sound");

		listener();
	}

	// -- S E L E C T E D - I T E M

	public void selectedItem(Reminder reminder) {
		int[] date = setDateCal(reminder.getDatetime());

		uid.setText(reminder.getID());
		topic.setText(reminder.getTopic());
		comment.setText(reminder.getComment());
		place.setText(reminder.getPlace());
		cPrio.select(reminder.getPrio().ordinal());
		sound.setSelection(reminder.isSound());
		dateTime.setDate(date[0], date[1] - 1, date[2]);
		time.setTime(date[3], date[4], 0);
	}

	// -- S A V E

	public Reminder save() {
		Reminder pojo = new Reminder();
		pojo.setUid(uid.getText());
		pojo.setTopic(topic.getText());
		pojo.setComment(comment.getText());
		pojo.setSound(sound.getSelection());
		pojo.setPlace(place.getText());
		pojo.setPrio(Priority.valueOf(cPrio.getText()));
		pojo.setDatetime(getDateFromCalendar());
		return pojo;
	}

	// -- D A T E - A N D - T I M E - F R O M - C A L E N D A R

	private Date getDateFromCalendar() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
		cal.set(Calendar.MONTH, dateTime.getMonth());
		cal.set(Calendar.YEAR, dateTime.getYear());
		cal.set(Calendar.HOUR_OF_DAY, time.getHours());
		cal.set(Calendar.MINUTE, time.getMinutes());
		return cal.getTime();
	}

	// -- S E T - D A T E - A N D - T I M E

	private int[] setDateCal(Date date) {
		String dateString = new SimpleDateFormat("dd.MM.yyyy").format(date);
		String timeString = new SimpleDateFormat("HH:mm").format(date);
		int[] dt = new int[5];
		dt[0] = Integer.valueOf(dateString.substring(6, 10));
		dt[1] = Integer.valueOf(dateString.substring(3, 5));
		dt[2] = Integer.valueOf(dateString.substring(0, 2));
		dt[3] = Integer.valueOf(timeString.substring(0, 2));
		dt[4] = Integer.valueOf(timeString.substring(3, 5));
		return dt;
	}

	// -- S E T - E N A B L E D

	public void setEnabled(boolean enabled) {
		comment.setEnabled(enabled);
		sound.setEnabled(enabled);
		place.setEnabled(enabled);
		cPrio.setEnabled(enabled);
		dateTime.setEnabled(enabled);
		time.setEnabled(enabled);
		topic.setEnabled(enabled);
	}

	public void test() {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
		instance.set(Calendar.MONTH, dateTime.getMonth());
		instance.set(Calendar.YEAR, dateTime.getYear());
		String dateString = new SimpleDateFormat("dd/MM/yyyy").format(instance.getTime());
		try {
			Date date = new SimpleDateFormat("dd.MM.yyyy").parse("12.03.2001");
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			System.out.println(sdf.format(instance.getTime()));

			System.out.println(Priority.valueOf(cPrio.getText()));

			System.out.println(dateString);

			System.out.println("------------------");
			int[] test = setDateCal(new Date());
			System.out.println(test[0]);
			System.out.println(test[1]);
			System.out.println(test[2]);
			System.out.println("------------------");
			System.out.println(new SimpleDateFormat("dd.MM.yyyy").format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void create() {
		uid.setText(UUID.randomUUID().toString());
		comment.setText("");
		sound.setSelection(false);
		place.setText("");
		cPrio.select(0);
		topic.setText("");
	}

	public void empty() {
		uid.setText("");
		comment.setText("");
		sound.setSelection(false);
		place.setText("");
		cPrio.select(0);
		topic.setText("");
	}

	private void listener() {
		topic.addModifyListener(e -> {
			if (!(topic.getText().length() > 0) && !(uid.getText().isEmpty())) {
				topic.setBackground(new Color(null, 255, 255, 0));
			} else {
				topic.setBackground(new Color(null, 255, 255, 255));
			}
		});

		comment.addModifyListener(e -> {
			if (!(comment.getText().length() > 0) && (!uid.getText().isEmpty())) {
				comment.setBackground(new Color(null, 255, 255, 0));
			} else {
				comment.setBackground(new Color(null, 255, 255, 255));
			}
		});
	}
}
