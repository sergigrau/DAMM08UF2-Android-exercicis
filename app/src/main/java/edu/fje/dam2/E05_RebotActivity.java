package edu.fje.dam2;

import java.util.ArrayList;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

public class E05_RebotActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.e05_activity_animacio);
		LinearLayout container = (LinearLayout) findViewById(R.id.contenidor);
		container.addView(new VistaAnimacio(this));
	}

	public class VistaAnimacio extends View {

		private static final int VERMELL = 0xffFF8080;
		private static final int BLAU = 0xff8080FF;
		public final ArrayList pilotes = new ArrayList();

		public VistaAnimacio(Context context) {
			super(context);

			ValueAnimator colorAnim = ObjectAnimator.ofInt(this,
					"backgroundColor", VERMELL, BLAU);
			colorAnim.setDuration(3000);
			colorAnim.setEvaluator(new ArgbEvaluator());
			colorAnim.setRepeatCount(ValueAnimator.INFINITE);
			colorAnim.setRepeatMode(ValueAnimator.REVERSE);
			colorAnim.start();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			if (event.getAction() != MotionEvent.ACTION_DOWN
					&& event.getAction() != MotionEvent.ACTION_MOVE) {
				return false;

			}

			E05_RebotForma novaPilota = afegirPilota(event.getX(), event.getY());

			// animacioSequencia de la pilota
			float iniciY = novaPilota.getY();
			float finalY = getHeight() - 50f;
			float h = (float) getHeight();
			float esdevenimentY = event.getY();
			int durada = (int) (500 * ((h - esdevenimentY) / h));

			ValueAnimator rebot = ObjectAnimator.ofFloat(novaPilota, "y",
					iniciY, finalY);
			rebot.setDuration(durada);
			rebot.setInterpolator(new AccelerateInterpolator());
			ValueAnimator squashAnim1 = ObjectAnimator.ofFloat(novaPilota, "x",
					novaPilota.getX(), novaPilota.getX() - 25f);
			squashAnim1.setDuration(durada / 4);
			squashAnim1.setRepeatCount(1);
			squashAnim1.setRepeatMode(ValueAnimator.REVERSE);
			squashAnim1.setInterpolator(new DecelerateInterpolator());

			ValueAnimator squashAnim2 = ObjectAnimator.ofFloat(novaPilota,
					"width", novaPilota.getWidth(), novaPilota.getWidth() + 50);
			squashAnim2.setDuration(durada / 4);
			squashAnim2.setRepeatCount(1);

			squashAnim2.setRepeatMode(ValueAnimator.REVERSE);

			squashAnim2.setInterpolator(new DecelerateInterpolator());
			ValueAnimator stretchAnim1 = ObjectAnimator.ofFloat(novaPilota,
					"y", finalY, finalY + 25f);

			stretchAnim1.setDuration(durada / 4);
			stretchAnim1.setRepeatCount(1);
			stretchAnim1.setInterpolator(new DecelerateInterpolator());
			stretchAnim1.setRepeatMode(ValueAnimator.REVERSE);

			ValueAnimator stretchAnim2 = ObjectAnimator.ofFloat(novaPilota,
					"height",novaPilota.getHeight(), novaPilota.getHeight() - 25);

			stretchAnim2.setDuration(durada / 4);
			stretchAnim2.setRepeatCount(1);
			stretchAnim2.setInterpolator(new DecelerateInterpolator());
			stretchAnim2.setRepeatMode(ValueAnimator.REVERSE);

			ValueAnimator animacioRebot = ObjectAnimator.ofFloat(novaPilota,
					"y", finalY, iniciY);

			animacioRebot.setDuration(durada);
			animacioRebot.setInterpolator(new DecelerateInterpolator());


			AnimatorSet conjunt = new AnimatorSet();

			conjunt.play(rebot).before(squashAnim1);
			conjunt.play(squashAnim1).with(squashAnim2);
			conjunt.play(squashAnim1).with(stretchAnim1);
			conjunt.play(squashAnim1).with(stretchAnim2);
			conjunt.play(animacioRebot).after(stretchAnim2);

			ValueAnimator animacioOpacitat = ObjectAnimator.ofFloat(novaPilota,
					"alpha", 1f, 0f);
			animacioOpacitat.setDuration(250);
			animacioOpacitat.addListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationEnd(Animator animation) {

					pilotes.remove(((ObjectAnimator) animation).getTarget());

				}

			});


			AnimatorSet animatorSet = new AnimatorSet();

			animatorSet.play(conjunt).before(animacioOpacitat);
			animatorSet.start();
			return true;

		}

		private E05_RebotForma afegirPilota(float x, float y) {

			OvalShape cercle = new OvalShape();
			cercle.resize(50f, 50f);

			ShapeDrawable dibuixable = new ShapeDrawable(cercle);
			E05_RebotForma forma = new E05_RebotForma(dibuixable);

			forma.setX(x - 25f);
			forma.setY(y - 25f);

			int red = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			int color = 0xff000000 | red << 16 | green << 8 | blue;

			Paint paint = dibuixable.getPaint();
			int colorFosc = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;

			RadialGradient gradient = new RadialGradient(37.5f, 12.5f,
			50f, color, colorFosc, Shader.TileMode.CLAMP);
			paint.setShader(gradient);
			forma.setPaint(paint);

			pilotes.add(forma);

			return forma;

		}

		@Override
		protected void onDraw(Canvas canvas) {
			for (int i = 0; i < pilotes.size(); ++i) {
				E05_RebotForma forma = (E05_RebotForma) pilotes.get(i);
				canvas.save();
				canvas.translate(forma.getX(), forma.getY());
				forma.getShape().draw(canvas);
				canvas.restore();
			}

		}
	}

}