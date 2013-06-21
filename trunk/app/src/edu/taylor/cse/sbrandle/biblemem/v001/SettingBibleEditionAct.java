package edu.taylor.cse.sbrandle.biblemem.v001;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalFactory;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;
import edu.taylor.cse.sbrandle.biblemem.v001.object.BibleEditionObject;

public class SettingBibleEditionAct extends Activity {

	private DatabaseManager databaseManager;

	private ListView settingBibleEditionListView;
	private TextView currentEdition;
	
	private ArrayList<BibleEditionObject> bibleEditionList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_bible_edition);

		// DB work
		try {
			databaseManager = GlobalFactory.getDatabaseManagerByLanguage(this);
			databaseManager.openDatabase();
		} catch (IOException e) {
			e.printStackTrace();
		}


		// Set bible edition list
		// Fine view resources
		// Setting button
		
		bibleEditionList = new ArrayList<BibleEditionObject>();
		for(int i=0 ; i<GlobalVariable.EDITION_COUNT ; i++)
			bibleEditionList.add(new BibleEditionObject(GlobalVariable.SETTING_BIBLE_EDITION_NAME_LIST[i], GlobalVariable.SETTING_BIBLE_EDITION_CODE_LIST[i]));

		settingBibleEditionListView = (ListView) findViewById(R.id.setting_bible_edition_listview);
		settingBibleEditionListView.setAdapter(new SettingBibleEditionAdapter(bibleEditionList));

		currentEdition = (TextView) findViewById(R.id.setting_bible_edition_current_edition);
		currentEdition.setText(databaseManager.getBibleEditionName());
		
		Button backButton = (Button) findViewById(R.id.setting_bible_edition_back_button);
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

	}

	@Override
	protected void onPause() {
		super.onPause();
		databaseManager.closeDatabase();
	}



	/*** Self Method or Class ***/

	/**
	 * Project List view Adapter
	 * It accepts custom list view rows
	 * 
	 * Date : May 28, 2013
	 * @author Seungmin Lee, rfrost77@gmail.com
	 * @since 1.0v
	 * @version "%I%, %G%"
	 *
	 */
	private class SettingBibleEditionAdapter extends BaseAdapter {

		private ArrayList<BibleEditionObject> bibleEditionList;
		private LayoutInflater mInflater;

		private Context settingBibleEditionAct = SettingBibleEditionAct.this;
		private Resources settingBibleEditionResources = settingBibleEditionAct.getResources();


		public SettingBibleEditionAdapter(ArrayList<BibleEditionObject> bibleEditionList) {
			super();
			this.bibleEditionList = bibleEditionList;
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return bibleEditionList.size();
		}

		@Override
		public Object getItem(int position) {
			return bibleEditionList.get(position);
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

			final BibleEditionObject bibleEditionObject = bibleEditionList.get(position);
			final String bibleEditionName = bibleEditionObject.getBibleEditionName();
			final String bibleEditionCode = bibleEditionObject.getBibleEditionCode();
			final String languageBibleMessage = settingBibleEditionResources.getString(R.string.setting_bible_edition_message);
			final String yes = settingBibleEditionResources.getString(R.string.yes_label);
			final String cancel = settingBibleEditionResources.getString(R.string.cancel_label);

			TextView name = (TextView) vi.findViewById(R.id.setting_property_text);
			name.setText(bibleEditionName);


			// DB and View Setting
			// Register long and short click listener to view

			vi.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(settingBibleEditionAct);
					builder.setTitle(bibleEditionName);
					builder.setMessage(languageBibleMessage);
					builder.setPositiveButton(yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							databaseManager.setBibleEditionNameCode(bibleEditionName, bibleEditionCode);
							currentEdition.setText(databaseManager.getBibleEditionName());
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

			return vi;
		}
	}
}
