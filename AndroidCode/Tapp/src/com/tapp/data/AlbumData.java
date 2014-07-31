package com.tapp.data;

public class AlbumData {

	private String albumName = "";
	private String singerName = "";
	private String imageUrl = "";

	public AlbumData() {
	}

	public AlbumData(String albumName, String singerName, String imageUrl) {
		super();
		this.albumName = albumName;
		this.singerName = singerName;
		this.imageUrl = imageUrl;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public String getSingerName() {
		return singerName;
	}
	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
