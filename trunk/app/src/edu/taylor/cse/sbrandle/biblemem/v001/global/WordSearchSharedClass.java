/** This class uses the user preferences class in order to create a place holder where other activities 
 * can access data outside of their scope. 
 */
package edu.taylor.cse.sbrandle.biblemem.v001.global;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class WordSearchSharedClass extends Activity{
	@SuppressWarnings("unused")
	private final String TAG = "SharedClass";
	private static final String APP_SHARED_PREFS = "edu.taylor.cse.sbrandle.biblemem.v001"; 
	private SharedPreferences sharedInformation;
	private Editor prefsEditor;

	
	public WordSearchSharedClass(Context context){
		this.sharedInformation = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
		this.prefsEditor = sharedInformation.edit();
	}

	/** This function sets the currently selected word.
	 */
	public void setCurrentlySelectecWord(String word){
		prefsEditor.putString("selectedWord",word);
		prefsEditor.commit();
	}
	
	public String  getCurrentlySelectecWord() {
		return sharedInformation.getString("selectedWord", "");
	}	
}