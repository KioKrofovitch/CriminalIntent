package com.bignerdranch.criminalintent;

import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class CrimeFragment extends Fragment {

	public static final String EXTRA_CRIME_ID = "com.bignerdranch.criminalintent.crime_id";
	private static final String DIALOG_DATE = "date";
	private static final int REQUEST_DATE = 0;

	private static final String DIALOG_TIME = "time";
	private static final int REQUEST_TIME = 1;

	public static final String TAG_KIO = "KIO";

	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private Button mTimeButton;
	private CheckBox mSolvedCheckBox;

	public static CrimeFragment newInstance(UUID crimeId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeId);

		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);

		return fragment;
	}

	private void updateDate() {
		String formatDate = DateFormat.format("EEEE, MMM dd, yyyy",
				mCrime.getDate()).toString();
		mDateButton.setText(formatDate);
	}

	private void updateTime() {
		// TODO KIO Is this really necessary to creat a calendar?
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(mCrime.getDate());

		Log.d(TAG_KIO, "Inside updateTime() mDate time is: "
				+ mCrime.getDate().getTime());
		Log.d(TAG_KIO, "Inside updateTime() mDate is: " + mCrime.getDate());

		Time time = new Time();
		time.set(mCrime.getDate().getTime());

		String timeFormat = time.format("%I:%M");
		Log.d(TAG_KIO, "timeFormat is: " + timeFormat);
		mTimeButton.setText(timeFormat);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		// Get crime ID from host Activity (which was passed it from calling
		// activity CrimeList
		UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstance) {

		// Get view for fragment
		View v = inflater.inflate(R.layout.fragment_crime, parent, false);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			if(NavUtils.getParentActivityName(getActivity()) != null){
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		// Crime Title
		mTitleField = (EditText) v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				mCrime.setTitle(c.toString());
			}

			public void beforeTextChanged(CharSequence c, int start, int count,
					int after) {
				// Do something?
			}

			public void afterTextChanged(Editable c) {
				// Do something?
			}
		});
		Log.d("KIO", "Crime title is: " + mTitleField);
		
		// Date Button
		mDateButton = (Button) v.findViewById(R.id.crime_date);
		updateDate();
		mDateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment
						.newInstance(mCrime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});
		Log.d("KIO", "Date is: " + mDateButton);

		// Time Button
		mTimeButton = (Button) v.findViewById(R.id.crime_time);
		Log.d("KIO", "Time is: " + mTimeButton);
		updateTime();
		mTimeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				TimePickerFragment dialog = TimePickerFragment
						.newInstance(mCrime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
				dialog.show(fm, DIALOG_TIME);
			}
		});
		Log.d("KIO", "Time is: " + mTimeButton);

		// "Solved" Check box
		mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// Set the crime's solved property
						mCrime.setSolved(isChecked);
					}
				});

		return v;
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("KIO", "onPause");
		CrimeLab.get(getActivity()).saveCrimes();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		if (requestCode == REQUEST_DATE) {
			Date date = (Date) data
					.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();
		}

		if (requestCode == REQUEST_TIME) {
			Date date = (Date) data
					.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mCrime.setDate(date);
			updateTime();
		}
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() ){
			case android.R.id.home:
				if ( NavUtils.getParentActivityName(getActivity()) != null){
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			case R.id.menu_item_delete_single_crime:
				CrimeLab.get(getActivity()).deleteCrime(mCrime);
				if ( NavUtils.getParentActivityName(getActivity()) != null){
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
