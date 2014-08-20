package com.bignerdranch.criminalintent;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {

	private static final String TAG = "CrimeListFragment";

	private ArrayList<Crime> mCrimes;
	private boolean mSubtitleVisible;
	private Callbacks mCallbacks;
	
	private Button mNewCrimeButton;
	
	/**
	 * Required interface for hosting activities
	 */
	public interface Callbacks {
		void onCrimeSelected(Crime crime);
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		// unchecked cast, so you must document this somehwere!
		mCallbacks = (Callbacks) activity;
	}
	
	@Override
	public void onDetach(){
		super.onDetach();
		mCallbacks = null;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		// Tap into the hosting activity and ask it to display the title
		getActivity().setTitle(R.string.crimes_title);

		// Get singleton and then get list of crimes
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
		
		// Create the Adapter
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		setListAdapter(adapter);
		
		// Remember the info about subtitle visibility
		setRetainInstance(true);
		mSubtitleVisible = false;
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		// Get view for fragment
		View v = inflater.inflate(R.layout.fragment_crime_list, parent, false);
		
		// Set custom empty view
		View empty = v.findViewById(R.id.custom_empty_view);
		ListView displayList = (ListView) v.findViewById(android.R.id.list);
		displayList.setEmptyView(empty);
		
		// Link Button for Empty Display
		mNewCrimeButton = (Button) v.findViewById(R.id.new_crime_button);
		mNewCrimeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Crime crime = new Crime();
				CrimeLab.get(getActivity()).addCrime(crime);
				
				Intent i = new Intent(getActivity(), CrimePagerActivity.class);
				i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
				startActivityForResult(i, 0);
			}
		});
		
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setSubtitle(R.string.subtitle);
		}
		
		// Register the ListView for a context menu
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			// Floating context menu for Froyo & Gingerbread
			registerForContextMenu(displayList);
		}
		else {
			// Use contextual action bar on Honeycomb and higher
			displayList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			displayList.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				
				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					// Required, but not used here
					return false;
				}
				
				@Override
				public void onDestroyActionMode(ActionMode mode) {
					// Required, but not used here					
				}
				
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.crime_list_item_context, menu);
					return true;
				}
				
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch ( item.getItemId() ){
						case R.id.menu_item_delete_crime:
							CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
							CrimeLab crimeLab = CrimeLab.get(getActivity());
							
							for (int i = adapter.getCount() - 1; i >= 0; i--){
								if( getListView().isItemChecked(i)){
									crimeLab.deleteCrime(adapter.getItem(i));
								}
							}
							
							mode.finish();
							adapter.notifyDataSetChanged();
							return true;
					}
					return false;
				}
				
				@Override
				public void onItemCheckedStateChanged(ActionMode mode, int position,
						long id, boolean checked) {
					// Required, but not used here
				}
			});
		}
		
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		CrimeLab.get(getActivity()).saveCrimes();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
		if (mSubtitleVisible && showSubtitle != null){
			showSubtitle.setTitle(R.string.hide_subtitle);
		}
	}

	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_new_crime:
				Crime crime = new Crime();
				CrimeLab.get(getActivity()).addCrime(crime);
				
				((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
				mCallbacks.onCrimeSelected(crime);
				return true;
			case R.id.menu_item_show_subtitle:
				if( getActivity().getActionBar().getSubtitle() == null ) {
					getActivity().getActionBar().setSubtitle(R.string.subtitle);
					mSubtitleVisible = true;
					item.setTitle(R.string.hide_subtitle);
				}
				else {
					getActivity().getActionBar().setSubtitle(null);
					mSubtitleVisible = false;
					item.setTitle(R.string.show_subtitle);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
		Crime crime = adapter.getItem(position);
		
		switch(item.getItemId()){
			case R.id.menu_item_delete_crime:
				CrimeLab.get(getActivity()).deleteCrime(crime);
				adapter.notifyDataSetChanged();
				return true;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Crime c = ((CrimeAdapter) getListAdapter()).getItem(position);
		Log.d(TAG, c.getTitle() + " was clicked.");

		mCallbacks.onCrimeSelected(c);
	}
	
	public void updateUI(){
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}

	private class CrimeAdapter extends ArrayAdapter<Crime> {

		public CrimeAdapter(ArrayList<Crime> crimes) {
			super(getActivity(), 0, crimes);
		}

		// Overriding this method is what allows us to use the custom list. This
		// is what gets called
		// behind the scenes when ListView and adapter have their conversations
		// about what to display
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// If we weren't given a view, then inflate one
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_crime, null);
			}

			// Configure the view for this specific Crime
			Crime c = getItem(position);

			TextView titleTextView = (TextView) convertView
					.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(c.getTitle());

			TextView dateTextView = (TextView) convertView
					.findViewById(R.id.crime_list_item_dateTextView);
			String formatDate = DateFormat.format("EEEE, MMM dd, yyyy",
					c.getDate()).toString();
			dateTextView.setText(formatDate);

			CheckBox solvedCheckBox = (CheckBox) convertView
					.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isSolved());

			return convertView;
		}

	}

}
