package com.rad.scrab.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.rad.scrab.dao.GameDAO;
import com.rad.scrab.model.Histories;
import com.rad.scrab.model.History;
import com.rad.scrab.model.Letters;
import com.rad.scrab.model.ReplayMatch;
import com.rad.scrab.model.ReplayUnit;
import com.rad.scrab.model.ReplayWord;
import com.rad.scrab.model.Unit;
import com.rad.scrab.model.User;
import com.rad.scrab.model.Word;
import com.rad.scrab.model.Words;

@Service("gameService")
public class GameServiceImpl implements GameService {

	private Letters letters = new Letters();
	private String[][] table = new String[15][15];
	private Gson gson = new Gson();
	private HashMap<Unit, String> contactmap = new HashMap<Unit, String>();
	private ArrayList<Integer> tnr = new ArrayList<Integer>();
	private ArrayList<Integer> botlets = new ArrayList<Integer>();
	private ArrayList<Integer> botlets2 = new ArrayList<Integer>();
	private ArrayList<Unit> units_done = new ArrayList<Unit>();
	private Random r = new Random();
	private ArrayList<Unit> units = new ArrayList<Unit>();
	private int gameCounter = 1;
	private Words words3 = new Words(null);
	private ArrayList<ArrayList<Unit>> words1 = new ArrayList<ArrayList<Unit>>();
	private int botscore = 0;
	private ReplayMatch replayMatch = new ReplayMatch();
	private User user = new User();
	private String currentPrincipalName;

	@Autowired
	private GameDAO gameDAO;

	@Autowired
	private UserService userService;

	@Autowired
	private DictionaryService dictionaryService;

	@Transactional
	public void saveReplayWord(ReplayWord replayWord) {
		this.gameDAO.saveReplayWord(replayWord);
	}

	@Transactional
	public Words getMatch(int id) {

		Words matchWords = gameDAO.getMatch(id);

		return matchWords;
	}

	@Transactional
	public void transferWords(Words words) {

		ArrayList<Unit> toReplayUnits = new ArrayList<Unit>();

		for (int j = 0; j < words.getWords().get(words.getWords().size() - 1).getWord().size(); j++) {

			Unit tempUnit = new Unit(words.getWords().get(words.getWords().size() - 1).getWord().get(j).getLetter(),
					words.getWords().get(words.getWords().size() - 1).getWord().get(j).getRow(),
					words.getWords().get(words.getWords().size() - 1).getWord().get(j).getColumn(), false);

			if (!units_done.contains(tempUnit)) {
				toReplayUnits.add(tempUnit);
			}
		}

		for (int i = 0; i < words.getWords().size(); i++) {
			for (int j = 0; j < words.getWords().get(i).getWord().size(); j++) {

				Unit temp_unit = new Unit(words.getWords().get(i).getWord().get(j).getLetter(),
						words.getWords().get(i).getWord().get(j).getRow(),
						words.getWords().get(i).getWord().get(j).getColumn(), false);

				if (!units_done.contains(temp_unit)) {

					units_done.add(temp_unit);
				}
			}
		}

		ArrayList<ReplayUnit> replayUnits = new ArrayList<ReplayUnit>();
		ReplayWord replayWord = new ReplayWord();
		for (int i = 0; i < toReplayUnits.size(); i++) {

			ReplayUnit tempReplayUnit = new ReplayUnit();
			tempReplayUnit.setLetter(toReplayUnits.get(i).getLetter());
			tempReplayUnit.setRow(toReplayUnits.get(i).getRow());
			tempReplayUnit.setCol(toReplayUnits.get(i).getColumn());
			tempReplayUnit.setReplayword(replayWord);
			replayUnits.add(tempReplayUnit);
		}

		replayMatch.setHumanpoints(replayMatch.getHumanpoints() + words.getScore());
		replayMatch = gameDAO.updateMatch(replayMatch);

		replayWord.setReplaymatch(replayMatch);
		replayWord.setReplayunits(replayUnits);
		replayWord.setScore(words.getScore());
		replayWord.setHuman(true);
		replayWord.setDate(new Date());
		saveReplayWord(replayWord);

	}

	public void getBotlets(Integer howmanyy) {
		for (int i = 0; i < howmanyy; i++) {
			int rr = r.nextInt(letters.getAllLetters().size());
			while (tnr.contains(rr)) {
				rr = r.nextInt(letters.getAllLetters().size());
			}

			tnr.add(rr);
			botlets.add(rr);
		}
	}

	public ArrayList<Integer> getLetters(Integer howmanyy) {

		ArrayList<Integer> ready = new ArrayList<Integer>();
		for (int i = 0; i < howmanyy; i++) {
			int rr = r.nextInt(letters.getAllLetters().size());
			while (tnr.contains(rr)) {
				rr = r.nextInt(letters.getAllLetters().size());

				if (tnr.size() == letters.getAllLetters().size() && ready.isEmpty()) {
					System.out.println("game over");
					rr = -1;

					ready.clear();
					ready.add(rr);
					return ready;

				}

			}

			tnr.add(rr);
			ready.add(rr);

		}
		System.out.println("tnr to " + tnr.toString());
		return ready;
	}

