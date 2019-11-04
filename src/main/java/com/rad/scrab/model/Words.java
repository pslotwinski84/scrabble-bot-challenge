package com.rad.scrab.model;

import java.util.ArrayList;
import java.util.List;

public class Words {

	private int score;
	private List<Word> words;

	public Words() {
		score = 0;
		words = new ArrayList<Word>();
	}

	public Words(List<Word> words) {
		this.words = words;
		score = 0;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}
}