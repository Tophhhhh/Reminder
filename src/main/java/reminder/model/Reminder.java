package reminder.model;

import java.util.Date;

import reminder.annotation.Mandatory;
import reminder.app.Priority;

public class Reminder {

	// TODO: change id to LONG
	public String id;
	@Mandatory(isMandatory = true)
	public String topic;
	@Mandatory(isMandatory = true)
	public String comment;
	public boolean sound;
	public String place;
	public Priority prio;
	@Mandatory(isMandatory = true)
	public Date datetime;

	public Reminder() {
		// Empty Constructor
	}

	public Reminder(String id, String topic, String comment, boolean sound, String place, Priority prio, Date datetime) {
		this.id = id;
		this.topic = topic;
		this.comment = comment;
		this.sound = sound;
		this.place = place;
		this.prio = prio;
		this.datetime = datetime;
	}

	// -- I D

	public String getID() {
		return id;
	}

	public void setUid(String uid) {
		this.id = uid;
	}

	// -- T O P I C

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	// -- C O M M E N T

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	// -- S O U N D

	public boolean isSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
	}

	// -- P L A C E

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	// -- P R I O R I T Y

	public Priority getPrio() {
		return prio;
	}

	public void setPrio(Priority prio) {
		this.prio = prio;
	}

	// -- D A T E
	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date date) {
		this.datetime = date;
	}
}
