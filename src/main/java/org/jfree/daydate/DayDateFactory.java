package org.jfree.daydate;

import org.jfree.date.DayDate;

import java.util.Date;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public abstract class DayDateFactory {
	public static DayDateFactory dayDateFactory = new SpreadSheetDateFactory();

	public static void setInstance(final DayDateFactory dayDateFactory) {
		DayDateFactory.dayDateFactory = dayDateFactory;
	}

	public static DayDateFactory instance() {
		return new SpreadSheetDateFactory();
	}

	protected abstract DayDate _makeDate(int ordinal);

	protected abstract DayDate _makeDate(int day, Month month, int year);

	protected abstract DayDate _make(int day, int month, int year);

	protected abstract DayDate _makeDate(Date date);

	protected abstract int _getMinimumyear();

	protected abstract int _getMaximumyear();

	public static DayDate makeDate(final int ordinal) {
		return dayDateFactory._makeDate(ordinal);
	}

	public static DayDate make(final int day, final Month month, final int year) {
		return dayDateFactory._makeDate(day, month, year);
	}

	public static DayDate make(final int day, final int month, final int year) {
		return dayDateFactory._make(day, month, year);
	}

	public static DayDate makeDate(final Date date) {
		return dayDateFactory._makeDate(date);
	}

	public static int getMinimumyear() {
		return dayDateFactory._getMinimumyear();
	}

	public static int getMaximumyear() {
		return dayDateFactory._getMaximumyear();
	}
}
