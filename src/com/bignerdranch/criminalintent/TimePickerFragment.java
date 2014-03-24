package com.bignerdranch.criminalintent;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class TimePickerFragment extends DialogFragment {
	public static final String EXTRA_TIME = "com.bignerdranche.android.criminalintent.time";
	
	private Date mDate;
	// Making these members might be a cop out???
	private int mHour;
	private int mMinute;
	
	public static TimePickerFragment newInstance(Date date){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TIME, date);
		
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	private void sendResult(int resultCode){
		if ( getTargetFragment() == null) {
			return;
		}
		
		Intent i = new Intent();
		i.putExtra(EXTRA_TIME, mDate);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		mDate = (Date) getArguments().getSerializable(EXTRA_TIME);
		
		// I had to create a Calendar object  for Date. I should create a Time object for this one
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		mHour = calendar.get(Calendar.HOUR);
		mMinute = calendar.get(Calendar.MINUTE);
		
		// Inflate the Time Dialog View
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
		
		// Get the time picker
		TimePicker timePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);
		timePicker.setCurrentHour(mHour);
		timePicker.setCurrentMinute(mMinute);
		
		timePicker.setOnTimeChangedListener( new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				Log.d("KIO", "mDate: "+mDate.getTime());
				Log.d("KIO", "hourOfDay: " + hourOfDay);
				Log.d("KIO", "minute: " + minute);
				
				// This is a hack TODO				
				long timeInMilliseconds = (60000*minute) + (3600000*hourOfDay);
				mDate.setTime(timeInMilliseconds);	
				Log.d("KIO", "mDate: "+mDate.getTime());
				getArguments().putSerializable(EXTRA_TIME, mDate);
			}
		});
	
		return new AlertDialog.Builder(getActivity())
			.setView(v)
			.setTitle(R.string.time_picker_title)
			.setPositiveButton(
					android.R.string.ok,
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.d("KIO", "Time Picker OK has been clicked");
							sendResult(Activity.RESULT_OK);	
						}
					})
			.create();
	}	
}
