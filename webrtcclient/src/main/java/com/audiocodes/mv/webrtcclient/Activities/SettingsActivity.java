package com.audiocodes.mv.webrtcclient.Activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.audiocodes.mv.webrtcclient.General.ACManager;
import com.audiocodes.mv.webrtcclient.General.AppUtils;
import com.audiocodes.mv.webrtcclient.General.MainApp;
import com.audiocodes.mv.webrtcclient.General.Prefs;
import com.audiocodes.mv.webrtcclient.R;
import com.audiocodes.mv.webrtcclient.Structure.CallEntry;
import com.audiocodes.mv.webrtcclient.Structure.SipAccount;
import com.audiocodes.mv.webrtcsdk.sip.enums.Transport;

import java.util.List;


public class SettingsActivity extends BaseAppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_activity);

        initGui();
    }

    private void initGui() {

        int[] settingsButtonClickListID = {R.id.settings_button_general, R.id.settings_button_account, R.id.settings_button_call_stats};

        View.OnClickListener settingsClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View clickedView) {
                if (clickedView == null) {
                    return;
                }
                int id = clickedView.getId();
                if (id == R.id.settings_button_general) {
                    startNextActivity(GeneralSettingsActivity.class);
                } else if (id == R.id.settings_button_account) {
                    startNextActivity(AccountActivity.class);
                } else if (id == R.id.settings_button_call_stats) {
                    startNextActivity(CallStatsActivity.class);
                }

            }
        };

        for (int settingsButtonClickID : settingsButtonClickListID) {
            View view = findViewById(settingsButtonClickID);
            if (view != null) {
                view.setOnClickListener(settingsClickListener);
            }
        }
    }
//
//
//    private void openNextScreen(Class cls) {
//        //start login and open app main screen
//        //ACManager.getInstance().startLogin();
//        startNextActivity(cls);
//    }

}
