package reminder.model;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import reminder.app.Priority;

@RunWith(MockitoJUnitRunner.class)
public class ReminderTest {

	@InjectMocks
	private Reminder testinstace;
	
	@Test
	public void testGetterAndSetter() throws ParseException {
		// G I V E N
		Long id = 1l;
		String topic = "Topic";
		String comment = "Comment";
		boolean isSound = false;
		String place = "Place";
		Priority prio = Priority.HIGH;

//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = sdf.parse(LocalDate.of(2022, 10, 1).toString());
		
		// W H E N
		testinstace.setUid(id);
		testinstace.setTopic(topic);
		testinstace.setComment(comment);
		testinstace.setSound(isSound);
		testinstace.setPlace(place);
		testinstace.setPrio(prio);
//		testinstace.setDatetime(date);
		
		// T H E N
		assertEquals(id, testinstace.getID());
		assertEquals(topic, testinstace.getTopic());
		assertEquals(comment, testinstace.getComment());
		assertEquals(isSound, testinstace.isSound());
		assertEquals(place, testinstace.getPlace());
		assertEquals(prio, testinstace.getPrio());
//		assertEquals(date, testinstace.getDatetime());
	}
}
