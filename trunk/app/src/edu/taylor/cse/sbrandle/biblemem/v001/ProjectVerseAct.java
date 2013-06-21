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
 * Project Verse List Activity
 * It shows verses in the project user clicked
 * 
 * Date : May 31, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class ProjectVerseAct extends Activity  implements OnClickListener{

	private DatabaseManager databaseManager;
	private ListView projectVerseListView;
	private ArrayList<ProjectVerseObject> projectVerseList;

	private int projectId;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_verse_list);


		// DB work

		try {
			databaseManager = GlobalFactory.getDatabaseManagerByLanguage(this);
			databaseManager.openDatabase();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		// Get project ID from previous Activity Intent
		// Set Activity Title with projectName

		Bundle extras = getIntent().getExtras();
		projectId = extras.getInt(GlobalVariable.PROJECT_ID);
		setTitle(databaseManager.getProject(projectId).getProjectName());


		// Project verse list view and Butotn Setting

		projectVerseListView = (ListView) findViewById(R.id.project_verse_list_view);

		Button addButton = (Button) findViewById(R.id.project_verse_add_button);
		addButton.setOnClickListener(this);

		Button backButton = (Button) findViewById(R.id.project_verse_back_button);
		backButton.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		databaseManager.openDatabase();
		refreshProjectVerseListView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		databaseManager.closeDatabase();
	}



	/*** OnClickListener ***/

	@Override
	public void onClick(View v) {

		Intent i = null;
		int id = v.getId();		// Get ID which is own of Button itself


		// Act by buttons id

		if(id == R.id.project_verse_add_button){
			i = new Intent(this, VerseChooserAct.class);
			i.putExtra(GlobalVariable.PROJECT, true);
			i.putExtra(GlobalVariable.PROJECT_ID, projectId);
			startActivity(i);
		}

		else if(id == R.id.project_verse_back_button){
			finish();
		}
	}



	/*** Self Method or Class ***/


	/**
	 * Refresh Project ListView
	 * It is called when activity begin or user change project list
	 * 
	 * @param
	 * @return
	 * 
	 */
	private void refreshProjectVerseListView(){
		projectVerseList = databaseManager.getProjectVerseListInProject(projectId);
		Collections.sort(projectVerseList, new ProjectVerseComparator());
		projectVerseListView.setAdapter(new ProjectVerseAdapter(projectVerseList));
	}



	/**
	 * Project Verset List view Adapter
	 * It accepts custom list view rows
	 * 
	 * 
	 * Date : May 31, 2013
	 * @author Seungmin Lee, rfrost77@gmail.com
	 * @since 1.0v
	 * @version "%I%, %G%"
	 *
	 */
	public class ProjectVerseAdapter extends BaseAdapter {

		private ArrayList<ProjectVerseObject> projectVerseList;
		private LayoutInflater mInflater;

		private Context projectVerseAct = ProjectVerseAct.this;


		public ProjectVerseAdapter(ArrayList<ProjectVerseObject> projectVerseList) {
			super();
			this.projectVerseList = projectVerseList;
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return projectVerseList.size();
		}

		public Object getItem(int position) {
			return projectVerseList.get(position);
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
			
			final ProjectVerseObject projectVerse = projectVerseList.get(position);
			final VerseObject verse = databaseManager.getVerse(projectVerse.getVerseId());
			final String reference = databaseManager.getBook(verse.getBook()).getName() + " " + (verse.getChapter()+1) + ":" + verse.getVerse();
			final String text = verse.getContents();
			
			TextView name = (TextView) vi.findViewById(R.id.project_verse_list_row_name);
			name.setText(reference);

			TextView description = (TextView) vi.findViewById(R.id.project_verse_list_row_description);
			description.setText(text);

			ImageView whetherImage = (ImageView) vi.findViewById(R.id.project_verse_list_row_whether);
			if(projectVerse.getPercent() != 100)
				whetherImage.setImageResource(R.drawable.project_verse_list_row_not);
			else
				whetherImage.setImageResource(R.drawable.project_verse_list_row_ok);


			// DB and View Setting
			// Register long and short click listener to view

			vi.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {

					// Move to ProjectVerseLongClickSettingDialog

					Intent i = new Intent(projectVerseAct, ProjectVerseLongClickSettingDialog.class);
					i.putExtra(GlobalVariable.VERSE_REF, reference);
					i.putExtra(GlobalVariable.VERSE_TEXT, text);
					i.putExtra(GlobalVariable.PROJECT_ID, projectId);
					i.putExtra(GlobalVariable.PROJECT_VERSE_ID, projectVerse.getProjectVerseId());
					i.putExtra(GlobalVariable.PROJECT_VERSE, true);
					startActivity(i);
					return false;
				}

			});
			vi.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// Move to ActivityChooserAct

					Intent i = new Intent(projectVerseAct, ActivityChooserAct.class);
					i.putExtra(GlobalVariable.VERSE_REF, reference);
					i.putExtra(GlobalVariable.VERSE_TEXT, text);
					i.putExtra(GlobalVariable.PROJECT_ID, projectId);
					i.putExtra(GlobalVariable.PROJECT_VERSE_ID, projectVerse.getProjectVerseId());
					startActivity(i);
				}
			});

			return vi;
		}
	}
}
