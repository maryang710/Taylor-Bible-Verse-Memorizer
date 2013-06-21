package edu.taylor.cse.sbrandle.biblemem.v001;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;
import edu.taylor.cse.sbrandle.biblemem.v001.dialog.ProjectVerseLongClickSettingDialog;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalFactory;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;
import edu.taylor.cse.sbrandle.biblemem.v001.global.ProjectVerseComparator;
import edu.taylor.cse.sbrandle.biblemem.v001.object.ProjectVerseObject;
import edu.taylor.cse.sbrandle.biblemem.v001.object.VerseObject;

/**
 * It is the Review Manager Act for helping user to review
 * 
 * Date : Jun 5, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class ReviewManagerAct extends Activity implements OnClickListener {

	private DatabaseManager databaseManager;
	private ListView reveiwManagerListView;
	private ArrayList<ProjectVerseObject> reveiwList;


	/*** Activity ***/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review_manager);


		// DB work
		try {
			databaseManager = GlobalFactory.getDatabaseManagerByLanguage(this);
			databaseManager.openDatabase();
		} catch (IOException e) {
			e.printStackTrace();
		}


		// Project verse list view and Butotn Setting

		reveiwManagerListView = (ListView) findViewById(R.id.review_manager_list_view);

		Button backButton = (Button) findViewById(R.id.review_manager_back_button);
		backButton.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		databaseManager.openDatabase();
		refreshReviewManagerListView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		databaseManager.closeDatabase();
	}



	/*** OnClickListener***/

	@Override
	public void onClick(View v) {

		//		Intent i = null;
		int id = v.getId();		// Get ID which is own of Button itself


		// Call Activity by its button ID

		if(id == R.id.review_manager_back_button){
			finish();
		}
	}



	/*** Self Method or Class ***/


	/**
	 * Refresh Review Manager ListView
	 * It calls already done verses
	 * 
	 * @param
	 * @return
	 * 
	 */
	private void refreshReviewManagerListView(){
		reveiwList = databaseManager.getDoneProjectVerseIdList();
		Collections.sort(reveiwList, new ProjectVerseComparator());
		reveiwManagerListView.setAdapter(new ReviewManagerAdapter(reveiwList));
	}



	/**
	 * Review Manager List view Adapter
	 * It accepts custom list view rows
	 * 
	 * 
	 * Date : May 31, 2013
	 * @author Seungmin Lee, rfrost77@gmail.com
	 * @since 1.0v
	 * @version "%I%, %G%"
	 *
	 */
	public class ReviewManagerAdapter extends BaseAdapter {

		private ArrayList<ProjectVerseObject> reveiwList;
		private LayoutInflater mInflater;

		private Context reviewManagerAct = ReviewManagerAct.this;


		public ReviewManagerAdapter(ArrayList<ProjectVerseObject> reveiwList) {
			super();
			this.reveiwList = reveiwList;
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return reveiwList.size();
		}

		public Object getItem(int position) {
			return reveiwList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}


		// Create a new ImageView for each item referenced by the Adapter

		public View getView(int position, View convertView, ViewGroup parent) {

			// Make a view

			View vi = convertView;
			if (convertView == null)
				vi = (View) mInflater.inflate(R.layout.project_verse_list_row, null);


			// UI Setting

			final ProjectVerseObject projectVerse = reveiwList.get(position);
			final VerseObject verse = databaseManager.getVerse(projectVerse.getVerseId());
			final String reference = databaseManager.getBook(verse.getBook()).getName() + " " + (verse.getChapter()+1) + ":" + verse.getVerse();
			final String text = verse.getContents();
			final int verseId = verse.get_id();

			TextView name = (TextView) vi.findViewById(R.id.project_verse_list_row_name);
			name.setText(reference);

			TextView description = (TextView) vi.findViewById(R.id.project_verse_list_row_description);
			description.setText(text);

			ImageView whetherImage = (ImageView) vi.findViewById(R.id.project_verse_list_row_whether);
			whetherImage.setImageResource(R.drawable.project_verse_list_row_ok);


			// DB and View Setting
			// Register long and short click listener to view

			vi.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {

					// Move to ProjectVerseLongClickSettingDialog

					Intent i = new Intent(reviewManagerAct, ProjectVerseLongClickSettingDialog.class);
					i.putExtra(GlobalVariable.VERSE_REF, reference);
					i.putExtra(GlobalVariable.VERSE_TEXT, text);
					i.putExtra(GlobalVariable.VERSE_ID, verseId);
					i.putExtra(GlobalVariable.REVIEW, true);
					startActivity(i);
					return false;
				}

			});
			vi.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// Move to ActivityChooserAct

					Intent i = new Intent(reviewManagerAct, ActivityChooserAct.class);
					i.putExtra(GlobalVariable.VERSE_REF, reference);
					i.putExtra(GlobalVariable.VERSE_TEXT, text);
					startActivity(i);
				}
			});

			return vi;
		}
	}
}
