package edu.taylor.cse.sbrandle.biblemem.v001.global;

import java.io.IOException;

import android.content.Context;

public interface MultiLanguageManager {
	
	public void insertBibleEditionNameCode(Context context) throws IOException;
	public String normalizeString(Context context, String word);
	public String getRandomAlphabet();
	public String getFirstLetter(String word);
}
