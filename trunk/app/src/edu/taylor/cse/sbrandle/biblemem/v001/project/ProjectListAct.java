package edu.taylor.cse.sbrandle.biblemem.v001.project;



import java.io.IOException;
import java.util.ArrayList;

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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.taylor.cse.sbrandle.biblemem.v001.R;
import edu.taylor.cse.sbrandle.biblemem.v001.database.DatabaseManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalFactory;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalManager;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;



/**
 * Project List Activity
 * It shows the project user registered
 * 
 * Date : May 28, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class ProjectListAct extends Activity  implements OnClickListener {

	private ListView projectListView;
	private ArrayList<ProjectObject> projectList;
	private DatabaseManager databaseManager;


	/*** Activity ***/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalManager.setCustomContentView(this, R.layout.project_list);
		findViewById(R.id.title_bar_text).setVisibility(View.GONE);
		

		// DB work

		try {
			databaseManager = GlobalFactory.getDatabaseManagerByLanguage(this);
			databaseManager.openDatabase();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		// Project list view and Butotn Setting

		projectListView = (ListView) findViewById(R.id.project_list_view);

		Button addProjectButton = (Button) findViewById(R.id.project_list_add_button);
		addProjectButton.setOnClickListener(this);

		Button backButton = (Button) findViewById(R.id.project_list_back_button);
		backButton.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		databaseManager.openDatabase();
		refreshProjectListView();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	protected void onPause() {
		super.onPause();
		databaseManager.closeDatabase();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}



	/*** OnClickListener ***/

	public void onClick(View v) {

		Intent i = null;
		int id = v.getId();		// Get ID which is own of Button itself


		// Act by buttons id

		if(id == R.id.project_list_add_button){
			i = new Intent(this, ProjectListAddDialog.class);
			i.putExtra(GlobalVariable.ADD_PROJECT, true);
			startActivity(i);
		}

		else if(id == R.id.project_list_back_button){
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
	private void refreshProjectListView() {
		projectList = databaseManager.getProjectList();
		projectListView.setAdapter(new ProjectAdapter(projectList));
	}



	/**
	 * Project List view Adapter
	 * It accepts custom list view rows
	 * 
	 * Date : May 28, 2013
	 * @author Seungmin Lee, rfrost77@gmail.com
	 * @since 1.0v
	 * @version "%I%, %G%"
	 *
	 */
	private class ProjectAdapter extends BaseAdapter{

		private ArrayList<ProjectObject> projectList;
		private LayoutInflater mInflater;

		private Context projectListAct = ProjectListAct.this;


		public ProjectAdapter(ArrayList<ProjectObject> projectList) {
			super();
			this.projectList = projectList;
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return projectList.size();
		}

		@Override
		public Object getItem(int position) {
			return projectList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}


		// create a new ImageView for each item referenced by the Adapter

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// Make a view

			View vi = convertView;
			if (convertView == null)
				vi = (View) mInflater.inflate(R.layout.project_list_row, null);


			// UI Setting

			final ProjectObject project = projectList.get(position);
			final int percent = project.getProjectPercent();
			
			TextView name = (TextView) vi.findViewById(R.id.project_list_row_name);
			name.setText(project.getProjectName());

			ProgressBar bar = (ProgressBar) vi.findViewById(R.id.project_list_row_progress_bar);
			bar.setProgress(percent);
			
			TextView progress = (TextView) vi.findViewById(R.id.project_list_row_progress_text);
			progress.setText(percent + "%");
			if(percent >= GlobalVariable.CRITERIA_OF_SUCCESS)
				progress.setTextColor(getResources().getColor(R.color.green));


			// DB and View Setting
			// Register long and short click listener to view

			vi.setOnLongClickListener(new OnLongClickListener(){
				
				@Override
				public boolean onLongClick(View v) {

					// Move to ProjectVerseAct

					Intent i = new Intent(projectListAct, ProjectListLongClickSettingDialog.class);
					i.putExtra(GlobalVariable.PROJECT_ID, project.getProjectId());
					i.putExtra(GlobalVariable.PROJECT_NAME, project.getProjectName());
					startActivity(i);
					return false;
				}
			});
			vi.setOnClickListener(new OnClickListener(){
				
				@Override
				public void onClick(View v) {

					// Move to ProjectVerseAct

					Intent i = new Intent(projectListAct, ProjectVerseAct.class);
					i.putExtra(GlobalVariable.PROJECT_ID, project.getProjectId());
					startActivity(i);
				}
			});

			return vi;
		}
	}
}
