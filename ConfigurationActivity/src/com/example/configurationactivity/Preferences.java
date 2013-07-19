package com.example.configurationactivity;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity implements
		OnPreferenceChangeListener {

	private ListPreference internet_connection;
	private ListPreference autosync_interval;
	private CheckBoxPreference checkboxPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		checkboxPref = (CheckBoxPreference) findPreference("checkboxPref");
		checkboxPref.setOnPreferenceChangeListener(this);

		internet_connection = (ListPreference) findPreference("internet_connection");
		internet_connection.setSummary(internet_connection.getEntry());
		internet_connection.setOnPreferenceChangeListener(this);

		autosync_interval = (ListPreference) findPreference("autosync_interval");
		autosync_interval.setSummary(autosync_interval.getEntry());
		autosync_interval.setOnPreferenceChangeListener(this);

	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference.getKey().equals("internet_connection")) {
			preference.setSummary((CharSequence) newValue);
		} else if (preference.getKey().equals("autosync_interval")) {
			preference.setSummary((CharSequence) newValue);
		} else if (preference.getKey().equals("checkboxPref")) {
			if (newValue.toString().equals("true")) {
				internet_connection.setEnabled(true);
				autosync_interval.setEnabled(true);
			} else {
				internet_connection.setEnabled(false);
				autosync_interval.setEnabled(false);
			}
		}
		return true;
	}

}
