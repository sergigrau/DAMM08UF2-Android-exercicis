package edu.fje.dam2;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Classe que implementa un Servei per a reproduir música
 *
 * @author sergi.grau@fje.edu
 * @version 2.0 16.01.2017
 */
public class E02_MusicaService extends Service {
    private static final String IDENTIFICADOR = "E02_MusicaService";
    MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "servei creat", Toast.LENGTH_LONG).show();
        Log.d(IDENTIFICADOR, "onCreate");

        player = MediaPlayer.create(this, R.raw.audio1);
        player.setLooping(false);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "servei parat", Toast.LENGTH_LONG).show();
        Log.d(IDENTIFICADOR, "onDestroy");
        player.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "servei iniciat", Toast.LENGTH_LONG).show();
        Log.d(IDENTIFICADOR, "onStartCommand");
        player.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
