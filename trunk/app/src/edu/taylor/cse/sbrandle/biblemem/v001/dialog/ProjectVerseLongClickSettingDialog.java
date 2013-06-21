package edu.taylor.cse.sbrandle.biblemem.v001.dialog;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.taylor.cse.sbrandle.biblemem.v001.ActivityChooserAct;
import edu.taylor.cse.sbrandle.biblemem.v001.R;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalFactory;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;

/**
 * It is Dialog for setting each project list item.
 * User can do many things in here such as rename, remove, etc.
 * 
 * Date : Jun 4, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class ProjectVerseLongClickSettingDialog extends Activity {

	private DatabaseManager databaseManager;

	private Context projectVerseLongClickSettingDialog;

	private String refString;
	private String textString;
	private int  projectId;
	private int projectVerseId;
	private int verseId;

	private boolean projectVerse;
	private boolean review;

	private String removeMessage;
	private String remove;
	private String cancel;

	private String initMessage;
	private String init;

	private final int ENTER = 0;
	private final int INITIALIZE = 1;
	private final int REMOVE = 2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_list_long_click_setting_dialog);


		// DB work

		try {
			databaseManager = GlobalFactory.getDatabaseManagerByLanguage(this);
			databaseManager.openDatabase();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		// Get the project index from previous activity

		Bundle extras = getIntent().getExtras();
		refString = extras.getString(GlobalVariable.VERSE_REF);
		textString = extras.getString(GlobalVariable.VERSE_TEXT);
		projectId = extras.getInt(GlobalVariable.PROJECT_ID);
		projectVerseId = extras.getInt(GlobalVariable.PROJECT_VERSE_ID);
		verseId = extras.getInt(GlobalVariable.VERSE_ID);

		projectVerse = extras.getBoolean(GlobalVariable.PROJECT_VERSE);
		review = extras.getBoolean(GlobalVariable.REVIEW);

		// Set title with bible reference

		setTitle(refString);


		// Setting Context and String for dialog

		projectVerseLongClickSettingDialog = ProjectVerseLongClickSettingDialog.this;

		removeMessage = getResources().getString(R.string.project_list_remove_dialog_message);
		remove = getResources().getString(R.string.remove_label);
		cancel = getResources().getString(R.string.cancel_label);

		initMessage = getResources().getString(R.string.project_list_init_dialog_message);
		init = getResources().getString(R.string.init_label);


		// Listview Setting
		// If it came from projectVerse, also show remove verse
		// Otherwise, don't show

		ListView settingListView = (ListView) findViewById(R.id.project_list_long_click_setting_dialog_list_view);
		if(projectVerse)
			settingListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.project_verse_long_click_setting_property_array)));
		else if(review)
			settingListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.review_manager_long_click_setting_property_array)));
		settingListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent i = null;
				AlertDialog.Builder builder = null;

				switch(position){
				case ENTER:		// Enter

					// Move to ActivityChooserAct

					i = new Intent(projectVerseLongClickSettingDialog, ActivityChooserAct.class);

					i.putExtra(GlobalVariable.VERSE_REF, refString);
					i.putExtra(GlobalVariable.VERSE_TEXT, textString);
					i.putExtra(GlobalVariable.PROJECT_ID, projectId);
					i.putExtra(GlobalVariable.PROJECT_VERSE_ID, projectVerseId);
					startActivity(i);
					finish();
					break;


				case INITIALIZE:		// Initialize

					// Make initialize dialog builder

					builder = new AlertDialog.Builder(projectVerseLongClickSettingDialog);
					builder.setTitle(init + " " + refString);
					builder.setMessage(initMessage);
					builder.setPositiveButton(init, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							// If it came from review, init all verses which is same with this ref text
							// Otherwise, just Init only this one

							if(review)
								databaseManager.updateSpecifiedVerseScore(verseId, 0);
							else
								databaseManager.updateVerseScore(projectId, projectVerseId, 0);
							dialog.dismiss();
							finish();
						}
					});
					builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});


					// Create and show the AlertDialog with builder

					AlertDialog initializeDialog = builder.create();
					initializeDialog.show();
					break;


				case REMOVE:		// Remove

					// Make remove dialog builder

					builder = new AlertDialog.Builder(projectVerseLongClickSettingDialog);
					builder.setTitle(remove + " " + refString);
					builder.setMessage(removeMessage);
					builder.setPositiveButton(remove, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							databaseManager.removeVerseFromProject(projectId, projectVerseId);
							dialog.dismiss();
							finish();
						}
					});
					builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});


					// Create and show the AlertDialog with builder

					AlertDialog removeDialog = builder.create();
					removeDialog.show();
					break;


				default:		// Default
					finish();
					break;
				}
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
}
