package com.rad.scrab.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.rad.scrab.model.Histories;
import com.rad.scrab.model.History;
import com.rad.scrab.model.ReplayMatch;
import com.rad.scrab.model.ReplayWord;
import com.rad.scrab.model.Words;

public interface GameDAO {

	public void saveReplayWord(ReplayWord replayWord);

	public void saveReplayMatch(ReplayMatch replayMatch);

	public List<History> getHistory(String username);

	public ReplayMatch updateMatch(ReplayMatch m);

	public Words getMatch(int id);

}
