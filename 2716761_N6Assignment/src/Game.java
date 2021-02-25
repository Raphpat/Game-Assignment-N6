
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.awt.*;

import game2D.*;

// Game demonstrates how we can override the GameCore class
// to create our own 'game'. We usually need to implement at
// least 'draw' and 'update' (not including any local event handling)
// to begin the process. You should also add code to the 'init'
// method that will initialise event handlers etc. By default GameCore
// will handle the 'Escape' key to quit the game but you should
// override this with your own event handler.

/**
 * @author David Cairns
 *
 */
@SuppressWarnings("serial")

public class Game extends GameCore {
	// Useful game constants
	static int screenWidth = 512;
	static int screenHeight = 17 * 32;

	// Screen offset
	int xo = 0;
	int yo = 0;

	float lift = 0.005f;
	float gravity = 0.0001f;

	// Game state flags
	boolean flap = false;

	// Game resources
	Animation landing;

	Sprite player = null;
	ArrayList<Sprite> clouds = new ArrayList<Sprite>();
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	TileMap tmap = new TileMap(); // Our tile map, note that we load it in init()

	long total; // The score will be the total time elapsed since a crash

	/**
	 * The obligatory main method that creates an instance of our class and starts
	 * it running
	 * 
	 * @param args The list of parameters this program might use (ignored)
	 */
	public static void main(String[] args) {

		Game gct = new Game();
		gct.init();
		// Start in windowed mode with the given screen height and width
		gct.run(false, screenWidth, screenHeight);
	}

	/**
	 * Initialise the class, e.g. set up variables, load images, create animations,
	 * register event handlers
	 */
	public void init() {
		Sprite s; // Temporary reference to a sprite

		// Load the tile map and print it out so we can check it is valid
		tmap.loadMap("maps", "map.txt");

		setSize(tmap.getPixelWidth() / 4, tmap.getPixelHeight());
		setVisible(true);

		// Create a set of background sprites that we can
		// rearrange to give the illusion of motion

		landing = new Animation();
		landing.loadAnimationFromSheet("images/landbird.png", 4, 1, 60);

		// Initialise the player with an animation
		player = new Sprite(landing);
		player.setMaxVelocity(0.2f);

		// Load a single cloud animation
		Animation ca = new Animation();
		ca.addFrame(loadImage("images/cloud.png"), 1000);

		// Create 3 clouds at random positions off the screen
		// to the right
		for (int c = 0; c < 3; c++) {
			s = new Sprite(ca);
			s.setX(screenWidth + (int) (Math.random() * 200.0f));
			s.setY(30 + (int) (Math.random() * 150.0f));
			s.setVelocityX(-0.02f);
			s.show();
			clouds.add(s);
		}

		initialiseGame();

		System.out.println(tmap);
	}

	/**
	 * You will probably want to put code to restart a game in a separate method so
	 * that you can call it to restart the game.
	 */
	public void initialiseGame() {
		total = 0;

		player.setX(64);
		player.setY(200);
		player.setVelocityX(0);
		player.setVelocityY(0);
		player.show();
	}

	/**
	 * Draw the current state of the game
	 */
	public void draw(Graphics2D g) {
		// Be careful about the order in which you draw objects - you
		// should draw the background first, then work your way 'forward'

		// First work out how much we need to shift the view
		// in order to see where the player is.

		// Center the view on the player once it reaches the middle of the screen
		// To move the view to the right
		if (player.getX() + xo >= screenWidth / 2) {
			if (-xo >= tmap.getPixelWidth() - screenWidth) {
				// If the right edge of the map is reached, do not move further
				xo = -tmap.getPixelWidth() + screenWidth;
			} else {
				// Center on player
				xo = -((int) player.getX()) + (screenWidth / 2);
			}
		}

		// Center the view on the player once it reaches the middle of the screen
		// To move the view to the left
		if (player.getX() + xo <= screenWidth / 2) {
			if (xo >= 0) {
				// If the left edge of the map is reached, do not move further
				xo = 0;
			} else {
				// Center on player
				xo = -((int) player.getX()) + (screenWidth / 2);
			}
		}

		// If relative, adjust the offset so that
		// it is relative to the player

		// ...?

		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Apply offsets to sprites then draw them
		for (Sprite s : clouds) {
			s.setOffsets(xo, yo);
			s.draw(g);
		}

		// Apply offsets to player and draw
		player.setOffsets(xo, yo);
		player.draw(g);

		if (!projectiles.isEmpty()) {
			for (int i = 0; i < projectiles.size(); i++) {
				Projectile rock = projectiles.get(i);

				// Apply offsets to the projectiles and draw
				rock.setOffsets(xo, yo);
				rock.drawTransformed(g);
			}
		}

		// Apply offsets to tile map and draw it
		tmap.draw(g, xo, yo);

		// Show score and status information
		String msg = String.format("Score: %d", total / 100);
		g.setColor(Color.darkGray);
		g.drawString(msg, getWidth() - 80, 50);
	}

	/**
	 * Update any sprites and check for collisions
	 * 
	 * @param elapsed The elapsed time between this call and the previous call of
	 *                elapsed
	 */
	public void update(long elapsed) {

		// Make adjustments to the speed of the sprite due to gravity
		// player.setVelocityY(player.getVelocityY() + (gravity * elapsed));

		player.setAnimationSpeed(1.0f);

		if (flap) {
			player.setAnimationSpeed(1.8f);
			player.setVelocityY(-0.04f);
		}

		for (Sprite s : clouds)
			s.update(elapsed);

		// Now update the sprites animation and position
		player.update(elapsed);

		if (!projectiles.isEmpty()) {
			for (int i = 0; i < projectiles.size(); i++) {
				Projectile rock = projectiles.get(i);
				rock.update(elapsed);
				if (!rock.isExploding() && checkTileCollision(rock, tmap)) {
					rock.destroy(elapsed);
				}

				if (rock.isExploding() && rock.getExplodingTime() >= 7 * rock.getExplosionTimePerFrame()) {
					projectiles.remove(i);
				}
			}
		}

		// Then check for any collisions that may have occurred
		handleScreenEdge(player, tmap, elapsed);
		checkTileCollision(player, tmap);
	}

