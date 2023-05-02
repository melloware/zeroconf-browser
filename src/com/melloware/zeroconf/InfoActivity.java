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

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Info tab in the main UI.
 * <p>
 * Copyright (c) 2010 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 1.0
 */
public class InfoActivity extends Activity {

   private static final String TAG = InfoActivity.class.getName();

   /*
    * (non-Javadoc)
    * @see android.app.Activity#onCreate(android.os.Bundle)
    */
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.info_scrollview);

      try {
         TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
         String clientVersion = "???";
         PackageInfo pi;
         try {
            pi = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            clientVersion = pi.versionName;
         } catch (NameNotFoundException e) {
            clientVersion = "???";
         }
         txtTitle.setText(getResources().getString(R.string.app_name) + " " + clientVersion);

         Button btnWebsite = (Button) findViewById(R.id.btnWebsite);
         Button btnSupport = (Button) findViewById(R.id.btnSupport);
         ImageView imgIcon = (ImageView) findViewById(R.id.imgIcon);

         btnWebsite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               String url = getResources().getString(R.string.app_url);
               Intent i = new Intent(Intent.ACTION_VIEW);
               i.setData(Uri.parse(url));
               startActivity(i);
            }
         });
         btnSupport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               String url = getResources().getString(R.string.app_forum);
               Intent i = new Intent(Intent.ACTION_VIEW);
               i.setData(Uri.parse(url));
               startActivity(i);
            }
         });
         imgIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               String url = getResources().getString(R.string.app_url);
               Intent i = new Intent(Intent.ACTION_VIEW);
               i.setData(Uri.parse(url));
               startActivity(i);
            }
         });
      } catch (Exception ex) {
         Log.e(TAG, ex.getMessage(), ex);
      }
   }

}
