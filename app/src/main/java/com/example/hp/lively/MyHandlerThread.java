package com.example.hp.lively;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergii Varenyk on 29.11.15.
 */
public class MyHandlerThread<Token> extends HandlerThread{
    private static final String TAG = "MyHandlerThread";
    private static final int MESSAGE = 0;
    Handler handler;
    Map<Token,String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());

    Handler responseHandler;
    Listener<Token> listener;

    public interface Listener<Token>{
        void handlerDownloaded(Token token, Bitmap image);
    }

    public void setListener(Listener<Token> listener){
        this.listener = listener;
    }

    public MyHandlerThread(Handler responseHandler) {
        super(TAG);
        this.responseHandler = responseHandler;
    }

    public MyHandlerThread(String name) {
        super(name);
    }

    public MyHandlerThread(String name, int priority) {
        super(name, priority);
    }

    public void queue(Token token, String url){
        requestMap.put(token, url);
        handler.obtainMessage(MESSAGE,token).sendToTarget();
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE){
                    @SuppressWarnings("unchecked")
                    Token token = (Token)msg.obj;
                    handleRequest(token);
                }
            }
        };

    }

    private void handleRequest(final Token token){
        try {
            final String url = requestMap.get(token);
            if (url == null) {
                return;
            }
            byte[] bitmapBytes = new LivelyFetch().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes,0,bitmapBytes.length);

            responseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (requestMap.get(token) != url){
                        return;
                    }
                    requestMap.remove(token);
                    listener.handlerDownloaded(token,bitmap);
                }
            });
        }catch (IOException ioe){
            Log.e("HandlerThread", ""+ioe.getMessage());
        }
    }

    public void clearQueue(){
        handler.removeMessages(MESSAGE);
        requestMap.clear();
    }
}
