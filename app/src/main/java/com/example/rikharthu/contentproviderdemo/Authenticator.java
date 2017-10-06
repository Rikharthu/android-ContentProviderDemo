package com.example.rikharthu.contentproviderdemo;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import timber.log.Timber;

public class Authenticator extends AbstractAccountAuthenticator {

    private final Context mContext;

    public Authenticator(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse,
                             String accountType, String authtokenType, String[] requiredFeatures, Bundle options)
            throws NetworkErrorException {
        Timber.d("Adding account");
        final Intent intent = new Intent(mContext,
                AuthenticatorActivity.class);
        intent.putExtra(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE, authtokenType);
        intent.putExtra(AuthenticatorActivity.PARAM_NEW_ACCOUNT, true);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account,
                               String authTokenType, Bundle options) throws NetworkErrorException {
        Timber.d("Getting auth token");
        // Check if the required authTokenType is the same as we handle
        if (!authTokenType
                .equals(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE,
                    "invalid authTokenType");
            return result;
        }

        // Get the password from the AccountManager
        final AccountManager am = AccountManager.get(mContext);
        final String password = am.getPassword(account);
        // TODO we could also check whether there is cached authToken with am.peekAuthToken()
        if (password != null) {
            // Password is stored, sign in against the server

            // TODO do a network call here
            try {
                Thread.sleep(2300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final Bundle result = new Bundle();
            // If call succeeds, then this method should return a bundle containing:
            // KEY_ACCOUNT_NAME, KEY_ACCOUNT_TYPE and KEY_AUTHTOKEN
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_AUTHTOKEN, "mock_authtoken_from_authenticator");
            result.putString(AccountManager.KEY_ACCOUNT_TYPE,
                    AuthenticatorActivity.PARAM_ACCOUNT_TYPE);
        }

        // Couldn't sign-in agaisnt the server, but let the caller know about it by passing information
        // on which activity to call
        final Intent intent = new Intent(mContext,
                AuthenticatorActivity.class);
        intent.putExtra(AuthenticatorActivity.PARAM_USER, account.name);
        intent.putExtra(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE,
                authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, "FAIL");
        final Bundle bundle = new Bundle();
        // Couldn't get auth token without user interraction, return intent at KEY_INTENT to notify the user
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String s) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
        return null;
    }
}
