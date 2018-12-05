package space.iegrsy.companyblogmobile;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.lgn_ed_username)
    TextInputEditText edUsername;

    @BindView(R.id.lgn_ed_password)
    TextInputEditText edPassword;

    @OnClick(R.id.lgn_btn_login)
    void login() {
        Toast.makeText(this, "login click", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.lgn_txt_signup)
    void signup() {
        Toast.makeText(this, "signup click", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }
}
