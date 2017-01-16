package edu.fje.dam2;

import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * Classe que encapsula una Forma que serà animada
 * 
 * @author sergi grau
 * @version 2.0 16.01.2017
 * 
 */
public class E05_RebotForma {

	private float x = 0, y = 0;
	private ShapeDrawable forma;
	private int color;
	private RadialGradient gradient;
	private float alpha = 1f;
	private Paint paint; // conte geometries d'una forma

	public E05_RebotForma(ShapeDrawable s) {
		forma = s;
	}

	public void setPaint(Paint valor) {
		paint = valor;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setX(float valor) {
		x = valor;
	}

	public float getX() {
		return x;
	}

	public void setY(float valor) {
		y = valor;
	}

	public float getY() {
		return y;
	}

	public void setShape(ShapeDrawable valor) {
		forma = valor;
	}

	public ShapeDrawable getShape() {
		return forma;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int value) {
		forma.getPaint().setColor(value);
		color = value;

	}

	public void setGradient(RadialGradient value) {
		gradient = value;
	}

	public RadialGradient getGradient() {
		return gradient;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
		forma.setAlpha((int) ((alpha * 255f) + .5f));
	}

	public float getWidth() {
		return forma.getShape().getWidth();
	}

	public void setWidth(float width) {
		Shape s = forma.getShape();
		s.resize(width, s.getHeight());
	}

	public float getHeight() {
		return forma.getShape().getHeight();
	}

	public void setHeight(float height) {
		Shape s = forma.getShape();
		s.resize(s.getWidth(), height);
	}

}