package edu.taylor.cse.sbrandle.biblemem.v001;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import edu.taylor.cse.sbrandle.biblemem.v001.layout.WordSearchGridLayout;
import edu.taylor.cse.sbrandle.biblemem.v001.layout.WordSearchTextView;


public class WordSearchAct extends Activity {

	private static final String[] numbers = new String[] { 
		"A", "B", "C", "D", "E",
		"F", "G", "H", "I", "J",
		"K", "L", "M", "N", "O",
		"P", "Q", "R", "S", "T",
		"U", "V", "W", "X", "Y", "Z",
		"A", "B", "C", "D", "E",
		"F", "G", "H", "I", "J",
		"K", "L", "M", "N", "O",
		"P", "Q", "R", "S", "T",
		"U", "V", "W", "X", "Y", "Z",
		"A", "B", "C", "D", "E",
		"F", "G", "H", "I", "J",
		"K", "L", "M", "N", "O",
		"P", "Q", "R", "S", "T",
		"U", "V", "W", "X", "Y", "Z"};
	private WordSearchGridLayout mGrid;
	private WordSearchTextView tapText;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_search);
		mGrid = (WordSearchGridLayout) findViewById(R.id.wordGrid);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, numbers);

		mGrid.setAdapter(adapter);
		//mGrid.setContext(this);
		String text = "These are great words of wisdom, no lo crees JOTY FED KEY ABC, Mama mia, super smash bross";
		tapText = (WordSearchTextView) findViewById(R.id.ctxview);
		tapText.initializeTextView(text.split(" "), this.getApplicationContext());
		tapText.setBackgroundColor(Color.WHITE);
	}
}	







