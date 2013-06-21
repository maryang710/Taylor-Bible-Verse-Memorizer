/** File Layout Creator Class allows us to add to our file layout structure.
 * @author Joshua Burkett
 * @author Eliezer Rodriguez
 * @version 1.0 Feb 9, 2012
 */

package edu.taylor.cse.sbrandle.biblemem.v001.global;

import java.io.File;
import android.util.Log;
import android.os.Environment;

public class FileLayoutCreator {
	private final static String TAG = "FileLayoutCreator"; 
	
	/** This function creates directories if they do not exists.
	 * @param path This is the path that we want our folder to be made.
	 * @param folderName This is the name that we want the new folder to have.
	 */
	public void createFileLayout(/*File path,*/ String folderName) {
		if (checkSD()) {
			File currentDir = new File(/*path + */folderName);
			if (!currentDir.exists()) {
				currentDir.mkdirs();
			}
		}
	}

	/** Check to see if the SD Card is usable.
	 * @return True if SD Card is in phone otherwise Return False
	 */
	public static boolean checkSD() {
		String sdEnv = Environment.getExternalStorageState();
		if (sdEnv.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else if (sdEnv.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			Log.e(TAG, "SD Card Check: read-only");
		} else if (sdEnv.equals(Environment.MEDIA_REMOVED)) {
			Log.e(TAG,"SD Card Check: not present");
		} else if (sdEnv.equals(Environment.MEDIA_UNMOUNTED)) {
			Log.e(TAG, "SD Card Check: not mounted");
		} else if (sdEnv.equals(Environment.MEDIA_BAD_REMOVAL)
				|| sdEnv.equals(Environment.MEDIA_CHECKING)
				|| sdEnv.equals(Environment.MEDIA_SHARED)
				|| sdEnv.equals(Environment.MEDIA_UNMOUNTABLE)
				|| sdEnv.equals(Environment.MEDIA_NOFS)) {
			Log.e(TAG, "SD Card Check: state unusable");
		} else {
			Log.e(TAG, "SD Card Check: catastrophic failure");
		}
		return false;
	}

	/** This function returns the path where audio files should be saved.
	 * @param programID This is the programID that the file should be saved with.
	 * @return This will return the path that the audio files should be saved in.
	 */
	public static String createAudioPath(String programID){

		return createAudioPath() + String.format("%05d", Integer.parseInt(programID)) + "/";
	}

	/** This function returns the path of the folder where audio files should be saved.
	 * @return This will return the path that the audio files should be saved in.
	 */
	public static String createAudioPath(){

		return Environment.getExternalStorageDirectory().getPath()+ "/5fish/Audio/" ;
	}

	/** This function returns the path where picture files should be saved.
	 * @return This will return the path where picture files should be saved.
	 */
	public static String createPicturePath(){
		return Environment.getExternalStorageDirectory().getPath()+ "/5fish/Pictures/";
	}
}
