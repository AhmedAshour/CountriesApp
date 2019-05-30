package me.ahmedashour.countriesapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.ahmedashour.countriesapp.R;
import me.ahmedashour.countriesapp.utils.Validation;

public class RegisterationActivity extends AppCompatActivity {

    public static final String TAG = RegisterationActivity.class.getSimpleName();
    public static final int RC_SIGN_IN = 1;
    private static final String EMAIL = "email";

    @BindView(R.id.et_email_registeration_activity)
    TextInputEditText etEmail;
    @BindView(R.id.et_password_registeration_activity)
    TextInputEditText etPassword;
    @BindView(R.id.et_confirm_password_registeration_activity)
    TextInputEditText etPasswordConfirmation;
    @BindView(R.id.btn_create_account_registeration_activity)
    MaterialButton btnCreateAccount;
    @BindView(R.id.tv_login_registeration_activity)
    TextView tvLogin;
    @BindView(R.id.btn_facebook_registeration_activity)
    LoginButton btnFacebook;
    @BindView(R.id.btn_google_registeration_activity)
    SignInButton btnGoogle;

    String userEmail, userPassword, userPasswordConfirmation;
    boolean isEmailValid, isPasswordValid, isPasswordMatch;

    private FirebaseAuth mAuth; // Firebase
    private CallbackManager callbackManager; // Facebook
    private GoogleSignInClient mGoogleSignInClient; // Google

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            startActivity(new Intent(RegisterationActivity.this, HomeActivity.class));
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        // Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Facebook
        btnFacebook.setReadPermissions(Arrays.asList(EMAIL));
        initFacebook();

    }

    @OnTextChanged({R.id.et_email_registeration_activity, R.id.et_password_registeration_activity})
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

    @OnTextChanged(R.id.et_confirm_password_registeration_activity)
    public void checkPasswordConfirmation() {

        userPasswordConfirmation = etPasswordConfirmation.getText().toString();
        isPasswordMatch = false;

        if (!userPassword.equals(userPasswordConfirmation))
            etPasswordConfirmation.setError(getString(R.string.password_doesnt_match));
        else isPasswordMatch = true;
    }

    @OnClick(R.id.btn_create_account_registeration_activity)
    public void createAccount() {

        if (isEmailValid && isPasswordValid && isPasswordMatch) {
            mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:success");
                                startActivity(new Intent(RegisterationActivity.this, HomeActivity.class));
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Snackbar.make(findViewById(R.id.btn_create_account_registeration_activity), "Authentication failed.",
                                        Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        } else
            Snackbar.make(findViewById(R.id.btn_create_account_registeration_activity), "Please enter correct values!",
                    Snackbar.LENGTH_SHORT).show();

    }

    @OnClick(R.id.btn_google_registeration_activity)
    public void createAccountUsingGoogle() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.tv_login_registeration_activity)
    public void goToLoginScreen() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        startActivity(new Intent(RegisterationActivity.this, HomeActivity.class));
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(findViewById(R.id.btn_google_registeration_activity), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    private void initFacebook() {
        callbackManager = CallbackManager.Factory.create();

        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        startActivity(new Intent(RegisterationActivity.this, HomeActivity.class));
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(findViewById(R.id.btn_facebook_registeration_activity), "Authentication failed.",
                                Snackbar.LENGTH_LONG).show();
                    }
                });
    }

}
