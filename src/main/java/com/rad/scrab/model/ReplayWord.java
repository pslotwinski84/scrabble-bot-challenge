package com.rad.scrab.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "REPLAYWORD")

public class ReplayWord {
	@Id
	@Column(name = "replaywordid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;

	@ManyToOne
	@JoinColumn(name = "matchid")
	private ReplayMatch replaymatch;

	@OneToMany(mappedBy = "replayword")
	@Cascade(CascadeType.ALL)

	private List<ReplayUnit> replayunits;

	private Date date;
	private int score;
	private boolean human;

	public boolean isHuman() {
		return human;
	}

	public void setHuman(boolean human) {
		this.human = human;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ReplayMatch getReplaymatch() {
		return replaymatch;
	}

	public void setReplaymatch(ReplayMatch replaymatch) {
		this.replaymatch = replaymatch;
	}

	public List<ReplayUnit> getReplayunits() {
		return replayunits;
	}

	public void setReplayunits(List<ReplayUnit> replayunits) {
		this.replayunits = replayunits;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
