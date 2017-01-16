package edu.fje.dam2;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Activitat que mostra una sequencia d'imatges
 * 
 * @author sergi grau
 * @version 2.0 16.01.2017
 * 
 */

public class E06_SequenciaImatgesActivity extends Activity {

	private AnimationDrawable animacio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e06_activity_sequencia_imatges);

		ImageView imatge = (ImageView) findViewById(R.id.imatge);
		imatge.setBackgroundResource(R.drawable.e06_animacio);
		animacio = (AnimationDrawable) imatge.getBackground();
	}

	public boolean onTouchEvent(MotionEvent event) {
		  if (event.getAction() == MotionEvent.ACTION_DOWN) {
			  animacio.start();
		    return true;
		  }
		  return super.onTouchEvent(event);
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.e06_sequencia_imatges, menu);
		return true;
	}

}
