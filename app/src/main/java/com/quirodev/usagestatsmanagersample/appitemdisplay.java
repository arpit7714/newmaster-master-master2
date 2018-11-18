


package com.quirodev.usagestatsmanagersample;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.quirodev.data.dbcontract;
import com.quirodev.data.dbhelper;
import com.quirodev.data.dbprovider;

import java.util.ArrayList;
import java.util.List;


public class appitemdisplay extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static String abc="appname";
    public static String appname1;
    public static String pkname1;
    public static String usagetime1;
    private Resources mResources;
    public String pkname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //obj=new dbprovider(getApplicationContext());
        Intent intent=getIntent();
        appname1=intent.getStringExtra(abc);
        pkname = intent.getStringExtra(pkname1);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Log.v("appname",appname1);

        setContentView(R.layout.activity_main2);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        if(intent!=null)
        {

            final int defaultButtonFilterColor = getResources().getColor(R.color.colorPrimary);
            Bitmap bitmap = BitmapUtil.drawableToBitmap(AppUtil.getPackageIcon(appitemdisplay.this,pkname ));
            Palette p = createPaletteSync(bitmap);
            Palette.Swatch swatch = p.getVibrantSwatch();
            int color = defaultButtonFilterColor;
            if (swatch != null) {
                color = swatch.getRgb();
            }
            if (swatch != null) {
                color = swatch.getRgb();
            }
                            try {
                                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                                Window window = getWindow();
                                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(color);
                            } catch (Exception e) {
                                // ignore
                            }
                            tabLayout.setBackgroundColor(color);


        }


    }
    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }
    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Intent intent=getIntent();
                    if (intent!=null){
                       // pkname=intent.getStringExtra(pkname1);
                        String usage = intent.getStringExtra(usagetime1);
                      //  String pkname = intent.getStringExtra(pkname);
                        Bundle bundle = new Bundle();
                        bundle.putString("usage", usage );
                        bundle.putString("appname1", appname1 );
                        bundle.putString("pkname",pkname);
                       // bundle.putString("pkname", pkname );
                        Bundle extras=intent.getExtras();
                        bundle.putBundle("extras",extras);
                        OrangeFragment F1 = new OrangeFragment();
                        F1.setArguments(bundle);
                        final int defaultButtonFilterColor = getResources().getColor(R.color.colorPrimary);
                        return F1;
                    }
                case 1:
                    return new AppleFragment();
                case 2:
                    return new GrapesFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Graphs";
                case 2:
                    return "Alarm";
                default:
                    return null;
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                Log.d(">>>>====----> Detail", "onOptionsItemSelected.android.R.id.home");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(">>>>====----> Detail", "onBackPressed");
        supportFinishAfterTransition();
    }


}
