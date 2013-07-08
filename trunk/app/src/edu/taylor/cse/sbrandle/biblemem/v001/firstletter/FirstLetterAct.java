package edu.taylor.cse.sbrandle.biblemem.v001.firstletter;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.taylor.cse.sbrandle.biblemem.v001.R;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalFactory;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;
import edu.taylor.cse.sbrandle.biblemem.v001.global.MultiLanguageManager;

/**
 * First Letter Memorizer Mode.
 * User can memorize Bible Verse by using first letter.
 * User put first letters in order in blank of a verse.
 * Three putting chance in one blank.
 * Blank is increased as try.
 * 
 * Date : May 21, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class FirstLetterAct extends Activity implements OnClickListener{

	private DatabaseManager databaseManager;
	private MultiLanguageManager multiLanguageManager;

	private TextView verseTextView;
	private Button answerButton1;
	private Button answerButton2;
	private Button answerButton3;
	private Button[] answerButtons;

	private String refString;
	private String textString;
	private int projectId;
	private int projectVerseId;

	private String[] wordsArray;
	private boolean[] hiddenWords;

	private int wordsArrayIndex;	// Word Index currently being worked on in wordsArray

	private float potential_points;
	private float word_worth;



	/*** Activity ***/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalManager.setCustomContentView(this, R.layout.first_letter);
		TextView titleBarText = (TextView) findViewById(R.id.title_bar_text);


		/*** Setting ***/

		// DB work

		try {
			multiLanguageManager = GlobalFactory.getMultiLanguageManager(this);
			databaseManager = GlobalFactory.getDatabaseManagerByLanguage(this);
			databaseManager.openDatabase();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		// Get Intent from previous Activity as a bundle form.
		// Intent includes chosen verse and project information
		// Set title bar text with ref string
		// Setting Verse text

		Bundle extras = getIntent().getExtras();
		refString = extras.getString(GlobalVariable.VERSE_REF);
		textString = extras.getString(GlobalVariable.VERSE_TEXT);
		projectId = extras.getInt(GlobalVariable.PROJECT_ID);
		projectVerseId =  extras.getInt(GlobalVariable.PROJECT_VERSE_ID);
		verseTextView = (TextView) findViewById(R.id.first_letter_verse_text);


		// Set title bar text with ref string and difficulty

		String difficulty = GlobalManager.getDifficultyString(this);
		titleBarText.setText(refString + " " + difficulty);


		// Button Setting

		Button helpButton = (Button) findViewById(R.id.first_letter_help_button);
		helpButton.setOnClickListener(this);

		Button doneButton = (Button) findViewById(R.id.first_letter_done_button);
		doneButton.setOnClickListener(this);

		answerButton1 = (Button) findViewById(R.id.first_letter_answer_1);
		answerButton1.setOnClickListener(this);

		answerButton2 = (Button) findViewById(R.id.first_letter_answer_2);
		answerButton2.setOnClickListener(this);

		answerButton3 = (Button) findViewById(R.id.first_letter_answer_3);
		answerButton3.setOnClickListener(this);

		answerButtons = new Button[3];
		answerButtons[0] = answerButton1;
		answerButtons[1] = answerButton2;
		answerButtons[2] = answerButton3;



		/*** Start First Letter Logic ***/


		// First showing activity

		if(savedInstanceState == null){
			startFirstLetter();
		}


		// When it change it's orientation

		else{

			// Load the instance

			wordsArray = savedInstanceState.getStringArray(GlobalVariable.WORDS_ARRAY);
			hiddenWords = savedInstanceState.getBooleanArray(GlobalVariable.HIDDEN_WORDS);
			wordsArrayIndex = savedInstanceState.getInt(GlobalVariable.WORDS_ARRAY_INDEX);
			potential_points = savedInstanceState.getFloat(GlobalVariable.POTENTIAL_POINTS);
			word_worth = savedInstanceState.getFloat(GlobalVariable.WORD_WORTH);
			String buttonText1 = savedInstanceState.getString(GlobalVariable.BUTTON_TEXT1);
			String buttonText2 = savedInstanceState.getString(GlobalVariable.BUTTON_TEXT2);
			String buttonText3 = savedInstanceState.getString(GlobalVariable.BUTTON_TEXT3);
			boolean buttonEnabled1 = savedInstanceState.getBoolean(GlobalVariable.BUTTON_ENABLED1);
			boolean buttonEnabled2 = savedInstanceState.getBoolean(GlobalVariable.BUTTON_ENABLED2);
			boolean buttonEnabled3 = savedInstanceState.getBoolean(GlobalVariable.BUTTON_ENABLED3);


			// Set Text with Hidden reign
			// Set Button with previous text

			verseTextView.setText(generateVerseString(wordsArray, wordsArrayIndex, hiddenWords));
			answerButton1.setText(buttonText1);
			answerButton2.setText(buttonText2);
			answerButton3.setText(buttonText3);
			answerButton1.setEnabled(buttonEnabled1);
			answerButton2.setEnabled(buttonEnabled2);
			answerButton3.setEnabled(buttonEnabled3);
		}	
	}

	@Override
	protected void onResume() {
		super.onResume();
		databaseManager.openDatabase();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// If it comes from memorize verse or review, no update.
		if(projectVerseId == 0 && projectId == 0){
			// do nothing
		}


		// If it comes from project, update score.
		else{

			// If it is already done, no update
			if(databaseManager.getProjectVerse(projectVerseId).getPercent() >= GlobalVariable.CRITERIA_OF_SUCCESS){
				// do nothing
			}


			// If it is not done yet, Update score.
			// Fail, 0. Success, 100.
			else{
				if(potential_points < GlobalVariable.CRITERIA_OF_SUCCESS)
					databaseManager.updateVerseScore(projectId, projectVerseId, 0);
				else
					databaseManager.updateVerseScore(projectId, projectVerseId, 100);
			}
		}
		databaseManager.closeDatabase();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the instance

		outState.putStringArray(GlobalVariable.WORDS_ARRAY, wordsArray);
		outState.putBooleanArray(GlobalVariable.HIDDEN_WORDS, hiddenWords);
		outState.putInt(GlobalVariable.WORDS_ARRAY_INDEX, wordsArrayIndex);
		outState.putFloat(GlobalVariable.POTENTIAL_POINTS, potential_points);
		outState.putFloat(GlobalVariable.WORD_WORTH, word_worth);
		outState.putString(GlobalVariable.BUTTON_TEXT1, answerButton1.getText().toString());
		outState.putString(GlobalVariable.BUTTON_TEXT2, answerButton2.getText().toString());
		outState.putString(GlobalVariable.BUTTON_TEXT3, answerButton3.getText().toString());
		outState.putBoolean(GlobalVariable.BUTTON_ENABLED1, answerButton1.isEnabled());
		outState.putBoolean(GlobalVariable.BUTTON_ENABLED2, answerButton2.isEnabled());
		outState.putBoolean(GlobalVariable.BUTTON_ENABLED3, answerButton3.isEnabled());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.help, menu);
		return true;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_help:
			Intent i = new Intent(this, FirstLetterHelpDialog.class);
			i.putExtra(GlobalVariable.VERSE_REF, refString);
			i.putExtra(GlobalVariable.VERSE_TEXT, textString);
			startActivity(i);
			break;
		default:
			break;
		}
		return true;
	}



	/*** OnClickListener ***/

	@Override
	public void onClick(View v) {

		Intent i = null;
		int id = v.getId();		// Get ID which is own of Button itself


		// Act by buttons id

		if(id == R.id.first_letter_help_button){
			i = new Intent(this, FirstLetterHelpDialog.class);
			i.putExtra(GlobalVariable.VERSE_REF, refString);
			i.putExtra(GlobalVariable.VERSE_TEXT, textString);
			startActivity(i);
		}

		else if(id == R.id.first_letter_done_button){
			finish();
		}


		// Answer Buttons Action

		else if(id == R.id.first_letter_answer_1 || id == R.id.first_letter_answer_2 || id == R.id.first_letter_answer_3){

			// Find Button and Texts
			// If user click button very fast, it happens error over the array size.
			// Prevent above situation

			if(setWordIndex(wordsArray, hiddenWords) >= wordsArray.length)
				return;
			Button button = (Button) v;
			String buttonText = ""+button.getText();
			String verseText = multiLanguageManager.getFirstLetter(wordsArray[setWordIndex(wordsArray, hiddenWords)]);


			// If Answer rightly, progress next level.
			// Set verse text with the hidden word that user answered.
			// Button answer text will be changed every level.

			if(buttonText.equals(verseText)){

				// Got this word, so change the hidden boolean to show.

				hiddenWords[wordsArrayIndex] = false;
				wordsArrayIndex++;
				potential_points = potential_points + word_worth;
				verseTextView.setText(generateVerseString(wordsArray, wordsArrayIndex, hiddenWords));


				// If user answered all verse hidden words, show the verse again with new blank.
				// Show progress dialog for letting user know the finish of one game.
				// Init the game.

				setWordIndex(wordsArray, hiddenWords);
				if(wordsArrayIndex >= wordsArray.length)
					showOneMoreDialog((int) potential_points);


				// If not finished yet
				// Set buttons with new random text

				else
					setAnswerButtonText(answerButtons, wordsArray, hiddenWords);
			}


			// If wrong answer, make button not work
			else{
				button.setEnabled(false);
			}
		}
	}



	/*** Self Mehotd ***/


	/**
	 * Start First Letter Memorizing.
	 * Split the verse and Set the text with hidden words.
	 * Button set with random text including answer.
	 * 
	 * @param
	 * @return
	 * 
	 */
	private void startFirstLetter(){

		// Split verse string
		// Choose hidden words in verse

		wordsArray = textString.split(" ");
		hiddenWords = GlobalManager.chooseHiddenWords(this, wordsArray);
		int num_of_hidden_words = GlobalManager.getHiddenWordsNumber(hiddenWords);


		// For progress bar, measure the percentage of each word.

		potential_points = 0;
		word_worth = ((float) 100 / num_of_hidden_words);


		// Init variables to start logic
		// Set Text with Hidden reign

		wordsArrayIndex = 0;
		verseTextView.setText(generateVerseString(wordsArray, wordsArrayIndex, hiddenWords));


		// Set Button Text

		setWordIndex(wordsArray, hiddenWords);
		setAnswerButtonText(answerButtons, wordsArray, hiddenWords);
	}



	/**
	 * Generate EditText of verse.
	 * Hide some words, Show some words.
	 * 
	 * @param wordsArray
	 * @param wordsArrayIndex
	 * @param hiddenWords
	 * @param verseTextView
	 * @return
	 */
	private String generateVerseString(String[] wordsArray, int wordsArrayIndex, boolean[] hiddenWords) {

		String showString = "";
		for (int i=0; i<wordsArray.length; i++) {

			// Unconditionally show words that user answered.

			if (i < wordsArrayIndex)
				showString = showString + wordsArray[i];


			// By hidden boolean, show or hide String

			else if (hiddenWords[i] == true)
				showString = showString + "_____";

			else
				showString = showString + wordsArray[i];


			// At the end of the string, add " "

			if (i<wordsArray.length-1) {
				showString = showString + " ";
			}
		}

		return showString;
	}



	/**
	 * Look for next hidden word -- skip words that are shown
	 * 
	 * @param wordsArray
	 * @param hiddenWords
	 */
	private int setWordIndex(String[] wordsArray, boolean[] hiddenWords){
		wordsArrayIndex = 0;
		while(wordsArrayIndex < wordsArray.length && hiddenWords[wordsArrayIndex] == false)
			wordsArrayIndex++;
		return wordsArrayIndex;
	}



	/**
	 * Set three random alphabets and indexs
	 * Set Button text with above variables
	 * 
	 * @param answerButtons
	 * @param wordsArray
	 * @param hiddenWords
	 */
	private void setAnswerButtonText(Button[] answerButtons, String[] wordsArray, boolean[] hiddenWords){

		// Set Random Answer Alphabets

		String answerAlphabet1 = multiLanguageManager.getFirstLetter(wordsArray[wordsArrayIndex]);
		String answerAlphabet2 = multiLanguageManager.getRandomAlphabet();
		String answerAlphabet3 = multiLanguageManager.getRandomAlphabet();

		while(answerAlphabet1.equals(answerAlphabet2))
			answerAlphabet2 = multiLanguageManager.getRandomAlphabet();

		while(answerAlphabet1.equals(answerAlphabet3) || answerAlphabet2.equals(answerAlphabet3))
			answerAlphabet3 = multiLanguageManager.getRandomAlphabet();


		// Set Random Index of buttons

		int answerIndex1 = (int) (Math.random() * 3);
		int answerIndex2 = (int) (Math.random() * 3);
		int answerIndex3 = (int) (Math.random() * 3);

		while(answerIndex1 == answerIndex2)
			answerIndex2 = (int) (Math.random() * 3);

		while(answerIndex1 == answerIndex3 || answerIndex2 == answerIndex3)
			answerIndex3 = (int) (Math.random() * 3);


		// Set Buttons text

		answerButtons[answerIndex1].setText(answerAlphabet1);
		answerButtons[answerIndex2].setText(answerAlphabet2);
		answerButtons[answerIndex3].setText(answerAlphabet3);
		answerButtons[answerIndex1].setEnabled(true);
		answerButtons[answerIndex2].setEnabled(true);
		answerButtons[answerIndex3].setEnabled(true);
	}



	/**
	 * Show Dialog which asks one more game.
	 * It shows how percent user answer rightly the verse.
	 * 
	 * @param
	 * @return
	 * 
	 */
	private void showOneMoreDialog(int potential_points){
		AlertDialog.Builder db = new AlertDialog.Builder(this);
		if(potential_points < GlobalVariable.CRITERIA_OF_SUCCESS){
			db.setTitle(refString + " " + getResources().getString(R.string.fail_label));
			db.setMessage(textString + "\n\n" + getResources().getString(R.string.one_more_dialog_text1) + " " + potential_points
					+ getResources().getString(R.string.one_more_dialog_text2) + " " + getResources().getString(R.string.one_more_dialog_text3));
		}
		else{
			db.setTitle(refString + " " + getResources().getString(R.string.success_label));
			db.setMessage(textString + "\n\n" + getResources().getString(R.string.one_more_dialog_text1) + " 100"
					+ getResources().getString(R.string.one_more_dialog_text2) + " " + getResources().getString(R.string.one_more_dialog_text3));
		}
		db.setPositiveButton(getResources().getString(R.string.yes_label), new 
				DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				startFirstLetter();
			}
		});
		db.setNegativeButton(getResources().getString(R.string.no_label), new 
				DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		db.show();
	}
}
