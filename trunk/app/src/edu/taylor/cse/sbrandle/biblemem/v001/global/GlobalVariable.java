package edu.taylor.cse.sbrandle.biblemem.v001.global;



public class GlobalVariable {

	// API Version Int
	
	public static final int HONEYCOMB = 11;
	
	
	
	// For Criteria of verse memorize in each method

	public static final int CRITERIA_OF_SUCCESS = 99;

	
	// How many verses are in DB
	
	public static final int HOW_MANY_VERSES = 31100;
	

	// Difficulty

	public static final String DIFFICULTY_KEY = "DIFFICULTY_KEY";
	public static final String DIFFICULTY = "DIFFICULTY";
	public static final String EASY_KEY = "EASY";
	public static final String NORMAL_KEY = "NORMAL";
	public static final String HARD_KEY = "HARD";
	public static final float EASY = (float) 0.2;
	public static final float NORMAL = (float) 0.5;
	public static final float HARD = (float) 0.8;
	public static final int EASY_INDEX = 0;
	public static final int NORMAL_INDEX = 1;
	public static final int HARD_INDEX = 2;


	// Today Verse Number

	public static final String TODAY_VERSE_KEY = "TODAY_VERSE_KEY";
	public static final String TODAY_VERSE_NUMBER = "TODAY_VERSE_NUMBER";
	public static final String TODAY = "TODAY";
	
	
	
	// Language code

	public static final int EDITION_COUNT = 2;

	public static final String KJV_NAME = "English King James Version";
	public static final String KRV_NAME = "한국어 개역한글";

	public static final String KJV_CODE = "KJV";
	public static final String KRV_CODE = "KRV";

	public static final String ENGLISH_CODE = "en";
	public static final String KOREAN_CODE = "ko";

	public static final String[] SETTING_BIBLE_EDITION_NAME_LIST = {KJV_NAME, KRV_NAME};
	public static final String[] SETTING_BIBLE_EDITION_CODE_LIST = {KJV_CODE, KRV_CODE};



	// Intent Key

	public static final String VERSE_REF = "VERSE_REF";
	public static final String VERSE_TEXT = "VERSE_TEXT";
	
	public static final String PROJECT_ID = "PROJECT_ID";
	public static final String PROJECT_VERSE_ID = "PROJECT_VERSE_ID";
	public static final String PROJECT_NAME= "PROJECT_NAME";
	
	public static final String MEMORIZE = "MEMORIZE";
	public static final String PROJECT = "PROJECT";

	public static final String PROJECT_VERSE = "PROJECT_VERSE";
	public static final String REVIEW = "REVIEW";

	public static final String ADD_PROJECT = "ADD_PROJECT";
	public static final String RENAME_PROJECT = "RENAME_PROJECT";

	public static final String VERSE_ID = "VERSE_ID";

	public static final String TODAY_VERSE_WIDGET = "TODAY_VERSE_WIDGET";

	// Locate Word Instant State Key

	public static final String WORDS_ARRAY = "WORDS_ARRAY";
	public static final String MIXED_WORDS = "MIXED_WORDS";
	public static final String HIDDEN_WORDS = "HIDDEN_WORDS";
	public static final String WORD_COUNT = "WORD_COUNT";
	public static final String WORD_TRY_COUNT = "WORD_TRY_COUNT";
	public static final String MIXED_WORDS_INDEX = "MIXED_WORDS_INDEX";
	public static final String POTENTIAL_POINTS = "POTENTIAL_POINTS";
	public static final String WORD_WORTH = "WORD_WORTH";
	public static final String NUM_OF_HIDDEN_WORDS = "NUM_OF_HIDDEN_WORDS";



	// First Letter Instant State Key
	// First Letter Alphaber

	public static final String WORDS_ARRAY_INDEX = "WORDS_ARRAY_INDEX";
	public static final String BUTTON_TEXT1 = "BUTTON_TEXT1";
	public static final String BUTTON_TEXT2 = "BUTTON_TEXT2";
	public static final String BUTTON_TEXT3 = "BUTTON_TEXT3";
	public static final String BUTTON_ENABLED1 = "BUTTON_ENABLED1";
	public static final String BUTTON_ENABLED2 = "BUTTON_ENABLED2";
	public static final String BUTTON_ENABLED3 = "BUTTON_ENABLED3";

	public static final String ALPHABET_EN = "ABCDEFGHIJKLNMOPQRSTUVWXYZ";
	public static final String ALPHABET_KO = "ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎ";
}
