package com.rad.scrab.model;

import java.util.ArrayList;
import java.util.List;

public class Word {
	private List<Unit> word;
	private int score;

	public Word() {
		score = 0;
		word = new ArrayList<Unit>();
	}

	public Word(List<Unit> word, int score) {
		this.word = word;
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void addMove(Unit m) {
		word.add(m);
	}

	public List<Unit> getWord() {
		return word;
	}

	public void setWord(List<Unit> word) {
		this.word = word;
	}

}
