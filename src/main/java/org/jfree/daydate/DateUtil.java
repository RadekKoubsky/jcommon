package org.jfree.daydate;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class DateUtil {
	private static final DateFormatSymbols DATE_FORMAT_SYMBOLS = new SimpleDateFormat().getDateFormatSymbols();

	public static String[] getMonthNames() {
		return DATE_FORMAT_SYMBOLS.getMonths();
	}

	public static boolean isLeapYear(final int year) {
		final boolean fourth = year % 4 == 0;
		final boolean hundredth = year % 100 == 0;
		final boolean fourthhundredth = year % 400 == 0;
		return fourth && (!hundredth || fourthhundredth);
	}

	/**
	 * Returns the number of the last day of the month, taking into account
	 * leap years.
	 *
	 * @param month the month.
	 * @param year  the year (in the range 1900 to 9999).
	 * @return the number of the last day of the month.
	 */
	public static int lastDayOfMonth(final Month month, final int year) {
		if (month == Month.FEBRUARY && isLeapYear(year)) {
			return month.lastDay() + 1;
		} else {
			return month.lastDay();
		}
	}

	/**
	 * Returns the number of leap years from 1900 to the specified year
	 * INCLUSIVE.
	 * <p>
	 * Note that 1900 is not a leap year.
	 *
	 * @param yyyy the year (in the range 1900 to 9999).
	 * @return the number of leap years from 1900 to the specified year.
	 */
	public static int leapYearCount(final int yyyy) {

		final int leap4 = (yyyy - 1896) / 4;
		final int leap100 = (yyyy - 1800) / 100;
		final int leap400 = (yyyy - 1600) / 400;
		return leap4 - leap100 + leap400;

	}
}
