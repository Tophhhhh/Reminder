package reminder.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import reminder.app.Priority;

@RunWith(MockitoJUnitRunner.class)
public class ReminderTest {
	
	@InjectMocks
	private Reminder testinstance;
	
	@Test
	public void testConstructor() {
		// G I V E N
		Reminder testOne = new Reminder("Topic", "Comment", false, "Place", Priority.MEDIUM, new Date());
		Reminder testTwo = new Reminder(1l, "Topic", "Comment", false, "Place", Priority.MEDIUM, new Date());
		
		// W H E N
		
		// T H E N
		assertNotNull(testOne);
		assertNotNull(testTwo);
	}
	
	@Test
	public void testGetterAndSetter() {
		// G I V E N
		Long id = 1l;
		String comment = "Comment";
		String place = "Place";
		String topic = "Topic";
		Date date = new Date();
		Priority prio = Priority.LOW;
		boolean isSound = false;
		
		// W H E N
		testinstance.setUid(id);
		testinstance.setComment(comment);
		testinstance.setDatetime(date);
		testinstance.setPlace(place);
		testinstance.setPrio(prio);
		testinstance.setSound(isSound);
		testinstance.setTopic(topic);
		
		// T H E N
		assertEquals(testinstance.getComment(), comment);
		assertEquals(testinstance.getDatetime(), date);
		assertEquals(testinstance.getPlace(), place);
		assertEquals(testinstance.getPrio(), prio);
		assertEquals(testinstance.isSound(), isSound);
		assertEquals(testinstance.getTopic(), topic);
		assertEquals(testinstance.getID(), id);
		
	}
	
	@Test
	public void testEquals() {
		// G I V E N
		
		// W H E N
		
		// T H E N
	}
	
	@Test
	public void testHashCode() {
		// G I V E N
		
		// W H E N
		
		// T H E N
	}
}