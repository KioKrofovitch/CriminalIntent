package com.bignerdranch.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class CrimePagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<Crime> mCrimes;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create view programatically
		mViewPager = new ViewPager(this );
		mViewPager .setId(R.id.viewPager);
		
		// Set the view to this activity
		setContentView(mViewPager);
		
		// Get data
		mCrimes = CrimeLab.get(this).getCrimes();
		
		// Implement FragmentStatePagerAdapter
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm){
			
			@Override
			public int getCount() {
				return mCrimes.size();
			}
			
			@Override
			public Fragment getItem(int pos){
				Crime crime = mCrimes.get(pos);
				return CrimeFragment.newInstance(crime.getId());
			}
			
		});
		
		// Implement the Page Change Listener
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				Crime crime = mCrimes.get(pos);
				if(crime.getTitle() != null){
					setTitle(crime.getTitle());
				}
			}
			
			// Do Nothing for these.
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// Tells you where your page is GOING to be
			}
			@Override
			public void onPageScrollStateChanged(int arg0) { 
				// Tells you if your page is actively being dragged
			}
		});
		
		// Loop through each Crime's UUID to find which one should be displayed
		UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for(int i = 0; i < mCrimes.size(); i++){
			if(mCrimes.get(i).getId().equals(crimeId)){
				mViewPager.setCurrentItem(i);
				break;
			}
		}
		
	}
}
