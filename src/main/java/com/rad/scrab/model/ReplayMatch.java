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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "REPLAYMATCH")
public class ReplayMatch {
	@Id
	@Column(name = "matchid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;

	@ManyToOne
	@JoinColumn(name = "USERNAME")
	private User user;

	@OneToMany(mappedBy = "replaymatch")
	@Cascade(CascadeType.ALL)

	private List<ReplayWord> replaywords;

	private Boolean win;
	private Date date;
	private Integer humanpoints;
	private Integer botpoints;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<ReplayWord> getReplaywords() {
		return replaywords;
	}

	public void setReplaywords(List<ReplayWord> replaywords) {
		this.replaywords = replaywords;
	}

	public Boolean getWin() {
		return win;
	}

	public void setWin(Boolean win) {
		this.win = win;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getHumanpoints() {
		return humanpoints;
	}

	public void setHumanpoints(Integer humanpoints) {
		this.humanpoints = humanpoints;
	}

	public Integer getBotpoints() {
		return botpoints;
	}

	public void setBotpoints(Integer botpoints) {
		this.botpoints = botpoints;
	}

}
