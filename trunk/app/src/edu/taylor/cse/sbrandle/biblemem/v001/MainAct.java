package edu.taylor.cse.sbrandle.biblemem.v001;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseKJVEnManager;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;
import edu.taylor.cse.sbrandle.biblemem.v001.project.ProjectListAct;
import edu.taylor.cse.sbrandle.biblemem.v001.project.ReviewManagerAct;
import edu.taylor.cse.sbrandle.biblemem.v001.setting.SettingAct;


/**
 * It's Main Activity.
 * Here is main functional buttons.
 * 
 * Date : May 20, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 * @comments : You have to change the encoding into UTF-8 for reading whole code because it is composed of partially in Korean.
 *
 */
public class MainAct extends Activity implements OnClickListener {

	private DatabaseManager databaseManager;


	/*** Activity ***/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalManager.setCustomContentView(this, R.layout.main);
		findViewById(R.id.title_bar_text).setVisibility(View.GONE);


		// DB work
		try {
			databaseManager = new DatabaseKJVEnManager(this);
			databaseManager.populateDatabase();
		} catch (IOException e) {
			e.printStackTrace();
		}


		// Find button views
		// Register OnClickListener

		Button memorizeButton = (Button) findViewById(R.id.main_memorize_button);
		memorizeButton.setOnClickListener(this);

		Button projectButton = (Button) findViewById(R.id.main_project_button);
		projectButton.setOnClickListener(this);

		Button reviewButton = (Button) findViewById(R.id.main_review_button);
		reviewButton.setOnClickListener(this);

		Button preferenceButton = (Button) findViewById(R.id.main_setting_button);
		preferenceButton.setOnClickListener(this);

		Button exitButton = (Button) findViewById(R.id.main_exit_button);
		exitButton.setOnClickListener(this);
	}



	/*** OnClickListener***/

	@Override
	public void onClick(View v) {

		Intent i = null;
		int id = v.getId();		// Get ID which is own of Button itself


		// Call Activity by its button ID

		if(id == R.id.main_memorize_button){
			i = new Intent(this, VerseChooserAct.class);
			i.putExtra(GlobalVariable.MEMORIZE, true);
			startActivity(i);
		}

		else if(id == R.id.main_project_button){
			i = new Intent(this, ProjectListAct.class);
			startActivity(i);
		}

		else if(id == R.id.main_review_button){
			i = new Intent(this, ReviewManagerAct.class);
			startActivity(i);
		}

		else if(id == R.id.main_setting_button){
			i = new Intent(this, SettingAct.class);
			startActivity(i);
		}

		else if(id == R.id.main_exit_button){
			finish();
		}
	}
}
