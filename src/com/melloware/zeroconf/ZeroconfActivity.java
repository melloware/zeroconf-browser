/*
    ZeroConf Browser - http://melloware.com/
    
    Copyright (C) 2010 Melloware Inc
    
    This program is commercial software: You should not be viewing this source
    code.
    
    You may not decompile, reverse engineer, disassemble or otherwise reduce 
    this software to a human perceivable form.  You may not modify, rent or 
    resell for profit this software, or create derivative works based upon this 
    software.  You may not publicize or distribute any registration code 
    algorithms, information, or registration codes used by this software 
    without permission of Melloware Inc, Inc.
    
    The Initial Developer of the Original Code is Emil A. Lefkof III.
    Portions created by Emil A. Lefkof III are
    Copyright (C) 2010 Melloware Inc
    All Rights Reserved.
 */
package com.melloware.zeroconf;

import java.util.Timer;
import java.util.TimerTask;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TabHost;

/**
 * Main activity of the ZeroConf Browser. Creates the tab layout and any other initialization activity needed.
 * <p>
 * Copyright (c) 2010 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 1.0
 */
public class ZeroconfActivity extends TabActivity {

   /**
    * Logger tag
    */
   private static final String TAG = ZeroconfActivity.class.getName();

   protected PostMortemReportExceptionHandler mDamageReport = new PostMortemReportExceptionHandler(this);
   protected static Timer timer = new Timer();

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      Log.i(TAG, "Creating ZeroConf Browser...");
      super.onCreate(savedInstanceState);

      requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
      mDamageReport.run();
      Thread.setDefaultUncaughtExceptionHandler(mDamageReport);

      setContentView(R.layout.main);

      // show the spinner and message
      setProgressBarIndeterminateVisibility(true);
      setTitle(R.string.msg_scan);

      // kill the scanning of the network in 5 seconds
      timer = new Timer();
      timer.schedule(new StopScanTask(), 5000);

      Resources res = getResources(); // Resource object to get Drawables
      TabHost tabHost = getTabHost(); // The activity TabHost
      TabHost.TabSpec spec; // Resusable TabSpec for each tab
      Intent intent; // Reusable Intent for each tab

      // Create an Intent to launch an Activity for the tab (to be reused)
      intent = new Intent().setClass(this, ServiceActivity.class);

      // Initialize a TabSpec for each tab and add it to the TabHost
      spec = tabHost.newTabSpec("services");
      spec.setIndicator(res.getText(R.string.tab_service), res.getDrawable(R.drawable.ic_tab_services));
      spec.setContent(intent);
      tabHost.addTab(spec);

      intent = new Intent().setClass(this, InfoActivity.class);
      spec = tabHost.newTabSpec("info");
      spec.setIndicator(res.getText(R.string.tab_info), res.getDrawable(R.drawable.ic_tab_info));
      spec.setContent(intent);
      tabHost.addTab(spec);

      tabHost.setCurrentTab(0);
   }

   /**
    * Changes the title bar back and stops the spinner.
    */
   class StopScanTask extends TimerTask {
      @Override
      public void run() {
         Log.i(TAG, "StopScanTask timer is up!");
         runOnUiThread(new Runnable() {
            public void run() {
               setProgressBarIndeterminateVisibility(false);
               setTitle(R.string.app_name);
               ServiceActivity.scanFinished(getApplicationContext());
            }
         });
      }
   }

   /*
    * (non-Javadoc)
    * @see android.app.Activity#onStart()
    */
   @Override
   protected void onStart() {
      Log.i(TAG, "Starting ZeroConf...");
      super.onStart();
   }

   /*
    * (non-Javadoc)
    * @see android.app.ActivityGroup#onStop()
    */
   @Override
   protected void onStop() {
      Log.i(TAG, "Stopping ZeroConf...");
      try {
         // clean up in case the scanner is still scanner before the user quits
         timer.cancel();
         timer.purge();
         ServiceActivity.scanFinished(getApplicationContext());
      } catch (Exception e) {
         Log.w("Exception", e);
      }
      super.onStop();
   }

}