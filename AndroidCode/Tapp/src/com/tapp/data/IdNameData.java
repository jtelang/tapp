package com.tapp.data;

public class IdNameData {

	private int id = 0;
	private String name = "";

	public IdNameData() {
	}

	public IdNameData(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
