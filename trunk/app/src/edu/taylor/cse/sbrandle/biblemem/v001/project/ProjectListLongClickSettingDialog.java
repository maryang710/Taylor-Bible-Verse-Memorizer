package edu.taylor.cse.sbrandle.biblemem.v001.project;

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
public class ProjectListLongClickSettingDialog extends Activity {

	private DatabaseManager databaseManager;
	private int projectId;
	private String projectName;

	private Context projectListLongClickSettingDialog;

	private String removeMessage;
	private String remove;
	private String cancel;

	private String initMessage;
	private String init;

	private final int ENTER = 0;
	private final int RENAME = 1;
	private final int INITIALIZE = 2;
	private final int REMOVE = 3;
	

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
		projectId = extras.getInt(GlobalVariable.PROJECT_ID);
		projectName = extras.getString(GlobalVariable.PROJECT_NAME);


		// Set title with project name

		setTitle(projectName);


		// Setting Context and String for dialog

		projectListLongClickSettingDialog = ProjectListLongClickSettingDialog.this;

		removeMessage = getResources().getString(R.string.project_list_remove_dialog_message);
		remove = getResources().getString(R.string.remove_label);
		cancel = getResources().getString(R.string.cancel_label);

		initMessage = getResources().getString(R.string.project_list_init_dialog_message);
		init = getResources().getString(R.string.init_label);


		// Listview Setting

		ListView settingListView = (ListView) findViewById(R.id.project_list_long_click_setting_dialog_list_view);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.project_list_long_click_setting_property_array));
		settingListView.setAdapter(adapter);
		settingListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent i = null;
				AlertDialog.Builder builder = null;

				switch(position){
				case ENTER:		// Enter

					// Move to ProjectVerseAct

					i = new Intent(projectListLongClickSettingDialog, ProjectVerseAct.class);
					i.putExtra(GlobalVariable.PROJECT_ID, projectId);
					startActivity(i);
					finish();
					break;


				case RENAME:		// Rename

					// Move to Project List Add Dialog

					i = new Intent(projectListLongClickSettingDialog, ProjectListAddDialog.class);
					i.putExtra(GlobalVariable.RENAME_PROJECT, true);
					i.putExtra(GlobalVariable.PROJECT_ID, projectId);
					startActivity(i);
					break;


				case INITIALIZE:		// Initialize

					// Make initialize dialog builder

					builder = new AlertDialog.Builder(projectListLongClickSettingDialog);
					builder.setTitle(init + " " + projectName);
					builder.setMessage(initMessage);
					builder.setPositiveButton(init, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							databaseManager.updateProjectVerseScore(projectId, 0);
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

					builder = new AlertDialog.Builder(projectListLongClickSettingDialog);
					builder.setTitle(remove + " " + projectName);
					builder.setMessage(removeMessage);
					builder.setPositiveButton(remove, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							databaseManager.removeProject(projectId);
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

		// If it comes with different name from rename dialog, finish this dialog
		if(!projectName.equals(databaseManager.getProject(projectId).getProjectName()))
			finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		databaseManager.closeDatabase();
	}
}
