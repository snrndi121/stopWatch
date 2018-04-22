package com.uki121.pooni;

/**
 * Created by uki121 on 2018-04-03.
 */

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;

import com.airbnb.lottie.LottieAnimationView;

import java.io.StringReader;

public class MainActivity extends AppCompatActivity {
  private static int WELCOME_TIMEOUT = 2000;
  //private final String BOOKDB_NAME = "pooni.db";  //sqlite database name
  private bookDBHelper bookdbHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //bookdbHelper = new bookDBHelper( MainActivity.this, BOOKDB_NAME, null, 1);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent welcome = new Intent( MainActivity.this, HomeActivity.class);
        startActivity(welcome);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
      }
    }, WELCOME_TIMEOUT);
  }
  @Override
  public void onPause() {
    super.onPause();

    // Remove the activity when its off the screen
    finish();
  }
}
