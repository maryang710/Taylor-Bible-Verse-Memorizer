/**
 * Array adapter for the Verse class.
 * Inspired by example in Reto Meier, <ul>Professional Android 4 Application Development</ul>, 
 */
package edu.taylor.cse.sbrandle.biblemem.v001;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

/**
 * @author sbrandle
 *
 */
public class VerseArrayAdapter extends ArrayAdapter<Verse> {

	private static final String TAG = "Verse_array_adapter";

	int resource;
	List<Verse> items;

	/**
	 * 
	 */
	public VerseArrayAdapter(Context context, int resource, List<Verse> items) {
	    super(context, resource, items);
	    this.resource = resource;
	    this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    // Create and inflate the View to display
	    LinearLayout newView;
	
	    if (convertView == null) {
	      // Inflate a new view if this is not an update.
	      newView = new LinearLayout(getContext());
	      String inflater = Context.LAYOUT_INFLATER_SERVICE;
	      LayoutInflater li;
	      li = (LayoutInflater)getContext().getSystemService(inflater);
	      li.inflate(resource, newView, true);
	    } else {
	      // Otherwise we'll update the existing View
	      newView = (LinearLayout)convertView;
	    }
	
	    //Verse classInstance = getItem(position);
        Log.d(TAG, "Displaying verse: " + items.get(position).toString());

	
	    // TODO Retrieve values to display from the
	    // classInstance variable.
	
	    // TODO Get references to the Views to populate from the layout.
	    // TODO Populate the Views with object property values.
	
	    return newView;
	}
	
}
