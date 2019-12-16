package com.me4502.FractalMaker.GUI;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.me4502.FractalMaker.FractalMaker;
import com.me4502.FractalMaker.expression.Expression;
import com.me4502.MAPL.MAPL;
import com.me4502.MAPL.GUI.Screens.MAPLScreen;
import com.me4502.MAPL.slick.SlickMAPL;

public class FractalScreen extends MAPLScreen {

	int index;
	StringBuilder formula = new StringBuilder();

	boolean show;
	int timer = 0;

	int copies = 1;

	public boolean hasFractalChanged = false;

	public FractalScreen() {

		formula.append("cos(x)-sin(x)>=sqrt(tan(y))");
		//rand = new Random();
		try {
			rand = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	Random rand;

	@Override
	public void init() {

		hasFractalChanged = false;
		values = new Boolean[FractalMaker.instance.getWindowWidth()/copies][(FractalMaker.instance.getWindowHeight()-40)/copies];

		if(formula.length() == 0) {
			values = null;
			return;
		}
		try {
			Expression expression = Expression.compile(formula.toString(), "x", "y", "w", "h", "c", "ri", "rf");
			expression.optimize();
			int c = 0;
			for(int r = 0; r < FractalMaker.instance.getWindowWidth()/copies; r++)
				for(int rr = 0; rr < (FractalMaker.instance.getWindowHeight()-40)/copies; rr++) {
					//                                     x  y   w                                       h                                        c  ri              rf
					float exp = (float)expression.evaluate(r, rr, FractalMaker.instance.getWindowWidth(), FractalMaker.instance.getWindowHeight(), c, rand.nextInt(), rand.nextFloat());
					values[r][rr] = exp == 1f;
					c++;
				}
		} catch (Throwable e) {
			values = null;
			return;
		}
	}

	Boolean[][] values;

	@Override
	public void render(int x, int y) {

		String toDraw = "";
		if(timer % 20 == 0)
			show = !show;
		StringBuilder temp = new StringBuilder();
		temp.append(formula.toString());
		temp.insert(index, show ? "|" : " ");
		toDraw = temp.toString();
		FractalMaker.instance.mainFont.drawCentredFont(getCentreX(), FractalMaker.instance.getWindowHeight() - 20, toDraw, 255, 255, 255, 255);
		if(hasFractalChanged)
			init();
		if(values == null)
			return;
		MAPL.inst().getRenderer().setTextureState(false);

		MAPL.inst().getRenderer().pixels().startPixels();
		for(int copY = 0; copY < copies; copY++)
			for(int copX = 0; copX < copies; copX++) {
				GL11.glPushMatrix();
				for(int i = 0; i < values.length; i++)
					for(int ii = 0; ii < values[i].length; ii++)
						if(values[i][ii])
							MAPL.inst().getRenderer().pixels().drawPixel(FractalMaker.instance.getWindowWidth()/copies*copX+i, (FractalMaker.instance.getWindowHeight()-40)/copies*copY+ii, 1.0f,1.0f,1.0f,1.0f);
				GL11.glPopMatrix();
			}

		MAPL.inst().getRenderer().pixels().endPixels();

		MAPL.inst().getRenderer().setTextureState(true);
	}

	@Override
	public void update() {
		timer++;
	}

	@Override
	public void onKeyPress(int key, char character) {

		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && key == Keyboard.KEY_S) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					String fname = JOptionPane.showInputDialog(SlickMAPL.gameFrame, "Name!", "File Name", 1);
					BufferedImage pixelImage = new BufferedImage(values.length, values[0].length, BufferedImage.TYPE_INT_RGB);
					//int[] pixels = new int[values.length*values[0].length];
					//int p = 0;
					for(int i = 0; i < values.length; i++)
						for(int ii = 0; ii < values[i].length; ii++) {
							//if(values[i][ii])
							//	pixels[p++] = Color.WHITE.getRGB();
							//else
							//	pixels[p++] = Color.BLACK.getRGB();
							pixelImage.setRGB(i, ii, values[i][ii] ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
						}

					//pixelImage.setRGB(0, 0, values.length, values[0].length, pixels, 0, values[0].length);

					AffineTransform at = new AffineTransform();
					//at.translate(values[0].length / 2, values.length / 2);
					//at.rotate(Math.PI/2);
					//at.rotate(Math.PI/2);
					//at.rotate(Math.PI/2);
					//at.translate(-values[0].length/2, -values.length/2);
					AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
					pixelImage = op.filter(pixelImage, null);
					FileOutputStream fileOut = null;
					try {

						fileOut = new FileOutputStream(new File(MAPL.inst().getApplicationDirectory(), fname + ".png"));
						try {
							ImageIO.write(pixelImage, "PNG", fileOut);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						if(fileOut != null)
							try {
								fileOut.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
					}
				}
			});
			return;
		}
		if(key == Keyboard.KEY_LEFT && index >= 1) {
			index--;
			return;
		} else if(key == Keyboard.KEY_RIGHT && index < formula.length()) {
			index++;
			return;
		} else if(key == Keyboard.KEY_BACK && formula.length() > 1 && index > 0)
			formula.deleteCharAt(--index);
		else if(key == Keyboard.KEY_BACK && formula.length() == 1) {
			formula.setLength(0);
			index = 0;
		} else if(key == Keyboard.KEY_DELETE && formula.length() > 1 && index < formula.length())
			formula.deleteCharAt(index);
		else if(key == Keyboard.KEY_DELETE && formula.length() == 1) {
			formula.setLength(0);
			index = 0;
		} else if(allowedChars.indexOf(String.valueOf(character)) > -1) {
			formula.insert(index++, character);
		} else if (key == Keyboard.KEY_RETURN) {

		}
		else
			return;
		hasFractalChanged = true;
	}

	@Override
	public void onMouseClick(int x, int y, int button) {

	}

	public static final String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890*/()><=-+.^;%&|!~?: ";
}