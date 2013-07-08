/** DatabaseManager is responsible for executing the queries to the database. 
 * This file access the created database file by the DatabaseCreator class. To maintain each activity modular, 
 * SQL statments should be placed here and access via an instance of this class inside the corresponding activity.  
 * @author Eliezer Rodriguez
 * @version 1.0 Febuary 11, 2013
 */
package edu.taylor.cse.sbrandle.biblemem.v001.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import edu.taylor.cse.sbrandle.biblemem.v001.BookObject;
import edu.taylor.cse.sbrandle.biblemem.v001.VerseObject;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalFactory;
import edu.taylor.cse.sbrandle.biblemem.v001.global.GlobalVariable;
import edu.taylor.cse.sbrandle.biblemem.v001.project.ProjectObject;
import edu.taylor.cse.sbrandle.biblemem.v001.project.ProjectVerseObject;

public abstract class DatabaseManager extends SQLiteOpenHelper {

	protected SQLiteDatabase db;

	private static final String DATABASE_PATH_INT = "/databases";
	private static final String DATABASE_NAME = "versememorizer.db";
	public static final int DATABASE_VERSION = 9;

	private Context context;



	/*** Constructor ***/


	/** This sets up an instance of LocalSQLiteHelper.
	 * @param context Give the context of this application so that we call and create the database for this application.
	 * @throws IOException 
	 */
	public DatabaseManager(Context context) throws IOException {
		super(context, getDatabaseFileAbsolutePath(context), null, DATABASE_VERSION);
		this.context = context;
	}

	public void openDatabase(){
		db = getWritableDatabase();
	}

	public void closeDatabase(){
		db.close();
	}

	/** This function will delete the database file
	 * @param context
	 * @return
	 */
	public boolean deleteDatabaseFile(Context context){
		return getDatabaseFile(context).delete();
	}

	/** This function returns the database file
	 * @param context
	 * @return
	 */
	private static File getDatabaseFile(Context context){
		String databaseFolderPath = context.getFilesDir().getPath()+ DATABASE_PATH_INT;
		File databaseFolder = new File(databaseFolderPath);
		databaseFolder.mkdirs();
		return new File(databaseFolderPath, DATABASE_NAME);
	}

	/** This function returns the absolute path where the database file should be saved.
	 * @param context Give the context of this application so that we call and create the database for this application.
	 * @return This will return the absolute path where the database file should be saved.
	 */
	public static String getDatabaseFileAbsolutePath(Context context){
		return getDatabaseFile(context).getAbsolutePath();
	}



	/**
	 * populatDatabase is responsible for scanning the assets folder and finding split database files *.db of 1MB or less
	 * and putting them back together. 
	 * 
	 * @param context
	 * @return
	 * @throws IOException
	 */
	public boolean populateDatabase() throws IOException {

		// If there is DB file in file storage, return false
		// Otherwise, Make the DB file

		File databaseFile = getDatabaseFile(context);
		if(!databaseFile.createNewFile()){
			return false;
		}
		else{
			// Get DB files from asset folder
			// Make variables related with output stream to the database file 

			byte[] buffer = new byte[1024];
			int length = 0;
			AssetManager assets = context.getAssets();
			OutputStream myOutput = new FileOutputStream(databaseFile);


			// Make input stream with a DB file
			// Read input stream and write to output stream

			InputStream myInput = assets.open(DATABASE_NAME);
			while((length=myInput.read(buffer)) > 0)
				myOutput.write(buffer, 0, length);
			myInput.close();


			// Export the output stream
			// The result is only one DB file

			myOutput.flush();
			myOutput.close();


			// Set Language code in DB
			// Set First Difficulty for easy

			GlobalFactory.getMultiLanguageManager().insertBibleEditionNameCode(context);
			SharedPreferences settingDifficulty = context.getSharedPreferences(GlobalVariable.DIFFICULTY_KEY, 0);
			SharedPreferences.Editor editor = settingDifficulty.edit();
			editor.putString(GlobalVariable.DIFFICULTY, GlobalVariable.EASY_KEY);
			editor.commit();
			return true;
		}
	}



	/*** SQL Methods ***/


	/*** Insert ***/

	public void createProject(String name){
		SQLiteStatement sql = db.compileStatement
				("INSERT INTO Project (name) VALUES(?);");
		sql.bindString(1, name);

		sql.execute();
	}



