package com.audiocodes.mv.webrtcclient.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.audiocodes.mv.webrtcclient.General.ACManager;
import com.audiocodes.mv.webrtcclient.General.AppUtils;
import com.audiocodes.mv.webrtcclient.General.Log;
import com.audiocodes.mv.webrtcclient.R;

public class PhoneStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (!AppUtils.getStringBoolean(R.string.enable_native_telephony_listener)){
                return;
            }
            if (ACManager.getInstance()!=null){
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                Log.d("onCallStateChanged", "state3:" + state);
                //put the Voip calls on hold for the duration of a a GSM calls

                if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                    //unhold
                    if (ACManager.getInstance().isAllredyInActiveCall()) {
                        Log.d("onCallStateChanged", "send unhold to active voip call if needed");
                        ACManager.getInstance().getActiveSession().hold(false);
                    }
                }
                if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) || state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                    //hold
                    if (ACManager.getInstance().isAllredyInActiveCall()) {
                        Log.d("onCallStateChanged", "send hold to active voip call if needed");
                        ACManager.getInstance().getActiveSession().hold(true);
                    }
                }
//                switch(state) {
//
//                    case TelephonyManager.EXTRA_STATE_IDLE:
//                        //unhold
//                        if (ACManager.getInstance().isAllredyInActiveCall()) {
//                            Log.d("onCallStateChanged", "send unhold to active voip call if needed");
//                            ACManager.getInstance().getActiveSession().hold(false);
//                        }
//                        break;
//                    case TelephonyManager.EXTRA_STATE_OFFHOOK:
//                    case TelephonyManager.EXTRA_STATE_RINGING:
//                        //hold
//                        if (ACManager.getInstance().isAllredyInActiveCall()) {
//                            Log.d("onCallStateChanged", "send hold to active voip call if needed");
//                            ACManager.getInstance().getActiveSession().hold(true);
//                        }
//                        break;
//                }
            }
        }
        catch (Exception e)
        {

        }
    }
}