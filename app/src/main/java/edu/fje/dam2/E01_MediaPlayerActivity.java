package edu.fje.dam2;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Aplicació per a Android que reprodueixi una pista de musica desada en els
 * recursos del dispositiu. L'aplicació ha de disposar de botons per parar,
 * pausar i iniciar la pista de musica. Ha de mostrar el temps de reproducció.
 *
 * @author sergi.grau@fje.edu
 * @version 2.0 16.01.2017
 */
public class E01_MediaPlayerActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private Button botoPausa, botoParar;
    private TextView etiquetaNom, etiquetaTemps;
    boolean isExecutant = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isExecutant) {
                etiquetaTemps.setText(String.valueOf(mediaPlayer.getCurrentPosition()));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e01_activity_media_player);
        botoPausa = (Button) findViewById(R.id.botoPausa);
        botoParar = (Button) findViewById(R.id.botoParar);
        etiquetaNom = (TextView) findViewById(R.id.nomPista);
        etiquetaTemps = (TextView) findViewById(R.id.tempsPista);
        mediaPlayer = MediaPlayer.create(this, R.raw.audio1);

        etiquetaNom.setText("Bach");
    }

    /**
     * Mètode que controla l'esdeveniment de reproduir
     *
     * @param view
     */
    public void reproduir(View view) {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            botoPausa.setText("Reprodueix");

        } else {
            mediaPlayer.start();
            botoPausa.setText("Pausa");
        }
    }

    /**
     * Mètode que controla l'esdeveniment de parar
     *
     * @param view
     */
    public void parar(View view) {
        mediaPlayer.stop();

    }

    @Override
    public void onStart() {
        super.onStart();

        Thread durada = new Thread(new Runnable() {
            public void run() {
                while (isExecutant) {
                    try {
                        Thread.sleep(1000);
                        handler.sendMessage(handler.obtainMessage());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        isExecutant = true;
        durada.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        isExecutant = false;
    }
}
