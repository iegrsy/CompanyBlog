package space.iegrsy.companyblogmobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import space.iegrsy.companyblogmobile.helper.AuthenticationUtil;
import space.iegrsy.companyblogmobile.R;

public class SplashActivity extends AppCompatActivity {

    private Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AuthenticationUtil.Authentication authentication = AuthenticationUtil.newInstance(context);
                if (authentication == null)
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("userid", authentication.userid);
                    startActivity(intent);
                }
                context.finish();
            }
        }).start();
    }
}
