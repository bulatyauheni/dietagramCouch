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
	private static final String CURR_CLIENT_NAME = "CURR_CLIENT_NAME";
	private static final String CURR_CLIENT_LIMIT = "CURR_CLIENT_LIMIT";

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
		setString(CURR_CLIENT_ID ,currentClientEmail, context);
    }

	public static String getCurrentClientEmail (Context context) {
		return getString (CURR_CLIENT_ID, context);
	}

	private static void setString (String id, String string, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(id, string);
		editor.commit();
	}

	private static String getString (String id, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(id, "");
	}

	public static void setCurrentClientName(String currentClientName, Context context) {
		setString(CURR_CLIENT_NAME ,currentClientName, context);
	}

	public static String getCurrentClientName (Context context) {
		return getString (CURR_CLIENT_NAME, context);
	}

	public static void setCurrentClientLimit(String currentClientName, Context context) {
		setString(CURR_CLIENT_LIMIT ,currentClientName, context);
	}

	public static String getCurrentClientLimit (Context context) {
		return getString (CURR_CLIENT_LIMIT, context);
	}
}
