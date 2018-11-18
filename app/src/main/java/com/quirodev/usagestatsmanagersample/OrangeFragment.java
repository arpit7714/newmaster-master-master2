package com.quirodev.usagestatsmanagersample;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActionBarContainer;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quirodev.data.dbcontract;
import com.quirodev.data.dbhelper;
import com.quirodev.data.dbprovider;
import com.quirodev.usagestatsmanagersample.R;

import static com.quirodev.usagestatsmanagersample.appitemdisplay.appname1;

public class OrangeFragment extends Fragment{

    public String usage,pkname2;
    public static dbprovider obj;

    public View v;
    public OrangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obj=new dbprovider(getContext());
      //  displaydatabaseinfo(appname1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //actionBar.setDisplayHomeAsUpEnabled(true);
        v = inflater.inflate(R.layout.fragmentorange, container, false);
        ImageView appicon=(ImageView) v.findViewById(R.id.appicon);
       appicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetail();
            }
        });
        String myValue = this.getArguments().getString("appname1");
        TextView t1= (TextView) v.findViewById(R.id.appname);
        TextView t3= (TextView) v.findViewById(R.id.usage1);
        TextView t4 = (TextView) v.findViewById(R.id.usage2);
        t1.setText(myValue);
    //    displaydatabaseinfo(myValue);
        pkname2=this.getArguments().getString("pkname");
        t1.setText(pkname2);
        //Log.v("testing1",pkname);
        Bundle extras = this.getArguments().getBundle("extras");
        Bitmap bmp=(Bitmap) extras.getParcelable("icon");
        appicon.setImageBitmap(bmp);
        usage = this.getArguments().getString("usage");
        TextView t2= (TextView) v.findViewById(R.id.usagetime);
        t2.setText(usage);
        final Button mOpenButton = v.findViewById(R.id.open);
        final Button mOpenButton2 = v.findViewById(R.id.open2);
        mOpenButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetail();
            }
        });
        final Intent LaunchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(pkname2);
        if (LaunchIntent == null) {
            mOpenButton.setClickable(false);
            mOpenButton.setAlpha(0.5f);
        } else {
            mOpenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(LaunchIntent);
                }
            });
        }

         //---------------------querying the data from database-------------------------------
        Log.v("apppn",appname1);
        dbhelper mdbhelper= new dbhelper(getActivity());
        SQLiteDatabase db= mdbhelper.getReadableDatabase();
        Log.v("testing123","d");
        //Cursor cursor=db.rawQuery("Select * from "+appdata.TABLE_NAME,null);
        String[] projection={
                dbcontract.appdata.APP_NAME,
                dbcontract.appdata._AL,
                dbcontract.appdata.APP_DURATION,
                dbcontract.appdata._ID
        };
        //String selection=dbcontract.appdata.APP_NAME+"=?";
        String [] args={appname1,};

        Cursor cursor = db.query(
                dbcontract.appdata.TABLE_NAME,
                projection,dbcontract.appdata.APP_NAME+"=?",args
                ,null,
                null,
                null
        );
        if (cursor!=null) {
            cursor.moveToLast();
            int appcolumnindex = cursor.getColumnIndex(dbcontract.appdata.APP_NAME);
            int appduration = cursor.getColumnIndex(dbcontract.appdata.APP_DURATION);
            int date=cursor.getColumnIndex(dbcontract.appdata._AL);
            TextView displayview=(TextView)v.findViewById(R.id.database);
           // displayview.setText("Number of rows in database table: " + cursor.getCount());
            Log.v("cursor",String.valueOf(cursor.getCount()));

                String appname = cursor.getString(appcolumnindex);
                String duration = cursor.getString(appduration);
                // displayview.append("\n"+appname+"  "+duration+" "+datecol+"\n");
               // Log.v("data123",appname+"  "+duration+"  "+datecol);
                t1.setText(appname);
                long num = Long.parseLong(duration);
                t4.setText("Today Usage : " + String.valueOf(DateUtils.covertingtime(num)));
                cursor.moveToPrevious();
                /*if (cursor!=null){
                    int appduration1 = cursor.getColumnIndex(dbcontract.appdata.APP_DURATION);
                    String duration1 = cursor.getString(appduration1);
                    long num1 = Long.parseLong(duration1);
                    t3.setText("Yesterday Usage : " + String.valueOf(DateUtils.covertingtime(num1)));
                }*/

            cursor.close();
        }
        //--------------------------------------------------------------------------------------


        return v;
    }

    private void openDetail() {
        Intent intent = new Intent(
                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + pkname2));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}