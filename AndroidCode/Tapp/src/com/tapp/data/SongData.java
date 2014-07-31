package com.tapp.data;

public class SongData {

	private String songName = "";
	private String singerName = "";
	private int buyFlag = 0;

	public SongData() {
	}

	public SongData(String songName, String singerName, int buyFlag) {
		super();
		this.songName = songName;
		this.singerName = singerName;
		this.buyFlag = buyFlag;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public String getSingerName() {
		return singerName;
	}
	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}
	public int getBuyFlag() {
		return buyFlag;
	}
	public void setBuyFlag(int buyFlag) {
		this.buyFlag = buyFlag;
	}

}
