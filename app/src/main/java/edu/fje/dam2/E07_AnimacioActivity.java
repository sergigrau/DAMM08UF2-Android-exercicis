package edu.fje.dam2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

/**
 * Classe animacioSequencia sequencia d'un boto
 * 
 * @author sergi.grau@fje.edu
 * @version 2.0 16.01.2017
 * 
 */

public class E07_AnimacioActivity extends Activity {
	private Button boto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e07_animacio);
		boto = (Button) findViewById(R.id.boto);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
//		AnimatorSet as = (AnimatorSet) AnimatorInflater.loadAnimator(this,
//			    R.animator.animacio);
//			as.setTarget(boto);
//			as.start();

		AnimatorSet as = new AnimatorSet();
		as.playSequentially(ObjectAnimator.ofFloat(boto, "x", 200f),
				ObjectAnimator.ofFloat(boto, "y", 200f),
				ObjectAnimator.ofFloat(boto, "x", 0f),
				ObjectAnimator.ofFloat(boto, "y", 0f));
		as.setDuration(600);

		as.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator a) {
				super.onAnimationEnd(a);
				a.start();
			}

		});
		as.start();
	}

}
