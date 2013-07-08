package edu.taylor.cse.sbrandle.biblemem.v001.widget.todayverse;

import java.io.IOException;
import java.util.Calendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RemoteViews;
import edu.taylor.cse.sbrandle.biblemem.v001.ActivityChooserAct;
import edu.taylor.cse.sbrandle.biblemem.v001.R;
import edu.taylor.cse.sbrandle.biblemem.v001.VerseObject;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseKJVEnManager;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalFactory;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;

/**
 * Today Verse Widget Provider
 * It shows today verse
 * User can memorize this when click the verse.
 * 
 * Date : Jun 25, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class TodayVerseWidget extends AppWidgetProvider {

	private DatabaseManager databaseManager;
	private RemoteViews remoteViews;
	private Context context;
	private AppWidgetManager appWidgetManager;


	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		
		// Get context, app widget manager
		// DB work

		this.context = context;
		this.appWidgetManager = appWidgetManager;
		try {
			databaseManager = new DatabaseKJVEnManager(context);
			databaseManager.populateDatabase();
			databaseManager = GlobalFactory.getDatabaseManagerByLanguage(context);
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		// Get Today

		Calendar cal = Calendar.getInstance();
		cal.setTime(cal.getTime());
		int today = cal.get(Calendar.DAY_OF_YEAR);


		// SharedPreferences work
		// If first, Set random verse and today date.
		// Else, Get verse.
		// If day is passed, Get new random Verse.

		SharedPreferences todayVerseNumberPref = context.getSharedPreferences(GlobalVariable.TODAY_VERSE_KEY, 0);
		int todayVerseNumber = todayVerseNumberPref.getInt(GlobalVariable.TODAY_VERSE_NUMBER, 0);


		// First load this widget

		if(todayVerseNumber == 0){
			todayVerseNumber = (int) (Math.random() * GlobalVariable.HOW_MANY_VERSES);
			SharedPreferences.Editor editor = todayVerseNumberPref.edit();
			editor.putInt(GlobalVariable.TODAY_VERSE_NUMBER, todayVerseNumber);
			editor.putInt(GlobalVariable.TODAY, today);
			editor.commit();
		}


		// Day is passed

		int todayPref = todayVerseNumberPref.getInt(GlobalVariable.TODAY, 0);
		if(todayPref != today){
			todayVerseNumber = (int) (Math.random() * GlobalVariable.HOW_MANY_VERSES);
			SharedPreferences.Editor editor = todayVerseNumberPref.edit();
			editor.putInt(GlobalVariable.TODAY_VERSE_NUMBER, todayVerseNumber);
			editor.putInt(GlobalVariable.TODAY, today);
			editor.commit();
		}


		remoteViews = new RemoteViews(context.getPackageName(), R.layout.today_verse_widget);


		// Perform this loop procedure for each App Widget that belongs to this provider

		for (int widgetId : appWidgetIds){

			

			// Show progress dialog
			// Dismiss Text
			// Set Verse Text in Thread
			
			remoteViews.setViewVisibility(R.id.today_verse_widget_ref, View.GONE);
			remoteViews.setViewVisibility(R.id.today_verse_widget_text, View.GONE);
			remoteViews.setViewVisibility(R.id.today_verse_widget_progress_bar, View.VISIBLE);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
			
			TodayVerseWidgetLoadVerseTask todayVerseWidgetLoadVerseTask = new TodayVerseWidgetLoadVerseTask();
			todayVerseWidgetLoadVerseTask.execute(todayVerseNumber, widgetId);
		}
	}



	/*** Self Class ***/

	/**
	 * Async Task Class
	 * It load verse in a thread.
	 * It shows loaded verse after thread work.
	 * 
	 * Date : Jun 26, 2013
	 * @author Seungmin Lee, rfrost77@gmail.com
	 * @since 1.0v
	 * @version "%I%, %G%"
	 *
	 */
	private class TodayVerseWidgetLoadVerseTask extends AsyncTask<Integer, Void, Void>{

		private String ref;
		private String text;
		private int widgetId;


		@Override
		protected Void doInBackground(Integer... args) {

			// Open Database
			// Get Verse Object
			// Get Ref and Verse Text from Verse object

			databaseManager.openDatabase();
			int todayVerseNumber = args[0];
			VerseObject verse = databaseManager.getVerse(todayVerseNumber);
			ref = databaseManager.getBook(verse.getBook()).getName() + " " + (verse.getChapter()+1) + ":" + verse.getVerse();
			text = verse.getContents();
			widgetId = args[1];
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			// dismiss progress dialog
			// Show Text
			// Close Database

			remoteViews.setViewVisibility(R.id.today_verse_widget_progress_bar, View.GONE);
			remoteViews.setViewVisibility(R.id.today_verse_widget_ref, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.today_verse_widget_text, View.VISIBLE);
			databaseManager.closeDatabase();


			// Set Click Listener to verse text view

			Intent intent = new Intent(context, ActivityChooserAct.class);
			intent.putExtra(GlobalVariable.VERSE_REF, ref);
			intent.putExtra(GlobalVariable.VERSE_TEXT, text);
			intent.putExtra(GlobalVariable.TODAY_VERSE_WIDGET, true);

			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			remoteViews.setOnClickPendingIntent(R.id.today_verse_widget_ref, pendingIntent);
			remoteViews.setOnClickPendingIntent(R.id.today_verse_widget_text, pendingIntent);


			// Set Today Verse
			// Update

			remoteViews.setTextViewText(R.id.today_verse_widget_ref, ref);
			remoteViews.setTextViewText(R.id.today_verse_widget_text, text);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
			super.onPostExecute(result);
		}
	}
}
