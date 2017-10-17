package com.luminous.eshopfloor.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by mappsdeveloper on 13-10-2017.
 */

public class TextFile {
    private static String filepath = "ESFAppLog";
    static File myExternalFile;



    //------------------------------------------------------------------------------------ External Storage----------------------------------------------------------------------------------

    static String  strFileRootName = "EshopFloor";
    static String  strFileName;
    static File fileRoot;
    static File fileCompletePath;
    static  StringBuilder text ;
    static FileWriter writer;
    public static  void writeTextFileDataInExternalStorage(Context context, String startExecutionDateTime, String pageName,  String  moduleAPIName , String moduleAPIStatus, String ModuleAPIResponceDataString )
    {
        ctx = context;
        strFileName = getDate_DateTime("Date");
        String nowDateTime = getDate_DateTime("DateTime");
        String totalTimeToExecute=  printDifference(startExecutionDateTime, nowDateTime);
        pageName = getCurrentActivitFragmentName();
        fileRoot = new File(Environment.getExternalStorageDirectory(), strFileRootName);
        if (!fileRoot.exists())
        {
            fileRoot.mkdirs();
            fileCompletePath = new File(fileRoot  , strFileName  + ".txt");
        }
        else
        {
            try
            {
                fileCompletePath = new File(fileRoot  , strFileName  + ".txt");
                text = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(fileCompletePath));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    text.append(line + '\n');
                }
                reader.close();
            }
            catch (IOException e)
            {
                Log.e("C2c", "Error occured while reading text file!!");
            }
        }
        try
        {
            writer = new FileWriter(fileCompletePath);
            String strWriteData = "\n  \n  \n  \n------------------------------------EshopFloopr---Log--------"+nowDateTime+"-----------\nStartTime  :  "+startExecutionDateTime+" \nEndTime    :  "+nowDateTime+" \nTotalTime  :  "+totalTimeToExecute+" \nPageName   :  "+pageName+"  \nAPIName    :  "+moduleAPIName+" \nAPIStatus  :  "+moduleAPIStatus+" \nAPIResponceData :  "+ModuleAPIResponceDataString        +"\n"+text.toString();

            writer.append(strWriteData);
            writer.flush();
            writer.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    public String readTextFileDataFromExternalStorage()
    {
        StringBuilder text = new StringBuilder();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(fileCompletePath));
            String line;
            while ((line = reader.readLine()) != null)
            {
                text.append(line + '\n');
            }
            reader.close();
        }
        catch (IOException e)
        {
            Log.e("C2c", "Error occured while reading text file!!");
        }
        try
        {
            JSONObject jjj = new JSONObject(text.toString());
            Log.e("ParceJsonData", "ParceJsonData :\n"+jjj);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return text.toString();
    }









    //----------------------------------------------------------------------------- Internal Storage------------------------------------------------------------------------------------------
    String myData;
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
    void writeTextFileDataInInternalStorage()
    {
        try {
            FileOutputStream fos = new FileOutputStream(myExternalFile);
            fos.write("TextBody for write all text".getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void  readTextFileDataFromInInternalStorage()
    {
        try {
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }








    // ---------------------------------------------------------------------------------- Date Time Utility-------------------------------------------------------------------------

    public static String getDate_DateTime(String type)
    {
        String TypeValue="";
        if(type.equalsIgnoreCase("date"))
        {
            TypeValue = DateFormat.format("dd-MM-yyyyy", System.currentTimeMillis()).toString();
        }
        else
        {
            //TypeValue   = DateFormat.format("dd-MM-yyyyy-hh:mm:ss:aa", System.currentTimeMillis()).toString();
            TypeValue   = DateFormat.format("dd/MM/yyyyy hh:mm:ss", System.currentTimeMillis()).toString();
        }
        return  TypeValue;
    }



    public  static String printDifference(String startDatee, String endDatee)
    {
        long different = ConvertStringToDate(endDatee).getTime() - ConvertStringToDate(startDatee).getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String HH = "" , MM = "" , SS = "";
        if(elapsedHours<10)
        {
            HH= "0"+elapsedHours;
        }
        else
        {
            HH= ""+elapsedHours;
        }
        if(elapsedMinutes<10)
        {
            MM= "0"+elapsedMinutes;
        }
        else
        {
            MM= ""+elapsedMinutes;
        }
        if(elapsedSeconds<10)
        {
            SS= "0"+elapsedSeconds;
        }
        else
        {
            SS= ""+elapsedSeconds;
        }
        String totalDiffrance = HH+":"+MM+":"+SS+"   Total:-("+different+" Milli Second)";
        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n",elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return totalDiffrance;
    }

    public static Date ConvertStringToDate(String strDate)
    {
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyyy hh:mm:ss");
        try
        {
            date = dateFormat.parse(strDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (Exception e){}
        return  date;
    }



    static Context ctx;
    public static String  getCurrentActivitFragmentName()
    {
        ActivityManager am = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        Log.d("CURRENT Activity ::", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName()+"   Package Name :  "+componentInfo.getPackageName());

        return  taskInfo.get(0).topActivity.getClassName();
    }
}
