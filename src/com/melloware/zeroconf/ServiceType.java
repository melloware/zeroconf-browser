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

import java.util.HashMap;

/**
 * Service Type POJO used to render the name and icon of a Service Type.
 * <p>
 * Copyright (c) 2010 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 1.0
 */
public class ServiceType implements Comparable<ServiceType> {

   /*
    * Constants for DNS record types
    */
   public static final String ADISK = "_ADISK";
   public static final String AFP = "_AFPOVERTCP";
   public static final String AIRFOIL = "_AIRFOILSPEAKER";
   public static final String AIRPLAY = "_AIRPLAY";
   public static final String AIRPORT = "_AIRPORT";
   public static final String AIRVIDEO = "_AIRVIDEOSERVER";
   public static final String APPLETV = "_APPLETV";
   public static final String APPLETV2 = "_APPLETV-V2";
   public static final String APPLETV_ITUNES = "_APPLETV-ITUNES";
   public static final String APPLE_IPHONE = "_APPLE-MOBDEV";
   public static final String ARCP = "_ARCP";
   public static final String DAAP = "_DAAP";
   public static final String DACP = "_DACP";
   public static final String DPAP = "_DPAP";
   public static final String EPPC = "_EPPC";
   public static final String FTP = "_FTP";
   public static final String GNTP = "_GNTP";
   public static final String GROWL = "_GROWL";
   public static final String HOMESHARING = "_HOME-SHARING";
   public static final String HTTP = "_HTTP";
   public static final String ICHAT = "_ICHAT";
   public static final String IMONKEY = "_IMONKEY";
   public static final String INTELLIERMOTE = "_INTELLIREMOTE";
   public static final String FAX_IPP = "_FAX-IPP";
   public static final String IPP = "_IPP";
   public static final String LIGHTSWITCH = "_LIGHTSWITCH";
   public static final String NETASSISTANT = "_NET-ASSISTANT";
   public static final String NSSOCKETPORT = "_NSSOCKETPORT";
   public static final String ODISK = "_ODISK";
   public static final String PASSWORD1 = "_1PASSWORD";
   public static final String PDL = "_PDL-DATASTREAM";
   public static final String PRESENCE = "_PRESENCE";
   public static final String PRINTER = "_PRINTER";
   public static final String RAOP = "_RAOP";
   public static final String READYNAS = "_READYNAS";
   public static final String RFB = "_RFB";
   public static final String RIVERMOTE = "_RIVERMOTE";
   public static final String RTSP = "_RTSP";
   public static final String SAMBA = "_SMB";
   public static final String SCANNER = "_SCANNER";
   public static final String SFTPSSH = "_SFTP-SSH";
   public static final String SLEEPPROXY = "_SLEEP-PROXY";
   public static final String SQP = "_SQP";
   public static final String SSH = "_SSH";
   public static final String TFTP = "_TFTP";
   public static final String TIVOREMOTE = "_TIVO-REMOTE";
   public static final String TIVOVIDEOS = "_TIVO-VIDEOS";
   public static final String TOUCHABLE = "_TOUCH-ABLE";
   public static final String TOUCHREMOTE = "_TOUCH-REMOTE";
   public static final String TRANSMISSION = "_TRANSMISSIONSERVER";
   public static final String WORKSTATION = "_WORKSTATION";
   public static final String X10 = "_X10COMM";
   public static final String ZWAVE = "_ZWAVECOMM";

   /**
    * The name of the service type like _daap.tcp.local
    */
   private String name;

   /**
    * The type of service like _DAAP after trim and upper-casing.
    */
   private String type;

   /**
    * Map of types to icons and names.
    */
   private static final HashMap<String, ServiceRecord> MAPPING = new HashMap<String, ServiceRecord>();

