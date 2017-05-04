package org.jfree.daydate;

import org.jfree.date.DayDate;
import org.jfree.date.SpreadsheetDate;

import java.util.Date;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class SpreadSheetDateFactory extends DayDateFactory {
	@Override
	protected DayDate _makeDate(final int ordinal) {
		return new SpreadsheetDate(ordinal);
	}

	@Override
	protected DayDate _makeDate(final int day, final Month month, final int year) {
		return new SpreadsheetDate(day, month, year);
	}

	@Override
	protected DayDate _make(final int day, final int month, final int year) {
		return new SpreadsheetDate(day, month, year);
	}

	@Override
	protected DayDate _makeDate(final Date date) {
		return null;
	}

	@Override
	protected int _getMinimumyear() {
		return 0;
	}

	@Override
	protected int _getMaximumyear() {
		return 0;
	}
}
