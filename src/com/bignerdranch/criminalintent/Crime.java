package com.bignerdranch.criminalintent;

import java.util.Date;
import java.util.UUID;

import android.text.format.Time;

public class Crime {
	
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	
	public Crime() {
		// Generate a unique identifier
		mId = UUID.randomUUID();
		mDate = new Date();
	}
	
	// Override this value so that when our ArrayAdapter grabs these guys to display, it can
	//   print out some meaningful text instead of the class name and memory address (default)
	@Override
	public String toString() {
		return mTitle;
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