   // static initializer
   static {
      MAPPING.put(ADISK, new ServiceRecord("Apple Disk", R.drawable.ic_svc_adisk));
      MAPPING.put(AFP, new ServiceRecord("Apple File Service", R.drawable.ic_svc_adisk));
      MAPPING.put(AIRFOIL, new ServiceRecord("Airfoil Speakers", R.drawable.ic_svc_airfoil));
      MAPPING.put(AIRPLAY, new ServiceRecord("Apple Airplay", R.drawable.ic_svc_appletv));
      MAPPING.put(AIRPORT, new ServiceRecord("Apple Airport", R.drawable.ic_svc_airport));
      MAPPING.put(AIRVIDEO, new ServiceRecord("Air Video Server", R.drawable.ic_svc_airvideo));
      MAPPING.put(APPLETV, new ServiceRecord("Apple TV", R.drawable.ic_svc_appletv));
      MAPPING.put(APPLE_IPHONE, new ServiceRecord("Apple iPhone", R.drawable.ic_svc_iphone));
      MAPPING.put(APPLETV2, new ServiceRecord("Apple TV2", R.drawable.ic_svc_appletv));
      MAPPING.put(APPLETV_ITUNES, new ServiceRecord("Apple TV iTunes", R.drawable.ic_svc_appletv));
      MAPPING.put(ARCP, new ServiceRecord("QuickTime Streaming Server", R.drawable.ic_svc_quicktime));
      MAPPING.put(DAAP, new ServiceRecord("Digital Audio Access Protocol", R.drawable.ic_svc_music));
      MAPPING.put(DACP, new ServiceRecord("Digital Audio Control Protocol", R.drawable.ic_svc_music));
      MAPPING.put(DPAP, new ServiceRecord("Digital Photo Access Protocol", R.drawable.ic_svc_iphoto));
      MAPPING.put(EPPC, new ServiceRecord("Remote Apple Events", R.drawable.ic_svc_adisk));
      MAPPING.put(FTP, new ServiceRecord("File Transport Protocol (FTP)", R.drawable.ic_svc_ftp));
      MAPPING.put(GNTP, new ServiceRecord("Growl Network Protocol", R.drawable.ic_svc_growl));
      MAPPING.put(GROWL, new ServiceRecord("Growl", R.drawable.ic_svc_growl));
      MAPPING.put(HOMESHARING, new ServiceRecord("Home Sharing", R.drawable.ic_svc_music));
      MAPPING.put(HTTP, new ServiceRecord("HTTP Server", R.drawable.ic_svc_http));
      MAPPING.put(ICHAT, new ServiceRecord("iChat", R.drawable.ic_svc_ichat));
      MAPPING.put(IMONKEY, new ServiceRecord("iMonkey", R.drawable.ic_svc_music));
      MAPPING.put(INTELLIERMOTE, new ServiceRecord("Intelliremote", R.drawable.ic_svc_intelliremote));
      MAPPING.put(FAX_IPP, new ServiceRecord("FAX Internet Printing Protocol", R.drawable.ic_svc_print));
      MAPPING.put(IPP, new ServiceRecord("Internet Printing Protocol", R.drawable.ic_svc_print));
      MAPPING.put(LIGHTSWITCH, new ServiceRecord("Lightswitch", R.drawable.ic_svc_homeautomation));
      MAPPING.put(NETASSISTANT, new ServiceRecord("Apple Remote Desktop", R.drawable.ic_svc_workstation));
      MAPPING.put(NSSOCKETPORT, new ServiceRecord("NSSocketPort", R.drawable.ic_svc_network));
      MAPPING.put(ODISK, new ServiceRecord("CD-DVD Sharing", R.drawable.ic_svc_dvd));
      MAPPING.put(PASSWORD1, new ServiceRecord("1Password", R.drawable.ic_svc_ssh));
      MAPPING.put(PDL, new ServiceRecord("Print PDL Stream", R.drawable.ic_svc_print));
      MAPPING.put(PRESENCE, new ServiceRecord("Bonjour Chat", R.drawable.ic_svc_ichat));
      MAPPING.put(PRINTER, new ServiceRecord("Printer", R.drawable.ic_svc_print));
      MAPPING.put(RAOP, new ServiceRecord("Remote Audio Output Protocol", R.drawable.ic_svc_music));
      MAPPING.put(READYNAS, new ServiceRecord("ReadyNAS", R.drawable.ic_svc_readynas));
      MAPPING.put(RFB, new ServiceRecord("Screen Sharing", R.drawable.ic_svc_workstation));
      MAPPING.put(RIVERMOTE, new ServiceRecord("Rivermote", R.drawable.ic_svc_music));
      MAPPING.put(RTSP, new ServiceRecord("Real Time Streaming Protocol", R.drawable.ic_svc_quicktime));
      MAPPING.put(SAMBA, new ServiceRecord("Samba Share", R.drawable.ic_svc_network));
      MAPPING.put(SCANNER, new ServiceRecord("Scanner", R.drawable.ic_svc_scanner));
      MAPPING.put(SFTPSSH, new ServiceRecord("Secure FTP SSH", R.drawable.ic_svc_ssh));
      MAPPING.put(SLEEPPROXY, new ServiceRecord("Bonjour Sleep Proxy", R.drawable.ic_svc_music));
      MAPPING.put(SQP, new ServiceRecord("SquareConnect Blaster", R.drawable.ic_svc_intelliremote));
      MAPPING.put(SSH, new ServiceRecord("Secure Shell (SSH)", R.drawable.ic_svc_ssh));
      MAPPING.put(TFTP, new ServiceRecord("Trivial File Transfer Protocol", R.drawable.ic_svc_ftp));
      MAPPING.put(TIVOREMOTE, new ServiceRecord("Tivo Remote", R.drawable.ic_svc_tivo));
      MAPPING.put(TIVOVIDEOS, new ServiceRecord("Tivo Videos", R.drawable.ic_svc_tivo));
      MAPPING.put(TOUCHABLE, new ServiceRecord("DACP Server", R.drawable.ic_svc_music));
      MAPPING.put(TOUCHREMOTE, new ServiceRecord("DACP Remote", R.drawable.ic_svc_music));
      MAPPING.put(TRANSMISSION, new ServiceRecord("Transmission BitTorrent", R.drawable.ic_svc_bittorrent));
      MAPPING.put(WORKSTATION, new ServiceRecord("Workstation", R.drawable.ic_svc_workstation));
      MAPPING.put(X10, new ServiceRecord("X10 Commander", R.drawable.ic_svc_homeautomation));
      MAPPING.put(ZWAVE, new ServiceRecord("Lightswitch Server", R.drawable.ic_svc_homeautomation));
   }

