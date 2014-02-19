package com.bignerdranch.criminalintent;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {
	
	private static final String TAG = "CrimeListFragment";
	
	private ArrayList<Crime> mCrimes;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Tap into the hosting activity and ask it to display the title
		getActivity().setTitle(R.string.crimes_title);
		
		// Get singleton and then get list of crimes
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
		
		// Create the Adapter
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Crime c = ( (CrimeAdapter) getListAdapter() ).getItem(position);
		Log.d(TAG, c.getTitle() + " was clicked.");
	}
	
	private class CrimeAdapter extends ArrayAdapter<Crime> {
		
		public CrimeAdapter(ArrayList<Crime> crimes){
			super(getActivity(), 0, crimes);
		}
		
		// Overriding this method is what allows us to use the custom list.  This is what gets called
		//   behind the scenes when ListView and adapter have their conversations about what to display
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			// If we weren't given a view, then inflate one
			if ( convertView == null ) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_crime, null);
			}
			
			// Configure the view for this specific Crime
			Crime c = getItem(position);
			
			TextView titleTextView = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(c.getTitle());
			
			TextView dateTextView = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
			String formatDate = DateFormat.format("EEEE, MMM dd, yyyy", c.getDate()).toString();
			dateTextView.setText(formatDate);
			
			CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isSolved());
			
			return convertView;
		}
		
	}
	
}
