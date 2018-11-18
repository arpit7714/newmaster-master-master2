package com.quirodev.usagestatsmanagersample;

import android.app.ActionBar;
import android.app.AppOpsManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.location.SettingInjectorService;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.transition.Fade;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar;
import android.view.MenuItem;



import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.quirodev.data.dbprovider;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class
MainActivity extends AppCompatActivity implements UsageContract.View {

    private ProgressBar progressBar;
    private TextView permissionMessage;
    private Button button;
    private UsageContract.Presenter presenter;
    private UsageStatAdapter adapter;
    private TextView mSwitchText;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    public static int mTotal;
    public dbprovider mdb;
    public int isnotification=1;
    public TextView tv;
    public AppItem1 overall;
    public static HashMap<String,Long> map = new HashMap<>();
    //public TextView tt;

    //this is used to show the average time smartphone used for today
    private long mtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this,dl,R.string.open, R.string.close);
        //tt=(TextView) findViewById(R.id.description);
        mdb=new dbprovider(getApplicationContext());

        dl.addDrawerListener(t);
        t.syncState();
        tv=(TextView) findViewById(R.id.description);
        tv.setText("Loading.......");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"graph", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), overallgraph.class);
                //  intent.putExtra(appitemdisplay.pkname1,usageStatsWrapper.mPackageName);
                v.getContext().startActivity(intent);
            }
            });
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      /*  nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.ignore:

                    case R.id.settings:

                    case R.id.notification:


                    case R.id.age:
                    default:
                        return true;
                }
            }
        });*/

        mSwitchText= (TextView) findViewById(R.id.enable_text);
        //app transition animation
        // https://guides.codepath.com/android/Shared-Element-Activity-Transition


        //items for spinner
       /* String [] spinneritems={"Today","Yesterday","Weekly","Monthly"};
        Spinner sp=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> newadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinneritems);
        newadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(newadapter);*/
     //   Intent intent = new Intent(this, Main2Activity.class);
      //  startActivity(intent);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        permissionMessage = (TextView) findViewById(R.id.grant_permission_message);
        button =(Button) findViewById(R.id.permissionbutton);

        //as we want to display our data as the linear vertical list , then this is the linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);

        //divider decoration
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        //dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider, getTheme()));
        //recyclerView.addItemDecoration(dividerItemDecoration);
       // recyclerView.addItemDecoration(new LineDividerItemDecoration(this, R.drawable.line_divider))

        adapter = new UsageStatAdapter(this);

        recyclerView.setAdapter(adapter);

        permissionMessage.setVisibility(VISIBLE);
        button.setVisibility(VISIBLE);
        button.setOnClickListener(v -> openSettings());
        mSwitchText.setVisibility(View.GONE);
        presenter = new UsagePresenter(this, this);
        newmethod();
    }

    private void openSettings() {
        Intent intent=new Intent(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public boolean hasPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (appOps != null) {
            int mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), context.getPackageName());
            return mode == AppOpsManager.MODE_ALLOWED;
        }
        return false;
    }

    //when you come back again to that activity after setting on usage access

    protected void newmethod(){
        if (hasPermission(getApplicationContext())) {
            permissionMessage.setVisibility(GONE);
            showProgressBar(true);
            button.setVisibility(GONE);
            mSwitchText.setVisibility(View.VISIBLE);
         //   nv.setVisibility(View.VISIBLE);
           // tt.setVisibility(VISIBLE);
            new MyAsyncTask().execute(0);

        }
        else{

           onUserHasNoPermission();

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (hasPermission(getApplicationContext())) {
            permissionMessage.setVisibility(GONE);
            button.setVisibility(GONE);
            mSwitchText.setVisibility(View.VISIBLE);
            showProgressBar(true);
       //     nv.setVisibility(View.VISIBLE);
            //tt.setVisibility(VISIBLE);
            new MyAsyncTask().execute(0);
        }
        else{
            onUserHasNoPermission();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu

                , menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchview = (SearchView) menuItem.getActionView();
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override

            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                userInput = userInput.toLowerCase(Locale.getDefault());
                adapter.filter(userInput);
              /*  List<AppItem1>arraylist = new ArrayList<>();
                arraylist.addAll(adapter.list);
                if(newText.length() !=0) {
                    adapter.list.clear();
                    for (AppItem1 wp : arraylist) {
                        String str = wp.appname;
                        if (str.toLowerCase(Locale.getDefault()).contains(userInput)) {
                            adapter.list.add(wp);
                        }
                    }
                }
                if(newText.length()==0)
                {
                    adapter.updateList(arraylist);
                  //  adapter.list.clear();
                 //   arraylist.addAll(adapter.list);
                }
          //      notifyDataSetChanged();
             /*   List<AppItem1>newList = new ArrayList<>();

                for(AppItem1 name:adapter.list)
                {
                    String  str = name.appname;
                    if(str.toLowerCase().contains(userInput))
                    {
                        newList.add(name);
                    }
                }
                adapter.updateList(adapter.list);*/

                return true;
            }



        });

      /*  SearchView.OnCloseListener closeListener = new SearchView.OnCloseListener(){

            @Override
            public boolean onClose() {
                adapter.updateList(arraylist);
                return true;
            }
        };*/

        return super.onCreateOptionsMenu(menu);
    }
    //this function was automatically called after onResume() meth
   /* @Override
    public void onUsageStatsRetrieved(List<UsageStatsWrapper> list) {
        showProgressBar(false);
        permissionMessage.setVisibility(GONE);
        adapter.setList(list);
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.age:
     //           Intent intent = new Intent(this, AgePrediction.class);
       //         this.startActivity(intent);
                break;
            case R.id.about:
                // another startActivity, this is for item with id "menu_item2"
                //   intent = new Intent(this, AboutApp.class);
                // this.startActivity(intent);
                break;
            case R.id.notification:
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Notification",
                        Toast.LENGTH_SHORT);

                toast.show();
                Intent intent = new Intent(this, AgePrediction.class);
                this.startActivity(intent);
                break;
            case R.id.graph:
                toast = Toast.makeText(getApplicationContext(),
                        "Overall usage graph",
                        Toast.LENGTH_SHORT);

                toast.show();
                intent = new Intent(this, overallgraph.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
    //this function was automatically called after onResume() meth
   /* @Override
    public void onUsageStatsRetrieved(List<UsageStatsWrapper> list) {
        showProgressBar(false);
        permissionMessage.setVisibility(GONE);
        adapter.setList(list);
    }*/

    @Override
    public void onUserHasNoPermission() {
        showProgressBar(false);
        permissionMessage.setVisibility(VISIBLE);
    }

    private void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(GONE);
        }
    }
    //asysctask to get app data in background
   /*private class myasynctask extends AsyncTask<Integer,Void,List<UsageStatsWrapper>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<UsageStatsWrapper> doInBackground(Integer... integers) {

            //Log.v("message","hello"+integers[0]);

            return presenter.retrieveUsageStats();
        }
        protected void onPostExecute(List<UsageStatsWrapper> appitems){
            adapter.setList(appitems);
           // for(UsageStatsWrapper item: appitems){
             //   Log.v("message",String.valueOf(item.getUsageStats().getTotalTimeInForeground()));

            //}
            showProgressBar(false);
        }
    */
    class MyAsyncTask extends AsyncTask<Integer, Void, List<AppItem1>> {

        //when ever we do swipe
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<AppItem1> doInBackground(Integer...integers) {
            return appitem.getApps(getApplicationContext(),0,1);
        }

        @Override
        protected void onPostExecute (List <AppItem1> appItems) {
            mTotal = 0;

            //to show averagetime
            //mSwitchText.setText(String.format(getResources().getString(R.string.total), AppUtil.formatMilliSeconds(mTotal)));
            //mSwipe.setRefreshing(false);
                Log.v("size",String.valueOf(appItems.size()));
            for(AppItem1 item :appItems){
                    //Log.v("testing5",item.mPackageName+"event time"+item.mEventTime+"event type"+item.mEventType+"usage time"+item.mUsageTime);
                  mdb.insert(item);
                if (item.mUsageTime <= 0) continue;
                mTotal += item.mUsageTime;
            }
            //inserting the overall duration of the smartphone usage into the database
            overall=new AppItem1();
            overall.appname="overall";
            overall.mUsageTime=mTotal;
            dbprovider dr=new dbprovider(getApplicationContext());
            dr.insert(overall);
            Log.v("cursor121231",String.valueOf(mTotal));
            Log.v("cursor121231",String.valueOf(overall.mUsageTime));



            if(isnotification == 1)
            {
                isnotification=0;
                int i=0;
                for(AppItem1 item :appItems){
                    //Log.v("testing5",item.mPackageName+"event time"+item.mEventTime+"event type"+item.mEventType+"usage time"+item.mUsageTime);
                    if(item.mUsageTime >= 60*60*1000)
                        map.put(item.appname,item.mUsageTime);
                    if(item.mUsageTime >= 60*60*1000)
                    {

                        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);




                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                        builder.setSmallIcon(R.drawable.ic_launcher)
                                .setColor(getResources().getColor(R.color.colorPrimary))
                                .setContentTitle(item.appname)
                                .setContentText("You have exceeded the daily time limit for this app")
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setPriority(NotificationManager.IMPORTANCE_HIGH);



                        builder.setAutoCancel(true);
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(i, builder.build());

                        i=i+1;
                    }

                }

            }
            //deco view
            DecoView arcView = (DecoView)findViewById(R.id.dynamicArcView);

           // Create background track
            arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                    .setRange(0, 100, 100)
                    .setInitialVisibility(false)
                    .setLineWidth(32f)
                    .build());

            //Create data series track
            SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                    .setRange(0, 100, 0)
                    .setLineWidth(32f)
                    .build();

            int series1Index = arcView.addSeries(seriesItem1);
            arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                    .setDelay(1000)
                    .setDuration(2000)
                    .build());


            int res=mTotal/864000;
            arcView.addEvent(new DecoEvent.Builder(res).setIndex(series1Index).setDelay(4000).build());
           // arcView.addEvent(new DecoEvent.Builder(100).setIndex(series1Index).setDelay(8000).build());
            //arcView.addEvent(new DecoEvent.Builder(10).setIndex(series1Index).setDelay(12000).build());
            //final TextView textPercentage = (TextView) findViewById(R.id.textPercentage);
            /*seriesItem1.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
                @Override
                public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                    float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                    tv.setText(String.format("%.0f%%", percentFilled * 100f));
                }

                @Override
                public void onSeriesItemDisplayProgress(float percentComplete) {

                }
            });*/

            /* Bundle bundle = new Bundle();
            String total_time_used = " Total time Used : " + String.valueOf(DateUtils.covertingtime(mTotal));
            bundle.putString("total_time_used", total_time_used );
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            FragmentOne F1 = new FragmentOne();
            F1.setArguments(bundle);
            FT.add(R.id.fragment1,F1);
            FT.commit();*/
            tv.setText(String.valueOf(DateUtils.covertingtime(mTotal)));
            adapter.setList(appItems);
            showProgressBar(false);
        }
    }
       // Intent intent = new Intent(MainActivity.this, AppleFragment.class);
        //intent.putExtra(appitemdisplay.abc,overall.m);
       // intent.putExtra(appitemdisplay.usagetime1,String.valueOf(overall.mUsageTime));
      //  intent.putExtra(appitemdisplay.pkname1,usageStatsWrapper.mPackageName);
        //startActivity(intent


}
