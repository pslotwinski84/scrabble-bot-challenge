package com.rad.scrab.model;

import java.util.Date;

public class History {
	private Date date;
	private Integer botpoints;
	private Integer humanpoints;
	private Boolean win;
	private int id;

	public History(int id, Date date, Integer botpoints, Integer humanpoints, Boolean win) {
		this.id = id;
		this.date = date;
		this.botpoints = botpoints;
		this.humanpoints = humanpoints;
		this.win = win;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getBotpoints() {
		return botpoints;
	}

	public void setBotpoints(Integer botpoints) {
		this.botpoints = botpoints;
	}

	public Integer getHumanpoints() {
		return humanpoints;
	}

	public void setHumanpoints(Integer humanpoints) {
		this.humanpoints = humanpoints;
	}

	public Boolean getWin() {
		return win;
	}

	public void setWin(Boolean win) {
		this.win = win;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
