package com.melloware.zeroconf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

public class PostMortemReportExceptionHandler implements UncaughtExceptionHandler, Runnable {

   public static final String EXCEPTION_REPORT_FILE = "zeroconf.trace";

   // "app title + this tag" = email subject
   private static final String MSG_SUBJECT_TAG = "Exception Report";

   // email will be sent to this account
   private static final String MSG_SENDTO = "mellowaredev@gmail.com";

   // the following may be something you wish to consider localizing
   private static final String MSG_BODY = "Please help by sending this email. "
            + "No personal information is being sent (you can check by reading the rest of the email).";

   private final Thread.UncaughtExceptionHandler mDefaultUEH;
   private Activity mApp = null;

   public PostMortemReportExceptionHandler(Activity aApp) {
      mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
      mApp = aApp;
   }

   @Override
   protected void finalize() throws Throwable {
      if (Thread.getDefaultUncaughtExceptionHandler().equals(this))
         Thread.setDefaultUncaughtExceptionHandler(mDefaultUEH);
      super.finalize();
   }

   public void uncaughtException(Thread t, Throwable e) {
      submit(e);
      // do not forget to pass this exception through up the chain
      if (mDefaultUEH != null)
         mDefaultUEH.uncaughtException(t, e);
   }

   public String getDebugReport(Throwable aException) {
      if (aException == null) {
         return "Null Exception";
      }
      NumberFormat theFormatter = new DecimalFormat("#0.");
      String theErrReport = "";

      PackageManager pm = mApp.getPackageManager();
      String packageName = mApp.getPackageName();

      String clientVersion = "???";
      try {
         final PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
         clientVersion = pi.versionName;
      } catch (NameNotFoundException e) {
         clientVersion = "???";
      }

      theErrReport += packageName + " " + clientVersion + " generated the following exception:\n";
      theErrReport += aException.toString() + "\n\n";

      // stack trace
      StackTraceElement[] theStackTrace = aException.getStackTrace();
      if (theStackTrace.length > 0) {
         theErrReport += "--------- Stack trace ---------\n";
         for (int i = 0; i < theStackTrace.length; i++) {
            theErrReport += theFormatter.format(i + 1) + "\t" + theStackTrace[i].toString() + "\n";
         }// for
         theErrReport += "-------------------------------\n\n";
      }

      // if the exception was thrown in a background thread inside
      // AsyncTask, then the actual exception can be found with getCause
      Throwable theCause = aException.getCause();
      if (theCause != null) {
         theErrReport += "----------- Cause -----------\n";
         theErrReport += theCause.toString() + "\n\n";
         theStackTrace = theCause.getStackTrace();
         for (int i = 0; i < theStackTrace.length; i++) {
            theErrReport += theFormatter.format(i + 1) + "\t" + theStackTrace[i].toString() + "\n";
         }// for
         theErrReport += "-----------------------------\n\n";
      }// if

      // app environment
      PackageInfo pi;
      try {
         pi = pm.getPackageInfo(packageName, 0);
      } catch (NameNotFoundException eNnf) {
         // doubt this will ever run since we want info about our own package
         pi = new PackageInfo();
         pi.versionName = "unknown";
         pi.versionCode = 69;
      }
      Date theDate = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss_zzz");
      theErrReport += "-------- Environment --------\n";
      theErrReport += "Time\t=" + sdf.format(theDate) + "\n";
      theErrReport += "Device\t=" + Build.FINGERPRINT + "\n";
      try {
         Field theMfrField = Build.class.getField("MANUFACTURER");
         theErrReport += "Make\t=" + theMfrField.get(null) + "\n";
      } catch (SecurityException e) {
      } catch (NoSuchFieldException e) {
      } catch (IllegalArgumentException e) {
      } catch (IllegalAccessException e) {
      }
      theErrReport += "Model\t=" + Build.MODEL + "\n";
      theErrReport += "Product\t=" + Build.PRODUCT + "\n";
      theErrReport += "App\t\t=" + mApp.getPackageName() + ", version " + pi.versionName + " (build " + pi.versionCode
               + ")\n";
      theErrReport += "Locale=" + mApp.getResources().getConfiguration().locale.getDisplayName() + "\n";
      theErrReport += "-----------------------------\n\n";

      theErrReport += "END REPORT.";
      return theErrReport;
   }

   protected void saveDebugReport(String aReport) {
      // save report to file
      try {
         FileOutputStream theFile = mApp.openFileOutput(EXCEPTION_REPORT_FILE, Context.MODE_PRIVATE);
         theFile.write(aReport.getBytes());
         theFile.close();
      } catch (IOException ioe) {
         // error during error report needs to be ignored, do not wish to start
         // infinite loop
      }
   }

   public void sendDebugReportToAuthor() {
      String theLine = "";
      String theTrace = "";
      try {
         BufferedReader theReader = new BufferedReader(new InputStreamReader(mApp.openFileInput(EXCEPTION_REPORT_FILE)));
         while ((theLine = theReader.readLine()) != null) {
            theTrace += theLine + "\n";
         }
         if (sendDebugReportToAuthor(theTrace)) {
            mApp.deleteFile(EXCEPTION_REPORT_FILE);
         }
      } catch (FileNotFoundException eFnf) {
         // nothing to do
      } catch (IOException eIo) {
         // not going to report
      }
   }

   public Boolean sendDebugReportToAuthor(String aReport) {
      if (aReport != null) {
         Intent theIntent = new Intent(Intent.ACTION_SEND);
         String theSubject = mApp.getResources().getString(R.string.app_name) + " " + MSG_SUBJECT_TAG;
         String theBody = "\n" + MSG_BODY + "\n\n" + aReport + "\n\n";
         theIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { MSG_SENDTO });
         theIntent.putExtra(Intent.EXTRA_TEXT, theBody);
         theIntent.putExtra(Intent.EXTRA_SUBJECT, theSubject);
         theIntent.setType("message/rfc822");
         Boolean hasSendRecipients = (mApp.getPackageManager().queryIntentActivities(theIntent, 0).size() > 0);
         if (hasSendRecipients) {
            mApp.startActivity(theIntent);
            return true;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   public void run() {
      sendDebugReportToAuthor();
   }

   public void submit(Throwable e) {
      if (e == null) {
         return;
      }
      String theErrReport = getDebugReport(e);
      saveDebugReport(theErrReport);
      // try to send file contents via email (need to do so via the UI thread)
      mApp.runOnUiThread(this);
   }
}
