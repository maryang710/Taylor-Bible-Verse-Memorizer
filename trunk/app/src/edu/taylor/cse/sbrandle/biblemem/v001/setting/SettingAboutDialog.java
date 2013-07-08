package edu.taylor.cse.sbrandle.biblemem.v001.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import edu.taylor.cse.sbrandle.biblemem.v001.R;

/**
 * It is About Activity Class which is shown as dialog.
 * It describes this application.
 * 
 * Date : May 20, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class SettingAboutDialog extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_dialog);

		TextView refText = (TextView) findViewById(R.id.help_dialog_ref_text);
		refText.setVisibility(View.GONE);

		TextView verseText = (TextView) findViewById(R.id.help_dialog_verse_text);
		verseText.setVisibility(View.GONE);

		TextView helpText = (TextView) findViewById(R.id.help_dialog_help_text);
		helpText.setText(getResources().getString(R.string.about_text));
	}
}
