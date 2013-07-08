package edu.taylor.cse.sbrandle.biblemem.v001.setting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.taylor.cse.sbrandle.biblemem.v001.R;
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
public class SettingDifficultyDialog extends Activity {

	private final int EASY = 0;
	private final int NORMAL = 1;
	private final int HARD = 2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_list_long_click_setting_dialog);

		
		// Listview Setting

		ListView settingDifficultyListView = (ListView) findViewById(R.id.project_list_long_click_setting_dialog_list_view);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.setting_difficulty));
		settingDifficultyListView.setAdapter(adapter);
		settingDifficultyListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				// SharedPreferences work

				SharedPreferences settingDifficulty = getSharedPreferences(GlobalVariable.DIFFICULTY_KEY, 0);
				SharedPreferences.Editor editor = settingDifficulty.edit();
				
				
				switch(position){
				case EASY:		// EASY

					// Set difficulty easy

					editor.putString(GlobalVariable.DIFFICULTY, GlobalVariable.EASY_KEY);
					break;


				case NORMAL:		// NORMAL

					// Set difficulty normal

					editor.putString(GlobalVariable.DIFFICULTY, GlobalVariable.NORMAL_KEY);
					break;


				case HARD:		// HARD

					// Set difficulty hard

					editor.putString(GlobalVariable.DIFFICULTY, GlobalVariable.HARD_KEY);
					break;


				default:		// Default
					finish();
					break;
				}
				editor.commit();
				finish();
			}
		});

	}
}
