package edu.taylor.cse.sbrandle.biblemem.v001;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.taylor.cse.sbrandle.biblemem.v001.firstletter.FirstLetterAct;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;
import edu.taylor.cse.sbrandle.biblemem.v001.locateword.LocateWordAct;
import edu.taylor.cse.sbrandle.biblemem.v001.setting.SettingDifficultyDialog;

/**
 * Activity Chooser Activity
 * User can choose mode to memorize a verse
 * 
 * Date : May 21, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class ActivityChooserAct extends Activity{

	private Context activityChooserAct;

	private String refString;
	private String textString;

	private int  projectId;
	private int projectVerseId;

	private boolean todayVerseWidget;


	/*** Activity ***/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalManager.setCustomContentView(this, R.layout.activity_chooser);


		// Get Intent from previous Activity as a bundle form.
		// Intent includes chosen verse and project information
		// Get context

		Bundle extras = getIntent().getExtras();
		refString = extras.getString(GlobalVariable.VERSE_REF);
		textString = extras.getString(GlobalVariable.VERSE_TEXT);
		projectId = extras.getInt(GlobalVariable.PROJECT_ID);
		projectVerseId =  extras.getInt(GlobalVariable.PROJECT_VERSE_ID);
		todayVerseWidget = extras.getBoolean(GlobalVariable.TODAY_VERSE_WIDGET);
		activityChooserAct = ActivityChooserAct.this;


		// Verse Text Setting

		TextView verseTextView = (TextView) findViewById(R.id.activity_chooser_verse_text);
		verseTextView.setText(textString);


		// Button setting
		// If it comes from widget, change the another verse button text to exit

		Button mflButton = (Button) findViewById(R.id.activity_chooser_mfl_button);
		mflButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activityChooserAct, FirstLetterAct.class);
				intent.putExtra(GlobalVariable.VERSE_REF, refString);
				intent.putExtra(GlobalVariable.VERSE_TEXT, textString);
				intent.putExtra(GlobalVariable.PROJECT_ID, projectId);
				intent.putExtra(GlobalVariable.PROJECT_VERSE_ID, projectVerseId);
				startActivity(intent);
			}
		});

		Button lwButton = (Button) findViewById(R.id.activity_chooser_lw_button);
		lwButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activityChooserAct, LocateWordAct.class);
				intent.putExtra(GlobalVariable.VERSE_REF, refString);
				intent.putExtra(GlobalVariable.VERSE_TEXT, textString);
				intent.putExtra(GlobalVariable.PROJECT_ID, projectId);
				intent.putExtra(GlobalVariable.PROJECT_VERSE_ID, projectVerseId);
				startActivity(intent);
			}
		});

		Button backButton = (Button) findViewById(R.id.activity_chooser_back_button);
		backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(todayVerseWidget){
					Intent intent = new Intent(activityChooserAct, MainAct.class);
					startActivity(intent);
				}
				finish();
			}
		});
		if(todayVerseWidget)
			backButton.setText(getResources().getString(R.string.back_label));
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Set title bar text with ref string and difficulty
		
		TextView titleBarText = (TextView) findViewById(R.id.title_bar_text);
		String difficulty = GlobalManager.getDifficultyString(activityChooserAct);
		titleBarText.setText(refString + " " + difficulty);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.difficulty, menu);
		return true;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_difficulty:
			Intent intent = new Intent(activityChooserAct, SettingDifficultyDialog.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return true;
	}
}
