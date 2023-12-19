package com.audiocodes.mv.webrtcclient.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.audiocodes.mv.webrtcclient.Callbacks.CallBackHandler;
import com.audiocodes.mv.webrtcclient.General.ACManager;
import com.audiocodes.mv.webrtcclient.General.AppUtils;
import com.audiocodes.mv.webrtcclient.General.MainApp;
import com.audiocodes.mv.webrtcclient.General.Prefs;
import com.audiocodes.mv.webrtcclient.R;
import com.audiocodes.mv.webrtcclient.Structure.SipAccount;
import com.audiocodes.mv.webrtcsdk.im.InstanceMessageStatus;
import com.audiocodes.mv.webrtcsdk.session.CallState;

public class TabletInfoFragment extends BaseFragment implements FragmentLifecycle {

    private static final String TAG = "ChatFragment";

    private TextView stateValue;
    private TextView displayNameValue;
    private TextView userNameValue;


    private CallBackHandler.LoginStateChanged loginStateChanged = new CallBackHandler.LoginStateChanged() {
        @Override
        public void loginStateChange(boolean state) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateDate();
                }
            });
        }

    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.main_fragment_tablet_info, container, false);
        CallBackHandler.registerLginStateChange(loginStateChanged);
        initGui(rootView);
        return rootView;
    }

    private void initGui(View rootView)
    {
        stateValue = (TextView) rootView.findViewById(R.id.tablet_info_state_value_textview);
        displayNameValue = (TextView) rootView.findViewById(R.id.tablet_info_display_name_value_textview);
        userNameValue = (TextView) rootView.findViewById(R.id.tablet_info_user_name_value_textview);

        updateDate();
    }

    private void updateDate(){
        String statusStr = (ACManager.getInstance().isRegisterState() ? getString(R.string.account_textview_status_connected) : getString(R.string.account_textview_status_disconnected));
        stateValue.setText(statusStr);
        SipAccount sipAccount = Prefs.getSipAccount();
        displayNameValue.setText(sipAccount.getDisplayName());
        userNameValue.setText(sipAccount.getUsername());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CallBackHandler.unregisterLoginStateChange(loginStateChanged);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }


}
