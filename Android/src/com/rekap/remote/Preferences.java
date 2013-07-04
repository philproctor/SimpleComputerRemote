package com.rekap.remote;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

import com.rekap.network.Network;

public class Preferences extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preferences);
        
        CharSequence[] servers = Network.GetServers();
        if (servers.length > 0)
        {
        	ListPreference serverPref = (ListPreference) findPreference("server");
        	
        	serverPref.setEntries(servers);
        	serverPref.setEntryValues(servers);
        }
        
	}
}
