package edu.fje.dam2;

import java.io.IOException;

import android.Manifest;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Classe que implementa un reproductor de música. Accedeix als recursos de
 * mediastore i mostra cançons, albums o llistes de favorits.
 * Mostra informació a la barra de notificacions
 *
 * @author sergi.grau@fje.edu
 * @version 2.0 16.01.2017
 */
public class E03_ReproductorMusica extends ListActivity {

    private static final int CONSTANT_PERMIS_READ_EXTERNAL_STORAGE = 1;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    CONSTANT_PERMIS_READ_EXTERNAL_STORAGE);
            return;
        }


        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.TITLE);

        cursor = obtenirCursorPistes(this, cursor);

        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor,
                new String[]{MediaStore.MediaColumns.TITLE},
                new int[]{android.R.id.text1}, CursorAdapter.NO_SELECTION);
        setListAdapter(adapter);

        //permet enviar un intent a l'activitat que s'obrirà en seleccionar la notificació
        Intent intent = new Intent(this, E03_ReproductorMusica.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // construïm una notificació, els action van al mateix intent per simplificació
        Notification n = new Notification.Builder(this)
                .setContentTitle("Musica DAM2")
                .setContentText("Android")
                .setSmallIcon(R.drawable.sergigrau)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.sergigrau, "anterior", pIntent)
                .addAction(R.drawable.sergigrau, "pausa", pIntent)
                .addAction(R.drawable.sergigrau, "següent", pIntent).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = adapter.getCursor();
        cursor.moveToPosition(position);

        long _id = cursor.getLong(cursor
                .getColumnIndex(MediaStore.Audio.Media._ID));
        String titol = cursor.getString(cursor
                .getColumnIndex(MediaStore.Audio.Media.TITLE));
        String artista = cursor.getString(cursor
                .getColumnIndex(MediaStore.Audio.Media.ARTIST));
        String album = cursor.getString(cursor
                .getColumnIndex(MediaStore.Audio.Media.ALBUM));
        int durada = cursor.getInt(cursor
                .getColumnIndex(MediaStore.Audio.Media.DURATION));


        String info = "_ID: " + _id + "\n" + "TITOL: " + titol + "\n"
                + "ARTISTA: " + artista + "\n" + "ALBUM: " + album + "\n"
                + "DURADA: " + durada / 1000 + "s";

//		String info = "_ID: " + _id  + "\n"
//				+ "ARTISTA: " + artista + "\n" + "ALBUM: " + album + "\n";


        Toast.makeText(this, info, Toast.LENGTH_LONG).show();
        reproduirPista(_id);

    }

    /**
     * Recupera tots els albums
     *
     * @param context
     * @param cursor
     * @return retorna un cursor amb tots els àlbums
     */
    private Cursor obtenirCursorAlbums(Context context, Cursor cursor) {
        String criteri = null;
        ContentResolver cr = context.getContentResolver();
        final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        final String _id = MediaStore.Audio.Albums._ID;
        final String album_id = MediaStore.Audio.Albums.ALBUM_ID;
        final String album = MediaStore.Audio.Albums.ALBUM;
        final String artista = MediaStore.Audio.Albums.ARTIST;
        final String[] columnes = {_id, album, artista};

        cursor = cr.query(uri, columnes, criteri, null, null);
        return cursor;
    }

    /**
     * Recupera els playlist
     *
     * @param context
     * @param cursor
     * @return retorna un cursor amb tots els playlist
     */
    private Cursor obtenirCursorFavorits(Context context, Cursor cursor) {
        ContentResolver cr = context.getContentResolver();
        final Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        final String id = MediaStore.Audio.Playlists._ID;
        final String nom = MediaStore.Audio.Playlists.NAME;
        final String[] columnes = {id, nom};
        final String criteri = MediaStore.Audio.Playlists.NAME.length()
                + " > 0 ";
        final Cursor crplaylists = cr.query(uri, columnes, criteri, null, nom
                + "    `ASC");
        return crplaylists;
    }

    /**
     * recupera totes les pistes
     *
     * @param context
     * @param cursor
     * @return retorna un cursor amb tots els pistes
     */
    private Cursor obtenirCursorPistes(Context context, Cursor cursor) {
        final String track_id = MediaStore.Audio.Media._ID;
        final String track_no = MediaStore.Audio.Media.TRACK;
        final String titol = MediaStore.Audio.Media.TITLE;
        final String artista = MediaStore.Audio.Media.ARTIST;
        final String durada = MediaStore.Audio.Media.DURATION;
        final String album = MediaStore.Audio.Media.ALBUM;
        final String compositor = MediaStore.Audio.Media.COMPOSER;
        final String any = MediaStore.Audio.Media.YEAR;
        final String path = MediaStore.Audio.Media.DATA;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        ContentResolver cr = context.getContentResolver();
        final String[] columnes = {track_id, track_no, artista, titol, album,
                durada, path, any, compositor};
        cursor = cr.query(uri, columnes, null, null, null);
        return cursor;
    }

    /**
     * Mètode privat que reprodueix una pista o so
     *
     * @param _id del recurs
     */
    private void reproduirPista(long _id) {

        MediaPlayer mp = new MediaPlayer();
        try {
            Uri u = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, _id);

            Log.i("dam2", u.toString());
            mp.setDataSource(this, u);

            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare();
            mp.start();

        } catch (IOException e) {
        }
    }

}
