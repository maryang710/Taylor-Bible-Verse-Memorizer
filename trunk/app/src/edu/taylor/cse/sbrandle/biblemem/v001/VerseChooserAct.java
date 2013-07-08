package edu.taylor.cse.sbrandle.biblemem.v001;

import java.io.IOException;
import java.util.ArrayList;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalFactory;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;
import edu.taylor.cse.sbrandle.biblemem.v001.setting.SettingBibleEditionAct;


/**
 * It's an Activity which is for choosing verse what a user is gonna memorize.
 * It uses Wheel Widget from kankan.wheel.widget package.
 * 
 * Date : May 20, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class VerseChooserAct extends Activity {

	private WheelView bookWheel; 
	private WheelView chapterWheel;
	private WheelView verseWheel; 

	private int projectId;

	private DatabaseManager databaseManager;

	private ArrayList<BookObject> books;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalManager.setCustomContentView(this, R.layout.verse_chooser);


		// DB work

		try {
			databaseManager = GlobalFactory.getDatabaseManagerByLanguage(this);
			databaseManager.openDatabase();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		// Get Intent from previous Activity as a bundle form.
		// Intent includes the activity information.

		Bundle extras = getIntent().getExtras();
		projectId =  extras.getInt(GlobalVariable.PROJECT_ID);
		boolean memorize = extras.getBoolean(GlobalVariable.MEMORIZE);
		boolean project = extras.getBoolean(GlobalVariable.PROJECT);


		// Find Wheel view resources

		bookWheel = (WheelView) findViewById(R.id.verse_chooser_wheel_book);
		chapterWheel = (WheelView) findViewById(R.id.verse_chooser_wheel_chapter);
		verseWheel = (WheelView) findViewById(R.id.verse_chooser_wheel_verse);


		// Buttons Setting
		// This Activity decides which buttons will be shown
		// by Activity information got from previous Activity intent. 


		Button backbutton = (Button) findViewById(R.id.verse_chooser_back_button);
		backbutton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});


		// if the previous Activity is MainAct, hide add project button.
		// Hide Title Text

		if(memorize){
			Button button = (Button) findViewById(R.id.verse_chooser_add_verse_to_project_button);
			button.setVisibility(View.GONE);

			Button memorizeButton = (Button) findViewById(R.id.verse_chooser_memorize_button);
			memorizeButton.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					startActivityChooser();
				}
			});
		}


		// if the previous Activity is Project, hide memorize button.
		// Set title text with project name

		else if(project){
			Button button = (Button) findViewById(R.id.verse_chooser_memorize_button);
			button.setVisibility(View.GONE);

			Button addVerseToProjectBtn = (Button) findViewById(R.id.verse_chooser_add_verse_to_project_button);
			addVerseToProjectBtn.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					addVerseThisVerse();
				}
			});

			backbutton.setText(getResources().getString(R.string.done_label));
		}
		
		
		// Get Book List
		// Book Widget List Setting
		// Wheel Event Setting
		
		books = databaseManager.getBookList();
		bookWheel.setViewAdapter(new BookAdapter(this, books));	
		
		chapterWheel.addScrollingListener( new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {

			}
			public void onScrollingFinished(WheelView wheel) {
				if(!VerseChooserAct.this.isFinishing())
					updateVerseWheel(verseWheel, Integer.valueOf(bookWheel.getCurrentItem()), wheel.getCurrentItem());
			}
		});
		
		bookWheel.addScrollingListener( new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {

			}
			public void onScrollingFinished(WheelView wheel) {
				if(!VerseChooserAct.this.isFinishing()){
					updateChapterWheel(chapterWheel, books.get(wheel.getCurrentItem()).getChapterCount());
					updateVerseWheel(verseWheel, wheel.getCurrentItem(), Integer.valueOf(chapterWheel.getCurrentItem()));
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			databaseManager = GlobalFactory.getDatabaseManagerByLanguage(this);
			databaseManager.openDatabase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		refreshVerseListWidget();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	protected void onPause() {
		super.onPause();
		databaseManager.closeDatabase();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edition, menu);
		return true;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edition:
			Intent intent = new Intent(this, SettingBibleEditionAct.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return true;
	}



	/*** Self Method ***/

	/**
	 * Refresh Widget by phone's bible edition setting
	 * 
	 * @param
	 * @return
	 * 
	 */
	private void refreshVerseListWidget(){

		// Set Title Bar Text

		TextView titleBarText = (TextView) findViewById(R.id.title_bar_text);
		titleBarText.setText(databaseManager.getBibleEditionName());

		
		// Chapter and Verst Widget List Setting
		
		updateChapterWheel(chapterWheel, books.get(bookWheel.getCurrentItem()).getChapterCount());
		updateVerseWheel(verseWheel, bookWheel.getCurrentItem(), Integer.valueOf(chapterWheel.getCurrentItem()));
	}



	/**
	 * The method which is called When memorize button is clicked.
	 * Get current wheel position
	 * Get verse information by current wheel position
	 * Make Intent includes verse String
	 * Move to ActivityChooserAct
	 * 
	 * @param
	 * @return
	 * 
	 */
	private void startActivityChooser() {

		// Get current wheel position
		// Get verse from DB by current wheel position
		// Get Reference and verse text from verse object

		int b = bookWheel.getCurrentItem();
		int c = chapterWheel.getCurrentItem();
		int v = verseWheel.getCurrentItem()+1; 
		VerseObject verse = databaseManager.getVerse(databaseManager.getVerseIdFromWidgetIndex(b, c, v));
		String refString = databaseManager.getBook(verse.getBook()).getName() + " " + (verse.getChapter()+1) + ":" + verse.getVerse();
		String textString = verse.getContents();


		// Set Intent to ActivityChoosetAct

		Intent intent = new Intent(this, ActivityChooserAct.class);
		intent.putExtra(GlobalVariable.VERSE_REF, refString);
		intent.putExtra(GlobalVariable.VERSE_TEXT, textString);
		startActivity(intent);
	}



	/**
	 * The method which is called When add verse to project button is clicked.
	 * Get current wheel position
	 * Get verse information by current wheel position
	 * Make Intent includes verse String
	 * Add select project to DB
	 * 
	 * @param
	 * @return
	 * 
	 */
	public void addVerseThisVerse(){

		// Get current wheel position
		// Get verse from DB by current wheel position

		int b = bookWheel.getCurrentItem();
		int c = chapterWheel.getCurrentItem();
		int v = verseWheel.getCurrentItem()+1;


		// Add project to DB
		// Show Toast text by result.
		// If the verse is already in the project, fail.
		// Otherwise, success.

		if(databaseManager.addVerseToProject(projectId, databaseManager.getVerseIdFromWidgetIndex(b, c, v), 0))
			Toast.makeText(this, getResources().getString(R.string.verse_chooser_add_verse_toast_success), Toast.LENGTH_LONG).show();
		else
			Toast.makeText(this, getResources().getString(R.string.verse_chooser_add_verse_toast_fail), Toast.LENGTH_LONG).show();
	}



	/**
	 * Update chapter wheel by users hand scrolling
	 * 
	 * @param wheel
	 * @param total
	 */
	private void updateChapterWheel(WheelView wheel, int total) {

		// Get String which is in current wheel position

		String[] totalItems = new String[total];
		for (int i =0; i < total; i++)
			totalItems[i] = (String.valueOf(i+1));


		// Make Adapter
		// Set String and position

		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this, totalItems);
		adapter.setTextSize(18);
		wheel.setViewAdapter(adapter);
		wheel.setCurrentItem(0);
	}



	/**
	 * Update verse wheel by users hand scrolling
	 * 
	 * @param wheel
	 * @param bid
	 * @param chpid
	 */
	private void updateVerseWheel(WheelView wheel, int bid, int chpid) {

		// Get total verse Strings from DB 

		int versetotal = databaseManager.getVerseCount(bid, chpid);
		String[] totalItems = new String[versetotal];
		for (int i =0; i < versetotal; i++)
			totalItems[i] = (String.valueOf(i+1));


		// Make Adapter
		// Set String and position

		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this, totalItems);
		adapter.setTextSize(18);
		wheel.setViewAdapter(adapter);
		wheel.setCurrentItem(0); 
	}



	/**
	 * Adapter for books
	 */
	private class BookAdapter extends AbstractWheelTextAdapter {

		private ArrayList<BookObject> books;


		protected BookAdapter(Context context, ArrayList<BookObject> books){
			super(context, R.layout.wheel_book_item, NO_RESOURCE);
			setItemTextResource(R.id.wheel_book_item_text);
			this.books = books;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			return super.getItem(index, cachedView, parent);
		}

		@Override
		public int getItemsCount() {
			return books.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return books.get(index).getName();
		}
	}
}
