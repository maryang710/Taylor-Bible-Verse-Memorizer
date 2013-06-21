package edu.taylor.cse.sbrandle.biblemem.v001;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;

public class Prefs extends PreferenceActivity {
	// Option names and default values
	private static final String OPT_ERROR_BEEP = "error_beep_pref";
	private static final boolean OPT_ERROR_BEEP_DEF = true;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
}
	
	@Override
	public boolean onCreateOptionsMenu(Menu prefs) {
		super.onCreateOptionsMenu(prefs);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.xml.prefs, prefs);
		return true;
	}
	
	/** Get the current value of the error beep option */
	public boolean getErrorBeep(Context context/**/) {
		//return PreferenceManager.getDefaultSharedPreferences(/*this*/context).getBoolean(OPT_ERROR_BEEP, OPT_ERROR_BEEP_DEF);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this/*context*/);
		if( sharedPref == null) return OPT_ERROR_BEEP_DEF;
		else return sharedPref.getBoolean(Prefs.OPT_ERROR_BEEP, OPT_ERROR_BEEP_DEF);
	}
	
	/** Get the current value of the hints option */
	/*public static boolean getHints(Context context) {
	return PreferenceManager.getDefaultSharedPreferences(context)
	.getBoolean(OPT_HINTS, OPT_HINTS_DEF);
	}*/
	
}