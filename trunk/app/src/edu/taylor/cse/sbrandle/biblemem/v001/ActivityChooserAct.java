package edu.taylor.cse.sbrandle.biblemem.v001;

import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
public class ActivityChooserAct extends Activity implements OnClickListener {

	private String refString;
	private String textString;

	private int  projectId;
	private int projectVerseId;



	/*** Activity ***/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chooser);


		// Get Intent from previous Activity as a bundle form.
		// Intent includes chosen verse and project information

		Bundle extras = getIntent().getExtras();
		refString = extras.getString(GlobalVariable.VERSE_REF);
		textString = extras.getString(GlobalVariable.VERSE_TEXT);
		projectId = extras.getInt(GlobalVariable.PROJECT_ID);
		projectVerseId =  extras.getInt(GlobalVariable.PROJECT_VERSE_ID);
		

		// Verse Setting

		TextView verseRefTextView = (TextView) findViewById(R.id.activity_chooser_verse_ref_text);
		verseRefTextView.setText(refString);

		TextView verseTextView = (TextView) findViewById(R.id.activity_chooser_verse_text);
		verseTextView.setText(textString);


		// Button setting

		Button mflButton = (Button) findViewById(R.id.activity_chooser_mfl_button);
		mflButton.setOnClickListener(this);

		Button lwButton = (Button) findViewById(R.id.activity_chooser_lw_button);
		lwButton.setOnClickListener(this);

		Button wdButton = (Button) findViewById(R.id.activity_chooser_word_search_button);
		wdButton.setOnClickListener(this);

		Button backButton = (Button) findViewById(R.id.activity_chooser_back_button);
		backButton.setOnClickListener(this);
	}



	/*** OnClickListener***/

	@Override
	public void onClick(View v) {

		Intent intent = null;
		int id = v.getId();		// Get ID which is own of Button itself


		// Call Activity by its button ID with verse and project information

		if(id ==  R.id.activity_chooser_mfl_button){
			intent = new Intent(this, FirstLetterAct.class);
			intent.putExtra(GlobalVariable.VERSE_REF, refString);
			intent.putExtra(GlobalVariable.VERSE_TEXT, textString);
			intent.putExtra(GlobalVariable.PROJECT_ID, projectId);
			intent.putExtra(GlobalVariable.PROJECT_VERSE_ID, projectVerseId);
			startActivity(intent);
		} 

		else if(id ==   R.id.activity_chooser_lw_button){
			intent = new Intent(this, LocateWordAct.class);
			intent.putExtra(GlobalVariable.VERSE_REF, refString);
			intent.putExtra(GlobalVariable.VERSE_TEXT, textString);
			intent.putExtra(GlobalVariable.PROJECT_ID, projectId);
			intent.putExtra(GlobalVariable.PROJECT_VERSE_ID, projectVerseId);
			startActivity(intent);
		}

		else if(id ==   R.id.activity_chooser_word_search_button){
			intent = new Intent(this, WordSearchAct.class);
			intent.putExtra(GlobalVariable.VERSE_REF, refString);
			intent.putExtra(GlobalVariable.VERSE_TEXT, textString);
			intent.putExtra(GlobalVariable.PROJECT_ID, projectId);
			intent.putExtra(GlobalVariable.PROJECT_VERSE_ID, projectVerseId);
			startActivity(intent);
		}

		else if(id ==   R.id.activity_chooser_back_button){
			finish();
		}
	}
}
