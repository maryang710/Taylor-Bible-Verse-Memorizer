package edu.taylor.cse.sbrandle.biblemem.v001.global;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.Window;
import edu.taylor.cse.sbrandle.biblemem.v001.R;

/**
 * Global Method Class
 * It calls method used some places.
 * 
 * Date : Jun 19, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class GlobalManager {

	/**
	 * It looks phone's SDK Version and set content view.
	 * Set custom title bar with below API 11
	 * Set custom Action bar with above API 11
	 * 
	 * @param
	 * @return
	 * 
	 */
	public static void setCustomContentView(Context context, int layoutId){
		Activity activity = (Activity) context;
		if(Build.VERSION.SDK_INT < GlobalVariable.HONEYCOMB){
			activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			activity.setContentView(layoutId);
			activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
		}
		else{
			activity.setContentView(layoutId);
		}
	}



	/**
	 * It split Korean into 3 word by their grammar.
	 * 
	 * @param string
	 * @return
	 */
	public static ArrayList<Character> splitKoreanIntoAlphabet(String string){
		ArrayList<Character> splitedString = new ArrayList<Character>();

		// typo스트링의 글자수 만큼 list에 담아둡니다.
		for (int i=0 ; i<string.length() ; i++) {
			char comVal = (char) (string.charAt(i)-0xAC00);


			// 한글일경우

			if (comVal >= 0 && comVal <= 11172){

				// 초성만 입력 했을 시엔 초성은 무시해서 List에 추가합니다.
				char uniVal = (char)comVal;

				// 유니코드 표에 맞추어 초성 중성 종성을 분리합니다..
				char cho = (char) ((((uniVal - (uniVal % 28)) / 28) / 21) + 0x1100);
				char jung = (char) ((((uniVal - (uniVal % 28)) / 28) % 21) + 0x1161);
				char jong = (char) ((uniVal % 28) + 0x11a7);

				if(cho!=4519){
					splitedString.add(cho);
				}
				if(jung!=4519){
					splitedString.add(jung);
				}
				if(jong!=4519){
					splitedString.add(jong);
				}
			} 


			// 한글이 아닐경우 Do nothing, just Store the value.

			else {
				comVal = (char) (comVal+0xAC00);
			}
		}
		return splitedString;
	}



	/**
	 * Make Random the number of hidden words and hidden position
	 * Use boolean array
	 * 
	 * @param hiddenWords
	 * @param iterationsLeft
	 */
	public static boolean[] chooseHiddenWords(Context context, String[] wordsArray) {

		// Get Difficulty float number

		SharedPreferences settingDifficulty = context.getSharedPreferences(GlobalVariable.DIFFICULTY_KEY, 0);
		float difficutly = getDifficulty(settingDifficulty.getString(GlobalVariable.DIFFICULTY, GlobalVariable.EASY_KEY));


		// Measure length the boolean array
		// Calculate the number of words to hide

		boolean[] hiddenWords = new boolean[wordsArray.length];
		int count = wordsArray.length;
		int numWordsToHide = (int) Math.ceil(count*difficutly);


		// Change the hidden boolean values from false to true
		// as many as count

		for(int i=0; i<count; i++) {
			if(i < numWordsToHide) 
				hiddenWords[i] = true;

			else
				hiddenWords[i] = false;
		}


		// Make Random hidden position

		for(int i=0; i<count; i++) {
			int swapLocation = (int) (Math.random()*count);
			boolean temp = hiddenWords[i];
			hiddenWords[i] = hiddenWords[swapLocation];
			hiddenWords[swapLocation] = temp;
		}

		return hiddenWords;
	}



	/**
	 * Get the number of hidden words.
	 * 
	 * @param hiddenWords
	 * @return
	 */
	public static int getHiddenWordsNumber(boolean[] hiddenWords){
		int num = 0;

		for(int i=0 ; i<hiddenWords.length ; i++)
			if(hiddenWords[i])
				num++;

		return num;
	}



	/**
	 * Get Difficulty percent float number
	 * 
	 * @param difficultyKey
	 * @return
	 */
	public static float getDifficulty(String difficultyKey){
		if(difficultyKey.equals(GlobalVariable.EASY_KEY)){
			return GlobalVariable.EASY;
		}
		else if(difficultyKey.equals(GlobalVariable.NORMAL_KEY)){
			return GlobalVariable.NORMAL;
		}
		else if(difficultyKey.equals(GlobalVariable.HARD_KEY)){
			return GlobalVariable.HARD;
		}
		return GlobalVariable.EASY;
	}



	/**
	 * Get Difficulty String
	 * 
	 * @param difficultyKey
	 * @return
	 */
	public static String getDifficultyString(Context context){
		SharedPreferences settingDifficulty = context.getSharedPreferences(GlobalVariable.DIFFICULTY_KEY, 0);
		String difficulty = settingDifficulty.getString(GlobalVariable.DIFFICULTY, GlobalVariable.EASY_KEY);
		String[] difficultyArray = context.getResources().getStringArray(R.array.setting_difficulty);
		if(difficulty.equals(GlobalVariable.EASY_KEY))
			return difficultyArray[GlobalVariable.EASY_INDEX];
		else if(difficulty.equals(GlobalVariable.NORMAL_KEY))
			return difficultyArray[GlobalVariable.NORMAL_INDEX];
		else if(difficulty.equals(GlobalVariable.HARD_KEY))
			return difficultyArray[GlobalVariable.HARD_INDEX];
		return null;
	}
}
