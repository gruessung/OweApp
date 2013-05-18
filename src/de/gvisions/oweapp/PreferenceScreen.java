package de.gvisions.oweapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferenceScreen extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.options);
	}
}
