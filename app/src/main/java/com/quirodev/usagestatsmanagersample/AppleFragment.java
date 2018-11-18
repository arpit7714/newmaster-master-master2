package com.quirodev.usagestatsmanagersample;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.quirodev.data.dbcontract;
import com.quirodev.data.dbhelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.app.ActionBar;
import android.view.MenuItem;



import static com.quirodev.usagestatsmanagersample.appitemdisplay.appname1;

public class AppleFragment extends Fragment {

    public AppleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // ActionBar ab=getActivity().getActionBar();


    }


    public String getday(int a){
        String[] days = new String[] {"Sunday","Monday","Tuesday", "Wednesday","Thursday","Friday", "Saturday"};
        return days[a];
    }
    public String getmonth(int month){
        switch (month){
            case 0:
                return "January";
            case 1:
                return "Feburary";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return "no";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragmentapple, container, false);
        TextView title1 = (TextView) v.findViewById(R.id.title1);
        title1.setText("DAILY");
        TextView title2 = (TextView) v.findViewById(R.id.title2);
        title2.setText("WEEKLY");
        TextView title3 = (TextView) v.findViewById(R.id.title3);
        title3.setText("MONTHLY");
        //  TextView title4 = (TextView) v.findViewById(R.id.title4);
        // title4.setText("YEARLY");

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Entry> entries1 = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(calendar.DATE, 1);
        //get the current month of the year
        int month = calendar.get(calendar.MONTH);

        //bar chart
        BarChart barChart = (BarChart) v.findViewById(R.id.barchart);

        //set the custom axis for the  first barchart
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new MyXAxisValueFormatter());
        xAxis.setLabelsToSkip(0);



        ArrayList<BarEntry> bargroup1 = new ArrayList<>();
        ArrayList<BarEntry> bargroup2 = new ArrayList<>();
        ArrayList<BarEntry> bargroup3=new ArrayList<>();


        dbhelper mdbhelper = new dbhelper(getActivity());
        SQLiteDatabase db = mdbhelper.getReadableDatabase();
        //Cursor cursor=db.rawQuery("Select * from "+appdata.TABLE_NAME,null);
        String[] projection = {
                dbcontract.appdata.APP_NAME,
                dbcontract.appdata._AL,
                dbcontract.appdata.APP_DURATION,
                dbcontract.appdata._ID
        };
        //String selection=dbcontract.appdata.APP_NAME+"=?";
        String[] args = {appname1,};

        Cursor cursor = db.query(
                dbcontract.appdata.TABLE_NAME,
                projection, dbcontract.appdata.APP_NAME + "=?", args
                , null,
                null,
                null
        );
        if (cursor != null) {
            int appcolumnindex = cursor.getColumnIndex(dbcontract.appdata.APP_NAME);
            int appduration = cursor.getColumnIndex(dbcontract.appdata.APP_DURATION);
            int date = cursor.getColumnIndex(dbcontract.appdata._AL);
            int id=cursor.getColumnIndex(dbcontract.appdata._ID);
            //TextView displayview=(TextView)v.findViewById(R.id.database);
            //displayview.setText("Number of rows in database table: " + cursor.getCount());
            Log.v("cursor1212", String.valueOf(cursor.getCount()));
            int i = 0;

            while (cursor.moveToNext() && i <= 6) {
                String appname = cursor.getString(appcolumnindex);
                String duration = cursor.getString(appduration);
                String datecol = cursor.getString(date);
                //  displayview.append("\n"+appname+"  "+duration+" "+datecol+"\n");
                //entries.add(new Entry(Long.parseLong(duration) / 60000, i));
                //entries1.add(new Entry(Long.parseLong(duration) / 60000, i));
                bargroup1.add(new BarEntry(Long.parseLong(duration) / 60000, i));
                bargroup3.add(new BarEntry(Long.parseLong(duration) / 60000, i));
                i++;
                Log.v("data123", appname + "  " + duration + "  " + datecol);
            }


            while (cursor.moveToNext() && i <= 30) {
                String appname = cursor.getString(appcolumnindex);
                String duration = cursor.getString(appduration);
                String datecol = cursor.getString(date);
                //  displayview.append("\n"+appname+"  "+duration+" "+datecol+"\n");
                // entries1.add(new Entry(Long.parseLong(duration) / 60000, i));
                bargroup3.add(new BarEntry(Long.parseLong(duration) / 60000, i));
                i++;
            }
            //for daily usage the last element of the table for each of the application
            cursor.moveToLast();
            String duration = cursor.getString(appduration);
            String id1=cursor.getString(id);
            //checking
            Log.v("durationtesting",duration);
            bargroup2.add(new BarEntry(Long.parseLong(duration) / 60000, 0));
            cursor.close();
        }



        // LineDataSet dataset = new LineDataSet(entries, "app usage");
        //LineDataSet dataSet2 = new LineDataSet(entries1, getmonth(month));

        //to adjust the labels of the axis of the graph
        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<String> label = new ArrayList<String>();

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        day--;
        /*Log.v("day121",String.valueOf(day));
        for(int j=0;j<=6;j++) {
            if (j==0) {
                labels.add(getday(day));
            }
            else{
                labels.add(getday((day+j)%6));
            }
        }*/
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        labels.add("6");
        labels.add("7");

        label.add("current");
        BarDataSet bardataset1 = new BarDataSet(bargroup2 ,"app usage");
        BarData data1 = new BarData(label, bardataset1);
        barChart.setData(data1);
        BarDataSet bardataset2=new BarDataSet(bargroup1,"app usage");
        BarDataSet bardataset3=new BarDataSet(bargroup3,"app usage");

        BarData data2= new BarData(labels , bardataset2);
        ArrayList<String> label1 = new ArrayList<String>();
        label1.add("1");
        label1.add("2");
        label1.add("3");
        label1.add("4");
        label1.add("5");
        label1.add("6");
        label1.add("7");
        label1.add("8");
        label1.add("9");
        label1.add("10");
        label1.add("11");
        label1.add("12");
        label1.add("13");
        label1.add("14");
        label1.add("15");
        label1.add("16");
        label1.add("17");
        label1.add("18");
        label1.add("19");
        label1.add("20");
        label1.add("21");
        label1.add("22");
        label1.add("23");
        label1.add("24");
        label1.add("25");
        label1.add("26");
        label1.add("27");
        label1.add("28");
        label1.add("29");
        label1.add("30");



        final ArrayList<String> xAxisLabel = new ArrayList<>();

        BarData data3=new BarData(label1,bardataset3);

        barChart.animateXY(5000,5000);
        bardataset2.setColors(ColorTemplate.COLORFUL_COLORS);
        bardataset3.setColors(ColorTemplate.COLORFUL_COLORS);
        bardataset1.setColors(ColorTemplate.COLORFUL_COLORS);
        BarChart barChart2 = (BarChart) v.findViewById(R.id.chart2);
        barChart2.setData(data2);
        barChart2.setDescription("weeks");
        barChart2.animateXY(5000,5000);




        BarChart barchart3 = (BarChart) v.findViewById(R.id.chart3);

        barchart3.setData(data3);
        barchart3.setDescription(getmonth(month));
        barchart3.animateXY(5000,5000);
        //BarChart barchart4 = (BarChart) v.findViewById(R.id.chart4);
        //barchart4.setData(data1);
        // barchart4.setDescription("yearly");
        // barchart4.animateY(5000);
        return v;
    }

    public class MyXAxisValueFormatter implements XAxisValueFormatter {

        @Override
        public String getXValue(String dateInMillisecons, int index, ViewPortHandler viewPortHandler) {
            try {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

                return sdf.format(c);
            } catch (Exception e) {
                return dateInMillisecons;
            }

        }
    }
}