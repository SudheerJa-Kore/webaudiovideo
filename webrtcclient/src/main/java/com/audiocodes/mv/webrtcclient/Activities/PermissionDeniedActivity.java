package com.audiocodes.mv.webrtcclient.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.audiocodes.mv.webrtcclient.General.ACManager;
import com.audiocodes.mv.webrtcclient.General.Prefs;
import com.audiocodes.mv.webrtcclient.Permissions.PermissionManager;
import com.audiocodes.mv.webrtcclient.R;


public class PermissionDeniedActivity extends BaseAppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.permission_denied_activity);

        initGui();
    }

    private void initGui() {

        Button continueButton = (Button)findViewById(R.id.permission_denied_button_continue);
        Button editButton = (Button)findViewById(R.id.permission_denied_button_edit);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFirstLogin = Prefs.isFirstLogin();
                if (isFirstLogin)
                {
                    startNextActivity(LoginActivity.class);
                    finish();
                }
                else {
                    //start login and open app main screen
                    ACManager.getInstance().startLogin();
                    startNextActivity(MainActivity.class);
                    finish();
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.askForSystemAlertWindowPermission();
                finish();
            }
        });
    }

}
