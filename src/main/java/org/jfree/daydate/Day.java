package org.jfree.daydate;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public enum Day {
	MONDAY(Calendar.MONDAY),
	TUESDAY(Calendar.TUESDAY),
	WEDNESDAY(Calendar.WEDNESDAY),
	THURSDAY(Calendar.THURSDAY),
	FRIDAY(Calendar.FRIDAY),
	SATURDAY(Calendar.SATURDAY),
	SUNDAY(Calendar.SUNDAY);

	private final int index;
	private static final DateFormatSymbols dateSymbols = new SimpleDateFormat().getDateFormatSymbols();

	Day(final int index) {
		this.index = index;
	}

	public static Day fromInt(final int index) {
		for (final Day day : Day.values()) {
			if (day.index == index) {
				return day;
			}
		}
		throw new IllegalArgumentException("Invalid day index " + index);
	}

	public static Day parse(String dayString) {
		dayString = dayString.trim();
		for (final Day day : Day.values()) {
			if (day.matches(day, dayString)) {
				return day;
			}
		}
		throw new IllegalArgumentException(String.format("%s is not a valid weekday string", dayString));
	}

	private boolean matches(final Day day, final String dayString) {
		return dayString.equalsIgnoreCase(toShortString()) ||
				dayString.equalsIgnoreCase(toString());
	}

	public String toString() {
		return dateSymbols.getWeekdays()[this.index];
	}

	public String toShortString() {
		return dateSymbols.getShortWeekdays()[this.index];
	}

	public int toInt() {
		return this.index;
	}
}
