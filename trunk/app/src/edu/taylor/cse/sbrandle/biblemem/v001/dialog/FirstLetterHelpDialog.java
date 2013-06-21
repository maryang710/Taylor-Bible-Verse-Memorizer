package edu.taylor.cse.sbrandle.biblemem.v001.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import edu.taylor.cse.sbrandle.biblemem.v001.R;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;

/**
 * It is First Letter Introduction Activity Class which is shown as dialog.
 * It describes how to use the first letter memorizer mode.
 * 
 * Date : May 21, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class FirstLetterHelpDialog extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_dialog);
		
		Bundle extras = getIntent().getExtras();
		
		TextView refText = (TextView) findViewById(R.id.help_dialog_ref_text);
		refText.setText(extras.getString(GlobalVariable.VERSE_REF));
		
		TextView verseText = (TextView) findViewById(R.id.help_dialog_verse_text);
		verseText.setText(extras.getString(GlobalVariable.VERSE_TEXT));
		
		TextView helpText = (TextView) findViewById(R.id.help_dialog_help_text);
		helpText.setText(getResources().getString(R.string.first_letter_help_text));
	}
}
