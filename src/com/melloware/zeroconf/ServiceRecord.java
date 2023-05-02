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

/**
 * A mapping of Service type to name to icon and proper name.
 * <p>
 * Copyright (c) 2010 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 1.0
 */
public class ServiceRecord {

   private String name;
   private int icon;

   /**
    * Constructor requiring both params
    * <p>
    * @param aName the name of the service
    * @param aIcon the icon of the service
    */
   public ServiceRecord(String aName, int aIcon) {
      this.name = aName;
      this.icon = aIcon;
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
   }

   /**
    * Gets the icon.
    * <p>
    * @return Returns the icon.
    */
   public int getIcon() {
      return icon;
   }

   /**
    * Sets the icon.
    * <p>
    * @param icon The icon to set.
    */
   public void setIcon(int icon) {
      this.icon = icon;
   }

}
