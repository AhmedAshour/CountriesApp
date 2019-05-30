package me.ahmedashour.countriesapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.ahmedashour.countriesapp.R;
import me.ahmedashour.countriesapp.utils.Validation;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.et_email_login_activity)
    TextInputEditText etEmail;
    @BindView(R.id.et_password_login_activity)
    TextInputEditText etPassword;
    @BindView(R.id.btn_login_login_activity)
    MaterialButton btnLogin;
    @BindView(R.id.btn_register_login_activity)
    MaterialButton btnRegister;

    private boolean isEmailValid, isPasswordValid;
    private String userEmail, userPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnTextChanged({R.id.et_email_login_activity, R.id.et_password_login_activity})
    public void checkEmailAndPasswordValidity() {
        userEmail = etEmail.getText().toString().trim();
        userPassword = etPassword.getText().toString();

        isEmailValid = Validation.isEmailValid(userEmail);
        isPasswordValid = Validation.isPasswordValid(userPassword);

        if (!isEmailValid)
            etEmail.setError(getString(R.string.wrong_email_format));
        if (!isPasswordValid)
            etPassword.setError(getString(R.string.wrong_password_format));
    }

    @OnClick(R.id.btn_login_login_activity)
    public void loginToAccount() {

        mAuth = FirebaseAuth.getInstance();
        if (isEmailValid && isPasswordValid) {
            mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            startActivity(new Intent(this, HomeActivity.class));
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Snackbar.make(findViewById(R.id.btn_login_login_activity), "Authentication failed.",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    });
        } else
            Snackbar.make(findViewById(R.id.btn_login_login_activity), getString(R.string.enter_correct_credentials),
                    Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_register_login_activity)
    public void goToRegisterationActivity() {
        startActivity(new Intent(LoginActivity.this, RegisterationActivity.class));
    }


}
