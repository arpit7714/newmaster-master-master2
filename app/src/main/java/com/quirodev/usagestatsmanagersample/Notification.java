package com.quirodev.usagestatsmanagersample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Notification extends AppCompatActivity {
    private TextView mWeatherTextView;
    private TextView titleView;
    private ImageView image1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // COMPLETED (3) Create an array of Strings that contain fake weather data
        /*
         * This String array contains dummy weather data. Later in the course, we're going to get
         * real weather data. For now, we want to get something on the screen as quickly as
         * possible, so we'll display this dummy data.
         */
     //   titleView.setText("How much time do you spend on your phone each day?");

        String[] dummyWeatherData = {
                "Today, May 17 - Clear - 17°C / 15°C",
                "Tomorrow - Cloudy - 19°C / 15°C",
                "Thursday - Rainy- 30°C / 11°C",
                "Friday - Thunderstorms - 21°C / 9°C",
                "Saturday - Thunderstorms - 16°C / 7°C",
                "Sunday - Rainy - 16°C / 8°C",
                "Monday - Partly Cloudy - 15°C / 10°C",
                "Tue, May 24 - Meatballs - 16°C / 18°C",
                "Wed, May 25 - Cloudy - 19°C / 15°C",
                "Thu, May 26 - Stormy - 30°C / 11°C",
                "Fri, May 27 - Hurricane - 21°C / 9°C",
                "Sat, May 28 - Meteors - 16°C / 7°C",
                "Sun, May 29 - Apocalypse - 16°C / 8°C",
                "Mon, May 30 - Post Apocalypse - 15°C / 10°C",
        };

        // COMPLETED (4) Append each String from the fake weather data array to the TextView
        /*
         * Iterate through the array and append the Strings to the TextView. The reason why we add
         * the "\n\n\n" after the String is to give visual separation between each String in the
         * TextView. Later, we'll learn about a better way to display lists of data.
         */
        //    for (String dummyWeatherDay : dummyWeatherData) {
        mWeatherTextView.setText("These days, almost 80% of the people in the world are addicted to Mobile phones. The normal phones which were used just to call were somewhat fine, but technology changed everything. Your mobile isnt just a mobile anymore. Its a Smartphone. It has AI, it can think almost like you. All of this tech in one small handy device makes it irresistible. And this is a bad thing.\n" +
                "\n" +
                "If a person uses Facebook, Instagram, Snapchat, Whatsapp, Twitter, etc, then atleast 6 hours per day is used just for these apps by most consumers. This kills time, plus it brings huge side-effects on the person’s health.\n" +
                "\n" +
                "Continuously using our mobile phone can destroy our thumbs, eye-sight, and even our spine. If the person has to use the Mobile phone everyday for alot of reasons, then the person should try to limit each usage time into just a max of 15 mins. Each session of using his/her phone should be limited to 15 mins. After this 15 mins, the person should get going with other activities.\n" +
                "\n" +
                "No matter what, try avoiding using your mobile phones atleast 2 hours prior to sleep. This relaxes your mind, and also reduces radiation effect. It leads to a deep and healthy sleep, which every single body requires. If possible, put your phone on airplane mode when using it last.\n" +
                "\n" +
                "I would say a person should limit himself to a total of 2 hours maximum at the current world, to use the mobile.");
        //  }
    }
}