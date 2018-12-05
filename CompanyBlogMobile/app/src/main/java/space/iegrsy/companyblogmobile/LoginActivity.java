package space.iegrsy.companyblogmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private String _username;
    private String _password;

    @BindView(R.id.lgn_ed_username)
    TextInputEditText edUsername;

    @BindView(R.id.lgn_ed_password)
    TextInputEditText edPassword;

    @BindView(R.id.lgn_progressbar_container)
    FrameLayout progress;

    private final DataBaseUtil.LoginListener loginListener = new DataBaseUtil.LoginListener() {
        @Override
        public void onLoginRest(boolean islogin, int userid) {
            showProgress(false);
            if (islogin) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);

                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Worng username or password", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @OnClick(R.id.lgn_btn_login)
    void login() {
        if (!isValidInput())
            return;

        showProgress(true);
        DataBaseUtil.login(
                getApplicationContext(),
                loginListener,
                _username,
                _username,
                _password
        );
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

    private boolean isValidInput() {
        String username = edUsername.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            _username = username;
            _password = password;
            return true;
        }

        if (username.isEmpty())
            edUsername.setError("Enter username");
        if (password.isEmpty())
            edPassword.setError("Enter password");

        return false;
    }

    private void showProgress(boolean b) {
        progress.setVisibility(b ? View.VISIBLE : View.GONE);
    }
}
