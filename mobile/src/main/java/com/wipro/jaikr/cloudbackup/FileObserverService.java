package com.wipro.jaikr.cloudbackup;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.FileObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
/**
 * Created by jaikr on 28-May-17.
 */

public class FileObserverService extends Service {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    private FileObserver recursiveFileObserver;
    private static int eventCount = 0 ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, getApplicationContext().MODE_PRIVATE);
        String path = sharedpreferences.getString("path",null);
        final Handler handler = new Handler(Looper.getMainLooper());
        if(path != null)
        recursiveFileObserver = new RecursiveFileObserver(path) {
            @Override
            public void onEvent(final int event, final String path) {

                handler.post(new Runnable() {
                    public void run() {
                       // Toast.makeText(getBaseContext(), "Your message to main thread", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getBaseContext(),"Event Happened : "+event,Toast.LENGTH_SHORT);
                        eventCount++;
                        switch (event)
                        {
                            case FileObserver.ACCESS:
                                Log.i("RecursiveFileObserver", "ACCESS: " + path);
                                Toast.makeText(getBaseContext(),"ACCESS: "+ path+" || event_count = "+eventCount ,Toast.LENGTH_SHORT).show();
                                break;
                            case FileObserver.ATTRIB:
                                Log.i("RecursiveFileObserver", "ATTRIB: " + path);
                                Toast.makeText(getBaseContext(),"ATTRIB: "+ path+" || event_count = "+eventCount,Toast.LENGTH_SHORT).show();
                                break;
                            case FileObserver.CLOSE_NOWRITE:
                                Log.i("RecursiveFileObserver", "CLOSE_NOWRITE: " + path);
                                Toast.makeText(getBaseContext(),"CLOSE_NOWRITE: "+ path+" || event_count = "+eventCount,Toast.LENGTH_SHORT).show();
                                break;
                            case FileObserver.CLOSE_WRITE:
                                Log.i("RecursiveFileObserver", "CLOSE_WRITE: " + path);
                                Toast.makeText(getBaseContext(),"ATTRIB: " + path+" || event_count = "+eventCount,Toast.LENGTH_SHORT).show();
                                break;
                            case FileObserver.CREATE:
                                Log.i("RecursiveFileObserver", "CREATE: " + path);
                                Toast.makeText(getBaseContext(),"CREATE:" + path+" || event_count = "+eventCount,Toast.LENGTH_SHORT).show();
                                break;
                            case FileObserver.DELETE:
                                Log.i("RecursiveFileObserver", "DELETE: " + path);
                                 Toast.makeText(getBaseContext(),"DELETE: "+ path+" || event_count = "+eventCount,Toast.LENGTH_SHORT).show();
                                break;
                            case FileObserver.DELETE_SELF:
                                Log.i("RecursiveFileObserver", "DELETE_SELF: " + path);
                                Toast.makeText(getBaseContext(),"DELETE_SELF: "+ path+" || event_count = "+eventCount,Toast.LENGTH_SHORT).show();
                                break;
                            case FileObserver.MODIFY:
                                Log.i("RecursiveFileObserver", "MODIFY: " + path);
                                Toast.makeText(getBaseContext(),"MODIFY: " + path+" || event_count = "+eventCount,Toast.LENGTH_SHORT).show();
                                break;
                            case FileObserver.MOVE_SELF:
                                Log.i("RecursiveFileObserver", "MOVE_SELF: " + path);
                                Toast.makeText(getBaseContext(),"MOVE_SELF: " + path+" || event_count = "+eventCount,Toast.LENGTH_SHORT).show();
                                break;
                            case FileObserver.MOVED_FROM:
                                Log.i("RecursiveFileObserver", "MOVED_FROM: " + path);
                                Toast.makeText(getBaseContext(),"MOVED_FROM: " + path+" || event_count = "+eventCount,Toast.LENGTH_SHORT).show();
                                break;
                            case FileObserver.MOVED_TO:
                                Log.i("RecursiveFileObserver", "MOVED_TO: " + path);
                               Toast.makeText(getBaseContext(),"MOVED_TO: " + path+" || event_count = "+eventCount,Toast.LENGTH_SHORT).show();
                                break;
                            case FileObserver.OPEN:
                                Log.i("RecursiveFileObserver", "OPEN: " + path);
                                Toast.makeText(getBaseContext(),"OPEN: " + path+" || event_count = "+eventCount,Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.i("RecursiveFileObserver", "DEFAULT(" + event + "): " + path);
                                Toast.makeText(getBaseContext(),"DEFAULT " + path+" || event_count = "+eventCount,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        };


    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        recursiveFileObserver.startWatching();
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        recursiveFileObserver.stopWatching();
        eventCount=0;
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
    }
}
