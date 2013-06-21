package edu.taylor.cse.sbrandle.biblemem.v001;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;
import edu.taylor.cse.sbrandle.biblemem.v001.dialog.LocateWordHelpDialog;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalFactory;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalMethod;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;
import edu.taylor.cse.sbrandle.biblemem.v001.global.MultiLanguageManager;
import edu.taylor.cse.sbrandle.biblemem.v001.layout.FlowLayout;

/**
 * Locate Word memorization method Activity
 * 
 * Date : May 28, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class LocateWordAct extends Activity implements OnClickListener {

	private DatabaseManager databaseManager;
	private MultiLanguageManager multiLanguageManager;

	private FlowLayout flowLayout;

	private TextView wordToTouchView;
	private TextView chanceTextView;

	private String refString;
	private String textString;
	private int projectId;
	private int projectVerseId;

	private String[] wordsArray;
	private boolean[] hiddenWords;

	private String mixedWords[];
	private ArrayList<Button> wordViews;

	//	private int wordCount;
	private int num_of_hidden_words;
	private int mixedWordsIndex;	// Word currently being processed in wordsArray

	private final int MAX_TRIES = 3;	// Maximum number of invalid guesses allowed.
	private int wordTryCount;	// Count failed location guesses. Used to show word after maxErrorCount guesses.

	private float potential_points;
	private float word_worth;



	/*** Activity ***/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locate_word);

		/*** Setting ***/

		// DB and Manager work

		try {
			multiLanguageManager = GlobalFactory.getMultiLanguageManager(this);
			databaseManager = GlobalFactory.getDatabaseManagerByLanguage(this);
			databaseManager.openDatabase();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		// Get Intent from previous Activity as a bundle form.
		// Intent includes chosen verse and project information

		Bundle extras = getIntent().getExtras();
		refString = extras.getString(GlobalVariable.VERSE_REF);
		textString = extras.getString(GlobalVariable.VERSE_TEXT);
		projectId = extras.getInt(GlobalVariable.PROJECT_ID);
		projectVerseId =  extras.getInt(GlobalVariable.PROJECT_VERSE_ID);


		// Setting Reference and Verse text

		TextView verseRefView = (TextView) findViewById(R.id.locate_word_verse_ref);
		verseRefView.setText(refString);

		wordToTouchView = (TextView) findViewById(R.id.locate_word_to_touch);
		chanceTextView = (TextView) findViewById(R.id.locate_word_chance_count_text);
		flowLayout = (FlowLayout) findViewById(R.id.locate_word_verse_text);

		// Button Setting

		Button doneButton = (Button) findViewById(R.id.locate_word_done_button);
		doneButton.setOnClickListener(this);

		Button helpButton = (Button) findViewById(R.id.locate_word_help_button);
		helpButton.setOnClickListener(this);



		/*** Start Locate Word Logic ***/


		// First showing activity

		if(savedInstanceState == null){
			startLocateWord();
		}


		// When it change it's orientation

		else{

			// Load the instance

			wordsArray = savedInstanceState.getStringArray(GlobalVariable.WORDS_ARRAY);
			wordTryCount = savedInstanceState.getInt(GlobalVariable.WORD_TRY_COUNT);
			mixedWordsIndex = savedInstanceState.getInt(GlobalVariable.MIXED_WORDS_INDEX);
			hiddenWords = savedInstanceState.getBooleanArray(GlobalVariable.HIDDEN_WORDS);
			num_of_hidden_words = savedInstanceState.getInt(GlobalVariable.NUM_OF_HIDDEN_WORDS);
			mixedWords = savedInstanceState.getStringArray(GlobalVariable.MIXED_WORDS);
			potential_points = savedInstanceState.getFloat(GlobalVariable.POTENTIAL_POINTS);
			word_worth = savedInstanceState.getFloat(GlobalVariable.WORD_WORTH);

			
			// Set Flow Layout

			setVerseTextButtonFlowLayout(wordsArray, hiddenWords, mixedWords, wordTryCount, mixedWordsIndex);
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

		// If it comes from memorize verse, no update.

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
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// Save the instance

		outState.putStringArray(GlobalVariable.WORDS_ARRAY, wordsArray);
		outState.putInt(GlobalVariable.WORD_TRY_COUNT, wordTryCount);
		outState.putInt(GlobalVariable.MIXED_WORDS_INDEX, mixedWordsIndex);
		outState.putBooleanArray(GlobalVariable.HIDDEN_WORDS, hiddenWords);
		outState.putInt(GlobalVariable.NUM_OF_HIDDEN_WORDS, num_of_hidden_words);
		outState.putStringArray(GlobalVariable.MIXED_WORDS, mixedWords);
		outState.putFloat(GlobalVariable.POTENTIAL_POINTS, potential_points);
		outState.putFloat(GlobalVariable.WORD_WORTH, word_worth);
	}



	/*** OnClickListener ***/


	@Override
	public void onClick(View v) {

		Intent i = null;
		int id = v.getId();		// Get ID which is own of Button itself


		// Act by buttons id

		if(id == R.id.locate_word_help_button){
			i = new Intent(this, LocateWordHelpDialog.class);
			i.putExtra(GlobalVariable.VERSE_REF, refString);
			i.putExtra(GlobalVariable.VERSE_TEXT, textString);
			startActivity(i);
		}

		else if (id ==R.id.locate_word_done_button) {
			finish();
		}



		/*** Verse Text buttons ***/


		else{

			// Find button itself view and string on the button.
			// Find Word displayed that needs to be located on screen.

			Button button = (Button) v;
			int buttonIndex = Integer.parseInt(button.getTag().toString());
			String buttonNormalizedWord = multiLanguageManager.normalizeString(this, wordsArray[buttonIndex]);
			String targetWord = wordToTouchView.getText().toString();


			// If user clicked on a valid location
			// Show the verse text position.

			if(buttonNormalizedWord.equals(targetWord)){

				// Init try count.
				// Increase words index.

				wordTryCount = MAX_TRIES;
				mixedWordsIndex++;
				potential_points = potential_points + word_worth;


				// Find the position of clicked button
				// Change the hidden words boolean from true to false
				// Show the text.
				// Set Disabled button

				setVerseTextButtonAnswered(button, wordsArray, buttonIndex);
				hiddenWords[buttonIndex] = false;


				// If user answered all verse hidden words, show the verse again with new blank.
				// Show progress dialog for letting user know the finish of one game.
				// Init the game.

				if(mixedWordsIndex >= num_of_hidden_words) {
					showOneMoreDialog((int) potential_points);
				}


				// if not finished yet
				// Move to next word

				else {
					wordToTouchView.setText(mixedWords[mixedWordsIndex]);
					chanceTextView.setText(""+wordTryCount);
				}
			}


			// Guessed location incorrectly and clicked on a hidden word.
			// Check whether should show answer.

			else {

				// Reduce chance and show the number of chance.

				chanceTextView.setText(""+(--wordTryCount));


				// If try MAX_TRIES times
				// Show the answer position and go to next word.

				if(wordTryCount <= 0) {

					// Have exceeded the error count.
					// Init try count.
					// Increase words index.
					// calculate current potential points

					wordTryCount = MAX_TRIES;
					mixedWordsIndex++;


					// Find the answer button
					// Show the answer text on the button.

					for(int k=0 ; k<wordsArray.length ; k++){
						if(targetWord.equals(multiLanguageManager.normalizeString(this, wordsArray[k])) && hiddenWords[k]==true) {
							setVerseTextButtonAnswered(wordViews.get(k), wordsArray, k);
							hiddenWords[k] = false;
							break;
						}
					}


					// If user answered all verse hidden words, show the verse again with new blank.
					// Show progress dialog for letting user know the finish of one game.
					// Init the game.

					if(mixedWordsIndex >= num_of_hidden_words) {
						showOneMoreDialog((int) potential_points);
					}


					// if not finished yet
					// Move to next word

					else {
						wordToTouchView.setText(mixedWords[mixedWordsIndex]);
						chanceTextView.setText(""+wordTryCount);
					}
				}
			}
		}
	}



	/*** Self Method ***/

	/**
	 * The First lobic of this game
	 * Make the button set and mixed word
	 * 
	 * @param
	 * @return
	 * 
	 */
	private void startLocateWord(){

		// Split verse string
		// Choose hidden words in verse

		wordsArray = textString.split(" ");
		wordTryCount = MAX_TRIES;
		mixedWordsIndex = 0;
		hiddenWords = GlobalMethod.chooseHiddenWords(this, wordsArray);
		num_of_hidden_words = GlobalMethod.getHiddenWordsNumber(hiddenWords);
		
		
		// Make Mixed words
		// randomize the mixed words array order
		
		mixedWords = new String[num_of_hidden_words];
		for(int i=0, k=0 ; i<wordsArray.length; i++)
			if(hiddenWords[i])
				mixedWords[k++] = multiLanguageManager.normalizeString(this, wordsArray[i]);

		for(int i=0 ; i<num_of_hidden_words ; i++) {
			String temp = mixedWords[i];
			int swapLocation = (int)(Math.random() * num_of_hidden_words);
			mixedWords[i] = mixedWords[swapLocation];
			mixedWords[swapLocation] = temp;
		}


		// For progress bar, measure the percentage of each word.

		potential_points = 0;
		word_worth = ((float) 100 / num_of_hidden_words);


		// Set Flow Layout

		setVerseTextButtonFlowLayout(wordsArray, hiddenWords, mixedWords, wordTryCount, mixedWordsIndex);
	}



	/**
	 * Set Flow Layout which includes buttons that point verse text.
	 * 
	 * @param wordsArray
	 * @param mixedWords
	 * @param wordCount
	 * @param wordTryCount
	 * @param currentWordIndex
	 * @param isEnabled
	 */
	private void setVerseTextButtonFlowLayout(String[] wordsArray, boolean[] hiddenWords, String[] mixedWords, int wordTryCount, int mixedWordsIndex){

		// Set word to touch view with upper case alphabet without any symbol.
		// The Locale parameter in toUpperCase below needs to be addressed when go multilingual

		wordToTouchView.setText(mixedWords[mixedWordsIndex]);
		chanceTextView.setText(""+wordTryCount);


		// Verse text setting
		// Create a TextView per word, set its text, and display it.
		// Text views are stored in a wordViews array list.

		wordViews = new ArrayList<Button>();
		flowLayout.removeAllViews();
		for (int i=0 ; i<wordsArray.length ; i++) {

			// Text View Setting
			// Intially, the texts are hidden.

			Button t = new Button(this);
			t.setTag(i);
			t.setPadding(10, 5, 10, 5);
			t.setTextColor(Color.BLACK);
			t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
			t.setBackgroundResource(R.drawable.verse_text_selector);
			if(!hiddenWords[i])
				setVerseTextButtonAnswered(t, wordsArray, i);
			

			// Add text view to word views list
			// Finally, add the text view to flow layout.

			wordViews.add(t);
			flowLayout.addView(t, new FlowLayout.LayoutParams(5, 5));


			// Register click event to the text view. 
			t.setOnClickListener(this);
		}
	}


	private void setVerseTextButtonAnswered(Button button, String[] wordsArray, int index){
		button.setBackgroundColor(getResources().getColor(R.color.gray));
		button.setText(wordsArray[index]);
		button.setEnabled(false);
		button.setVisibility(View.GONE);
		button.setVisibility(View.VISIBLE);
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
				startLocateWord();
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
