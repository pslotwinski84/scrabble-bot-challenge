package com.rad.scrab.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "REPLAYUNIT")

public class ReplayUnit {
	@Id
	@Column(name = "replayunitid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;

	@ManyToOne
	@JoinColumn(name = "replaywordid")
	private ReplayWord replayword;

	private int letter;
	private int row;
	private int col;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ReplayWord getReplayword() {
		return replayword;
	}

	public void setReplayword(ReplayWord replayword) {
		this.replayword = replayword;
	}

	public int getLetter() {
		return letter;
	}

	public void setLetter(int letter) {
		this.letter = letter;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

}