	/**
	 * Insert verse to project
	 * you have to fix it if you try to add new language version
	 * 
	 * @param projectId
	 * @param bid
	 * @param chpid
	 * @param vnum
	 * @param versePercent
	 * @return
	 */
	public boolean addVerseToProject(int projectId, int verseId, int versePercent){

		// Get verse id from index that user selected
		// Check whether it exists or not
		
		String sql = "SELECT DISTINCT project_verse_id FROM ProjectVerse WHERE project_id="+  projectId + " AND verse_id=" + verseId + ";";
		Cursor row = db.rawQuery(sql, null);


		// It it is, return false to prevent double inserting

		if ((row.getCount() > 0) && (!row.isClosed())) {
			row.close();
			return false;
		}


		// otherwise, insert to all language project verse

		else{
			row.close();
			SQLiteStatement  sqlInsert = db.compileStatement
					("INSERT INTO ProjectVerse (project_id, verse_id, percent) VALUES(?, ?, ?);");
			sqlInsert.bindString(1, String.valueOf(projectId));
			sqlInsert.bindString(2, String.valueOf(verseId));
			sqlInsert.bindString(3, "0");
			sqlInsert.execute();

			updateProjectScore(projectId);
			return true;
		}
	}



	/*** Select ***/

	/**
	 * Get verse id from index that user selected
	 * 
	 * @param projectId
	 * @param bid
	 * @param chpid
	 * @param vnum
	 * @param versePercent
	 * @return
	 */
	public int getVerseIdFromWidgetIndex(int bid, int chpid, int vnum){

		// Select Verse Set from KJV, KRV

		String sql = "SELECT DISTINCT _id FROM KJV WHERE book=" + bid + " AND chapter=" + chpid + " AND verse=" + vnum;
		Cursor row = db.rawQuery(sql, null);

		int verseId = 0;
		if ((row.getCount() > 0) && (!row.isClosed())) {
			row.moveToFirst();
			verseId = row.getInt(0);
		}
		row.close();
		return verseId;
	}



	/**
	 * getVerseTotalFomBookIDANDChapterID returns an integer representing the total number of Verses found in a chapter
	 * @param bid this is the book ID 
	 * @param chpid this is the chapter ID
	 * @return
	 */
	public int getVerseCount(int bid, int chpid){

		String sql = " Select verse_count FROM VerseCount VC WHERE VC.chapter_id ="
				+ chpid + " AND VC.book_id=" + bid;
		Cursor row = db.rawQuery(sql, null);

		int verseTotal = 0;
		if ((row.getCount() > 0) && (!row.isClosed())) {
			row.moveToFirst();
			verseTotal = row.getInt(0);
		} 

		row.close();
		return verseTotal;
	}



	public ProjectObject getProject(int projectId){

		String sql = "SELECT DISTINCT project_id, name, percent FROM Project WHERE project_id=" + projectId + ";";
		Cursor row = db.rawQuery(sql, null);

		ProjectObject project = null;
		if ((row.getCount() > 0) && (!row.isClosed())) {
			row.moveToFirst();
			project = new ProjectObject(row.getInt(0), row.getString(1), row.getInt(2));
		} 

		row.close();
		return project;
	}



	public ArrayList<ProjectObject> getProjectList(){

		String sql = "SELECT DISTINCT project_id, name, percent FROM Project;";
		Cursor row = db.rawQuery(sql, null);

		ArrayList<ProjectObject> projectList = new ArrayList<ProjectObject>();
		if ((row.getCount() > 0) && (!row.isClosed())) {
			row.moveToFirst();
			do {
				projectList.add(new ProjectObject(row.getInt(0), row.getString(1), row.getInt(2)));
			} while(row.moveToNext());
		} 

		row.close();
		return projectList;
	}



	public ProjectVerseObject getProjectVerse(int projectVerseId){
		String sql = "SELECT DISTINCT project_verse_id, project_id, verse_id, percent FROM ProjectVerse WHERE project_verse_id=" + projectVerseId + ";";
		Cursor row = db.rawQuery(sql, null);

		ProjectVerseObject projectVerse = null;
		if ((row.getCount() > 0) && (!row.isClosed())) {
			row.moveToFirst();
			projectVerse = new ProjectVerseObject(row.getInt(0), row.getInt(1), row.getInt(2), row.getInt(3));
		} 
		row.close();
		return projectVerse;
	}



	public ArrayList<ProjectVerseObject> getProjectVerseListInProject(int projectId){
		String sql = "SELECT DISTINCT project_verse_id, project_id, verse_id, percent FROM ProjectVerse WHERE project_id=" + projectId + ";";
		Cursor row = db.rawQuery(sql, null);

		ArrayList<ProjectVerseObject> projectVerseList = new ArrayList<ProjectVerseObject>();
		if ((row.getCount() > 0) && (!row.isClosed())) {
			row.moveToFirst();
			do {
				projectVerseList.add(new ProjectVerseObject(row.getInt(0), row.getInt(1), row.getInt(2), row.getInt(3)));
			} while(row.moveToNext());
		} 
		row.close();
		return projectVerseList;
	}



