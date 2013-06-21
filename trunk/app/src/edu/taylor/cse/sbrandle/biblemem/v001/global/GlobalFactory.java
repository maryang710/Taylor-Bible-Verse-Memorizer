package edu.taylor.cse.sbrandle.biblemem.v001.global;

import java.io.IOException;
import java.util.Locale;

import android.content.Context;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseKJVEnManager;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseKRVKoManager;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;

public class GlobalFactory {

	/**
	 * It sets database manager by phone's language setting
	 * You can extend later with more languages.
	 * 
	 * @param context
	 * @return
	 * @throws IOException 
	 */
	public static DatabaseManager getDatabaseManagerByLanguage(Context context) throws IOException {
		DatabaseManager databaseManager = new DatabaseKJVEnManager(context);
		databaseManager.openDatabase();
		if(databaseManager.getBibleEditionCode().equals(GlobalVariable.KRV_CODE))
			databaseManager = new DatabaseKRVKoManager(context);
		databaseManager.close();
		return databaseManager;
	}



	/**
	 * It sets multilanguage manager by phone's language setting
	 * You can extend later with more languages.
	 * 
	 * @param context
	 * @return
	 */
	public static MultiLanguageManager getMultiLanguageManager(){
		if(Locale.getDefault().getLanguage().equals(GlobalVariable.ENGLISH_CODE))
			return new MultiLanguageKJVManager();
		else if(Locale.getDefault().getLanguage().equals(GlobalVariable.KOREAN_CODE))
			return new MultiLanguageKRVManager();
		return null;
	}



	/**
	 * It sets multilanguage manager by phone's language setting
	 * You can extend later with more languages.
	 * 
	 * @param context
	 * @return
	 * @throws IOException 
	 */
	public static MultiLanguageManager getMultiLanguageManager(Context context) throws IOException{
		DatabaseManager databaseManager = new DatabaseKJVEnManager(context);
		databaseManager.openDatabase();
		if(databaseManager.getBibleEditionCode().equals(GlobalVariable.KJV_CODE))
			return new MultiLanguageKJVManager();
		else if(databaseManager.getBibleEditionCode().equals(GlobalVariable.KRV_CODE))
			return new MultiLanguageKRVManager();
		databaseManager.close();
		return null;
	}
}

