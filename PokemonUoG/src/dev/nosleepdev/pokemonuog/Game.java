package dev.nosleepdev.pokemonuog;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import dev.nosleepdev.pokemonuog.display.Display;
import dev.nosleepdev.pokemonuog.gfx.ImageLoader;
import dev.nosleepdev.pokemonuog.gfx.SpriteSheet;

public class Game implements Runnable {

	private Display display;
	public String title;
	public int width, height;

	private boolean running = false;
	private Thread thread;

	private BufferStrategy bs;
	private Graphics g;
	private BufferedImage tileset;
	private BufferedImage background;
	private BufferedImage pokemon;
	private SpriteSheet tileSheet;
	private SpriteSheet pokeSheet;

	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
	}

	private void init() {
		display = new Display(title, width, height);
		tileset = ImageLoader.loadImage("/textures/tileset.png");
		background = ImageLoader.loadImage("/textures/background.png");
		pokemon = ImageLoader.loadImage("/textures/pokemon.png");
		tileSheet = new SpriteSheet(tileset);
		pokeSheet = new SpriteSheet(pokemon);
	}

	private void tick() {

	}

	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g.clearRect(0, 0, width, height);
		//Draw here
		//pixel 0,0 is upper left, y axis top to bottom

		g.drawImage(background, 0, 0, null);
		//pokeball
		g.drawImage(tileSheet.crop(75, 140, 16, 16), 172, 295, null);
		//93 207 18 24 gym sign
		g.drawImage(tileSheet.crop(93, 207, 18, 24), 162, 275, null);
		//159 4 18 16 trainer tips sign
		g.drawImage(tileSheet.crop(159, 4, 18, 16), 182, 275, null);
		//1743 11 25 25 mew
		g.drawImage(pokeSheet.crop(1743, 11, 25, 25), 430, 338, null);

		//End drawing
		bs.show();
		g.dispose();

	}

	public void run() {
		init();

		while(running) {
			tick();
			render();
		}

		stop();
	}

	public synchronized void start() {
		if(running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start(); //calls run method
	}

	public synchronized void stop() {
		if(!running)
			return;

		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
