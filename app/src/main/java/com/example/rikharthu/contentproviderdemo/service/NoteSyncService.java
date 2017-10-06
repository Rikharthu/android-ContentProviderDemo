package com.example.rikharthu.contentproviderdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import timber.log.Timber;

/**
 * SyncAdapter needs to be wrapped in a service to work
 */
public class NoteSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static NoteSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        Timber.d("Creating NoteSyncService");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new NoteSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
