package com.bignerdranch.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
	
	private UUID mId;
	private String mTitle;
	private Date mDate;  // KIO: used java.util and not java.sql, in case this causes errors later
	private boolean mSolved;
	
	public Crime() {
		// Generate a unique identifier
		mId = UUID.randomUUID();
		mDate = new Date();
	}
	
	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean solved) {
		mSolved = solved;
	}

	public UUID getId() {
		return mId;
	}
	
	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}
}
