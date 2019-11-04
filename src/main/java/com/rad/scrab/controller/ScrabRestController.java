package com.rad.scrab.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.rad.scrab.model.Hist;
import com.rad.scrab.model.Histories;
import com.rad.scrab.model.History;
import com.rad.scrab.model.Unit;
import com.rad.scrab.model.Word;
import com.rad.scrab.model.Words;
import com.rad.scrab.service.DictionaryService;
import com.rad.scrab.service.GameService;

@RestController

public class ScrabRestController {
	@Autowired
	private GameService gameService;

	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private Gson gson;

	@GetMapping("/gameover")

	public String gameover(Model model) throws Exception {

		gameService.gameOver();

		return "gameover";
	}

	@GetMapping("/newgame")
	public Word newgame(Model model) throws Exception {

		if (dictionaryService.getDict() == null) {
			dictionaryService.readDict();
			gameService.fillTable();
		}
		dictionaryService.readAlphabet();

		ArrayList<Unit> unitsfinal = gameService.newGame();

		if (unitsfinal != null && unitsfinal.get(unitsfinal.size() - 1).getLetter() == -1) {
			gameService.gameOver();
		}

		Word wordReady = new Word(unitsfinal, 0);

		return wordReady;
	}

	@GetMapping("/checkword")
	public Boolean checkword(@RequestParam("id") String word) throws Exception {

		boolean exists;

		if (dictionaryService.getDict().containsKey(word)) {
			exists = true;
			System.out.println(exists);
		} else {
			exists = false;
			System.out.println(exists);
		}

		return exists;
	}

	@GetMapping("/getletters")
	public ArrayList<Integer> getletters(@RequestParam("id") String howmany) {

		Integer howmanyy = Integer.parseInt(howmany);

		ArrayList<Integer> ready = gameService.getLetters(howmanyy);

		return ready;

	}

	@PostMapping("/exchange")
	public String getexchange(@RequestBody String string) throws UnsupportedEncodingException {

		string = URLDecoder.decode(string, "UTF-8");
		System.out.println("logson to " + string);
		ArrayList<Integer> exArr = gson.fromJson(string, new TypeToken<List<Integer>>() {
		}.getType());
		gameService.swapExchange(exArr);
		return "";
	}

	@PostMapping("/gethistory")
	public Histories gethistory() {
		ArrayList<History> historyArr = gameService.getHistory();

		ArrayList<Hist> hists = new ArrayList<Hist>();

		for (int i = 0; i < historyArr.size(); i++) {
			hists.add(new Hist(historyArr.get(i)));
		}

		Histories histories = new Histories(hists);

		return histories;

	}

	@PostMapping("/getmatch")
	public Words getmatch(@RequestBody String string) {

		Integer id = Integer.parseInt(string);

		Words matchWords = gameService.getMatch(id);

		return matchWords;
	}

	@PostMapping("/newwords")
	public Word newwords(@RequestBody String string) throws JsonSyntaxException, UnsupportedEncodingException {

		System.out.println(dictionaryService.getAlphabet().get("adpu").toString());

		String string3 = URLDecoder.decode(string, "UTF-8");

		Words words = gson.fromJson(string3, Words.class);

		gameService.transferWords(words);

		ArrayList<Unit> unitsfinal = gameService.spitWord(dictionaryService.getDict(), dictionaryService.getAlphabet());

		Word wordReady = new Word(unitsfinal, 0);

		return wordReady;

	}

	@PostMapping("/newwordsonly")
	public Word newwordsonly() {

		ArrayList<Unit> unitsfinal = gameService.spitWord(dictionaryService.getDict(), dictionaryService.getAlphabet());

		Word wordReady = new Word(unitsfinal, 0);

		return wordReady;
	}
}
