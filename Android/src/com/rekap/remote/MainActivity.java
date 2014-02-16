package com.rekap.remote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
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
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.app_name)
                   .setItems(R.array.options, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch(which) {
                    case 0:
                        startActivity(new Intent(getBaseContext(), Preferences.class));
                        break;

                    case 1:
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        imm.showSoftInput(layout, InputMethodManager.SHOW_FORCED);
                        break;

                    case 2:
                        Network.Connect(Globals.Server);
                        break;
                    }
                }
            });
            builder.show();
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    NetInput.VolumeUp();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    NetInput.VolumeDown();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

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
}
