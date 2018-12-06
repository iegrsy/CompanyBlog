package space.iegrsy.companyblogmobile;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = SignupActivity.class.getSimpleName();

    private String _email;
    private String _username;
    private String _password;
    private String _birthday;

    @BindView(R.id.sgn_ed_email)
    TextInputEditText edEmail;

    @BindView(R.id.sgn_ed_username)
    TextInputEditText edUsername;

    @BindView(R.id.sgn_ed_password)
    TextInputEditText edPassword;

    @BindView(R.id.sgn_ed_password_try)
    TextInputEditText edPasswordTry;

    @BindView(R.id.sgn_ed_birthday)
    TextInputEditText edBirthday;

    @BindView(R.id.sgn_progressbar_container)
    FrameLayout progress;

    private final DataBaseUtil.AddUserListener addUserListener = new DataBaseUtil.AddUserListener() {
        @Override
        public void isCreateUser(boolean b) {
            showProgress(false);
            if (b)
                onBackPressed();
            else
                Snackbar.make(findViewById(android.R.id.content),
                        "Not create user try again later.", Snackbar.LENGTH_SHORT).show();
        }
    };

    @OnClick(R.id.sgn_btn_signup)
    void signup() {
        if (!isValidInput())
            return;

        showProgress(true);
        DataBaseUtil.QUser user = new DataBaseUtil.QUser();
        user.username = _username;
        user.password = _password;
        user.email = _email;
        user.date = _birthday;

        DataBaseUtil.addUser(getApplicationContext(), addUserListener, user);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);
    }

    private boolean isValidInput() {
        boolean valid = true;

        String email = edEmail.getText().toString().trim();
        String username = edUsername.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String passwordTry = edPasswordTry.getText().toString().trim();
        String birthday = edBirthday.getText().toString().trim();

        if (email.isEmpty()) {
            edEmail.setError("Enter email");
            valid = false;
        }
        if (username.isEmpty()) {
            edUsername.setError("Enter username");
            valid = false;
        }
        if (password.isEmpty()) {
            edPassword.setError("Enter password");
            valid = false;
        }
        if (passwordTry.isEmpty()) {
            edPasswordTry.setError("Enter password again");
            valid = false;
        }
        if (birthday.isEmpty()) {
            edBirthday.setError("Enter birthday");
            valid = false;
        }
        if (!email.contains("@") || !email.contains(".")) {
            edEmail.setError("Enter valid email");
            valid = false;
        }
        if (username.length() < 4) {
            edUsername.setError("Must be a length greater than 3 letter");
            valid = false;
        }
        if (!password.equals(passwordTry)) {
            edPasswordTry.setError("Passwords do not match");
            valid = false;
        }
        if (birthday.length() != 10) {
            edBirthday.setError("Not correct date format");
            valid = false;
        }

        if (valid) {
            _username = username;
            _password = password;
            _email = email;
            _birthday = birthday;
        }

        return valid;
    }

    private void showProgress(boolean b) {
        progress.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SignupActivity.this.finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
