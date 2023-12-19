package com.audiocodes.mv.webrtcclient.FCM;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FCMMessage implements Serializable {

    private final Map<String, String> data = new HashMap<>();

    private String from;

    public FCMMessage(RemoteMessage remoteMessage){
        this.from = remoteMessage.getFrom();
        Map<String, String> map = remoteMessage.getData();
        for (Map.Entry<String, String> entry : map.entrySet()){
            data.put(entry.getKey(), entry.getValue());
        }
    }

    public String getString(String key){
        return data.get(key);
    }

    public boolean getBoolean(String key){
        return data.get(key) != null;
    }

    public int getInt(String key){
        return Integer.parseInt(data.get(key));
    }

    public String toString(){
        return from + ": " + new JSONObject(data).toString();
    }
}
