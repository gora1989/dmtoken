package dm.com.token;

/**
 * Created by gora3 on 5/19/17.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class SplashScreenActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(true) {
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                finish();
            }
        }, SPLASH_TIME_OUT);*/
    }

    public void onClick(View view) {
        if (view.getId() == R.id.getstarted) {
            if (true) {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(SplashScreenActivity.this, LogInActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

}
