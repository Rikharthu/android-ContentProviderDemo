package com.example.rikharthu.contentproviderdemo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.rikharthu.contentproviderdemo.data.NotesContentProvider;

import static com.example.rikharthu.contentproviderdemo.AuthenticatorActivity.PARAM_NEW_ACCOUNT;

public class LandingActivity extends AppCompatActivity {

    public static final int NEW_ACCOUNT = 0;
    public static final int EXISTING_ACCOUNT = 1;
    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager
                .getAccountsByType(AuthenticatorActivity.PARAM_ACCOUNT_TYPE);
        if (accounts.length == 0) {
            final Intent i = new Intent(this, AuthenticatorActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            i.putExtra(PARAM_NEW_ACCOUNT, true);
            startActivityForResult(i, NEW_ACCOUNT);
        } else {
            Account account = accounts[0];

            ContentResolver.requestSync(account, NotesContentProvider.AUTHORITY, new Bundle());

            String password = mAccountManager.getPassword(accounts[0]);
            if (password == null) {
                final Intent i = new Intent(this, AuthenticatorActivity.class);
                i.putExtra(AuthenticatorActivity.PARAM_USER, accounts[0].name);
                startActivityForResult(i, EXISTING_ACCOUNT);
            } else {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }
}
