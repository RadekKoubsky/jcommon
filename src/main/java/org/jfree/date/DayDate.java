/* ========================================================================
 * JCommon : a free general purpose class library for the Java(tm) platform
 * ========================================================================
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited and Contributors.
 * 
 * Project Info:  http://www.jfree.org/jcommon/index.html
 *
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, 
 * USA.  
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * ---------------
 * SerialDate.java
 * ---------------
 * (C) Copyright 2001-2006, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: SerialDate.java,v 1.9 2011/10/17 20:08:22 mungady Exp $
 *
 * Changes (from 11-Oct-2001)
 * --------------------------
 * 11-Oct-2001 : Re-organised the class and moved it to new package 
 *               com.jrefinery.date (DG);
 * 05-Nov-2001 : Added a getDescription() method, and eliminated NotableDate 
 *               class (DG);
 * 12-Nov-2001 : IBD requires setDescription() method, now that NotableDate 
 *               class is gone (DG);  Changed getPreviousDayOfWeek(), 
 *               getFollowingDayOfWeek() and getNearestDayOfWeek() to correct 
 *               bugs (DG);
 * 05-Dec-2001 : Fixed bug in SpreadsheetDate class (DG);
 * 29-May-2002 : Moved the month constants into a separate interface 
 *               (MonthConstants) (DG);
 * 27-Aug-2002 : Fixed bug in addMonths() method, thanks to N???levka Petr (DG);
 * 03-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 13-Mar-2003 : Implemented Serializable (DG);
 * 29-May-2003 : Fixed bug in addMonths method (DG);
 * 04-Sep-2003 : Implemented Comparable.  Updated the isInRange javadocs (DG);
 * 05-Jan-2005 : Fixed bug in addYears() method (1096282) (DG);
 * 
 */

package org.jfree.date;

import org.jfree.daydate.*;

import java.io.Serializable;
import java.util.Calendar;

/**
 * <pre>
 * An abstract class that defines our requirements for manipulating dates,
 * without tying down a particular implementation.
 *
 * Requirement 1 : match at least what Excel does for dates;
 * Requirement 2 : the date represented by the class is immutable;
 *
 * Why not just use java.util.Date?  We will, when it makes sense.  At times,
 * java.util.Date can be *too* precise - it represents an instant in time,
 * accurate to 1/1000th of a second (with the date itself depending on the
 * time-zone).  Sometimes we just want to represent a particular day (e.g. 21
 * January 2015) without concerning ourselves about the time of day, or the
 * time-zone, or anything else.  That's what we've defined SerialDate for.
 *
 * You can call getInstance() to get a concrete subclass of SerialDate,
 * without worrying about the exact implementation.
 * </pre>
 *
 * @author David Gilbert
 */
