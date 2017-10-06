package com.example.rikharthu.contentproviderdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import timber.log.Timber;

/**
 * Service that is used to allow other processes to communicate with our {@link Authenticator}
 */
public class AuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private Authenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new Authenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        // Must return Authetnicator's binder
        Timber.d("Binding to Authenticator");
        return mAuthenticator.getIBinder();
    }
}
