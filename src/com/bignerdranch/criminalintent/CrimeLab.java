package com.bignerdranch.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

// Singleton to store one instance of the Crime Lab
public class CrimeLab {
	
	private ArrayList<Crime> mCrimes;
	
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	
	// Constructor
	private CrimeLab(Context appContext) {
		mAppContext = appContext;
		mCrimes= new ArrayList<Crime>();
	}
	
	// Create Singleton
	public static CrimeLab get(Context c) {
		// If the singleton doesn't exist yet, create it.
		if ( sCrimeLab == null ) {
			// Don't assume that context 'c' will always be what you expect!  Be
			//    extra safe and call the method to make sure.
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		
		return sCrimeLab;
	}
	
	// Add a single Crime to the list
	public void addCrime(Crime c){
		mCrimes.add(c);
	}
	
	// Return entire list of crimes
	public ArrayList<Crime> getCrimes() {
		return mCrimes;
	}
	
	// Return only a specific crime
	public Crime getCrime(UUID id) {
		for(Crime c : mCrimes) {
			if(c.getId().equals(id)) {
				return c;
			}
		}
		return null;
	}

}
