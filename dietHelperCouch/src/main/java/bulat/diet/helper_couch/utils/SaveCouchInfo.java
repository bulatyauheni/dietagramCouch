package bulat.diet.helper_couch.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.Random;

import bulat.diet.helper_couch.activity.Info;

public class SaveCouchInfo {
	

	private static final String COUCH_ID = "COUCH_ID";
	private static final String CURR_CLIENT_ID = "CURR_CLIENT_ID";

	public static void setCouchId (String value, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();				
		editor.putString(COUCH_ID, value);
		editor.commit();
	}
	
	public static String getCouchId (Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);				
				return preferences.getString(COUCH_ID, "");
	}

    public static void setCurrentClientEmail(String currentClientEmail, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(CURR_CLIENT_ID, currentClientEmail);
		editor.commit();
    }

	public static String getCurrentClientEmail (Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(CURR_CLIENT_ID, "");
	}
}
