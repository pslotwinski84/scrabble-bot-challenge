package com.rad.scrab.model;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Unit {

	private int letter, row, column;
	private boolean bonus;

	public Unit(int letter, int row, int column, boolean bonus) {
		this.letter = letter;
		this.row = row;
		this.column = column;
		this.bonus = bonus;
	}

	public int getLetter() {
		return letter;
	}

	public void setLetter(int letter) {
		this.letter = letter;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public boolean isBonus() {
		return bonus;
	}

	public void setBonus(boolean bonus) {
		this.bonus = bonus;
	}

	public static Comparator<Unit> RowComparator = new Comparator<Unit>() {

		public int compare(Unit s1, Unit s2) {
			Integer StudentName1 = s1.getRow();
			Integer StudentName2 = s2.getRow();

			// ascending order
			return StudentName1.compareTo(StudentName2);

			// descending order
			// return StudentName2.compareTo(StudentName1);
		}
	};

	public static Comparator<Unit> ColumnComparator = new Comparator<Unit>() {

		public int compare(Unit s1, Unit s2) {
			Integer StudentName1 = s1.getColumn();
			Integer StudentName2 = s2.getColumn();

			// ascending order
			return StudentName1.compareTo(StudentName2);

			// descending order
			// return StudentName2.compareTo(StudentName1);
		}
	};

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (bonus ? 1231 : 1237);
		result = prime * result + column;
		result = prime * result + letter;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unit other = (Unit) obj;
		if (bonus != other.bonus)
			return false;
		if (column != other.column)
			return false;
		if (letter != other.letter)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

}
