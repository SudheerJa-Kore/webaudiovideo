package com.audiocodes.mv.webrtcclient.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.audiocodes.mv.webrtcclient.General.ACManager;
import com.audiocodes.mv.webrtcclient.General.AppUtils;
import com.audiocodes.mv.webrtcclient.General.MainApp;
import com.audiocodes.mv.webrtcclient.General.Prefs;
import com.audiocodes.mv.oauth.OAuthManager;
import com.audiocodes.mv.webrtcclient.R;
import com.audiocodes.mv.webrtcclient.Structure.SipAccount;
import com.audiocodes.mv.webrtcsdk.log.Log;
import com.audiocodes.mv.webrtcsdk.sip.enums.Transport;


public class LoginActivity extends BaseAppCompatActivity {

    public static final String OAUTH_FAILED = "OAUTH_FAILED";

    private boolean oAuthEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);

        initGui();
    }

    private void initGui()
    {

        TextView outhFailedTV = findViewById(R.id.login_textview_outh_failed);
        if(getIntent().getBooleanExtra(OAUTH_FAILED, false)){
            outhFailedTV.setVisibility(View.VISIBLE);
        }
        else{
            outhFailedTV.setVisibility(View.GONE);
        }

        SipAccount sipAccount = new SipAccount();


        EditText userNameEditText = findViewById(R.id.login_editText_username);
        //userNameEditText.setText(sipAccount.getUsername());
        EditText passwordEditText = findViewById(R.id.login_editText_password);
        EditText displayNameEditText = findViewById(R.id.login_editText_displayname);
        EditText domainEditText = findViewById(R.id.login_editText_domain);
        EditText sipAddressEditText = findViewById(R.id.login_editText_sipaddress);
        EditText portEditText = findViewById(R.id.login_editText_port);
        Spinner transportSpinner = findViewById(R.id.login_editText_transport);
        transportSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Transport.values()));

        Button loginButton = findViewById(R.id.login_button_login);
        CheckBox autologin = findViewById(R.id.autologin_checkBox);
        autologin.setChecked(true);

        CheckBox disconnectCall = findViewById(R.id.call_disconnect_checkBox);
        disconnectCall.setChecked(true);

        CheckBox usePush = findViewById(R.id.push_checkBox);
        usePush.setChecked(Prefs.usePush());

        usePush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Prefs.setUsePush(isChecked);
            }
        });

        CheckBox oauthCB = findViewById(R.id.oauth_checkBox);
        oauthCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout layout = findViewById(R.id.oauth_layout);
                oAuthEnable = isChecked;
                if(isChecked){
                    layout.setVisibility(View.VISIBLE);
                }
                else{
                    layout.setVisibility(View.GONE);
                }
            }
        });

        CheckBox useVideoHardware = findViewById(R.id.video_hardware_checkbox);
        useVideoHardware.setChecked(Prefs.isVideoHardware());

        useVideoHardware.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Prefs.setVideoHardware(isChecked);
            }
        });

        EditText oauthUrlEditText = findViewById(R.id.login_editText_oauth_url);
        EditText oauthRealmEditText = findViewById(R.id.login_editText_oauth_realm);
        EditText oauthClientIdEditText = findViewById(R.id.login_editText_oauth_clientid);

        if(OAuthManager.getInstance().isEnabled()){
            Log.d("BBB", "getting in here");
            oauthCB.setVisibility(View.GONE);
            oAuthEnable = true;
            LinearLayout layout = (LinearLayout) findViewById(R.id.oauth_layout);
            layout.setVisibility(View.VISIBLE);
        }

        userNameEditText.setText(sipAccount.getUsername());
        passwordEditText.setText(sipAccount.getPassword());
        displayNameEditText.setText(sipAccount.getDisplayName());
        domainEditText.setText(sipAccount.getDomain());
        sipAddressEditText.setText(sipAccount.getProxy());

        boolean debugMode = AppUtils.getStringBoolean(R.string.enable_debug_mode);
        if (debugMode)
        {
            userNameEditText.setText(sipAccount.getUsername());
            passwordEditText.setText(sipAccount.getPassword());
            displayNameEditText.setText(sipAccount.getDisplayName());
            domainEditText.setText(sipAccount.getDomain());
            sipAddressEditText.setText(sipAccount.getProxy());

            for (int i=0; i< transportSpinner.getAdapter().getCount(); i++) {
                if(transportSpinner.getItemAtPosition(i).toString().toLowerCase().equals(sipAccount.getTransport().toString().toLowerCase()))
                {
                    transportSpinner.setSelection(i);
                }
            }

            //userNameEditText.setText(sipAccount.getUsername());
            //userNameEditText.setText(sipAccount.getUsername());
            portEditText.setText(String.valueOf(sipAccount.getPort()));

            //advancedInfoLayout.setVisibility( View.VISIBLE );
            oauthCB.setChecked(AppUtils.getStringBoolean(R.string.oauth_enable));
            oauthUrlEditText.setText(getString(R.string.oauth_url));
            oauthRealmEditText.setText(getString(R.string.oauth_realm));
            oauthClientIdEditText.setText(getString(R.string.oauth_client_id));
        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateField(userNameEditText)&&validateField(passwordEditText)) {

                    String userName = userNameEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();
                    sipAccount.setUsername(userName);
                    if (!oAuthEnable) {
                        sipAccount.setPassword(password);
                    } else {
                        sipAccount.setPassword("no password");
                    }
                    //if(validateField(displayNameEditText)&&validateField(domainEditText)&&validateField(sipAddressEditText)&&validateField(turnServerEditText)&&validateField(stunserverEditText))
                    if (validateField(displayNameEditText) && validateField(domainEditText) && validateField(sipAddressEditText) && validateField(portEditText)) {
                        sipAccount.setDisplayName(displayNameEditText.getText().toString().trim());
                        sipAccount.setDomain(domainEditText.getText().toString().trim());
                        sipAccount.setProxy(sipAddressEditText.getText().toString().trim());
                        sipAccount.setTransport(AppUtils.getTransport(transportSpinner.getSelectedItem().toString().trim()));

                        int portNumber;
                        try {
                            portNumber = Integer.valueOf(portEditText.getText().toString().trim());
                        } catch (Exception e) {
                            //use default port number;
                            portNumber = Integer.valueOf(getString(R.string.sip_account_port_default));
                        }
                        sipAccount.setPort(portNumber);
                        //sipAccount.setPassword(turnServerEditText.getText().toString().trim());
                        //sipAccount.setUsername(stunserverEditText.getText().toString().trim());
                    }

                    Prefs.setSipAccount(sipAccount);
                    if (oAuthEnable) {
                        OAuthManager.getInstance().setURL(oauthUrlEditText.getText().toString());
                        OAuthManager.getInstance().setRealm(oauthRealmEditText.getText().toString());
                        OAuthManager.getInstance().setClientId(oauthClientIdEditText.getText().toString());
                        OAuthManager.getInstance().authorize(userName, password, new OAuthManager.LoginCallback() {
                            @Override
                            public void onAuthorize(boolean success) {
                                if (success) {
                                    openNextScreen(autologin.isChecked(), disconnectCall.isChecked());
                                } else {
                                    Toast.makeText(MainApp.getGlobalContext(), getString(R.string.oauth_error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        openNextScreen(autologin.isChecked(), disconnectCall.isChecked());
                    }
                }
                Prefs.setSipAccount(sipAccount);
                if(!oAuthEnable){
                    openNextScreen(autologin.isChecked(), disconnectCall.isChecked());
                }
            }
        });

        boolean predefinedConfigMode = AppUtils.getStringBoolean(R.string.predefined_config_mode);
        if (predefinedConfigMode)
        {
            userNameEditText.setEnabled(false);
            passwordEditText.setEnabled(false);
            displayNameEditText.setEnabled(false);
            domainEditText.setEnabled(false);
            sipAddressEditText.setEnabled(false);
            portEditText.setEnabled(false);
            transportSpinner.setEnabled(false);

            //use click to call
            autologin.setChecked(false);
            autologin.setEnabled(false);

            loginButton.setText(R.string.login_button_start_text);
        }
    }

    private boolean validateField(EditText editText)
    {
        if(editText!=null && editText.getText() !=null && !editText.getText().toString().trim().equals(""))
        {
            return true;
        }
        return false;
    }

    private void openNextScreen(boolean autologin, boolean disconnectCall)
    {
        Prefs.setFirstLogin(false);
        //start login and open app main screen
        ACManager.getInstance().startLogin(autologin, disconnectCall);
        startNextActivity(MainActivity.class);
    }

}
