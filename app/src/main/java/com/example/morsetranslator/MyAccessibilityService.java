package com.example.morsetranslator;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityService  extends AccessibilityService {
    String TAG="accessibility";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG,"Service Connected");

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        System.out.println("Event Occurred");
        Log.e(TAG, "onAccessibilityEvent: event=" + event);
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (null == nodeInfo) return;


    }

    @Override
    public void onInterrupt() {
        Log.e(TAG,"Accessibility Interrupted" );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"Service Destroyed");
    }
}
