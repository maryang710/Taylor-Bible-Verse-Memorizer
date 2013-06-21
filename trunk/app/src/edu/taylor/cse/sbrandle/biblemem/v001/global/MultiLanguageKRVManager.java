package edu.taylor.cse.sbrandle.biblemem.v001.global;

import java.io.IOException;

import android.content.Context;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseKJVEnManager;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;

/**
 * Manage KRV version bible logic
 * 
 * Date : Jun 19, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class MultiLanguageKRVManager implements MultiLanguageManager {
	
	/**
	 * insert bible edition name and code by their edition setting
	 * 
	 * @param context
	 * @return
	 */
	@Override
	public void insertBibleEditionNameCode(Context context) throws IOException{
		DatabaseManager databaseManager = new DatabaseKJVEnManager(context);
		databaseManager.openDatabase();
		databaseManager.insertBibleEditionNameCode(GlobalVariable.KRV_NAME , GlobalVariable.KRV_CODE);
		databaseManager.closeDatabase();
	}
	
	/**
	 * Change the word to upper case.
	 * Remove all symbol.
	 * It should go different by it's local language setting
	 * 
	 * @param word
	 * @return
	 */
	@Override
	public String normalizeString(Context context, String word) {
		return word;
	}
	
	/**
	 * Get random alphabet
	 * 
	 * @return
	 */
	@Override
	public String getRandomAlphabet(){
		String alphabet = GlobalVariable.ALPHABET_KO;
		return "" + alphabet.charAt((int)(Math.random()*alphabet.length()));
	}

	/**
	 * Get First Letter
	 * 
	 * @return String
	 */
	@Override
	public String getFirstLetter(String word){
		return ""+GlobalMethod.splitKoreanIntoAlphabet(word).get(0);
	}
}
