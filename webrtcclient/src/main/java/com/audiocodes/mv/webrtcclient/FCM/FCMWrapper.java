package com.audiocodes.mv.webrtcclient.FCM;

import android.util.Log;

import com.audiocodes.mv.webrtcclient.General.ACManager;
import com.audiocodes.mv.webrtcclient.General.MainApp;
import com.audiocodes.mv.webrtcclient.General.Prefs;
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMWrapper extends FirebaseMessagingService
{
	public static final String TAG = "FCMWrapper";

	public static final String INCOMING_CALL = "m";
	public static final String PUSH_INCOMING_CALL = "i";
	public static final String PUSH_REGISTER = "r";

	private static FCMWrapper instance;

	public static synchronized FCMWrapper getInstance()
	{
		if (instance == null)
		{
			instance = new FCMWrapper();
		}
		return instance;
	}

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		if(Prefs.usePush()){
			Log.d(TAG, "fcm onMessageReceived " + remoteMessage.getFrom());
			FCMMessage message = new FCMMessage((remoteMessage));
			Log.d(TAG, "message " + message);
			String type = message.getString("messageType");
			if(type != null && type.equals(INCOMING_CALL)) {
				//INCOMING_CALL deprecated format
				Log.d(TAG, "incomingCall");
				ACManager.getInstance().startLogin();
			}
			else if(type != null && type.equals(PUSH_INCOMING_CALL)) {
				//INCOMING_CALL
				Log.d(TAG, "PUSH_INCOMING_CALL");
				ACManager.getInstance().startLogin();
			}
			else if(type != null && type.equals(PUSH_REGISTER)) {
				//REGISTER
				Log.d(TAG, "PUSH_REGISTER");
				ACManager.getInstance().startLogin();
			}
		}
		else{
			Log.d(TAG, "no push");
		}
	}

	public void init(){
		try{
			Log.d(TAG, "init FCM");
			//FirebaseApp.initializeApp(AndroidAdapter.getContext());
			FirebaseMessaging.getInstance().setAutoInitEnabled(true);
			updatePushToken();
		}
		catch(Throwable th) {
			Log.e(TAG, "init FCM failed", th);
		}
	}

	public void updatePushToken()
	{
		if(Prefs.usePush()){
			String token = FirebaseInstanceId.getInstance().getToken();
			Log.d(TAG, "updatePushToken FCM: " + token);
			if(token != null && !token.equals("")){
				//update register
				AudioCodesUA.getInstance().setPushToken(MainApp.getGlobalContext(), token ,"webrtcclient");
			}
		}
		else{
			Log.d(TAG, "no push");
		}
	}

}
