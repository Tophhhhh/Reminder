package reminder.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import reminder.annotation.Mandatory;
import reminder.app.Priority;
import reminder.model.Reminder;

public class ServiceDaoImpl implements IServiceDao {

	private static final String REMINDER = "reminder";
	private static final String ID = "id";
	private static final String TOPIC = "topic";
	private static final String COMMENT = "comment";
	private static final String SOUND = "sound";
	private static final String PLACE = "place";
	private static final String PRIORITY = "priority";
	private static final String DATETIME = "datetime";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private List<Reminder> reminderslist;
	private boolean reversedFilter = false;

	private File file;
	private Document document;
	private Transformer transformer;

	/**
	 * Load all.
	 *
	 * @return the list
	 */
	@Override
	public List<Reminder> loadAll() {
		reminderslist = new ArrayList<>();
		try {
			file = getFile();
			document = getBuilder(DocumentBuilderFactory.newInstance()).parse(file);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		NodeList list = document.getElementsByTagName(TOPIC);
		for (int i = 0; i <= list.getLength() - 1; i++) {
			String id = document.getElementsByTagName(ID).item(i).getTextContent();
			String topic = document.getElementsByTagName(TOPIC).item(i).getTextContent();
			String comment = document.getElementsByTagName(COMMENT).item(i).getTextContent();
			String sound = document.getElementsByTagName(SOUND).item(i).getTextContent();
			String place = document.getElementsByTagName(PLACE).item(i).getTextContent();
			String prio = document.getElementsByTagName(PRIORITY).item(i).getTextContent();
			String datetime = document.getElementsByTagName(DATETIME).item(i).getTextContent();

			try {
				reminderslist.add(new Reminder(Long.parseLong(id), topic, comment, Boolean.valueOf(sound), place,
						Priority.valueOf(prio), sdf.parse(datetime)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return reminderslist;
	}

	/**
	 * Load filtered.
	 *
	 * @param index the index
	 * @return the list
	 */
	@Override
	public List<Reminder> loadFiltered(String index) {
		return loadAll().stream().filter(c -> c.getTopic().toLowerCase().contains(index.toLowerCase()))
				.collect(Collectors.toList());
	}

	private boolean reminderExist(Reminder dummy) {
		for (Reminder i : reminderslist) {
			if (i.getID().equals(dummy.getID())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates the course.
	 *
	 * @param newReminder the new course
	 */
	@Override
	public void createReminder(Reminder newReminder) {
		if (reminderExist(newReminder)) {
			update(newReminder);
			return;
		}

		loadAll();
		Element newReminderXml = document.createElement(REMINDER);
		document.getLastChild().appendChild(newReminderXml);

		Element id = document.createElement(ID);
		id.appendChild(document.createTextNode(UUID.randomUUID().toString()));
		newReminderXml.appendChild(id);

		Element topic = document.createElement(TOPIC);
		topic.appendChild(document.createTextNode(newReminder.getTopic()));
		newReminderXml.appendChild(topic);

		Element comment = document.createElement(COMMENT);
		comment.appendChild(document.createTextNode(String.valueOf(newReminder.getComment())));
		newReminderXml.appendChild(comment);

		Element sound = document.createElement(SOUND);
		sound.appendChild(document.createTextNode(String.valueOf(newReminder.isSound())));
		newReminderXml.appendChild(sound);

		Element place = document.createElement(PLACE);
		place.appendChild(document.createTextNode(String.valueOf(newReminder.getPlace())));
		newReminderXml.appendChild(place);

		Element priority = document.createElement(PRIORITY);
		priority.appendChild(document.createTextNode(newReminder.getPrio().toString()));
		newReminderXml.appendChild(priority);

		Element datetime = document.createElement(DATETIME);
		datetime.appendChild(document.createTextNode(sdf.format(newReminder.getDatetime())));
		newReminderXml.appendChild(datetime);
		save();
	}

	@Override
	public void copy(List<Reminder> reminder) {
		for (Reminder i : reminder) {
			loadAll();
			Element newReminderXml = document.createElement(REMINDER);
			document.getLastChild().appendChild(newReminderXml);

			Element id = document.createElement(ID);
			id.appendChild(document.createTextNode(UUID.randomUUID().toString()));
			newReminderXml.appendChild(id);

			Element description = document.createElement(TOPIC);
			description.appendChild(document.createTextNode(i.getTopic()));
			newReminderXml.appendChild(description);

			Element comment = document.createElement(COMMENT);
			comment.appendChild(document.createTextNode(String.valueOf(i.getComment())));
			newReminderXml.appendChild(comment);

			Element sound = document.createElement(SOUND);
			sound.appendChild(document.createTextNode(String.valueOf(i.isSound())));
			newReminderXml.appendChild(sound);

			Element place = document.createElement(PLACE);
			place.appendChild(document.createTextNode(String.valueOf(i.getPlace())));
			newReminderXml.appendChild(place);

			Element priority = document.createElement(PRIORITY);
			priority.appendChild(document.createTextNode(i.getPrio().toString()));
			newReminderXml.appendChild(priority);

			Element datetime = document.createElement(DATETIME);
			datetime.appendChild(document.createTextNode(i.getDatetime().toString()));
			newReminderXml.appendChild(datetime);
			save();
		}
	}

	/**
	 * Save.
	 */
	public void save() {
		try {
			transformer = getTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(new DOMSource(document), new StreamResult(file));
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	/**
	 * DeleteCourse.
	 *
	 * @param id the id
	 */
	@Override
	public void delete(List<String> id) {
		for (String d : id) {
			loadAll();
			NodeList list = document.getElementsByTagName(REMINDER);
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				String courseId = list.item(i).getTextContent().trim().split("\n")[0].trim();
				if (courseId.equals(d)) {
					node.getParentNode().removeChild(node);
				}
			}
			save();
		}
	}

	/**
	 * Update.
	 *
	 * @param currentReminder the current course
	 */
	@Override
	public void update(Reminder currentReminder) {
		List<Reminder> reminder = loadAll();
		int index = -1;
		for (int i = 0; i < reminder.size(); i++) {
			if (reminder.get(i).getID().equals(currentReminder.getID())) {
				index = i;
			}
		}
		document.getElementsByTagName(TOPIC).item(index).setTextContent(currentReminder.getTopic());
		document.getElementsByTagName(COMMENT).item(index).setTextContent(currentReminder.getComment());
		document.getElementsByTagName(SOUND).item(index).setTextContent(String.valueOf(currentReminder.isSound()));
		document.getElementsByTagName(PLACE).item(index).setTextContent(currentReminder.getPlace());
		document.getElementsByTagName(PRIORITY).item(index).setTextContent(currentReminder.getPrio().toString());
		document.getElementsByTagName(DATETIME).item(index).setTextContent(sdf.format(currentReminder.getDatetime()));
		save();
	}

	/**
	 * Sort courses.
	 *
	 * @param sort       the sort
	 * @param isFiltered the is filtered
	 * @param search     the search
	 * @return the list
	 */
	@Override
	public List<Reminder> sortReminder(String sort, boolean isFiltered, String search) {
		List<Reminder> cours = (isFiltered) ? loadFiltered(search) : loadAll();
		switch (sort) {
		case "ID":
			if (reversedFilter) {
				cours.sort(Comparator.comparing(Reminder::getID).reversed().thenComparing(Reminder::getTopic));
				reversedFilter = false;
			} else {
				cours.sort(Comparator.comparing(Reminder::getID).thenComparing(Reminder::getTopic));
				reversedFilter = true;
			}
			break;
		case "Thema":
			if (reversedFilter) {
				cours.sort(Comparator.comparing(Reminder::getTopic).reversed());
				reversedFilter = false;
			} else {
				cours.sort(Comparator.comparing(Reminder::getTopic));
				reversedFilter = true;
			}
			break;
		case "Kommentar":
			if (reversedFilter) {
				cours.sort(Comparator.comparing(Reminder::getComment).reversed().thenComparing(Reminder::getTopic));
				reversedFilter = false;
			} else {
				cours.sort(Comparator.comparing(Reminder::getComment).thenComparing(Reminder::getTopic));
				reversedFilter = true;
			}
			break;
		case "Sound":
			if (reversedFilter) {
				cours.sort(Comparator.comparing(Reminder::isSound).reversed().thenComparing(Reminder::getTopic));
				reversedFilter = false;
			} else {
				cours.sort(Comparator.comparing(Reminder::isSound).thenComparing(Reminder::getTopic));
				reversedFilter = true;
			}
			break;
		case "Ort":
			if (reversedFilter) {
				cours.sort(Comparator.comparing(Reminder::getPlace).reversed().thenComparing(Reminder::getTopic));
				reversedFilter = false;
			} else {
				cours.sort(Comparator.comparing(Reminder::getPlace).thenComparing(Reminder::getTopic));
				reversedFilter = true;
			}
			break;
		case "Priorität":
			if (reversedFilter) {
				cours.sort(Comparator.comparing(Reminder::getPrio).reversed().thenComparing(Reminder::getTopic));
				reversedFilter = false;
			} else {
				cours.sort(Comparator.comparing(Reminder::getPrio).thenComparing(Reminder::getTopic));
				reversedFilter = true;
			}
			break;
		case "Datum":
			if (reversedFilter) {
				cours.sort(Comparator.comparing(Reminder::getDatetime).reversed().thenComparing(Reminder::getTopic));
				reversedFilter = false;
			} else {
				cours.sort(Comparator.comparing(Reminder::getDatetime).thenComparing(Reminder::getTopic));
				reversedFilter = true;
			}
			break;
		default:
			break;
		}
		return cours;
	}

	/**
	 * Creates the csv.
	 *
	 * @param reminderslist the csv
	 */
	@Override
	public void createCsv(List<Reminder> reminder) {
		File fpw = getCsvPath();
		try (PrintWriter writer = new PrintWriter(fpw)) {

			StringBuilder sb = new StringBuilder();
			sb.append("ID;Thema;Priorität;Kommentar;Ort;Zeitstempel;Sound\n");
			for (Reminder i : reminder) {
				sb.append(i.getID() + ";");
				sb.append(i.getTopic().replace(';', ',') + ";");
				sb.append(i.getPrio() + ";");
				sb.append(i.getComment().replace(';', ',') + ";");
				sb.append(i.getPlace().replace(';', ',') + ";");
				sb.append(sdf.format(i.getDatetime()) + ";");
				sb.append(i.isSound() + ";\n");
			}
			writer.write(sb.toString());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the xml.
	 *
	 * @param xml the xml
	 */
	@Override
	public void createXml(List<Reminder> xml) {
		List<Reminder> reminder = xml;
		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
		try {
			File outFile = getXmlPath();
			FileOutputStream out = new FileOutputStream(outFile);

			XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(out, "UTF-8");
			writer.writeStartDocument("UTF-8", "1.0");
			writer.writeStartElement("Entries");
			writer.writeCharacters("\n");
			for (Reminder i : reminder) {
				writer.writeStartElement("Reminder");
				writer.writeCharacters("\n");

				writer.writeStartElement(ID);
				writer.writeCharacters(i.getID().toString());
				writer.writeEndElement();
				writer.writeCharacters("\n");

				writer.writeStartElement(TOPIC);
				writer.writeCharacters(i.getTopic());
				writer.writeEndElement();
				writer.writeCharacters("\n");

				writer.writeStartElement(PRIORITY);
				writer.writeCharacters(String.valueOf(i.getPrio()));
				writer.writeEndElement();
				writer.writeCharacters("\n");

				writer.writeStartElement(COMMENT);
				writer.writeCharacters(i.getComment());
				writer.writeEndElement();
				writer.writeCharacters("\n");

				writer.writeStartElement(PLACE);
				writer.writeCharacters(i.getPlace());
				writer.writeEndElement();
				writer.writeCharacters("\n");

				writer.writeStartElement(DATETIME);
				writer.writeCharacters(sdf.format(i.getDatetime()));
				writer.writeEndElement();
				writer.writeCharacters("\n");

				writer.writeStartElement(SOUND);
				writer.writeCharacters(String.valueOf(i.isSound()));
				writer.writeEndElement();
				writer.writeCharacters("\n");

				writer.writeEndElement();
				writer.writeCharacters("\n");
			}
			writer.writeEndElement();
			writer.writeEndDocument();
			writer.flush();
			writer.close();
			out.close();
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	private File getFile() {
		return new File("src/main/resources/database/database.xml");
	}

	/**
	 * Gets the builder.
	 *
	 * @param builderFact the builder fact
	 * @return the builder
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	protected DocumentBuilder getBuilder(DocumentBuilderFactory builderFact) throws ParserConfigurationException {
		return builderFact.newDocumentBuilder();
	}

	/**
	 * Gets the transformer.
	 *
	 * @return the transformer
	 * @throws TransformerConfigurationException    the transformer configuration
	 *                                              exception
	 * @throws TransformerFactoryConfigurationError the transformer factory
	 *                                              configuration error
	 */
	protected Transformer getTransformer()
			throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		return TransformerFactory.newInstance().newTransformer();
	}

	/**
	 * File print writer.
	 *
	 * @return the file
	 */
	protected File getCsvPath() {
		String path = System.getProperty("user.home") + "/Desktop";
		return new File(path + "/ReminderCSV_File.csv");
	}

	/**
	 * Gets the xml path.
	 *
	 * @return the xml path
	 */
	protected File getXmlPath() {
		String path = System.getProperty("user.home") + "/Desktop";
		return new File(path + "/Reminder_XMLFile.xml");
	}

	/**
	 * @param reminder
	 * @return isMandaory
	 */
	public boolean checkMandatory(Reminder reminder) {
		boolean isMandatory = false;
		for (Field f : reminder.getClass().getDeclaredFields()) {
			Mandatory mandatory = f.getAnnotation(Mandatory.class);
			if (mandatory != null && mandatory.isMandatory()) {
				try {
					String getter = "get" + String.valueOf(f.getName().charAt(0)).toUpperCase()
							+ f.getName().substring(1);
					Method m = reminder.getClass().getMethod(getter);
					Object value = m.invoke(reminder);
					if (value instanceof Integer) {
						isMandatory = ((Integer) value) > 0;
					} else if (value instanceof String) {
						isMandatory = !((String) value).trim().isEmpty();
					} else if (value instanceof Date) {
						isMandatory = !((Date) value).toString().isEmpty();
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				if (!isMandatory) {
					return isMandatory;
				}
			}
		}
		return isMandatory;
	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub
		
	}
}
