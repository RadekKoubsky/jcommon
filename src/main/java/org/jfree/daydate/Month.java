package org.jfree.daydate;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public enum Month {
	JANUARY(1),
	FEBRUARY(2),
	MARCH(3),
	APRIL(4),
	MAY(5),
	JUNE(6),
	JULY(7),
	AUGUST(8),
	SEPTEMBER(9),
	OCTOBER(10),
	NOVEMBER(11),
	DECEMBER(12);

	private static final int[] LAST_DAY_OF_MONTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private final int index;
	private static final DateFormatSymbols dateSymbols = new SimpleDateFormat().getDateFormatSymbols();

	Month(final int index) {
		this.index = index;
	}

	public static Month fromInt(final int monthIndex) {
		for (final Month month : Month.values()) {
			if (monthIndex == month.index) {
				return month;
			}
		}
		throw new IllegalArgumentException("Invalid month index " + monthIndex);
	}

	public static Month parse(String monthString) {
		monthString = monthString.trim();
		for (final Month month : Month.values()) {
			if (month.matches(monthString)) {
				return month;
			}
		}
		try {
			return fromInt(Integer.parseInt(monthString));
		} catch (final NumberFormatException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException(String.format("%s is not a valid month string", monthString));
	}

	private boolean matches(final String monthString) {
		return monthString.equalsIgnoreCase(toString()) || monthString.equalsIgnoreCase(toShortString());
	}

	public String toString() {
		return dateSymbols.getMonths()[this.index - 1];
	}

	public String toShortString() {
		return dateSymbols.getShortMonths()[this.index - 1];
	}

	public int lastDay() {
		return LAST_DAY_OF_MONTH[this.index];
	}

	public int toInt() {
		return this.index;
	}
}
