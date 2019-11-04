package com.rad.scrab.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rad.scrab.model.Histories;
import com.rad.scrab.model.History;
import com.rad.scrab.model.ReplayMatch;
import com.rad.scrab.model.ReplayUnit;
import com.rad.scrab.model.ReplayWord;
import com.rad.scrab.model.Unit;
import com.rad.scrab.model.Word;
import com.rad.scrab.model.Words;

@Repository
public class GameDAOImpl implements GameDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	public void saveReplayWord(ReplayWord replayWord) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(replayWord);
	}

	public void saveReplayMatch(ReplayMatch replayMatch) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(replayMatch);
	}

	@SuppressWarnings("unchecked")
	public List<History> getHistory(String username) {
		Session session = this.sessionFactory.getCurrentSession();
		List<ReplayMatch> historyList = session
				.createQuery("select c from ReplayMatch as c inner join c.user as e where e.username=:eid")
				.setParameter("eid", username).list();
		System.out.println(historyList.size());
		ArrayList<History> history = new ArrayList<>();

		for (ReplayMatch m : historyList) {
			History temp_history = new History(m.getId(), m.getDate(), m.getBotpoints(), m.getHumanpoints(),
					m.getWin());
			history.add(temp_history);
		}
		return history;
	}

	public ReplayMatch updateMatch(ReplayMatch m) {
		Session session = this.sessionFactory.getCurrentSession();
		m = (ReplayMatch) session.merge(m);
		return m;
	}

	@SuppressWarnings("unchecked")
	public Words getMatch(int id) {
		Session session = this.sessionFactory.getCurrentSession();

		List<ReplayWord> replayWords = session
				.createQuery("select c from ReplayWord as c inner join c.replaymatch as e where e.id=:eid")
				.setParameter("eid", id).list();

		Words words = new Words();

		for (ReplayWord rw : replayWords) {
			ArrayList<Unit> word = new ArrayList<Unit>();
			List<ReplayUnit> wordUnits = rw.getReplayunits();

			for (ReplayUnit u : wordUnits) {
				word.add(new Unit(u.getLetter(), u.getRow(), u.getCol(), false));
			}

			words.getWords().add(new Word(word, 0));
		}

		return words;
	}

}
