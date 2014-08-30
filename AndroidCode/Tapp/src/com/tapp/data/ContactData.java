package com.tapp.data;

public class ContactData {

	private int id = 0;
	private int user_id = 0;
	private String name = "";
	private String phone = "";
	private int userType = 0;
	private String photo = "";
	private String bio = "";
	private int rawContatId = 0;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return user_id;
	}
	public void setUseId(int user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public int getRawContatId() {
		return rawContatId;
	}
	public void setRawContatId(int rawContatId) {
		this.rawContatId = rawContatId;
	}

}
