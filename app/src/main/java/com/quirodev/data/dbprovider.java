package com.quirodev.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.quirodev.usagestatsmanagersample.AppItem1;
import com.quirodev.usagestatsmanagersample.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class dbprovider {

    private static dbhelper mdbhelper;
    private static dbprovider instance;


    //to get the current date and save into the database
    Date c = Calendar.getInstance().getTime();
    //System.out.println("Current time => " + c);
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    String formattedDate = df.format(c);
    //we make the instance of the sqllitehllper class to
    //get the instance of the database and use mthod like
    //getwritabledatabase() and getreadabledatabase()
    public  dbprovider(Context context){
        mdbhelper=new dbhelper(context);
    }

    //public static dbprovider getinstance(){
    //  return instance;
    //}

    public void method(){
        Log.v("taking easy","yoiod");
    }
    public void insert(AppItem1 appitem){
        ContentValues values = itemToContentValue(appitem);
        if(!exist(appitem)) {
            mdbhelper.getWritableDatabase().insert(dbcontract.appdata.TABLE_NAME, null, values);
        }
        else {
            String []args={appitem.appname,formattedDate};
            mdbhelper.getWritableDatabase().update(dbcontract.appdata.TABLE_NAME,values,
                    dbcontract.appdata.APP_NAME + "=?"+" AND "+ dbcontract.appdata._AL+"=?",
                    args
            );
        }
    }
    private ContentValues itemToContentValue(AppItem1 item) {
        ContentValues values = new ContentValues();
        values.put(dbcontract.appdata.APP_NAME,item.appname);
        Log.v("date",formattedDate);
        values.put(dbcontract.appdata._AL,formattedDate);
        values.put(dbcontract.appdata.APP_DURATION,item.mUsageTime);
        return values;
    }


    public boolean exist(AppItem1 appitem) {
        //SQLiteDatabase database = mdbhelper.getWritableDatabase();
        Cursor cursor = null;
        String[] projection = {
                dbcontract.appdata.APP_NAME,
                dbcontract.appdata._AL,
                dbcontract.appdata.APP_DURATION,
                dbcontract.appdata._ID
        };
        String[] args = {appitem.appname,formattedDate};
        String selection=dbcontract.appdata.APP_NAME+"=?"+" AND "+dbcontract.appdata._AL+"=?";
        cursor = mdbhelper.getReadableDatabase().query(
                dbcontract.appdata.TABLE_NAME,
                projection, selection,
                args
                , null,
                null,
                null
        );
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }


           /* if (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(dbcontract.appdata._ID));
                return id > 0;
            }
        }finally {
            if (cursor!=null)
                cursor.close();
            }
          return false;*/
    }
    public List<AppItem1> getapp(String name){
        Cursor cursor=null;
        List <AppItem1> items=new ArrayList<>();
        try{
            String[] projection={
                    dbcontract.appdata.APP_NAME,
                    dbcontract.appdata.APP_DURATION,
                    dbcontract.appdata._AL,
                    dbcontract.appdata._ID
            };
            //String selection=dbcontract.appdata.APP_NAME+"=?";
            String [] args={name};
            cursor = mdbhelper.getReadableDatabase().query(
                    dbcontract.appdata.TABLE_NAME,
                    projection,dbcontract.appdata.APP_NAME+"=?",args
                    ,null,
                    null,
                    null
            );
            while(cursor.moveToNext()){
                items.add(cursorToItem(cursor));
            }
        }
        finally {
            if(cursor!=null)
                cursor.close();
        }
        return items;

    }
    private AppItem1 cursorToItem(Cursor cursor){
        AppItem1 appitem=new AppItem1();
        appitem.appname=cursor.getString(cursor.getColumnIndex(dbcontract.appdata.APP_NAME));
        appitem.mUsageTime=cursor.getLong(cursor.getColumnIndex(dbcontract.appdata.APP_DURATION));
        return appitem;
    }


    public void insertvalue(String appItem1,int hour,int min){
        ContentValues values=item(appItem1,hour,min);
        if(!exist1(appItem1)) {
            mdbhelper.getWritableDatabase().insert(dbcontract.notification.TABLE_NAME, null, values);
        }
        else {
            String []args={appItem1};
            mdbhelper.getWritableDatabase().update(dbcontract.notification.TABLE_NAME,values,
                    dbcontract.notification.APP_NAME + "=?",
                    args
            );
        }

    }

    private ContentValues item(String appname,int hour,int min) {
        ContentValues values = new ContentValues();
        values.put(dbcontract.notification.APP_NAME,appname);
        //Log.v("date",formattedDate);
        values.put(dbcontract.notification.HOURS,hour);
        values.put(dbcontract.notification.MIN,min);
        return values;
    }



    public boolean exist1(String appname) {
        //SQLiteDatabase database = mdbhelper.getWritableDatabase();
        Cursor cursor = null;
        String[] projection = {
                dbcontract.notification.APP_NAME,
                dbcontract.notification._ID
        };
        String[] args = {appname};
        String selection=dbcontract.notification.APP_NAME+"=?";
        cursor = mdbhelper.getReadableDatabase().query(
                dbcontract.notification.TABLE_NAME,
                projection, selection,
                args
                , null,
                null,
                null
        );
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }


           /* if (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(dbcontract.appdata._ID));
                return id > 0;
            }
        }finally {
            if (cursor!=null)
                cursor.close();
            }
          return false;*/
    }
}