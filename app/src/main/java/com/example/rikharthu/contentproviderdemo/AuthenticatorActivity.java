package com.example.rikharthu.contentproviderdemo;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    // Must match registered account type in res/xml/authenticator.xml
    public static final String PARAM_ACCOUNT_TYPE = "com.example.rikharthu.contentproviderdemo";
    public static final String PARAM_USER = "PARAM_USER";
    public static final String PARAM_AUTHTOKEN_TYPE = "PARAM_AUTHTOKEN_TYPE";
    public static final String PARAM_NEW_ACCOUNT = "PARAM_NEW_ACCOUNT";
    private static final String PARAM_USER_PASSWORD = "PARAM_USER_PASSWORD";

    @BindView(R.id.email_et)
    TextView mEmailEt;
    @BindView(R.id.password_et)
    TextView mPasswordEt;
    @BindView(R.id.login_btn)
    Button mLoginBtn;

    private AccountManager mAccountManager;
    private String mAuthTokenType;
    private boolean mIsAddingNewAccount;


    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_authenticator);
        ButterKnife.bind(this);

        mAccountManager = AccountManager.get(this);

        Intent intent = getIntent();
        if (intent != null) {
            mAuthTokenType = intent.getStringExtra(PARAM_AUTHTOKEN_TYPE);
            mIsAddingNewAccount = intent.getBooleanExtra(PARAM_NEW_ACCOUNT, false);
        }

        if (mIsAddingNewAccount) {
            mLoginBtn.setText("Register");
        }
    }

    @OnClick(R.id.login_btn)
    void onSubmit() {
        final String email = mEmailEt.getText().toString();
        final String password = mPasswordEt.getText().toString();

        Timber.d("Logging in...");
        // simulate login flow
        new Handler().postDelayed(() -> {
            if (email.equalsIgnoreCase("vasya@mail.ru")
                    && password.equalsIgnoreCase("pupkin123")) {
                onLoginSuccess(email, password, "authenticatoractivity_mock_authtoken");
            } else {
                onLoginFailed("Incorrect email or password");
            }
        }, 2300);
    }

    private void onLoginFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    private void onLoginSuccess(String email, String password, String authtoken) {
        Timber.d("Successfully logged in!");
        Intent result = new Intent();
        result.putExtra(AccountManager.KEY_ACCOUNT_NAME, email);
        result.putExtra(AccountManager.KEY_ACCOUNT_TYPE, PARAM_ACCOUNT_TYPE);
        result.putExtra(AccountManager.KEY_AUTHTOKEN, authtoken);
        result.putExtra(PARAM_USER_PASSWORD, password);

        final Account account = new Account(email, PARAM_ACCOUNT_TYPE);

        if (mIsAddingNewAccount) {
            mAccountManager.addAccountExplicitly(account, password, null);
            mAccountManager.setAuthToken(account, PARAM_AUTHTOKEN_TYPE, authtoken);
        } else {
            Timber.d("Updating password for " + email);
            mAccountManager.setPassword(account, password);
        }

        setAccountAuthenticatorResult(result.getExtras());
        setResult(RESULT_OK, result);
        finish();
    }
}
