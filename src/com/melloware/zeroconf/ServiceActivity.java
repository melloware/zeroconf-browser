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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeMap;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceTypeListener;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Service tab which contains the expandable list of DNS Services and the
 * details of each service type as they are gathered.
 * <p>
 * Copyright (c) 2010 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 1.0
 */
public class ServiceActivity extends ExpandableListActivity implements ServiceListener, ServiceTypeListener {

   /**
    * Tag used for logging
    */
   private static final String TAG = ServiceActivity.class.getName();

   /**
    * Value used to identify in ZeroConf
    */
   private static final String HOSTNAME = "melloware";

   /**
    * Sorted array of top level items which are the Service Types
    */
   public static final ArrayList<ServiceType> GROUPS = new ArrayList<ServiceType>();

   /**
    * Sorted list of the details for each service type
    */
   public static final TreeMap<String, ArrayList<ServiceInfo>> DETAILS = new TreeMap<String, ArrayList<ServiceInfo>>();

   /**
    * Instance of Bonjour/Rendezvous/ZeroConf handler
    */
   public static JmDNS jmdns = null;

   /**
    * Allows an application to receive Wifi Multicast packets.
    */
   private static MulticastLock multicastLock = null;

   /**
    * The backing adapter for the ListView of services
    */
   private static DNSExpandableListAdapter mAdapter;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      mAdapter = new DNSExpandableListAdapter();
      setListAdapter(mAdapter);
   }

   /*
    * (non-Javadoc)
    * @see android.app.Activity#onStart()
    */
   @Override
   protected void onStart() {
      Log.i(TAG, "Starting ServiceActivity...");
      super.onStart();
      new Thread() {
         @Override
         public void run() {
            try {
               Log.i(TAG, "Starting Mutlicast Lock...");
               WifiManager wifi = (WifiManager) ServiceActivity.this.getSystemService(Context.WIFI_SERVICE);
               // get the device ip address
               final InetAddress deviceIpAddress = getDeviceIpAddress(wifi);
               multicastLock = wifi.createMulticastLock(getClass().getName());
               multicastLock.setReferenceCounted(true);
               multicastLock.acquire();
               Log.i(TAG, "Starting ZeroConf probe....");
               jmdns = JmDNS.create(deviceIpAddress, HOSTNAME);
               jmdns.addServiceTypeListener(ServiceActivity.this);
            } catch (IOException ex) {
               Log.e(TAG, ex.getMessage(), ex);
            }
            Log.i(TAG, "Started ZeroConf probe....");
         }
      }.start();
   }

   /*
    * (non-Javadoc)
    * @see android.app.ActivityGroup#onStop()
    */
   @Override
   protected void onStop() {
      Log.i(TAG, "Stopping ServiceActivity...");
      super.onStop();
      stopScan();
      DETAILS.clear();
      GROUPS.clear();
      if (!isFinishing()) {
         mAdapter.notifyDataSetChanged();
      }
   }

   /**
    * Stops scanning and cleans up locks.
    */
   private static void stopScan() {
      new Thread() {
         @Override
         public void run() {
            try {
               if (jmdns != null) {
                  Log.i(TAG, "Stopping ZeroConf probe....");
                  jmdns.unregisterAllServices();
                  jmdns.close();
                  jmdns = null;
               }
               if (multicastLock != null) {
                  Log.i(TAG, "Releasing Mutlicast Lock...");
                  multicastLock.release();
                  multicastLock = null;
               }
            } catch (Exception ex) {
               Log.e(TAG, ex.getMessage(), ex);
            }
         }
      }.start();
   }

   /**
    * Gets the current Android device IP address or return 10.0.0.2 which is
    * localhost on Android.
    * <p>
    * @return the InetAddress of this Android device
    */
   private InetAddress getDeviceIpAddress(WifiManager wifi) {
      InetAddress result = null;
      try {
         // default to Android localhost
         result = InetAddress.getByName("10.0.0.2");

         // figure out our wifi address, otherwise bail
         WifiInfo wifiinfo = wifi.getConnectionInfo();
         int intaddr = wifiinfo.getIpAddress();
         byte[] byteaddr = new byte[] { (byte) (intaddr & 0xff), (byte) (intaddr >> 8 & 0xff),
                  (byte) (intaddr >> 16 & 0xff), (byte) (intaddr >> 24 & 0xff) };
         result = InetAddress.getByAddress(byteaddr);
      } catch (UnknownHostException ex) {
         Log.w(TAG, String.format("getDeviceIpAddress Error: %s", ex.getMessage()));
      }

      return result;
   }

   /**
    * Delegate method from mDNS when a service is added.
    */
   public void serviceAdded(ServiceEvent event) {
      Log.i(TAG, String.format("ZeroConf serviceAdded(event=\n%s\n)", event.toString()));
      ArrayList<ServiceInfo> list = DETAILS.get(event.getType());
      if (list != null) {
         ServiceInfo info = event.getInfo();
         if (!list.contains(info)) {
            list.add(info);
         }
      }
   }

   /**
    * Delegate method from mDNS when a service is removed.
    */
   public void serviceRemoved(ServiceEvent event) {
      Log.w(TAG, String.format("ZeroConf serviceRemoved(event=\n%s\n)", event.toString()));
   }

   /**
    * Delegate method from mDNS when a service is resolved.
    */
   public void serviceResolved(ServiceEvent event) {
      Log.i(TAG, String.format("ZeroConf serviceResolved(event=\n%s\n)", event.toString()));
      ArrayList<ServiceInfo> list = DETAILS.get(event.getType());
      if (list != null) {
         ServiceInfo info = event.getInfo();
         if (!list.contains(info)) {
            list.add(info);
         }
      }
   }

   /**
    * Delegate method from mDNS when a new service type is discovered.
    */
   public void serviceTypeAdded(final ServiceEvent event) {
      Log.i(TAG, String.format("ZeroConf serviceTypeAdded(event=\n%s\n)", event.toString()));
      jmdns.addServiceListener(event.getType(), this);
      runOnUiThread(new Runnable() {
         public void run() {
            final ServiceType type = new ServiceType();
            type.setName(event.getType());
            GROUPS.add(type);
            Collections.sort(GROUPS);
            DETAILS.put(event.getType(), new ArrayList<ServiceInfo>());
            mAdapter.notifyDataSetChanged();
         }
      });
   }

   /**
    * Delegate method from mDNS when a subtype is discovered.
    */
   public void subTypeForServiceTypeAdded(ServiceEvent event) {
      Log.i(TAG, String.format("ZeroConf subTypeForServiceTypeAdded(event=\n%s\n)", event.toString()));
   }

   /**
    * When a scan is complete show a message if no services found.
    * <p>
    * @param context the ApplicationContext
    */
   public static void scanFinished(Context context) {
      if (GROUPS.size() == 0) {
         final ServiceType type = new ServiceType();
         type.setName(context.getResources().getString(R.string.msg_noservices));
         GROUPS.add(type);
         mAdapter.notifyDataSetChanged();
         stopScan();
      }
   }

   /**
    * ExpandableListAdapter that displays the Service Types as groups and when
    * each Service Type is expanded displays a list of all discovered Services
    * for that Service Type.
    */
   public class DNSExpandableListAdapter extends BaseExpandableListAdapter {

      LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

      public Object getChild(int groupPosition, int childPosition) {
         try {
            Iterator<ArrayList<ServiceInfo>> it = DETAILS.values().iterator();
            int i = 0;
            while (it.hasNext()) {
               ArrayList<ServiceInfo> type = it.next();
               if (i == groupPosition) {
                  ServiceInfo service = type.get(childPosition);
                  ServiceInfo resolvedService = jmdns.getServiceInfo(service.getType(), service.getName());
                  if (resolvedService != null) {
                     service = resolvedService;
                  }
                  StringBuffer buf = new StringBuffer();
                  buf.append("<b>");
                  buf.append(service.getName());
                  buf.append("</b><br/>");
                  buf.append(service.getTypeWithSubtype());
                  buf.append("<br/>");
                  buf.append(service.getServer());
                  buf.append(':');
                  buf.append(service.getPort());
                  buf.append("<br/>");
                  buf.append(service.getInetAddresses()[0]);
                  buf.append("<br/>");
                  for (Enumeration<String> names = service.getPropertyNames(); names.hasMoreElements();) {
                     buf.append("<br/>");
                     String prop = names.nextElement();
                     buf.append("<b>");
                     buf.append(prop);
                     buf.append("</b>");
                     buf.append(" = ");
                     buf.append("<i>");
                     buf.append(service.getPropertyString(prop));
                     buf.append("</i>");
                  }
                  return buf.toString();
               }
               i++;
            }
         } catch (Exception e) {
            Log.w("Exception", e);
         }

         return "Not Available";
      }

      public long getChildId(int groupPosition, int childPosition) {
         return childPosition;
      }

      public int getChildrenCount(int groupPosition) {
         Iterator<ArrayList<ServiceInfo>> it = DETAILS.values().iterator();
         int i = 0;
         while (it.hasNext()) {
            ArrayList<ServiceInfo> type = it.next();
            if (i == groupPosition) {
               return type.size();
            }
            i++;
         }
         return 1;
      }

      public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
               ViewGroup parent) {
         convertView = this.inflater.inflate(R.layout.child_row, parent, false);
         ((TextView) convertView.findViewById(R.id.childvalue)).setText(Html.fromHtml(getChild(groupPosition,
                  childPosition).toString()));
         return convertView;
      }

      public Object getGroup(int groupPosition) {
         return GROUPS.get(groupPosition);
      }

      public int getGroupCount() {
         return GROUPS.size();
      }

      public long getGroupId(int groupPosition) {
         return groupPosition;
      }

      public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
         convertView = this.inflater.inflate(R.layout.group_row, parent, false);
         ServiceType type = (ServiceType) getGroup(groupPosition);
         ImageView imageView = (ImageView) convertView.findViewById(R.id.serviceicon);
         imageView.setImageResource(type.getImageIcon());
         ((TextView) convertView.findViewById(R.id.service)).setText(type.toString());
         return convertView;
      }

      public boolean isChildSelectable(int groupPosition, int childPosition) {
         return true;
      }

      public boolean hasStableIds() {
         return true;
      }
   }

}
