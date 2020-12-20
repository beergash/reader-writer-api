package it.beergash.reader.writer.api.utility.model;

import java.time.LocalDate;

public class Match {

	private String homeTeam;
	private String guestTeam;
	private LocalDate matchDate;
	private String score;

	public Match() {
	}

	public Match(String homeTeam, String guestTeam, LocalDate matchDate, String score) {
		super();
		this.homeTeam = homeTeam;
		this.guestTeam = guestTeam;
		this.matchDate = matchDate;
		this.score = score;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public String getGuestTeam() {
		return guestTeam;
	}

	public void setGuestTeam(String guestTeam) {
		this.guestTeam = guestTeam;
	}

	public LocalDate getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(LocalDate matchDate) {
		this.matchDate = matchDate;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

}
