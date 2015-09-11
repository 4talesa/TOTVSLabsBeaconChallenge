package farto.cleva.guilherme.totvs.db;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

	public static final String PREFS_NAME = "plant-health-management";

	private static SharedPreferences preferences = null;

	private static PreferencesHelper helper = null;

	public static PreferencesHelper getInstance(Context context) {
		if (helper == null) {
			helper = new PreferencesHelper();
		}

		preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		return helper;
	}

	private PreferencesHelper() {
		super();
	}

	public String getStringValue(String key, String defValue) {
		return preferences.getString(key, defValue);
	}

	public void setStringValue(String key, String value) {
		SharedPreferences.Editor editor = preferences.edit();

		editor.putString(key, value);

		editor.commit();
	}

	public void removeStringValue(String key) {
		SharedPreferences.Editor editor = preferences.edit();

		editor.remove(key);

		editor.commit();
	}

}
