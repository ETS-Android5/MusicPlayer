package au.edu.unsw.infs3634.musicrecommender;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Hide the actionbar when we load the splash screen, if it is not null
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final Handler handler = new Handler();
        //Instantiate a new handler and runnable
        //After 3 seconds, take user to MainActivity
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                //Finish so that user cannot navigate back to splash screen once loaded
                finish();
            }
        }, 3000);
    }
}
