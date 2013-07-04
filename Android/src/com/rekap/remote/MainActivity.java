package com.rekap.remote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.rekap.network.NetInput;
import com.rekap.network.Network;

public class MainActivity extends Activity {
	
	OnClickListener leftEvent = new OnClickListener() {
		public void onClick(View v) {
			NetInput.LeftClick();
		}
	};
	
	OnClickListener rightEvent = new OnClickListener() {
		public void onClick(View v) {
			NetInput.RightClick();
		}
	};
	
	OnClickListener menuEvent = new OnClickListener() {
		public void onClick(View v) {
			openOptionsMenu();
		}
	};
	
	private Context context;
	private KeypadHandler keypadHandler = new KeypadHandler();
	private RelativeLayout layout;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;
        
        layout = (RelativeLayout)findViewById(R.id.background);
        final Button leftClick = (Button)findViewById(R.id.leftClick);
        final Button rightClick = (Button)findViewById(R.id.rightClick);
        final ImageButton menuClick = (ImageButton)findViewById(R.id.menuButton);
        
        layout.setOnTouchListener(new TouchpadHandler());
        layout.setOnKeyListener(keypadHandler);
        leftClick.setOnClickListener(leftEvent);
        rightClick.setOnClickListener(rightEvent);
        menuClick.setOnClickListener(menuEvent);
        
        loadPreferences();
        Network.LocatorStart();
        
        if (Globals.FirstRun) {
            new AlertDialog.Builder(this)
        		.setMessage(R.string.firstruntext)
        		.setNeutralButton("OK", null)
        		.show();
            Globals.FirstRun = false;
        }
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	loadPreferences();
    }
    
    public void loadPreferences()
    {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	
    	Globals.AutoConnect = prefs.getBoolean(Globals.AUTOCONNECT, true);
    	Globals.FirstRun = prefs.getBoolean(Globals.FIRSTRUN, true);
    	Globals.Sensitivity = ((float)(prefs.getInt(Globals.SENSITIVITY, 50) + 20)) / 100;
    	Globals.Server = prefs.getString(Globals.SERVER, "First");
    	
    	if (Globals.FirstRun) {
    		SharedPreferences.Editor editor = prefs.edit();
    		editor.putBoolean(Globals.FIRSTRUN, false);
    		editor.commit();
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId())
    	{
    	case R.id.menu_keyboard:
    		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            imm.showSoftInput(layout, InputMethodManager.SHOW_FORCED);
    		return true;
    		
    	case R.id.menu_settings:
    		startActivity(new Intent(getBaseContext(), Preferences.class));
    		return true;
    		
    	case R.id.menu_reconnect:
    		Network.Connect(Globals.Server);
    		return true;
    	
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
