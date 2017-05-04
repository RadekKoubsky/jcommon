package org.jfree.daydate;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public enum WeekInMonth {
	FIRST(1), SECOND(2), THIRD(3), FOURTH(4), LAST(0);

	private final int index;

	WeekInMonth(final int index) {
		this.index = index;
	}

	public int toInt() {
		return this.index;
	}
}
