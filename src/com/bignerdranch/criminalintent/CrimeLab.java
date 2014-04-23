package com.bignerdranch.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

// Singleton to store one instance of the Crime Lab
public class CrimeLab {
	
	private static final String TAG = "CrimeLab";
	private static final String FILENAME = "crimes.json";
	
	private ArrayList<Crime> mCrimes;
	
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	CriminalIntentJSONSerializer mSerializer;
	
	// Constructor
	private CrimeLab(Context appContext) {
		mAppContext = appContext;
		mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
		
		try {
			mCrimes = mSerializer.loadCrimes();
		} catch (Exception e) {
			mCrimes = new ArrayList<Crime>();
			Log.e(TAG, "Error loading crimes: ", e);
		}
		
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
	
	// Delete a single Crime to the list
	public void deleteCrime(Crime c){
		Log.d(TAG, "Deleting this crime: " + c.getTitle() );
		mCrimes.remove(c);
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
	
	// Save all crimes to private app sandbox
	public boolean saveCrimes(){
		try {
			mSerializer.saveCrimes(mCrimes);
			Log.d(TAG, "crimes saved to file");
			return true;
		} catch (Exception e) {
			Log.d(TAG, "Error saving crimes: ", e);
			return false;
		}
	}

}
