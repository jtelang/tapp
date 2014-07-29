package com.tapp.data;

public class MyCreditData {

	private String orderNo = "";
	private String price = "";

	public MyCreditData() {
	}

	public MyCreditData(String orderNo, String price) {
		super();
		this.orderNo = orderNo;
		this.price = price;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

}
