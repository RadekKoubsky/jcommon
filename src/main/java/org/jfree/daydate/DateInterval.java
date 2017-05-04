package org.jfree.daydate;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public enum DateInterval {
	OPEN() {
		@Override
		public boolean isIn(final int date, final int start, final int end) {
			return date > start && date < end;
		}
	},
	CLOSED_LEFT() {
		@Override
		public boolean isIn(final int date, final int start, final int end) {
			return date >= start && date < end;
		}
	},
	CLOSED_RIGHT() {
		@Override
		public boolean isIn(final int date, final int start, final int end) {
			return date > start && date <= end;
		}
	},
	CLOSED() {
		@Override
		public boolean isIn(final int date, final int start, final int end) {
			return date >= start && date <= end;
		}
	};


	public abstract boolean isIn(
			int date, int start, int end);
}
