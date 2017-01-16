package edu.fje.dam2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Aplicació per a Android que reprodueixi una pista de musica desada en els
 * recursos del dispositiu. L'aplicació ha de disposar de botons per parar,
 * pausar i iniciar la pista de musica. Ha de mostrar el temps de reproducció.
 * 
 * @author sergi.grau@fje.edu
 * @version 2.0 16.01.2017
 * 
 */

public class E02_MusicaActivity extends Activity implements OnClickListener {
	private static final String IDENTIFICADOR = "E02_MusicaService";
	private Button botoPausa;
	private Button botoParar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e02_activity_musica);
		botoPausa = (Button) findViewById(R.id.botoPausa);
		botoParar = (Button) findViewById(R.id.botoParar);
		
		botoPausa.setOnClickListener(this);
		botoParar.setOnClickListener(this);
		    
	}

	public void onClick(View src) {
		switch (src.getId()) {
		case R.id.botoPausa:

			botoPausa.setText("Reprodueix");
			Log.d(IDENTIFICADOR, "onClick: iniciant servei");
			startService(new Intent(this, E02_MusicaService.class));
			break;

		case R.id.botoParar:
			Log.d(IDENTIFICADOR, "onClick: parant servei");
			stopService(new Intent(this, E02_MusicaService.class));
			break;
		}
	}
}