package edu.taylor.cse.sbrandle.biblemem.v001.dialog;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import edu.taylor.cse.sbrandle.biblemem.v001.R;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalFactory;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;

/**
 * It is Project List Add Dialog
 * It add project when it came from project list activity
 * It rename project when it came from long lick dialog
 * 
 * Date : Jun 4, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class ProjectListAddDialog extends Activity implements TextWatcher{

	private DatabaseManager databaseManager;
	private Button addButton;
	private EditText nameEditText;
	private int project_id;


	/*** Activity ***/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_list_add_dialog);


		// DB work

		try {
			databaseManager = GlobalFactory.getDatabaseManagerByLanguage(this);
			databaseManager.openDatabase();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		// Get Intent from previous Activity as a bundle form.
		// Intent includes activity information

		Bundle extras = getIntent().getExtras();
		boolean addProject = extras.getBoolean(GlobalVariable.ADD_PROJECT);
		boolean renameProject = extras.getBoolean(GlobalVariable.RENAME_PROJECT);
		project_id = extras.getInt("projectId");



		// Edit Text and Button Setting

		nameEditText =  (EditText) findViewById(R.id.project_list_add_dialog_name_text);
		nameEditText.addTextChangedListener(this);

		addButton = (Button) findViewById(R.id.project_list_add_dialog_add_button);
		setAddButton();

		Button cancelButton = (Button) findViewById(R.id.project_list_add_dialog_cancel_button);
		cancelButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
			}
		});


		// If it is for add project
		// Create new project to DB with the name given by user

		if(addProject){
			setTitle(getResources().getString(R.string.add_project_label));
			addButton.setText(getResources().getString(R.string.add_label));
			addButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					databaseManager.createProject(nameEditText.getText().toString());
					finish();
				}
			});
		}


		// If it is for rename project

		else if(renameProject){
			setTitle(getResources().getString(R.string.rename_label));
			addButton.setText(getResources().getString(R.string.rename_label));
			addButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					databaseManager.renameProject(nameEditText.getText().toString(), project_id);
					finish();
				}
			});
		}




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



	/*** TextWatcher ***/

	@Override
	public void afterTextChanged(Editable s) {
		setAddButton();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}



	/*** Self Method ***/


	/**
	 * If name is given by user, set add button enabled,
	 * otherwise, disabled.
	 * 
	 * @param
	 * @return
	 * 
	 */
	private void setAddButton(){
		if(nameEditText.getText().toString().equals(""))
			addButton.setEnabled(false);
		else
			addButton.setEnabled(true);
	}
}