	public ArrayList<ProjectVerseObject> getDoneProjectVerseIdList(){
		String sql = "SELECT DISTINCT project_verse_id, project_id, verse_id, percent FROM ProjectVerse WHERE percent=100;";
		Cursor row = db.rawQuery(sql, null);

		ArrayList<ProjectVerseObject> projectVerseList = new ArrayList<ProjectVerseObject>();
		if ((row.getCount() > 0) && (!row.isClosed())) {
			row.moveToFirst();
			do {
				projectVerseList.add(new ProjectVerseObject(row.getInt(0), row.getInt(1), row.getInt(2), row.getInt(3)));
			} while(row.moveToNext());
		} 
		row.close();
		return projectVerseList;
	}



	/*** Update, Delete ***/

	public void renameProject(String name, int projectId){
		SQLiteStatement sql = db.compileStatement
				("UPDATE Project SET name='" + name + "' WHERE project_id=" + projectId + ";");
		sql.execute();
	}



	public void removeProject(int projectId){
		SQLiteStatement sql = db.compileStatement
				("DELETE FROM Project WHERE project_id=" + projectId);
		sql.execute();

		sql = db.compileStatement
				("DELETE FROM ProjectVerse WHERE project_id=" + projectId);
		sql.execute();
	}



	public void removeVerseFromProject(int projectId, int projectVerseId){
		SQLiteStatement sql = db.compileStatement
				("DELETE FROM ProjectVerse WHERE project_verse_id="+ projectVerseId);
		sql.execute();

		updateProjectScore(projectId);
	}



	public void updateVerseScore(int projectId, int projectVerseId, int percent){
		SQLiteStatement sql = db.compileStatement
				("UPDATE ProjectVerse SET percent=" + percent + " WHERE project_verse_id=" + projectVerseId + ";");
		sql.execute();

		updateProjectScore(projectId);
	}



	public void updateProjectVerseScore(int projectId, int percent){
		SQLiteStatement sql = db.compileStatement
				("UPDATE ProjectVerse SET percent=" + percent + " WHERE project_id=" + projectId + ";");
		sql.execute();

		updateProjectScore(projectId);
	}



	public void updateSpecifiedVerseScore(int verseId, int percent){
		SQLiteStatement sqlUpdate = db.compileStatement
				("UPDATE ProjectVerse SET percent=" + percent + " WHERE verse_id=" + verseId + ";");
		sqlUpdate.execute();

		String sqlSelect = "SELECT DISTINCT project_id "
				+ "FROM ProjectVerse WHERE verse_id=" + verseId + ";";
		Cursor row = db.rawQuery(sqlSelect, null);

		if ((row.getCount() > 0) && (!row.isClosed())) {
			row.moveToFirst();
			do {
				updateProjectScore(row.getInt(0));
			} while(row.moveToNext());
		} 
		row.close();
	}



	public void updateProjectScore(int projectId){
		SQLiteStatement sql = db.compileStatement
				("UPDATE Project SET percent=(SELECT SUM(percent)/COUNT(percent) as AVG FROM ProjectVerse" +
						" WHERE project_id=" + projectId + ") WHERE project_id =" + projectId + ";");
		sql.execute();
	}



	/*** About Language ***/

	public String getBibleEditionCode(){
		String sql = "SELECT bible_edition_code FROM BibleEdition;";
		Cursor row = db.rawQuery(sql, null);

		String code = null;
		if ((row.getCount() > 0) && (!row.isClosed())) {
			row.moveToFirst();
			code = row.getString(0);
		}
		row.close();
		return code;
	}



	public String getBibleEditionName(){
		String sql = "SELECT bible_edition_name FROM BibleEdition;";
		Cursor row = db.rawQuery(sql, null);

		String name = null;
		if ((row.getCount() > 0) && (!row.isClosed())) {
			row.moveToFirst();
			name = row.getString(0);
		}
		row.close();
		return name;
	}



	public void insertBibleEditionNameCode(String bibleEditionName, String bibleEditionCode){
		SQLiteStatement sql = db.compileStatement("INSERT INTO BibleEdition (bible_edition_name, bible_edition_code) VALUES(?, ?);");
		sql.bindString(1, bibleEditionName);
		sql.bindString(2, bibleEditionCode);
		sql.execute();
	}



	public void setBibleEditionNameCode(String bibleEditionName, String bibleEditionCode){
		SQLiteStatement sql = db.compileStatement("UPDATE BibleEdition SET bible_edition_name='" + bibleEditionName + "', bible_edition_code='" + bibleEditionCode + "';");
		sql.execute();
	}



	/*** Override ***/

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}



	/*** abstract Methods ***/

	public abstract ArrayList<BookObject> getBookList();
	public abstract BookObject getBook(int bookId);
	public abstract VerseObject getVerse(int _id);
}