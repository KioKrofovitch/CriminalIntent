package com.bignerdranch.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class CrimeFragment extends Fragment {
	
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mCrime = new Crime();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstance) {
		
		// Get view for fragment
		View v = inflater.inflate(R.layout.fragment_crime, parent, false);
		
		// Crime Title
		mTitleField = (EditText) v.findViewById(R.id.crime_title);
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before, int count){
				mCrime.setTitle(c.toString());
			}
			
			public void beforeTextChanged(CharSequence c, int start, int count, int after){
				// Do something?
			}
			
			public void afterTextChanged(Editable c){
				// Do something?
			}
		});
		
		// Date Button
		mDateButton = (Button)v.findViewById(R.id.crime_date);
		String formatDate = DateFormat.format("EEEE, MMM dd, yyyy", mCrime.getDate()).toString();
		mDateButton.setText(formatDate);
		mDateButton.setEnabled(false); // Disabled only for now; also thats why we didnt set a listener here.
		
		// "Solved" Check box
		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//Set the crime's solved property
				mCrime.setSolved(isChecked);
			}
		});
		
		return v;
	}

}
