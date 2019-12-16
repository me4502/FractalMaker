package com.me4502.FractalMaker;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.opengl.renderer.Renderer;

import com.me4502.FractalMaker.GUI.FractalScreen;
import com.me4502.MAPL.MAPL;
import com.me4502.MAPL.MAPLException;
import com.me4502.MAPL.GUI.Screens.MAPLScreen;
import com.me4502.MAPL.GUI.Screens.ScreenType;
import com.me4502.MAPL.rendering.FontRenderer;
import com.me4502.MAPL.slick.MAPLSlickProgram;
import com.me4502.MAPL.slick.SlickMAPL;
import com.me4502.MAPL.slick.rendering.SlickFontRenderer;
import com.me4502.MAPL.util.config.YAMLConfiguration;
import com.me4502.MAPL.util.yaml.YAMLFormat;
import com.me4502.MAPL.util.yaml.YAMLProcessor;

public class FractalMaker implements MAPLSlickProgram {

	public static FractalMaker instance;

	public FractalMaker() {

		instance = this;
	}

	public static final int startX = 600, startY = 650;
	private static float scaleX, scaleY;
	private static int displayWidth, displayHeight;

	public GameContainer container;
	public LinkedList<MAPLScreen> currentScreens;

	public FontRenderer mainFont;

	public static void main(String[] args) {

		Renderer.setRenderer(Renderer.VERTEX_ARRAY_RENDERER);
		//Initializes the engine.
		new SlickMAPL().setup(new FractalMaker(), "Mathematical Equation Visualizer", startX, startY, false);
		scaleX = 1f;
		scaleY = 1f;
	}

	@Override
	public String getProgramName() {
		return "Mathematical Equation Visualizer";
	}

	@Override
	public int getWindowWidth() {
		return displayWidth;
	}

	@Override
	public int getWindowHeight() {
		return displayHeight;
	}

	@Override
	public float getWindowScaleX() {
		return scaleX;
	}

	@Override
	public float getWindowScaleY() {
		return scaleY;
	}

	@Override
	public YAMLConfiguration getConfiguration() {
		return null;
	}

	FractalConfig config;

	@Override
	public void load() {
		MAPL.inst().setProgram(this);
		//MAPL.inst().setGUIRenderer(maplRenderer = new AccountingRenderer());
		MAPL.inst().initialize();

		if(!new File(MAPL.inst().getApplicationDirectory(), "config.yml").exists())
			try {
				new File(MAPL.inst().getApplicationDirectory(), "config.yml").createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		config = new FractalConfig(new YAMLProcessor(new File(MAPL.inst().getApplicationDirectory(), "config.yml"), true, YAMLFormat.EXTENDED));
		config.load();
	}

	@Override
	public boolean close() {
		config.save();
		return true;
	}

	@Override
	public void init(GameContainer arg0) {

		displayWidth = arg0.getWidth();
		displayHeight = arg0.getHeight();
		container = arg0;
		arg0.getInput().enableKeyRepeat();
		mainFont = new SlickFontRenderer("Arial",Font.PLAIN,16);
		mainFont.init();
		SlickFontRenderer.setAntiAliasing(true);

		/* Instantiates all the screens */
		try {
			ScreenType.registerScreen("Fractal", FractalScreen.class);
		} catch(MAPLException e){
			e.printStackTrace();
			SlickMAPL.gameFrame.removeAll();
			return;
		}

		currentScreens = new LinkedList<MAPLScreen>();
		currentScreens.add(ScreenType.getInstanceOfScreen("Fractal"));
	}

	@Override
	public void update(GameContainer arg0, int arg1) {
		if(displayWidth != arg0.getWidth() || displayHeight != arg0.getHeight()) {
			displayWidth = arg0.getWidth();
			displayHeight = arg0.getHeight();
			for(MAPLScreen s : currentScreens)
				s.init();
		}
		if(!arg0.isPaused())
			currentScreens.get(currentScreens.size() - 1).update();
	}

	@Override
	public void render(GameContainer arg0, Graphics arg1) {
		scaleX = (float) arg0.getWidth() / startX;
		scaleY = (float) arg0.getHeight() / startY;
		for(MAPLScreen screen : currentScreens)
			screen.render(arg0.getInput().getMouseX(), arg0.getInput().getMouseY());
	}

	@Override
	public void mousePressed(int arg0, int arg1, int arg2) {

		currentScreens.get(currentScreens.size() - 1).onMouseClick(arg1, arg2, arg0);
	}

	@Override
	public void keyPressed(int arg0, char arg1) {

		currentScreens.get(currentScreens.size() - 1).onKeyPress(arg0, arg1);
	}

	public void setCurrentScreen(MAPLScreen screen) {
		currentScreens.clear();
		currentScreens.add(screen);
	}

	public void addCurrentScreen(MAPLScreen screen) {
		currentScreens.add(screen);
	}

	public void removeCurrentScreen() {
		currentScreens.remove(currentScreens.size() - 1);
	}

	public void removeScreen(MAPLScreen screen) {
		for(MAPLScreen s : currentScreens) {
			if(s.getClass() == screen.getClass())
				currentScreens.remove(s);
		}
	}

	public MAPLScreen getScreenOfType(String type) {

		MAPLScreen compare = ScreenType.getInstanceOfScreen(type);

		for(MAPLScreen s : currentScreens) {
			if(s.getClass() == compare.getClass())
				return s;
		}

		return null;
	}

	@Override
	public void mouseDragged(int oldX, int oldY, int newX, int newY) {
		currentScreens.get(currentScreens.size() - 1).onMouseDrag(oldX, oldY, newX, newY);
	}

	@Override
	public void mouseReleased(int arg0, int arg1, int arg2) {
		currentScreens.get(currentScreens.size() - 1).onMouseRelease(arg0, arg1, arg2);
	}

	@Override
	public void mouseWheelMoved(int arg0) {

	}

	@Override
	public void mouseClicked(int arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void mouseMoved(int arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void keyReleased(int arg0, char arg1) {

	}

	@Override
	public String getProgramVersion() {
		return "1.0";
	}
}