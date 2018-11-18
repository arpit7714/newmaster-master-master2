package com.quirodev.usagestatsmanagersample;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import android.widget.ToggleButton;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.BarEntry;
import com.quirodev.data.dbcontract;
import com.quirodev.data.dbhelper;
import com.quirodev.data.dbprovider;
import com.quirodev.usagestatsmanagersample.MainActivity;
import com.quirodev.usagestatsmanagersample.R;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.quirodev.usagestatsmanagersample.appitemdisplay.abc;
import static com.quirodev.usagestatsmanagersample.appitemdisplay.appname1;

public class GrapesFragment extends Fragment {

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static GrapesFragment inst;
    private TextView alarmTextView;
    public dbprovider db2;
    int hours = 1;
    int min = 0;
    Calendar calendar = null;

    public GrapesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db2 = new dbprovider(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmentgrapes, container, false);
        // Inflate the layout for this fragment
        Button b1 = (Button) v.findViewById(R.id.button);
        TimePicker simpleTimePicker = (TimePicker) v.findViewById(R.id.simpleTimePicker);
        simpleTimePicker.setHour(1);
        simpleTimePicker.setIs24HourView(true);
        simpleTimePicker.setMinute(0);
        //int hours =simpleTimePicker.getHour();
        //int minutes = simpleTimePicker.getMinute();
        TextView tr = (TextView) v.findViewById(R.id.displaytime);
       // db.insertvalue("overall", 2, 1);




        simpleTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                hours = view.getHour();
                min = view.getMinute();
                Log.v("data00", appname1);
                Log.v("timer1212", String.valueOf(hours));
                Log.v("timer1212", String.valueOf(min));

                //db.insertvalue(appname1,hours,min);
                //tr.append(String.valueOf(hours)+" "+String.valueOf(min));
                col(hours,min);

            }
        });
        //--------------------------------------to access value in  datatbase-------------------
        dbhelper mdbhelper = new dbhelper(getActivity());
        SQLiteDatabase db = mdbhelper.getReadableDatabase();
        //Cursor cursor=db.rawQuery("Select * from "+appdata.TABLE_NAME,null);
        String[] projection = {
                dbcontract.notification.APP_NAME,
                dbcontract.notification.HOURS,
                dbcontract.notification.MIN
        };
        //String selection=dbcontract.appdata.APP_NAME+"=?";
        String[] args = {appname1,};

        Cursor cursor = db.query(
                dbcontract.notification.TABLE_NAME,
                projection, dbcontract.notification.APP_NAME + "=?", args
                , null,
                null,
                null
        );
        if (cursor != null) {
            int appcolumnindex = cursor.getColumnIndex(dbcontract.notification.APP_NAME);
            int hourindex = cursor.getColumnIndex(dbcontract.notification.HOURS);
            int minindex = cursor.getColumnIndex(dbcontract.notification.MIN);
            //TextView displayview=(TextView)v.findViewById(R.id.database);
            //displayview.setText("Number of rows in database table: " + cursor.getCount());
            Log.v("cursor1212", String.valueOf(cursor.getCount()));
            while (cursor.moveToNext()) {
                String appname = cursor.getString(appcolumnindex);
                String hoursduration = cursor.getString(hourindex);
                String minutes = cursor.getString(minindex);
                tr.append(appname+" "+hoursduration+"  "+minutes);
            }
            cursor.close();
        }
//-----------------------------------------------------------------------------------------
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();//Notification();
            }
        });
        return v;
       }
        public void showNotification () {

            // define sound URI, the sound to be played when there's a notification

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
            builder.setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(getString(R.string.close))
                    .setContentText("hleogrkgj")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH);


            builder.setAutoCancel(true);
            NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, builder.build());

            }
            public void col(int h,int m){
                db2.insertvalue(appname1,h,m);
            }
    }