	/**
	 * Checks and handles collisions with the edge of the screen
	 * 
	 * @param s       The Sprite to check collisions for
	 * @param tmap    The tile map to check
	 * @param elapsed How much time has gone by since the last call
	 */
	public void handleScreenEdge(Sprite s, TileMap tmap, long elapsed) {
		// This method just checks if the sprite has gone off the bottom screen.
		// Ideally you should use tile collision instead of this approach

		if (s.getY() + s.getHeight() > tmap.getPixelHeight()) {
			// Put the player back on the map 1 pixel above the bottom
			s.setY(tmap.getPixelHeight() - s.getHeight() - 1);

			// and make them bounce
			s.setVelocityY(-s.getVelocityY());
		}
	}

	/**
	 * Override of the keyPressed event defined in GameCore to catch our own events
	 * 
	 * @param e The event that has been generated
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_ESCAPE)
			stop();

		if (key == KeyEvent.VK_UP)
			if (-player.getVelocityY() < player.getMaxVelocity())
				player.setVelocityY(player.getVelocityY() - 0.01f);

		if (key == KeyEvent.VK_DOWN) {
			if (player.getVelocityY() < player.getMaxVelocity())
				player.setVelocityY(player.getVelocityY() + 0.01f);
		}

		if (key == KeyEvent.VK_LEFT) {
			if (-player.getVelocityX() < player.getMaxVelocity())
				player.setVelocityX(player.getVelocityX() - 0.01f);
		}

		if (key == KeyEvent.VK_RIGHT) {
			if (-player.getVelocityY() < player.getMaxVelocity())
				player.setVelocityX(player.getVelocityX() + 0.01f);
		}

		if (key == KeyEvent.VK_SPACE) {
			// TODO Add handler to pass mouse position so that the projectile shoots to
			// where the mouse is
			Point p = MouseInfo.getPointerInfo().getLocation();

			projectiles.add(new Projectile(player.getX(), player.getY(), p.x - xo, p.y + yo));
		}

		if (key == KeyEvent.VK_S) {
			// Example of playing a sound as a thread
			Sound s = new Sound("sounds/caw.wav");
			s.start();
		}
	}

	public boolean boundingBoxCollision(Sprite s1, Sprite s2) {
		return false;
	}

	/**
	 * Check and handles collisions with a tile map for the given sprite 's'.
	 * Initial functionality is limited...
	 * 
	 * @param s    The Sprite to check collisions for
	 * @param tmap The tile map to check
	 * @return true if there was a tile collision
	 */

	public boolean checkTileCollision(Sprite s, TileMap tmap) {
		// Take a note of a sprite's current position
		float sx = s.getX();
		float sy = s.getY();

		// Find out how wide and how tall a tile is
		float tileWidth = tmap.getTileWidth();
		float tileHeight = tmap.getTileHeight();

		// Divide the sprite x coordinate by the width of a tile, to get
		// the number of tiles across the x axis that the sprite is positioned at
		int xtile = (int) (sx / tileWidth);
		// The same applies to the y coordinate
		int ytile = (int) (sy / tileHeight);

		// What tile character is at the top left of the sprite s?
		char ch = tmap.getTileChar(xtile, ytile);

		if (ch != '.') // If it's not a dot (empty space), handle it
		{
			// Stop the sprite and move him back one pixel
			s.stop();
			s.shiftX(1);
			s.shiftY(1);
			// TODO Improve object destruction
			return true;
		}

		// We need to consider the other corners of the sprite
		// The above looked at the top left position, let's look at the bottom left.
		xtile = (int) (sx / tileWidth);
		ytile = (int) ((sy + (s.getHeight() * s.getScale())) / tileHeight);
		ch = tmap.getTileChar(xtile, ytile);

		// If it's not empty space
		if (ch != '.') {
			// Stop the sprite and move him back one pixel
			s.stop();
			s.shiftX(1);
			s.shiftY(-1);
			// TODO Improve object destruction
			return true;
		}

		// Top right
		xtile = (int) ((sx + (s.getHeight() * s.getScale())) / tileWidth);
		ytile = (int) (sy / tileHeight);
		ch = tmap.getTileChar(xtile, ytile);

		// If it's not empty space
		if (ch != '.') {
			// Stop the sprite and move him back one pixel
			s.stop();
			s.shiftX(-1);
			s.shiftY(1);
			return true;
		}

		// Bottom right
		xtile = (int) ((sx + (s.getHeight() * s.getScale())) / tileWidth);
		ytile = (int) ((sy + (s.getHeight() * s.getScale())) / tileHeight);
		ch = tmap.getTileChar(xtile, ytile);

		// If it's not empty space
		if (ch != '.') {
			// Stop the sprite and move him back one pixel
			s.stop();
			s.shiftX(-1);
			s.shiftY(-1);
			return true;
		}

		return false;
	}

	public void keyReleased(KeyEvent e) {

		int key = e.getKeyCode();

		// Switch statement instead of lots of ifs...
		// Need to use break to prevent fall through.
		switch (key) {
		case KeyEvent.VK_ESCAPE:
			stop();
			break;
		case KeyEvent.VK_UP:
			flap = false;
			break;
		default:
			break;
		}
	}
}