public abstract class DayDate implements Comparable,
		Serializable {

	public abstract int getOrdinalDay();

	public abstract int getYear();

	public abstract int getMonth();

	public abstract int getDayOfMonth();

	protected abstract Day getDayOfWeekForOrdinalZero();

	/**
	 * Creates a new date by adding the specified number of days to the base
	 * date.
	 *
	 * @param days the number of days to add (can be negative).
	 * @return a new date.
	 */
	public DayDate plusDays(final int days) {
		return DayDateFactory.makeDate(getOrdinalDay() + days);
	}

	/**
	 * Creates a new date by adding the specified number of months to the base
	 * date.
	 * <p>
	 * If the base date is close to the end of the month, the day on the result
	 * may be adjusted slightly:  31 May + 1 month = 30 June.
	 *
	 * @param months the number of months to add (can be negative).
	 * @return a new date.
	 */
	public DayDate addMonths(final int months) {
		final int thisMonthAsOrdinal = 12 * getYear() + getMonth() - 1;
		final int resultMonthAsOrdinal = thisMonthAsOrdinal + months;
		final int resultYear = resultMonthAsOrdinal / 12;
		final Month resultMonth = Month.fromInt(resultMonthAsOrdinal % 12 + 1);
		final int resultDay = correctLastDayOfMonth(getDayOfMonth(), resultMonth, resultYear);
		return DayDateFactory.make(resultDay, resultMonth, resultYear);
	}

	/**
	 * Creates a new date by adding the specified number of years to the base
	 * date.
	 *
	 * @param years the number of years to add (can be negative).
	 * @return A new date.
	 */
	public DayDate plusYears(final int years) {
		final int resultYear = getYear() + years;
		final int lastDayOfMonthInResultYear = DateUtil.lastDayOfMonth(Month.fromInt(getMonth()), resultYear);
		final int resultDay = correctLastDayOfMonth(getDayOfMonth(), Month.fromInt(getMonth()), resultYear);
		return DayDateFactory.make(resultDay, Month.fromInt(getMonth()), resultYear);
	}

	private int correctLastDayOfMonth(final int day, final Month month, final int year) {
		return Math.min(day, DateUtil.lastDayOfMonth(month, year));
	}

	/**
	 * Returns the latest date that falls on the specified day-of-the-week and
	 * is BEFORE the base date.
	 *
	 * @param targetDayOfWeek a code for the target day-of-the-week.
	 * @return the latest date that falls on the specified day-of-the-week and
	 * is BEFORE the base date.
	 */
	public DayDate getPreviousDayOfWeek(final Day targetDayOfWeek) {
		int offsetToTarget = targetDayOfWeek.toInt() - getDayOfWeek().toInt();
		if (offsetToTarget >= 0) {
			offsetToTarget -= 7;
		}
		return plusDays(offsetToTarget);
	}

	/**
	 * Returns the earliest date that falls on the specified day-of-the-week
	 * and is AFTER the base date.
	 *
	 * @param targetDayOfWeek a code for the target day-of-the-week.
	 * @return the earliest date that falls on the specified day-of-the-week
	 * and is AFTER the base date.
	 */
	public DayDate getFollowingDayOfWeek(final Day targetDayOfWeek) {
		int offsetToTarget = targetDayOfWeek.toInt() - getDayOfWeek().toInt();
		if (offsetToTarget <= 0) {
			offsetToTarget += 7;
		}
		return plusDays(offsetToTarget);
	}

	/**
	 * Returns the date that falls on the specified day-of-the-week and is
	 * CLOSEST to the base date.
	 *
	 * @param targetDayOfWeek a code for the target day-of-the-week.
	 * @return the date that falls on the specified day-of-the-week and is
	 * CLOSEST to the base date.
	 */
	public DayDate getNearestDayOfWeek(final Day targetDayOfWeek) {
		final int offsetToThisWeeksTarget = targetDayOfWeek.toInt() - getDayOfWeek().toInt();
		final int offsetToFutureTarget = (offsetToThisWeeksTarget + 7) % 7;
		final int offsetToPreviousTarget = (offsetToThisWeeksTarget - 7) % 7;
		if (offsetToFutureTarget > 3) {
			return plusDays(offsetToPreviousTarget);
		} else {
			return plusDays(offsetToFutureTarget);
		}
	}

	/**
	 * Rolls the date forward to the last day of the month.
	 *
	 * @param base the base date.
	 * @return a new serial date.
	 */
	public DayDate getEndOfMonth(final DayDate base) {
		final Month month = Month.fromInt(getMonth());
		return DayDateFactory.make(DateUtil.lastDayOfMonth(month, getYear()), month, getYear());
	}

	/**
	 * Returns a java.util.Date.  Since java.util.Date has more precision than
	 * SerialDate, we need to define a convention for the 'time of day'.
	 *
	 * @return this as <code>java.util.Date</code>.
	 */
	public java.util.Date toDate() {
		final Calendar calendar = Calendar.getInstance();
		calendar.set(getYear(), getMonth() - 1, getDayOfMonth(), 0, 0, 0);
		return calendar.getTime();
	}

	/**
	 * Converts the date to a string.
	 *
	 * @return a string representation of the date.
	 */
	public String toString() {
		return String.format("%02d-%s-%d", getDayOfMonth(), getMonth(), getYear());
	}

	public Day getDayOfWeek() {
		final Day startingDay = getDayOfWeekForOrdinalZero();
		final int startingOffset = startingDay.toInt() - Day.SUNDAY.toInt();
		final int ordinalDayOfWeek = (getOrdinalDay() + startingOffset) % 7;
		return Day.fromInt(ordinalDayOfWeek + Day.SUNDAY.toInt());
	}

	/**
	 * Returns the difference (in days) between this date and the specified
	 * 'other' date.
	 * <p>
	 * The result is positive if this date is after the 'other' date and
	 * negative if it is before the 'other' date.
	 *
	 * @param other the date being compared to.
	 * @return the difference between this and the other date.
	 */
	public int daySince(final DayDate other) {
		return getOrdinalDay() - other.getOrdinalDay();
	}

	/**
	 * Returns true if this SerialDate represents the same date as the
	 * specified SerialDate.
	 *
	 * @param other the date being compared to.
	 * @return <code>true</code> if this SerialDate represents the same date as
	 * the specified SerialDate.
	 */
	public boolean isOn(final DayDate other) {
		return getOrdinalDay() == other.getOrdinalDay();
	}

	/**
	 * Returns true if this SerialDate represents an earlier date compared to
	 * the specified SerialDate.
	 *
	 * @param other The date being compared to.
	 * @return <code>true</code> if this SerialDate represents an earlier date
	 * compared to the specified SerialDate.
	 */
	public boolean isBefore(final DayDate other) {
		return getOrdinalDay() < other.getOrdinalDay();
	}

	/**
	 * Returns true if this SerialDate represents the same date as the
	 * specified SerialDate.
	 *
	 * @param other the date being compared to.
	 * @return <code>true</code> if this SerialDate represents the same date
	 * as the specified SerialDate.
	 */
	public boolean isOnOrBefore(final DayDate other) {
		return getOrdinalDay() <= other.getOrdinalDay();
	}

	/**
	 * Returns true if this SerialDate represents the same date as the
	 * specified SerialDate.
	 *
	 * @param other the date being compared to.
	 * @return <code>true</code> if this SerialDate represents the same date
	 * as the specified SerialDate.
	 */
	public boolean isAfter(final DayDate other) {
		return getOrdinalDay() > other.getOrdinalDay();
	}

	/**
	 * Returns true if this SerialDate represents the same date as the
	 * specified SerialDate.
	 *
	 * @param other the date being compared to.
	 * @return <code>true</code> if this SerialDate represents the same date
	 * as the specified SerialDate.
	 */
	public boolean isOnOrAfter(final DayDate other) {
		return getOrdinalDay() >= other.getOrdinalDay();
	}

	/**
	 * Returns <code>true</code> if this {@link DayDate} is within the
	 * specified range (INCLUSIVE).  The date order of d1 and d2 is not
	 * important.
	 *
	 * @param d1 a boundary date for the range.
	 * @param d2 the other boundary date for the range.
	 * @return A boolean.
	 */
	public boolean isInRange(final DayDate d1, final DayDate d2) {
		return isInRange(d1, d2, DateInterval.CLOSED);
	}

	/**
	 * Returns <code>true</code> if this {@link DayDate} is within the
	 * specified range (caller specifies whether or not the end-points are
	 * included).  The date order of d1 and d2 is not important.
	 *
	 * @param d1           a boundary date for the range.
	 * @param d2           the other boundary date for the range.
	 * @param dateInterval a code that controls whether or not the start and end
	 *                     dates are included in the range.
	 * @return A boolean.
	 */
	public boolean isInRange(final DayDate d1, final DayDate d2,
			final DateInterval dateInterval) {
		final int start = Math.min(d1.getOrdinalDay(), d2.getOrdinalDay());
		final int end = Math.max(d1.getOrdinalDay(), d2.getOrdinalDay());
		return dateInterval.isIn(getOrdinalDay(), start, end);
	}

}
