/** This DatabaseCreator is responsible of creating the initial database file that the application will use.
 * The database file is created via a number of JSON files containing the KJV version of the Bible which 
 * I  downloaded from the public domain. This file also is responsible for putting back together *.db files
 * that may be used from an already created database which had been split in 1MB pieces into the assets folder.
 * @author Eliezer Rodriguez
 * @version 1.0 Febuary 11, 2013
 */
package edu.taylor.cse.sbrandle.biblemem.v001.database;

import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * It's for making new big Database file for first once.
 * You may use it for making a Database file from other bible json files
 * You need to fix some code for explicitly calling it.
 * It's only for developer.
 * You have to uncomment of all method and variables to use.
 * 
 * Date : May 29, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class DatabaseKJVEnCreator extends SQLiteOpenHelper {

//	private SQLiteDatabase db;

//	private int bookNum;
//	private int currentInsertBook;
//	private int currentInsertChapter;

	private final String CREATE_STATS_TABLE = "CREATE TABLE Stats(stat_id INTEGER PRIMARY KEY AUTOINCREMENT, activity_name TEXT, misses INTEGER, score_percent, INTEGER, possible_points INTEGER);";
	private final String CREATE_ABBREVIATIONS_TABLE = "CREATE TABLE Abbreviations(abbreviation_id INTEGER PRIMARY KEY AUTOINCREMENT, book_id INTEGER NOT NULL, abbreviation_name TEXT);";
	private final String CREATE_BOOK_TABLE = "CREATE TABLE Book(book_id INTEGER PRIMARY KEY, testament TEXT,name TEXT, alternate_name TEXT);";
	private final String CREATE_CHAPTER_TABLE = "CREATE TABLE Chapter(chapter_id INTEGER PRIMARY KEY AUTOINCREMENT, chapter_number INTEGER, book_id);";
	private final String CREATE_VERSE_TABLE = "CREATE TABLE Verse(verse_id INTEGER PRIMARY KEY AUTOINCREMENT, chapter_id INTEGER not NULL,   verse_num INTEGER not NULL, text TEXT not NULL, book_id INTEGER not NULL);";  
	private final String CREATE_VERSECOUNT_TABLE = "CREATE TABLE VerseCount(verse_count_id INTEGER PRIMARY KEY AUTOINCREMENT, chapter_id INTEGER not NULL,  book_id INTEGER not NULL,   verse_count INTEGER not NULL);";  
	private final String CREATE_PROJECT_TABLE = "CREATE TABLE Project(project_id INTEGER PRIMARY KEY AUTOINCREMENT,  project_percent INTEGER,   name TEXT, description TEXT);";  
	private final String CREATE_PROJECT_VERSES_TABLE = "CREATE TABLE Project_Verses(pv_id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER NOT NULL, verse_text TEXT, verse_ref TEXT, verse_percent INTEGER);";  

//	private SQLiteStatement insertVerses;
//	private SQLiteStatement insertChapter;
//	private SQLiteStatement insertVerseCounts;
//	private SQLiteStatement insertAbNames; 
//	private SQLiteStatement insertBook;

//	private Context context;


	/** This sets up an instance of LocalSQLiteHelper.
	 * 
	 * @param context Give the context of this application so that we call and create the database for this application.
	 * @throws IOException 
	 */
	public DatabaseKJVEnCreator(Context context){
		super(context, DatabaseManager.getDatabaseFileAbsolutePath(context), null, DatabaseManager.DATABASE_VERSION);
//		this.context = context;
	}



	/**
	 * Execute the SQL queries that will generate the database file
	 */
	@Override	
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_STATS_TABLE);
		db.execSQL(CREATE_BOOK_TABLE);
		db.execSQL(CREATE_ABBREVIATIONS_TABLE);
		db.execSQL(CREATE_CHAPTER_TABLE);
		db.execSQL(CREATE_VERSECOUNT_TABLE);
		db.execSQL(CREATE_VERSE_TABLE);
		db.execSQL(CREATE_PROJECT_TABLE);
		db.execSQL(CREATE_PROJECT_VERSES_TABLE);
	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}



	//	/**
	//	 * This function calls the on-create function if the database file has not yet been created
	//	 * @throws IOException
	//	 */
	//	public void instanciateDatabaseFile() throws IOException {
	//		bookNum = 0;
	//		currentInsertBook = 0;
	//		currentInsertChapter = 0;
	//		db = getWritableDatabase();
	//		createDatabaseFromScratchUsingJson();
	//	}
	//
	//
	//
	//	/**
	//	 * createDatabaseFromScratchUsingJson is responsible of starting the reading of the JSON files containing the KJV bible.
	//	 * This function is only called if there are no split database files of 1MB already in the assets folder. Its important to note
	//	 * that this function takes about three or more minutes to execute. It is recommended that once this function runs the first time, 
	//	 * the File created should be split using JHSplit, and added to the assets folder. 
	//	 * 
	//	 * @param context
	//	 * @throws IOException
	//	 * @throws JSONException
	//	 */
	//	public  void createDatabaseFromScratchUsingJson() throws IOException {
	//		AssetManager assetManager = context.getAssets();
	//		InputStream is = null; 
	//		JsonReader reader = null; 
	//		String[] jsonObjectNames = assetManager.list("KJV");
	//
	//		for(String fileName: jsonObjectNames) {
	//			is = context.getAssets().open("KJV/"+fileName);
	//			reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
	//			reader.beginArray();
	//			while (reader.hasNext()){
	//				if(fileName.equals("kjvbooks.json"))
	//					insertBookObjectIntoDatabase(parseBookObject(reader));
	//				else
	//					insertVerseOjectIntoDatabase(parseVerseObject(reader));
	//			}
	//			reader.endArray();
	//		}
	//	}
	//
	//	
	//	
	//	/**
	//	 * insertBookObjectIntoDatabase executes a query to insert a book object into the new database file.	
	//	 * @param bookObject
	//	 */
	//	public void insertBookObjectIntoDatabase(BookObject bookObject){
	//
	//		int chapter = 1;
	//		
	//		insertBook = db.compileStatement
	//				("INSERT INTO Book(book_id, testament, name, alternate_name) VALUES(?,?,?,?);");
	//		
	//		insertBook.bindLong(1, bookObject.getBookID());
	//		insertBook.bindString(2, bookObject.getTestament());
	//		insertBook.bindString(3,  bookObject.getBookName());
	//		insertBook.bindString(4, bookObject.getAlternateName());
	//		insertBook.execute();
	//
	//		//inserting abbreviation names
	//		for(int i =0; i < bookObject.getAbreviatedNames().size(); i++){
	//			insertAbNames	= db.compileStatement
	//					("INSERT INTO Abbreviations(book_id, abbreviation_name) VALUES(?,?);");
	//			
	//			insertAbNames.bindLong(1, bookObject.getBookID());
	//			insertAbNames.bindString(1,bookObject.getAbreviatedNames().get(i));
	//			insertAbNames.execute();
	//		}
	//
	//		// inserting verse count
	//		for(int j =0; j < bookObject.getVerseCount().size(); j++){
	//			insertVerseCounts = db.compileStatement
	//					("INSERT INTO VerseCount(chapter_id, book_id, verse_count) VALUES(?,?,?);");
	//			
	//			insertVerseCounts.bindLong(1, chapter);
	//			insertVerseCounts.bindLong(2, bookObject.getBookID());
	//			insertVerseCounts.bindLong(3, bookObject.getVerseCount().get(j));
	//			insertVerseCounts.execute();
	//			chapter++;
	//		}
	//	}
	//	
	//	
	//	
	//	/**
	//	 * insertVerseOjectIntoDatabase executes a query to insert the parsed verse object into the database
	//	 * @param verseObject
	//	 */
	//	public void insertVerseOjectIntoDatabase(VerseJsonObject verseObject){
	//		insertVerses = db.compileStatement
	//				("INSERT INTO Verse(chapter_id, verse_num, text,book_id) VALUES(?,?,?,?);");
	//		insertChapter = db.compileStatement
	//				("INSERT INTO Chapter(chapter_number, book_id) VALUES(?,?);");
	//
	//		if((currentInsertBook != verseObject.getbook_id()) 
	//				&& (currentInsertChapter != verseObject.getchapter_id())){
	//			
	//			insertChapter.bindLong(1,  verseObject.getchapter_id());
	//			insertChapter.bindLong(2, verseObject.getbook_id());
	//			insertChapter.execute();
	//			currentInsertBook = verseObject.getbook_id();
	//			currentInsertChapter = verseObject.getchapter_id();
	//		}
	//		
	//		insertVerses.bindLong(1, verseObject.getchapter_id());
	//		insertVerses.bindLong(2, verseObject.getverse_id());
	//		insertVerses.bindString(3, verseObject.gettext());
	//		insertVerses.bindLong(4, verseObject.getbook_id());
	//		insertVerses.execute();
	//	}
	//
	//
	//	
	//	/**
	//	 * parseBookObject parses the part of the KJV part of the file that contains the book informations
	//	 * @param reader
	//	 * @return
	//	 * @throws IOException
	//	 */
	//	public BookObject parseBookObject(JsonReader reader) throws IOException {
	//
	//		String bookName="";
	//		String testament ="";
	//		String alternateName ="";
	//
	//		ArrayList<String> abbrevs = new ArrayList<String>();
	//		ArrayList<Integer> vCount = new ArrayList<Integer>();
	//
	//		bookNum++;
	//
	//		reader.beginObject();
	//		while (reader.hasNext()) {
	//			String name = reader.nextName();
	//			if (name.equals("name"))				bookName = reader.nextString();
	//			else if (name.equals("testament"))		testament = reader.nextString();
	//			else if (name.equals("abbreviations"))	abbrevs =  getAbreviations(reader);
	//			else if (name.equals("verse_counts"))	vCount = getBookCount(reader);
	//			else if (name.equals("altname"))		alternateName = reader.nextString();
	//			else									reader.skipValue();
	//		}
	//		reader.endObject();
	//		return new BookObject(testament, bookName, alternateName, abbrevs, vCount, bookNum, 0 );
	//	}
	//
	//
	//
	//	/**
	//	 * parseVerseObject parses part of the KJV JSON file and extracts verse text
	//	 * @param reader
	//	 * @return
	//	 * @throws IOException
	//	 */
	//	public VerseJsonObject parseVerseObject(JsonReader reader) throws IOException {
	//		VerseJsonObject VerseObject =null;
	//		reader.beginObject();
	//		while (reader.hasNext()) {
	//			String name = reader.nextName();
	//			if (name.equals("fields"))
	//				VerseObject = getFields(reader);
	//			else 
	//				reader.skipValue();
	//		}
	//		reader.endObject();
	//		return VerseObject;
	//	}
	//
	//
	//
	//	/**
	//	 * getBookCount parses the KJV JSON file and collects the number of books in the file
	//	 * @param reader
	//	 * @return
	//	 * @throws IOException
	//	 */
	//	public  ArrayList<Integer> getBookCount(JsonReader reader) throws IOException {
	//		ArrayList<Integer> countArray = new ArrayList<Integer> ();
	//		reader.beginArray();
	//		while (reader.hasNext()) 
	//			countArray.add(reader.nextInt());
	//		reader.endArray();
	//		return countArray;
	//
	//	}
	//
	//
	//
	//	/**
	//	 * getAbreviations parses the different abbreviations for the book names found in the KJV JSON Files
	//	 * @param reader
	//	 * @return
	//	 * @throws IOException
	//	 */
	//	public  ArrayList<String> getAbreviations(JsonReader reader) throws IOException {
	//		ArrayList<String> abbArray = new ArrayList<String> ();
	//		reader.beginArray();
	//		while (reader.hasNext()) 
	//			abbArray.add(reader.nextString());
	//		reader.endArray();
	//		return abbArray;
	//	}
	//
	//
	//
	//	/**
	//	 * getFields is part of the parser for the KJV Json files.
	//	 *  This function extracts the verse and chapter information from the JSON files
	//	 * @param reader
	//	 * @return
	//	 * @throws IOException
	//	 */
	//	public  VerseJsonObject getFields(JsonReader reader) throws IOException {
	//		int chapter_id = 0;
	//		String text = "";
	//		int verse_id = 0; 
	//		int book_id = 0;
	//
	//		reader.beginObject();
	//		while (reader.hasNext()) {
	//			String name1 = reader.nextName();
	//
	//			if (name1.equals("chapter_id")) 		chapter_id = reader.nextInt();
	//			else if (name1.equals("text")) 			text = reader.nextString();
	//			else if (name1.equals("verse_id")) 		verse_id = reader.nextInt();
	//			else if (name1.equals("book_id")) 		book_id = reader.nextInt();
	//			else 									reader.skipValue();
	//		}
	//
	//		reader.endObject();
	//		return new VerseJsonObject(text, chapter_id, verse_id, book_id);
	//	}
}