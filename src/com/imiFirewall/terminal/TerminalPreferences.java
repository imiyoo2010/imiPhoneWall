package com.imiFirewall.terminal;

import com.imiFirewall.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class TerminalPreferences extends PreferenceActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Load the preferences from an XML resource
    addPreferencesFromResource(R.xml.preferences);
  }
}
