package com.rad.scrab.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.rad.scrab.model.Histories;
import com.rad.scrab.model.History;
import com.rad.scrab.model.ReplayWord;
import com.rad.scrab.model.Unit;
import com.rad.scrab.model.Words;

public interface GameService {
	public ArrayList<Unit> spitWord(HashMap<String, Boolean> dict, HashMap<String, ArrayList<String>> alphabet2);

	public void fillTable();

	public int countScore(ArrayList<ArrayList<Unit>> words);

	public String checkPosition(int row, int column);

	public Unit findUnit(int row, int column);

	public ArrayList<ArrayList<Integer>> getCombinations(ArrayList<Integer> l, Integer length);

	public void submitWord();

	public ArrayList<Integer> getLetters(Integer howmanyy);

	public void transferWords(Words words);

	public void setCycle(int cycle);

	public int getCycle();

	public ArrayList<Unit> newGame();

	public void getBotlets(Integer howmanyy);

	public void gameOver();

	public void saveReplayWord(ReplayWord word);

	public ArrayList<History> getHistory();

	public Words getMatch(int id);

	public void swapExchange(ArrayList<Integer> exArr);
}
