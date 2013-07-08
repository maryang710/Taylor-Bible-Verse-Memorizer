package edu.taylor.cse.sbrandle.biblemem.v001.setting;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import edu.taylor.cse.sbrandle.biblemem.v001.R;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalFactory;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalManager;

/**
 * Setting Activity
 * User can set preferences.
 * There is a sound preference.
 * 
 * Date : May 21, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class SettingAct extends Activity {

	private DatabaseManager databaseManager;
	private ListView settingListView;

	private final int VERSION_INFORMATION = 0;
	private final int BIBLE_EDITION = 1;
	private final int DIFFICULTY = 2;
	private final int ABOUT_APP = 3;
	private final int INITIALIZE_APP = 4;


	/*** Activity ***/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalManager.setCustomContentView(this, R.layout.setting);
		TextView titleBarText = (TextView) findViewById(R.id.title_bar_text);
		titleBarText.setText(getResources().getString(R.string.setting_label));


		// DB work
		try {
			databaseManager = GlobalFactory.getDatabaseManagerByLanguage(this);
			databaseManager.openDatabase();
		} catch (IOException e) {
			e.printStackTrace();
		}


		// Setting Listview
		// Setting button

		settingListView = (ListView) findViewById(R.id.setting_listview);

		Button backButton = (Button) findViewById(R.id.setting_back_button);
		backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		databaseManager.openDatabase();
		settingListView.setAdapter(new SettingListAdapter(getResources().getStringArray(R.array.setting_property_array)));
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	protected void onPause() {
		super.onPause();
		databaseManager.closeDatabase();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}



	/*** Self Method or Class ***/

	/**
	 * Setting List view Adapter
	 * It accepts custom setting list view rows
	 * 
	 * Date : May 28, 2013
	 * @author Seungmin Lee, rfrost77@gmail.com
	 * @since 1.0v
	 * @version "%I%, %G%"
	 *
	 */
	private class SettingListAdapter extends BaseAdapter{

		private String[] settingPropertyArray;
		private LayoutInflater mInflater;

		private Context settingAct = SettingAct.this;
		private Resources settingActResources = settingAct.getResources();

		private String initTitle = settingActResources.getString(R.string.setting_init_title);
		private String initMessage = settingActResources.getString(R.string.setting_init_message);
		private String init = settingActResources.getString(R.string.init_label);
		private String cancel = settingActResources.getString(R.string.cancel_label);
		private String version;


		public SettingListAdapter(String[] settingPropertyArray) {
			super();
			this.settingPropertyArray = settingPropertyArray;
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			try {
				version = settingAct.getPackageManager().getPackageInfo(settingAct.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}	
		}

		@Override
		public int getCount() {
			return settingPropertyArray.length;
		}

		@Override
		public Object getItem(int position) {
			return settingPropertyArray[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}


		// create a new ImageView for each item referenced by the Adapter

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// Make a view

			View vi = convertView;
			if (convertView == null)
				vi = (View) mInflater.inflate(R.layout.setting_list_row, null);


			// UI Setting
			// Setting text to each listview property and information
			// Setting click listener to each list property

			TextView property = (TextView) vi.findViewById(R.id.setting_property_text);
			property.setText(settingPropertyArray[position]);

			TextView information = (TextView) vi.findViewById(R.id.setting_information_text);

			switch(position){
			case VERSION_INFORMATION:		// Show Version Info to list view
				information.setVisibility(View.VISIBLE);
				information.setText(version);
				vi.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// do nothing
					}
				});
				break;


			case BIBLE_EDITION:		// Go to Set language and bible edition Activity
				information.setVisibility(View.GONE);
				vi.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						Intent i = new Intent(settingAct, SettingBibleEditionAct.class);
						startActivity(i);
					}
				});
				break;


			case DIFFICULTY:		// Set difficulty

				information.setVisibility(View.VISIBLE);


				// Get Difficulty from Shared Preference
				// Set Multi Language Difficulty display
				// Set click listener

				String difficulty = GlobalManager.getDifficultyString(settingAct);
				information.setText(difficulty);

				vi.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						Intent i = new Intent(settingAct, SettingDifficultyDialog.class);
						startActivity(i);
					}
				});
				break;


			case ABOUT_APP:		// Show about dialog
				information.setVisibility(View.GONE);
				vi.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						Intent i = new Intent(settingAct, SettingAboutDialog.class);
						startActivity(i);
					}
				});
				break;


			case INITIALIZE_APP:		// Show initialization dialog
				information.setVisibility(View.GONE);
				vi.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(settingAct);
						builder.setTitle(initTitle);
						builder.setMessage(initMessage);
						builder.setPositiveButton(init, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									databaseManager.deleteDatabaseFile(settingAct);
									databaseManager.populateDatabase();
									settingListView.setAdapter(new SettingListAdapter(
											getResources().getStringArray(R.array.setting_property_array)));
								} catch (IOException e) {
									e.printStackTrace();
								}
								dialog.dismiss();
							}
						});
						builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});


						// Create and show the AlertDialog with builder

						AlertDialog dialog = builder.create();
						dialog.show();
					}
				});
				break;

			default:
				break;
			};

			return vi;
		}
	}
}