	@Transactional
	public ArrayList<Unit> spitWord(HashMap<String, Boolean> dict, HashMap<String, ArrayList<String>> alphabet2) {

		HashMap<ArrayList<Unit>, Integer> scores = new HashMap<ArrayList<Unit>, Integer>();

		for (int c = botlets.size(); c >= 2; c--) {
			ArrayList<ArrayList<Integer>> combis = getCombinations(botlets, c);
			ArrayList<String> words2 = new ArrayList<String>();

			for (ArrayList<Integer> a : combis) {
				String oneword = new String();
				for (Integer in : a) {
					oneword += letters.getLetter(in);
				}
				words2.add(oneword);
				System.out.println("slowo to " + oneword);
			}
			combination: for (int b = 0; b < words2.size(); b++) {
				String alphaKey = new String();
				HashMap<String, Integer> wordmap = new HashMap<String, Integer>();

				for (int i = 0; i < words2.get(b).length(); i++) {

					int count = words2.get(b).length()
							- words2.get(b).replaceAll(words2.get(b).substring(i, i + 1), "").length();
					wordmap.put(words2.get(b).substring(i, i + 1), count);
				}

				Map<String, Integer> treeMap = new TreeMap<String, Integer>(wordmap);

				for (Map.Entry<String, Integer> entry2 : treeMap.entrySet()) {

					for (int i = 0; i < entry2.getValue(); i++) {

						alphaKey += entry2.getKey();
					}

				}

				if (!alphabet2.containsKey(alphaKey)) {
					continue combination;
				}

				else {

					String fWord = new String();

					for (int e = 0; e < alphabet2.get(alphaKey).size(); e++) {

						fWord = alphabet2.get(alphaKey).get(e);

						System.out.println("alpha to " + alphaKey);
						System.out.println("fword to " + fWord);

						ArrayList<Integer> realWordInts = new ArrayList<Integer>();

						botlets2 = new ArrayList<Integer>();
						botlets2.addAll(botlets);
						for (int i = 0; i < fWord.length(); i++) {

							for (int j = 0; j < botlets.size(); j++) {

								if (fWord.substring(i, i + 1).equals(letters.getLetter(botlets.get(j)))
										&& botlets2.contains((botlets.get(j)))) {
									System.out.println(i);
									realWordInts.add(botlets.get(j));
									botlets2.remove(botlets.get(j));
									break;
								}
							}
						}

						for (int i = 0; i < 15; i++)
							word: for (int j = 0; j < 15 - realWordInts.size(); j++) {
								units.clear();

								for (int k = 0; k < realWordInts.size(); k++) {

									if (findUnit(i, j + k) == null) {

										units.add(new Unit(realWordInts.get(k), i, j + k, true));
									} else {
										continue word;
									}

								}
								words3.setWords(null);
								submitWord();

								System.out.println("game to " + gameCounter);

								int condition = 1;
								if (gameCounter == 0) {
									condition = 1;
								} else {
									condition = 2;
								}

								if (words3.getWords() != null && words3.getWords().size() >= condition) {

									String mainword = new String();

									for (int f = 0; f < words3.getWords().get(words3.getWords().size() - 1).getWord()
											.size(); f++) {

										mainword += letters.getLetter(words3.getWords()
												.get(words3.getWords().size() - 1).getWord().get(f).getLetter());
									}

									if (!dict.containsKey(mainword)) {
										continue word;
									}

									for (int h = 0; h < words3.getWords().size() - 1; h++) {

										String secondWord = new String();
										for (int z = 0; z < words3.getWords().get(h).getWord().size(); z++) {
											secondWord += letters
													.getLetter(words3.getWords().get(h).getWord().get(z).getLetter());
										}
										System.out.println(secondWord);
										if (!dict.containsKey(secondWord)) {

											System.out.println("nie zawiera slowa " + secondWord);

											continue word;
										}

									}

									int score = countScore(words1);

									scores.put(new ArrayList<Unit>(units), score);

								}

							}

						for (int i = 0; i < 15; i++)
							word: for (int j = 0; j < 15 - realWordInts.size(); j++) {
								units.clear();

								for (int k = 0; k < realWordInts.size(); k++) {

									if (findUnit(j + k, i) == null) {

										units.add(new Unit(realWordInts.get(k), j + k, i, true));
									} else {
										continue word;
									}

								}
								words1 = null;
								words3.setWords(null);
								submitWord();
								int condition = 1;
								if (gameCounter == 0) {
									condition = 1;
								} else {
									condition = 2;
								}
								if (words3.getWords() != null && words3.getWords().size() >= condition) {

									for (int h = 0; h < words3.getWords().size() - 1; h++) {

										String mainword = new String();

										for (int f = 0; f < words3.getWords().get(words3.getWords().size() - 1)
												.getWord().size(); f++) {

											mainword += letters.getLetter(words3.getWords()
													.get(words3.getWords().size() - 1).getWord().get(f).getLetter());
										}

										if (!dict.containsKey(mainword)) {
											continue word;
										}

										String secondWord = new String();
										for (int z = 0; z < words3.getWords().get(h).getWord().size(); z++) {
											secondWord += letters
													.getLetter(words3.getWords().get(h).getWord().get(z).getLetter());
										}
										System.out.println(secondWord);
										if (!dict.containsKey(secondWord)) {

											System.out.println("nie zawiera slowa " + secondWord);

											continue word;
										}

									}

									int score = countScore(words1);

									scores.put(new ArrayList<Unit>(units), score);

								}

							}

					}
				}
			}
		}

		if (scores.isEmpty()) {

			for (int g = 0; g < botlets.size(); g++) {

				for (int i = 0; i < 15; i++)
					word: for (int j = 0; j < 15; j++) {
						units.clear();

						if (findUnit(i, j) == null) {
							units.add(new Unit(botlets.get(g), i, j, true));
						} else {
							continue word;
						}

						words3.setWords(null);
						submitWord();

						System.out.println("game to " + gameCounter);

						if (words3.getWords() != null && words3.getWords().size() >= 1) {

							String mainword = new String();

							for (int f = 0; f < words3.getWords().get(words3.getWords().size() - 1).getWord()
									.size(); f++) {

								mainword += letters.getLetter(words3.getWords().get(words3.getWords().size() - 1)
										.getWord().get(f).getLetter());
							}

							if (!dict.containsKey(mainword)) {
								continue word;
							}

							for (int h = 0; h < words3.getWords().size() - 1; h++) {

								String secondWord = new String();
								for (int z = 0; z < words3.getWords().get(h).getWord().size(); z++) {
									secondWord += letters
											.getLetter(words3.getWords().get(h).getWord().get(z).getLetter());
								}
								System.out.println(secondWord);
								if (!dict.containsKey(secondWord)) {

									System.out.println("nie zawiera slowa " + secondWord);

									continue word;
								}

							}

							int score = countScore(words1);

							scores.put(new ArrayList<Unit>(units), score);

						}

					}

			}
		}

		final Map<ArrayList<Unit>, Integer> sortedByCount = scores.entrySet().stream()
				.sorted((Map.Entry.<ArrayList<Unit>, Integer>comparingByValue().reversed()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		ArrayList<Unit> unitsfinal = new ArrayList<Unit>();
		int firstcounter = 0;

		int finalscore = 0;
		for (Map.Entry<ArrayList<Unit>, Integer> entry2 : sortedByCount.entrySet()) {

			if (firstcounter == 0) {
				unitsfinal = entry2.getKey();
				finalscore = entry2.getValue();
				firstcounter++;
			} else {
				break;
			}
		}

		ArrayList<Integer> finalInts = new ArrayList<Integer>();

		for (int i = 0; i < unitsfinal.size(); i++) {
			finalInts.add(unitsfinal.get(i).getLetter());
		}

		botlets.removeAll(finalInts);

		for (int i = 0; i < finalInts.size(); i++) {

			if (tnr.size() == letters.getAllLetters().size()) {
				break;
			}

			int rr = r.nextInt(letters.getAllLetters().size());
			while (tnr.contains(rr)) {
				rr = r.nextInt(letters.getAllLetters().size());

			}

			tnr.add(rr);
			botlets.add(rr);

		}

		ArrayList<Unit> toReplayUnits = new ArrayList<Unit>(unitsfinal);

		if (botlets.size() == 0) {
			unitsfinal.add(new Unit(finalscore, -1, -1, true));
		}

		else {
			unitsfinal.add(new Unit(finalscore, 0, 0, false));

		}

		for (int i = 0; i < unitsfinal.size() - 1; i++) {

			Unit temp_unit = new Unit(unitsfinal.get(i).getLetter(), unitsfinal.get(i).getRow(),
					unitsfinal.get(i).getColumn(), false);

			if (!units_done.contains(temp_unit)) {

				units_done.add(temp_unit);

			}
		}

		ArrayList<ReplayUnit> replayUnits = new ArrayList<ReplayUnit>();
		ReplayWord replayWord = new ReplayWord();
		for (int i = 0; i < toReplayUnits.size(); i++) {

			ReplayUnit tempReplayUnit = new ReplayUnit();
			tempReplayUnit.setLetter(toReplayUnits.get(i).getLetter());
			tempReplayUnit.setRow(toReplayUnits.get(i).getRow());
			tempReplayUnit.setCol(toReplayUnits.get(i).getColumn());
			tempReplayUnit.setReplayword(replayWord);
			replayUnits.add(tempReplayUnit);
		}

		replayMatch.setBotpoints(replayMatch.getBotpoints() + finalscore);
		replayMatch = gameDAO.updateMatch(replayMatch);

		replayWord.setReplaymatch(replayMatch);
		replayWord.setReplayunits(replayUnits);
		replayWord.setScore(finalscore);
		replayWord.setHuman(false);
		replayWord.setDate(new Date());
		saveReplayWord(replayWord);

		gameCounter++;
		return unitsfinal;
	}

	public void fillTable() {
		table[0][0] = "3s";
		table[0][7] = "3s";
		table[0][14] = "3s";
		table[7][0] = "3s";
		table[7][14] = "3s";
		table[14][0] = "3s";
		table[14][7] = "3s";
		table[14][14] = "3s";

		table[1][1] = "2s";
		table[2][2] = "2s";
		table[3][3] = "2s";
		table[4][4] = "2s";
		table[1][13] = "2s";
		table[2][12] = "2s";
		table[3][11] = "2s";
		table[4][10] = "2s";
		table[10][4] = "2s";
		table[11][3] = "2s";
		table[12][2] = "2s";
		table[13][1] = "2s";
		table[10][10] = "2s";
		table[11][11] = "2s";
		table[12][12] = "2s";
		table[12][12] = "2s";
		table[13][13] = "2s";

		table[7][7] = "2s";

		table[0][3] = "2l";
		table[0][11] = "2l";
		table[1][5] = "3l";
		table[1][10] = "3l";
		table[2][6] = "2l";
		table[2][9] = "2l";
		table[3][0] = "2l";
		table[3][7] = "2l";
		table[3][14] = "2l";
		table[5][1] = "3l";
		table[5][5] = "3l";
		table[5][9] = "3l";
		table[5][13] = "3l";
		table[6][2] = "2l";
		table[6][6] = "2l";
		table[6][8] = "2l";
		table[6][12] = "2l";
		table[7][3] = "2l";
		table[7][11] = "2l";
		table[8][2] = "2l";
		table[8][6] = "2l";
		table[8][8] = "2l";
		table[8][12] = "2l";
		table[9][1] = "3l";
		table[9][5] = "3l";
		table[9][9] = "3l";
		table[9][13] = "3l";
		table[11][0] = "2l";
		table[11][7] = "2l";
		table[11][14] = "2l";
		table[12][6] = "2l";
		table[12][8] = "2l";
		table[13][5] = "3l";
		table[13][9] = "3l";
		table[14][3] = "2l";
		table[14][11] = "2l";

	}

	public int countScore(ArrayList<ArrayList<Unit>> words) {
		int globalcounter = 0;
		for (int i = 0; i < words.size(); i++) {
			int wordcounter = 0;
			int wordbonus = 1;

			int sevencounter = 0;
			for (int j = 0; j < words.get(i).size(); j++) {

				if (words.get(i).get(j).isBonus() == true) {

					sevencounter++;

					if (table[words.get(i).get(j).getRow()][words.get(i).get(j).getColumn()] == null) {
						wordcounter += letters.getPoint(words.get(i).get(j).getLetter());
						continue;
					}

					if (table[words.get(i).get(j).getRow()][words.get(i).get(j).getColumn()].equals("2l")) {
						wordcounter += letters.getPoint(words.get(i).get(j).getLetter()) * 2;
					}
					if (table[words.get(i).get(j).getRow()][words.get(i).get(j).getColumn()].equals("3l")) {
						wordcounter += letters.getPoint(words.get(i).get(j).getLetter()) * 3;
					}

					if (table[words.get(i).get(j).getRow()][words.get(i).get(j).getColumn()].equals("")) {
						wordcounter += letters.getPoint(words.get(i).get(j).getLetter());
					}
					if (table[words.get(i).get(j).getRow()][words.get(i).get(j).getColumn()].equals("3s")) {
						wordcounter += letters.getPoint(words.get(i).get(j).getLetter());
						wordbonus = wordbonus * 3;
					}
					if (table[words.get(i).get(j).getRow()][words.get(i).get(j).getColumn()].equals("2s")) {
						wordcounter += letters.getPoint(words.get(i).get(j).getLetter());
						wordbonus = wordbonus * 2;
					}

				} else {
					// alert('say hello');
					wordcounter += letters.getPoint(words.get(i).get(j).getLetter());
				}

			}

			if (sevencounter == 7) {
				globalcounter += 50;
			}

			System.out.println("wordbonus to " + wordbonus);
			wordcounter = wordcounter * wordbonus;
			globalcounter += wordcounter;
		}
		System.out.println("globalcounter to " + globalcounter);
		return globalcounter;
	}

	public String checkPosition(int row, int column) {

		String sides = new String();
		for (int i = 0; i < units_done.size(); i++) {
			if (units_done.get(i).getRow() == row - 1 && units_done.get(i).getColumn() == column) {
				contactmap.put(units_done.get(i), "up");

				sides += "up";
			}
			if (units_done.get(i).getRow() == row + 1 && units_done.get(i).getColumn() == column) {
				contactmap.put(units_done.get(i), "down");
				sides += "down";
				System.out.println("down");
			}
			if (units_done.get(i).getRow() == row && units_done.get(i).getColumn() == column - 1) {
				contactmap.put(units_done.get(i), "left");
				sides += "left";
			}
			if (units_done.get(i).getRow() == row && units_done.get(i).getColumn() == column + 1) {
				contactmap.put(units_done.get(i), "right");
				sides += "right";
			}

		}
		return sides;
	}

	public Unit findUnit(int row, int column) {

		for (int i = 0; i < units_done.size(); i++) {
			if (units_done.get(i).getRow() == row && units_done.get(i).getColumn() == column) {
				return units_done.get(i);
			}
		}

		return null;
	}

	public ArrayList<ArrayList<Integer>> getCombinations(ArrayList<Integer> l, Integer length) {

		Set<Integer> mySet = new HashSet<>(l);
		Set<Set<Integer>> combis = Sets.combinations(mySet, length);

		ArrayList<ArrayList<Integer>> cfinal = new ArrayList<ArrayList<Integer>>();

		for (Set<Integer> c : combis) {
			ArrayList<Integer> h = new ArrayList<Integer>(c);
			cfinal.add(h);
		}
		System.out.println(cfinal.toString());

		return cfinal;
	}

	public void submitWord() {

		Collections.sort(units, Unit.RowComparator);
		Collections.sort(units, Unit.ColumnComparator);
		Collections.sort(units_done, Unit.RowComparator);
		Collections.sort(units_done, Unit.ColumnComparator);

		boolean cbool = true;
		boolean crow = true;
		ArrayList<ArrayList<Unit>> words = new ArrayList<ArrayList<Unit>>();
		for (int i = 0; i < units.size(); i++) {
			if (units.get(i).getColumn() != units.get(0).getColumn()) {
				cbool = false;
			}
		}

		for (int i = 0; i < units.size(); i++) {
			if (units.get(i).getRow() != units.get(0).getRow()) {
				crow = false;
			}
		}

		if (units.size() == 1) {
			cbool = true;
			crow = true;
		}

		if (cbool) {
			ArrayList<Integer> rownrs = new ArrayList<Integer>();
			for (int i = 0; i < units.size(); i++) {
				rownrs.add(units.get(i).getRow());
			}
			// rownrs.sort();
			boolean ok = true;
			int counter = 0;
			int difference = 0;
			int rowmiddle = 0;
			ArrayList<Unit> mainword = new ArrayList<Unit>();
			for (int i = 0; i < rownrs.size() - 1; i++) {
				if (rownrs.get(i) - rownrs.get(i + 1) != -1) {
					ok = false;
					System.out.println("niedozwolone slowo, jedziemy dalej");
					counter++;
					difference = rownrs.get(i) - rownrs.get(i + 1);
					rowmiddle = rownrs.get(i + 1) - 1;
				}

			}
			if (!ok) {

				if (counter > 1) {
					System.out.println("niedozwolony wyraz, kilka roznych wyrazow w jednej linii");
					return;
				}

				if (counter == 1 && difference != -2) {
					System.out.println("niedozwolony wyraz, przerwa miedzy wyrazami zbyt dluga");
					System.out.println("difference to " + difference);
					return;
				} else {
					System.out.println("ok");
					System.out.println("rowmiddle " + rowmiddle);
					// System.out.println(checkLetter(rowmiddle+1, units[0].column));

					if (checkPosition(rowmiddle + 1, units.get(0).getColumn()).contains("up")
							&& checkPosition(rowmiddle - 1, units.get(0).getColumn()).contains("down")) {
						System.out.println("double check");

						mainword.add(findUnit(rowmiddle, units.get(0).getColumn()));

						for (int i = 0; i < rownrs.size(); i++) {
							mainword.add(findUnit(rownrs.get(i), units.get(0).getColumn()));
						}

						Collections.sort(mainword, Unit.RowComparator);
						Collections.sort(mainword, Unit.ColumnComparator);

					} else {
						return;
					}

					// alert(JSON.stringify(contactmap));
				}
			}
			if (ok) {
				ArrayList<Integer> uprows = new ArrayList<Integer>();
				if (checkPosition(rownrs.get(0), units.get(0).getColumn()).contains("up")) {

					for (int i = 0; i < units_done.size(); i++) {
						if (units_done.get(i).getRow() < rownrs.get(0)
								&& units_done.get(i).getColumn() == units.get(0).getColumn()) {
							uprows.add(units_done.get(i).getRow());
						}
					}
				}
				Collections.sort(uprows);
				;
				Collections.reverse(uprows);

				ArrayList<Integer> uprowsfinal = new ArrayList<Integer>();

				if (!uprows.isEmpty()) {

					uprowsfinal.add(uprows.get(0));

				}
				for (int i = 0; i < uprows.size() - 1; i++) {
					if (uprows.get(i) - uprows.get(i + 1) == 1) {
						// if (!uprowsfinal.includes(uprows[i])){
						// uprowsfinal.push(uprows[i]);
						// }
						if (!uprowsfinal.contains(uprows.get(i + 1))) {
							uprowsfinal.add(uprows.get(i + 1));
						}
					} else {
						break;
					}

				}
				// alert(JSON.stringify(uprowsfinal));

				ArrayList<Integer> downrows = new ArrayList<Integer>();
				if (checkPosition(rownrs.get(rownrs.size() - 1), units.get(0).getColumn()).contains("down")) {

					for (int i = 0; i < units_done.size(); i++) {
						if (units_done.get(i).getRow() > rownrs.get(0)
								&& units_done.get(i).getColumn() == units.get(0).getColumn()) {
							downrows.add(units_done.get(i).getRow());
						}
					}
				}
				Collections.sort(downrows);
				// uprows.reverse();

				ArrayList<Integer> downrowsfinal = new ArrayList<Integer>();

				if (!downrows.isEmpty()) {
					downrowsfinal.add(downrows.get(0));
				}
				for (int i = 0; i < downrows.size() - 1; i++) {
					if (downrows.get(i) - downrows.get(i + 1) == -1) {
						// if (!uprowsfinal.includes(uprows[i])){
						// uprowsfinal.push(uprows[i]);
						// }
						if (!downrowsfinal.contains(downrows.get(i + 1))) {
							downrowsfinal.add(downrows.get(i + 1));
						}
					} else {
						break;
					}

				}

				for (int i = 0; i < uprowsfinal.size(); i++) {

					downrowsfinal.add(uprowsfinal.get(i));
				}

				for (int i = 0; i < downrowsfinal.size(); i++) {
					if (findUnit(downrowsfinal.get(i), units.get(0).getColumn()) != null) {
						mainword.add(findUnit(downrowsfinal.get(i), units.get(0).getColumn()));
					}
				}
				for (int i = 0; i < units.size(); i++) {
					mainword.add(units.get(i));
				}

			}
			boolean contact = false;
			for (int i = 0; i < units.size(); i++) {

				if (!checkPosition(units.get(i).getRow(), units.get(i).getColumn()).isEmpty()) {
					contact = true;
					break;
				}
			}

			contactmap.clear();
			for (int i = 0; i < units.size(); i++) {

				checkPosition(units.get(i).getRow(), units.get(i).getColumn());
			}

			if (gameCounter == 0) {
				boolean contact_middle = false;

				for (int i = 0; i < units.size(); i++) {
					if (units.get(i).getColumn() == 7 && units.get(i).getRow() == 7) {
						contact_middle = true;
						break;
					}
				}

				if (mainword.size() != 1 && contact_middle) {

					words.add(mainword);

					for (int z = 0; z < words.size(); z++) {
						System.out.println("word nr " + z + " to " + gson.toJson(words.get(z)));
					}

					dictionary(words);
					words1 = words;
					words3 = doClass(words);

				}

				return;
			}

			if (!contact) {
				System.out.println("returning");
				return;
			}

			ArrayList<Integer> colnrs = new ArrayList<Integer>();
			ArrayList<Integer> wordnrs = new ArrayList<Integer>();

			for (Map.Entry<Unit, String> entry : contactmap.entrySet()) {

				colnrs.clear();
				if (entry.getValue().equals("right")) {
					wordnrs.clear();
					colnrs.clear();
					for (int j = 0; j < units_done.size(); j++) {

						if (units_done.get(j).getRow() == entry.getKey().getRow()) {
							colnrs.add(units_done.get(j).getColumn());
						}
					}
					Collections.sort(colnrs);

					ArrayList<Integer> colnrs2 = new ArrayList<Integer>();

					for (int k = 0; k < colnrs.size(); k++) {
						if (colnrs.get(k) >= entry.getKey().getColumn()) {

							colnrs2.add(colnrs.get(k));

						}
					}

					colnrs = colnrs2;

					boolean still = true;
					ArrayList<Unit> wordUnits = new ArrayList<Unit>();
					if (colnrs.size() == 1) {
						Unit wordUnit = findUnit(entry.getKey().getRow(), entry.getKey().getColumn());
						wordUnits.add(wordUnit);
					} else {

						for (int j = 0; j < colnrs.size() - 1; j++) {

							if (still && colnrs.get(j) >= entry.getKey().getColumn()
									&& colnrs.get(j) - colnrs.get(j + 1) == -1) {
								if (!wordnrs.contains(colnrs.get(j))) {
									wordnrs.add(colnrs.get(j));
								}
								if (!wordnrs.contains(colnrs.get(j + 1))) {
									wordnrs.add(colnrs.get(j + 1));
								}
							} else {
								break;
							}
						}

						if (wordnrs.isEmpty()) {
							wordnrs.add(colnrs.get(0));
						}

					}

					for (int j = 0; j < wordnrs.size(); j++) {
						Unit wordUnit = findUnit(entry.getKey().getRow(), wordnrs.get(j));
						wordUnits.add(wordUnit);

					}

					boolean second = false;

					// alert(JSON.stringify(wordUnits));
					for (int j = 0; j < words.size(); j++) {
						if (words.get(j).get(0).getRow() == entry.getKey().getRow()) {
							second = true;
							for (int k = 0; k < wordUnits.size(); k++) {

								words.get(j).add(wordUnits.get(k));
							}
						}
					}

					if (second == false) {

						ArrayList<Unit> newunits = new ArrayList<Unit>();

						for (int j = 0; j < units.size(); j++) {
							if (units.get(j).getRow() == entry.getKey().getRow()) {
								newunits.add(units.get(j));
							}
						}

						for (int j = 0; j < newunits.size(); j++) {
							wordUnits.add(newunits.get(j));
						}

						if (wordUnits.size() > 1) {

							Collections.sort(wordUnits, Unit.RowComparator);
							Collections.sort(wordUnits, Unit.ColumnComparator);

						}
						words.add(wordUnits);
					}

					// alert(JSON.stringify(wordUnits));

				}

				if (entry.getValue() == "left") {
					wordnrs.clear();
					colnrs.clear();
					ArrayList<Unit> wordUnits = new ArrayList<Unit>();
					for (int j = 0; j < units_done.size(); j++) {

						if (units_done.get(j).getRow() == entry.getKey().getRow()) {
							colnrs.add(units_done.get(j).getColumn());
						}
					}
					Collections.sort(colnrs);

					for (int k = 0; k < colnrs.size(); k++) {
						if (colnrs.get(k) > entry.getKey().getColumn()) {

							colnrs = new ArrayList<Integer>(colnrs.subList(0, k));
							break;
						}
					}
					Collections.reverse(colnrs);
					boolean still = true;
					if (colnrs.size() == 1) {
						Unit wordUnit = findUnit(entry.getKey().getRow(), entry.getKey().getColumn());
						wordUnits.add(wordUnit);
					} else {
						for (int j = 0; j < colnrs.size() - 1; j++) {

							if (still && colnrs.get(j) <= entry.getKey().getColumn()
									&& colnrs.get(j) - colnrs.get(j + 1) == 1) {
								if (!wordnrs.contains(colnrs.get(j))) {
									wordnrs.add(colnrs.get(j));
								}
								if (!wordnrs.contains(colnrs.get(j + 1))) {
									wordnrs.add(colnrs.get(j + 1));

								}
							} else {
								break;
							}
						}

						if (wordnrs.isEmpty()) {
							wordnrs.add(colnrs.get(0));
						}

					}

					for (int j = 0; j < wordnrs.size(); j++) {
						Unit wordUnit = findUnit(entry.getKey().getRow(), wordnrs.get(j));

						if (wordUnit != null) {

							wordUnits.add(wordUnit);
						}
					}

					ArrayList<Unit> newunits = new ArrayList<Unit>();

					for (int j = 0; j < units.size(); j++) {
						if (units.get(j).getRow() == entry.getKey().getRow()) {
							newunits.add(units.get(j));
						}
					}

					for (int j = 0; j < newunits.size(); j++) {

						if (!wordUnits.contains(newunits.get(j))) {
							wordUnits.add(newunits.get(j));
						}
					}
					if (wordUnits.size() > 1) {
						System.out.println("problematic to " + gson.toJson(wordUnits));
						Collections.sort(wordUnits, Unit.RowComparator);
						Collections.sort(wordUnits, Unit.ColumnComparator);

					}
					words.add(wordUnits);
				}

				// end CONTACT

			}
			;

			if (mainword.size() != 1) {
				Collections.sort(mainword, Unit.RowComparator);
				Collections.sort(mainword, Unit.ColumnComparator);

				words.add(mainword);
			}
		}

		if (crow) {

			ArrayList<Integer> colnrs = new ArrayList<Integer>();
			for (int i = 0; i < units.size(); i++) {
				colnrs.add(units.get(i).getColumn());
			}
			// colnrs.sort();
			boolean ok = true;
			int counter = 0;
			int difference = 0;
			int colmiddle = 0;
			ArrayList<Unit> mainword = new ArrayList<Unit>();
			for (int i = 0; i < colnrs.size() - 1; i++) {
				if (colnrs.get(i) - colnrs.get(i + 1) != -1) {
					ok = false;
					System.out.println("niedozwolone slowo, jedziemy dalej");
					counter++;
					difference = colnrs.get(i) - colnrs.get(i + 1);
					colmiddle = colnrs.get(i + 1) - 1;
				}

			}
			if (!ok) {
				if (counter > 1) {
					System.out.println("niedozwolony wyraz, kilka roznych wyrazow w jednej linii");
					return;
				}

				if (counter == 1 && difference != -2) {
					System.out.println("niedozwolony wyraz, przerwa miedzy wyrazami zbyt dluga");
					System.out.println("difference to " + difference);
					return;
				} else {
					System.out.println("ok");
					System.out.println("colmiddle " + colmiddle);
					System.out.println(checkPosition(units.get(0).getRow(), colmiddle + 1));

					if (checkPosition(units.get(0).getRow(), colmiddle + 1).contains("left")
							&& checkPosition(units.get(0).getRow(), colmiddle - 1).contains("right")) {
						System.out.println("double check");

						mainword.add(findUnit(units.get(0).getRow(), colmiddle));

						for (int i = 0; i < colnrs.size(); i++) {
							mainword.add(findUnit(units.get(0).getRow(), colnrs.get(i)));
						}
						Collections.sort(mainword, Unit.RowComparator);
						Collections.sort(mainword, Unit.ColumnComparator);

					} else {
						return;
					}

					// alert(JSON.stringify(contactmap));
				}
			}
			if (ok) {
				ArrayList<Integer> leftcols = new ArrayList<Integer>();
				if (checkPosition(units.get(0).getRow(), colnrs.get(0)).contains("left")) {

					for (int i = 0; i < units_done.size(); i++) {
						if (units_done.get(i).getColumn() < colnrs.get(0)
								&& units_done.get(i).getRow() == units.get(0).getRow()) {
							leftcols.add(units_done.get(i).getColumn());
						}
					}
				}
				Collections.sort(leftcols);
				Collections.reverse(leftcols);

				ArrayList<Integer> leftcolsfinal = new ArrayList<Integer>();

				if (!leftcols.isEmpty()) {
					leftcolsfinal.add(leftcols.get(0));
				}
				for (int i = 0; i < leftcols.size() - 1; i++) {
					if (leftcols.get(i) - leftcols.get(i + 1) == 1) {
						// if (!uprowsfinal.includes(uprows[i])){
						// uprowsfinal.push(uprows[i]);
						// }
						if (!leftcolsfinal.contains(leftcols.get(i + 1))) {
							leftcolsfinal.add(leftcols.get(i + 1));
						} else {
							break;
						}
					}
				}

				// alert(JSON.stringify(uprowsfinal));

				ArrayList<Integer> rightcols = new ArrayList<Integer>();
				if (checkPosition(units.get(0).getRow(), colnrs.get(colnrs.size() - 1)).contains("right")) {

					for (int i = 0; i < units_done.size(); i++) {
						if (units_done.get(i).getColumn() > colnrs.get(colnrs.size() - 1)
								&& units_done.get(i).getRow() == units.get(0).getRow()) {
							rightcols.add(units_done.get(i).getColumn());
						}

					}
				}
				Collections.sort(rightcols);
				// uprows.reverse();

				ArrayList<Integer> rightcolsfinal = new ArrayList<Integer>();

				if (!rightcols.isEmpty()) {

					rightcolsfinal.add(rightcols.get(0));
				}
				for (int i = 0; i < rightcols.size() - 1; i++) {
					if (rightcols.get(i) - rightcols.get(i + 1) == -1) {
						// if (!uprowsfinal.includes(uprows[i])){
						// uprowsfinal.push(uprows[i]);
						// }
						if (!rightcolsfinal.contains(rightcols.get(i + 1))) {
							rightcolsfinal.add(rightcols.get(i + 1));
						}
					} else {
						break;
					}

				}

				for (int i = 0; i < leftcolsfinal.size(); i++) {

					rightcolsfinal.add(leftcolsfinal.get(i));
				}

				for (int i = 0; i < rightcolsfinal.size(); i++) {
					if (findUnit(units.get(0).getRow(), rightcolsfinal.get(i)) != null) {

						mainword.add(findUnit(units.get(0).getRow(), rightcolsfinal.get(i)));
					}
				}

				for (int i = 0; i < units.size(); i++) {
					mainword.add(units.get(i));
				}
			}
			boolean contact = false;
			for (int i = 0; i < units.size(); i++) {

				if (!checkPosition(units.get(i).getRow(), units.get(i).getColumn()).isEmpty()) {
					contact = true;
					System.out.println("contact yeah");
					break;
				}
			}

			contactmap.clear();
			for (int i = 0; i < units.size(); i++) {

				checkPosition(units.get(i).getRow(), units.get(i).getColumn());
			}

			// System.out.println("contact map size to " +contactmap.size());

			words.clear();

			if (gameCounter == 0) {
				boolean contact_middle = false;

				for (int i = 0; i < units.size(); i++) {
					if (units.get(i).getColumn() == 7 && units.get(i).getRow() == 7) {
						contact_middle = true;
						break;
					}
				}

				if (mainword.size() != 1 && contact_middle) {

					words.add(mainword);

					for (int z = 0; z < words.size(); z++) {
						System.out.println("word nr " + z + " to " + gson.toJson(words.get(z)));
					}

					dictionary(words);
					words1 = words;
					words3 = doClass(words);

				}

				return;
			}

			if (!contact) {
				System.out.println("returning");
				return;
			}

			ArrayList<Integer> rownrs = new ArrayList<Integer>();
			ArrayList<Integer> wordnrs = new ArrayList<Integer>();

			for (Map.Entry<Unit, String> entry : contactmap.entrySet()) {

				if (entry.getValue().equals("down")) {
					ArrayList<Unit> wordUnits = new ArrayList<Unit>();
					wordnrs.clear();
					rownrs.clear();
					for (int j = 0; j < units_done.size(); j++) {

						if (units_done.get(j).getColumn() == entry.getKey().getColumn()) {
							rownrs.add(units_done.get(j).getRow());
						}
					}
					Collections.sort(rownrs);

					ArrayList<Integer> rownrs2 = new ArrayList<Integer>();

					for (int k = 0; k < rownrs.size(); k++) {
						if (rownrs.get(k) >= entry.getKey().getRow()) {

							rownrs2.add(rownrs.get(k));

						}
					}

					rownrs = rownrs2;

					boolean still = true;

					if (rownrs.size() == 1) {
						System.out.println("say hi");
						Unit wordUnit = findUnit(entry.getKey().getRow(), entry.getKey().getColumn());
						wordUnits.add(wordUnit);
					} else {

						for (int j = 0; j < rownrs.size() - 1; j++) {

							if (still && rownrs.get(j) >= entry.getKey().getRow()
									&& rownrs.get(j) - rownrs.get(j + 1) == -1) {
								if (!wordnrs.contains(rownrs.get(j))) {
									wordnrs.add(rownrs.get(j));
								}
								if (!wordnrs.contains(rownrs.get(j + 1))) {
									wordnrs.add(rownrs.get(j + 1));
								}
							} else {

								break;
							}

						}

						if (wordnrs.isEmpty()) {
							wordnrs.add(rownrs.get(0));
						}

					}

					for (int j = 0; j < wordnrs.size(); j++) {
						Unit wordUnit = findUnit(wordnrs.get(j), entry.getKey().getColumn());
						wordUnits.add(wordUnit);

					}

					boolean second = false;

					for (int j = 0; j < words.size(); j++) {

						if (words != null && !words.isEmpty() && !words.get(j).isEmpty()) {

							if (words.get(j).get(0).getColumn() == entry.getKey().getColumn()) {
								second = true;
								System.out.println("second");
								for (int k = 0; k < wordUnits.size(); k++) {

									words.get(j).add(wordUnits.get(k));
								}
							}
						}
					}
					if (second == false) {

						ArrayList<Unit> newunits = new ArrayList<Unit>();

						for (int j = 0; j < units.size(); j++) {
							if (units.get(j).getColumn() == entry.getKey().getColumn()) {
								newunits.add(units.get(j));
							}
						}

						for (int j = 0; j < newunits.size(); j++) {
							wordUnits.add(newunits.get(j));
						}

						// System.out.println("newunits to" + gson.toJson(newunits));

						if (wordUnits.size() > 1) {

							Collections.sort(wordUnits, Unit.RowComparator);
							Collections.sort(wordUnits, Unit.ColumnComparator);

						}
						words.add(wordUnits);
					}

					// alert(JSON.stringify(wordUnits));

				}

				if (entry.getValue() == "up") {
					wordnrs.clear();
					rownrs.clear();
					ArrayList<Unit> wordUnits = new ArrayList<Unit>();
					for (int j = 0; j < units_done.size(); j++) {

						if (units_done.get(j).getColumn() == entry.getKey().getColumn()) {
							rownrs.add(units_done.get(j).getRow());
						}
					}
					Collections.sort(rownrs);

					for (int k = 0; k < rownrs.size(); k++) {
						if (rownrs.get(k) > entry.getKey().getRow()) {

							rownrs = new ArrayList<Integer>(rownrs.subList(0, k));
							System.out.println("rownrs to" + rownrs);
							break;
						}
					}
					Collections.reverse(rownrs);
					boolean still = true;
					if (rownrs.size() == 1) {
						Unit wordUnit = findUnit(entry.getKey().getRow(), entry.getKey().getColumn());
						wordUnits.add(wordUnit);
						// System.out.println("stringify"+ gson.toJson(findUnit(rownrs.get(0),
						// entry.getKey().getColumn())));
					} else {
						for (int j = 0; j < rownrs.size() - 1; j++) {

							if (still && rownrs.get(j) <= entry.getKey().getRow()
									&& rownrs.get(j) - rownrs.get(j + 1) == 1) {
								if (!wordnrs.contains(rownrs.get(j))) {
									wordnrs.add(rownrs.get(j));
								}
								if (!wordnrs.contains(rownrs.get(j + 1))) {
									wordnrs.add(rownrs.get(j + 1));
								}
							}

							else {
								break;
							}

						}

						if (wordnrs.isEmpty()) {
							wordnrs.add(rownrs.get(0));
						}

					}

					for (int j = 0; j < wordnrs.size(); j++) {
						Unit wordUnit = findUnit(wordnrs.get(j), entry.getKey().getColumn());
						wordUnits.add(wordUnit);

					}

					ArrayList<Unit> newunits = new ArrayList<Unit>();

					for (int j = 0; j < units.size(); j++) {
						if (units.get(j).getColumn() == entry.getKey().getColumn()) {
							newunits.add(units.get(j));
						}
					}

					for (int j = 0; j < newunits.size(); j++) {
						wordUnits.add(newunits.get(j));
					}

					Collections.sort(wordUnits, Unit.RowComparator);
					Collections.sort(wordUnits, Unit.ColumnComparator);

					words.add(wordUnits);

					// alert(JSON.stringify(wordUnits));

				}

			}

			if (mainword.size() != 1) {
				Collections.sort(mainword, Unit.RowComparator);
				Collections.sort(mainword, Unit.ColumnComparator);

				words.add(mainword);
				System.out.println("mainword to " + gson.toJson(mainword));
			}
		}
		for (int z = 0; z < words.size(); z++) {
			System.out.println("word nr " + z + " to " + gson.toJson(words.get(z)));
		}

		// System.out.println(countWords(words));

		dictionary(words);
		words1 = words;
		words3 = doClass(words);
		// alert(JSON.stringify(words3));

	}

	private Words doClass(ArrayList<ArrayList<Unit>> words) {
		ArrayList<Word> words2 = new ArrayList<Word>();
		for (int i = 0; i < words.size(); i++) {
			Word word_temp = new Word(words.get(i), 0);
			words2.add(word_temp);

		}
		words3 = new Words(words2);
		return words3;
	}

	private void dictionary(ArrayList<ArrayList<Unit>> words) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCycle(int cycle) {
		gameCounter = cycle;

	}

	@Override
	public int getCycle() {
		// TODO Auto-generated method stub
		return gameCounter;
	}

	public void swapExchange(ArrayList<Integer> exArr) {
		tnr.removeAll(exArr);
	}

	@Transactional
	public ArrayList<Unit> newGame() {

		gameCounter = 0;
		units.clear();
		units_done.clear();
		botlets.clear();
		tnr.clear();
		getBotlets(7);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		currentPrincipalName = authentication.getName();

		user = userService.getUserByName(currentPrincipalName);

		replayMatch = new ReplayMatch();
		replayMatch.setUser(user);
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
		// HH:mm:ss");
		// String timestring = LocalDateTime.now().format(formatter);
		Date date = new Date();
		replayMatch.setDate(date);
		replayMatch.setBotpoints(0);
		replayMatch.setHumanpoints(0);
		replayMatch.setWin(false);
		gameDAO.saveReplayMatch(replayMatch);

		int botstarts = r.nextInt(2);
		System.out.println(botstarts);
		if (botstarts == 1) {

			ArrayList<Unit> unitsfinal = spitWord(dictionaryService.getDict(), dictionaryService.getAlphabet());
			return unitsfinal;
		} else {
			gameCounter++;
			return null;
		}

	}

	@Transactional

	public ArrayList<History> getHistory() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		currentPrincipalName = authentication.getName();

		ArrayList<History> historyMatches = (ArrayList<History>) gameDAO.getHistory(currentPrincipalName);

		return historyMatches;
	}

	public void gameOver() {
		// tutaj save do bazy danych zmiennej gsonowej replay
	}

}
