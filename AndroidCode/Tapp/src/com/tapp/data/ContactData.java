package com.tapp.data;

public class ContactData {

	private int id = 0;
	private String phoneNo = "";
	private String name = "";
	private int contactTypeFlag = 0;
	private String photoUrl = "";
	private int rawContatId = 0;
	private String status = "";

	public ContactData() {
		super();
	}

	public ContactData(String phoneNo, String name, int contactTypeFlag, String photoUrl, int rawContatId, String status) {
		super();
		this.phoneNo = phoneNo;
		this.name = name;
		this.contactTypeFlag = contactTypeFlag;
		this.photoUrl = photoUrl;
		this.rawContatId = rawContatId;
		this.status = status;
	}

	public ContactData(int id, String phoneNo, String name, int contactTypeFlag, String photoUrl, int rawContatId, String status) {
		super();
		this.id = id;
		this.phoneNo = phoneNo;
		this.name = name;
		this.contactTypeFlag = contactTypeFlag;
		this.photoUrl = photoUrl;
		this.rawContatId = rawContatId;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getContactTypeFlag() {
		return contactTypeFlag;
	}

	public void setContactTypeFlag(int contactTypeFlag) {
		this.contactTypeFlag = contactTypeFlag;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public int getRawContatId() {
		return rawContatId;
	}

	public void setRawContatId(int rawContatId) {
		this.rawContatId = rawContatId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
