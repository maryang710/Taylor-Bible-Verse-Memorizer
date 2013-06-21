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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalFactory;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;
import edu.taylor.cse.sbrandle.biblemem.v001.object.BookObject;


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
		setContentView(R.layout.verse_chooser);


		// DB work

		try {
			databaseManager = GlobalFactory.getDatabaseManagerByLanguage(this);
			databaseManager.openDatabase();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		// Get Intent from previous Activity as a bundle form.
		// Previous Activities are MainAct, Project...?
		// Intent includes the activity information.
		// Find Wheel view resources

		Bundle extras = getIntent().getExtras();
		projectId =  extras.getInt(GlobalVariable.PROJECT_ID);
		boolean memorize = extras.getBoolean(GlobalVariable.MEMORIZE);
		boolean project = extras.getBoolean(GlobalVariable.PROJECT);

		bookWheel = (WheelView) findViewById(R.id.verse_chooser_wheel_book);
		chapterWheel = (WheelView) findViewById(R.id.verse_chooser_wheel_chapter);
		verseWheel = (WheelView) findViewById(R.id.verse_chooser_wheel_verse);


		// Verse wheel setting

		verseWheel.setVisibleItems(5);
		verseWheel.setViewAdapter(new VerseAdapter(this, 31));


		// Chapter wheel setting

		chapterWheel.setVisibleItems(3);
		chapterWheel.setViewAdapter(new ChapterAdapter(this, 50));
		chapterWheel.addScrollingListener( new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {

			}
			public void onScrollingFinished(WheelView wheel) {
				if(!VerseChooserAct.this.isFinishing())
					updateVerseWheel(verseWheel, Integer.valueOf(bookWheel.getCurrentItem()), wheel.getCurrentItem());
			}
		});


		// Make book adapter
		// Register the adapter to book wheel
		// Book wheel setting

		books = databaseManager.getBookList();
		bookWheel.setVisibleItems(3);
		bookWheel.setViewAdapter(new BookAdapter(this, books));
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


		// Buttons Setting
		// This Activity decides which buttons will be shown
		// by Activity information got from previous Activity intent. 

		// if the previous Activity is MainAct, hide add project button.

		Button backbutton = (Button) findViewById(R.id.verse_chooser_back_button);
		backbutton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

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


		// if the previous Activity is Project...?, hide memorize button.

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
	}

	@Override
	protected void onResume() {
		super.onResume();
		databaseManager.openDatabase();
	}

	@Override
	protected void onPause() {
		super.onPause();
		databaseManager.closeDatabase();
	}



	/*** Self Method ***/


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

		int b = bookWheel.getCurrentItem();
		int c = chapterWheel.getCurrentItem();
		int v = verseWheel.getCurrentItem()+1; 
		String[] projectVerse = databaseManager.getRefVerse(b,c,v);


		// Convert Verse to String

		String refString = projectVerse[0];
		String textString = projectVerse[1];


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

		if(databaseManager.addVerseToProject(projectId, b, c, v, 0))
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



	/**
	 * Adapter for chapters
	 */
	private class ChapterAdapter extends AbstractWheelTextAdapter {

		//chapter numbers
		private String chapters[];


		protected ChapterAdapter(Context context, int chptotal) {
			super(context, R.layout.wheel_item, NO_RESOURCE);
			setItemTextResource(R.id.wheel_item_text);

			chapters = new String[chptotal];
			for ( int i =0; i < chptotal; i++)
				chapters[i] = (String.valueOf(i+1));
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			return super.getItem(index, cachedView, parent);
		}

		@Override
		public int getItemsCount() {
			return chapters.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return chapters[index];
		}
	}



	/**
	 * Adapter for Verses
	 */
	private class VerseAdapter extends AbstractWheelTextAdapter {

		//chapter numbers
		private String verses[];


		protected VerseAdapter(Context context, int vtotal) {
			super(context, R.layout.wheel_item, NO_RESOURCE);
			setItemTextResource(R.id.wheel_item_text);

			verses = new String[vtotal];
			for ( int i =0; i < vtotal; i++)
				verses[i] = (String.valueOf(i+1));
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			return super.getItem(index, cachedView, parent);
		}

		@Override
		public int getItemsCount() {
			return verses.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return verses[index];
		}
	}
}
