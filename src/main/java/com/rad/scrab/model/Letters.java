package com.rad.scrab.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Letters {

	private List<String> letters = new ArrayList<String>();
	private List<Integer> points = new ArrayList<Integer>();

	public Letters() {

		letters.add("a");
		points.add(1);
		letters.add("a");
		points.add(1);
		letters.add("a");
		points.add(1);
		letters.add("a");
		points.add(1);
		letters.add("a");
		points.add(1);
		letters.add("a");
		points.add(1);
		letters.add("a");
		points.add(1);
		letters.add("a");
		points.add(1);
		letters.add("a");
		points.add(1);
		letters.add("ą");
		points.add(5);
		letters.add("b");
		points.add(3);
		letters.add("b");
		points.add(3);
		letters.add("c");
		points.add(2);
		letters.add("c");
		points.add(2);
		letters.add("c");
		points.add(2);
		letters.add("ć");
		points.add(6);
		letters.add("d");
		points.add(2);
		letters.add("d");
		points.add(2);
		letters.add("d");
		points.add(2);
		letters.add("e");
		points.add(1);
		letters.add("e");
		points.add(1);
		letters.add("e");
		points.add(1);
		letters.add("e");
		points.add(1);
		letters.add("e");
		points.add(1);
		letters.add("e");
		points.add(1);
		letters.add("e");
		points.add(1);
		letters.add("ę");
		points.add(5);
		letters.add("f");
		points.add(5);
		letters.add("g");
		points.add(3);
		letters.add("g");
		points.add(3);
		letters.add("h");
		points.add(3);
		letters.add("h");
		points.add(3);
		letters.add("i");
		points.add(1);
		letters.add("i");
		points.add(1);
		letters.add("i");
		points.add(1);
		letters.add("i");
		points.add(1);
		letters.add("i");
		points.add(1);
		letters.add("i");
		points.add(1);
		letters.add("i");
		points.add(1);
		letters.add("i");
		points.add(1);
		letters.add("j");
		points.add(3);
		letters.add("j");
		points.add(3);
		letters.add("k");
		points.add(2);
		letters.add("k");
		points.add(2);
		letters.add("k");
		points.add(2);
		letters.add("l");
		points.add(2);
		letters.add("l");
		points.add(2);
		letters.add("l");
		points.add(2);
		letters.add("ł");
		points.add(3);
		letters.add("ł");
		points.add(3);
		letters.add("m");
		points.add(2);
		letters.add("m");
		points.add(2);
		letters.add("m");
		points.add(2);
		letters.add("n");
		points.add(1);
		letters.add("n");
		points.add(1);
		letters.add("n");
		points.add(1);
		letters.add("n");
		points.add(1);
		letters.add("n");
		points.add(1);
		letters.add("ń");
		points.add(7);
		letters.add("o");
		points.add(1);
		letters.add("o");
		points.add(1);
		letters.add("o");
		points.add(1);
		letters.add("o");
		points.add(1);
		letters.add("o");
		points.add(1);
		letters.add("o");
		points.add(1);
		letters.add("ó");
		points.add(5);
		letters.add("p");
		points.add(2);
		letters.add("p");
		points.add(2);
		letters.add("p");
		points.add(2);
		letters.add("r");
		points.add(1);
		letters.add("r");
		points.add(1);
		letters.add("r");
		points.add(1);
		letters.add("r");
		points.add(1);
		letters.add("s");
		points.add(1);
		letters.add("s");
		points.add(1);
		letters.add("s");
		points.add(1);
		letters.add("s");
		points.add(1);
		letters.add("ś");
		points.add(5);
		letters.add("t");
		points.add(2);
		letters.add("t");
		points.add(2);
		letters.add("t");
		points.add(2);
		letters.add("u");
		points.add(3);
		letters.add("u");
		points.add(3);
		letters.add("w");
		points.add(1);
		letters.add("w");
		points.add(1);
		letters.add("w");
		points.add(1);
		letters.add("w");
		points.add(1);
		letters.add("y");
		points.add(2);
		letters.add("y");
		points.add(2);
		letters.add("y");
		points.add(2);
		letters.add("y");
		points.add(2);
		letters.add("z");
		points.add(1);
		letters.add("z");
		points.add(1);
		letters.add("z");
		points.add(1);
		letters.add("z");
		points.add(1);
		letters.add("z");
		points.add(1);
		letters.add("ż");
		points.add(5);
		letters.add("ź");
		points.add(9);

		for (int i = 0; i < letters.size(); i++) {
			letters.get(i).toLowerCase();
			System.out.println(letters.get(i));
		}

	}

	public String getLetter(int z) {
		return letters.get(z);

	}

	public Integer getPoint(int z) {
		return points.get(z);
	}

	@SuppressWarnings("rawtypes")
	public List getAllLetters() {
		return letters;
	}

}
