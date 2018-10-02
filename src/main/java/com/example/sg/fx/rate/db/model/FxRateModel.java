package com.example.sg.fx.rate.db.model;

import java.io.Serializable;
import java.util.Date;

public class FxRateModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private double bid;

	private double ask;

	private String currency;

	private String tenor;

	private Date timestamp;

	public double getBid() {
		return bid;
	}

	public void setBid(final double bid) {
		this.bid = bid;
	}

	public double getAsk() {
		return ask;
	}

	public void setAsk(final double ask) {
		this.ask = ask;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	public String getTenor() {
		return tenor;
	}

	public void setTenor(final String tenor) {
		this.tenor = tenor;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
