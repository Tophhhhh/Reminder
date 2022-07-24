package reminder.util;

public class StringUtil {

	/**
	 * Looks if String is not null and not empty
	 * 
	 * @param text
	 * @return true if String is valid
	 */
	public static boolean isValid(String text) {
		return (text != null && !text.isEmpty());
	}
}
