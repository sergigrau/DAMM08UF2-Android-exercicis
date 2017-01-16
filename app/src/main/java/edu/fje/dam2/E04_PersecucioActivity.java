package edu.fje.dam2;

import android.os.Bundle;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.os.Handler;
import android.os.Message;

/**
 * Classe que implementa un joc de persecució d'un botó
 * 
 * @author sergi grau
 * @version 2.0 16.01.2017
 * 
 */
public class E04_PersecucioActivity extends Activity {
	private Button boto;
	private RelativeLayout root;
	private boolean isExecutant = true;
	private int i = 0;
	private int x = 0;
	private int y = 0;
	private int destX = 0;
	private int destY = 0;
	private DisplayMetrics dm = new DisplayMetrics();
	private int statusBarOffset = 0;
	private int temps = 5000;

	Handler handler = new Handler() {
		/**
		 * Mètode que es crida cada vegada que Handler rep un missatge
		 */
		@Override
		public void handleMessage(Message msg) {
			boto.setText(String.valueOf(i++));
			destX = (int) (Math.random() * root.getWidth());
			destY = (int) (Math.random() * root.getHeight());

			TranslateAnimation anim = new TranslateAnimation(x, destX, y, destY);
			x = destX;
			y = destY;
			anim.setDuration(1000);
			// anim.setFillAfter( true );
			boto.startAnimation(anim);
			boto.setX(x);
			boto.setY(y);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e04_activity_persecucio);
		boto = (Button) findViewById(R.id.boto);
		boto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.boto:

						ValueAnimator colorAnim = ObjectAnimator.ofInt(root,
								"backgroundColor", 0x00FF00FF, 0xffFFFFFF);
						colorAnim.setDuration(1000);
						colorAnim.setEvaluator(new ArgbEvaluator());
						colorAnim.setRepeatMode(ValueAnimator.REVERSE);
						colorAnim.start();

						destX = (int) (Math.random() * root.getWidth());
						destY = (int) (Math.random() * root.getHeight());

						TranslateAnimation anim = new TranslateAnimation(x, destX, y, destY);
						x = destX;
						y = destY;
						anim.setDuration(1000);
						boto.startAnimation(anim);
						boto.setX(x);
						boto.setY(y);

						temps -= 100;
				}
			}
		});

		root = (RelativeLayout) findViewById(R.id.layout);
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		statusBarOffset = dm.heightPixels - root.getMeasuredHeight();
	}

	@Override
	public void onStart() {
		super.onStart();
		boto.animate().translationX(10f);

		Thread fil = new Thread(new Runnable() {

			public void run() {
				try {
					while (isExecutant) {
						Thread.sleep(temps);

						handler.sendMessage(handler.obtainMessage());

					}
				} catch (Throwable t) {
				}
			}
		});
		isExecutant = true;
		fil.start();
	}

	@Override
	public void onStop() {
		super.onStop();
		isExecutant = false;
	}
}