   /**
    * Gets the name.
    * <p>
    * @return Returns the name.
    */
   public String getName() {
      return name;
   }

   /**
    * Sets the name.
    * <p>
    * @param name The name to set.
    */
   public void setName(String name) {
      this.name = name;
      this.type = this.name;
      this.type = this.type.toUpperCase();
      final String[] split = this.type.split("\\.");
      this.type = split[0];
   }

   /**
    * Sort by name value
    */
   public int compareTo(ServiceType type) {
      return this.name.compareTo(type.getName());
   }

   /*
    * (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (!this.getClass().equals(obj.getClass()))
         return false;
      ServiceType type = (ServiceType) obj;
      return (this.name.equals(type.name));

   }

   /*
    * (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode() {
      int hash = 1;
      hash = hash * 31 + this.name.hashCode();
      return hash;
   }

   /*
    * (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      String result = this.type;
      if (MAPPING.containsKey(result)) {
         result = MAPPING.get(result).getName();
      }
      return result;
   }

   /**
    * Gets the current icon based on service type.
    * <p>
    * @return the R.drawable value of the image
    */
   public int getImageIcon() {
      int result = R.drawable.icon;
      final String serviceType = this.type;

      if (MAPPING.containsKey(serviceType)) {
         result = MAPPING.get(serviceType).getIcon();
      }

      return result;
   }

}
