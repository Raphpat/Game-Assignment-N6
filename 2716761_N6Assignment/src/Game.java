
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JOptionPane;

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
	static int screenHeight = 16 * 32;

	// Screen offset
	int xo = 0;
	int yo = 0;

	float lift = 0.005f;
	float gravity = 0.0001f;

	// Game state flags
	boolean flap = false;
	boolean debug = false;
	boolean gameEnd = false;
	long gameEndTime = 0;
	String level = "menu";
	Color colour1 = Color.black;
	Color colour2 = Color.black;

	// Game resources
	Animation mage;
	Animation end;

	Player player = null;
	Sprite[] enemy = null;
	Sprite cup = null;
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	TileMap tmap = new TileMap(); // Our tile map, note that we load it in initialiseGame()

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
//		Sprite s; // Temporary reference to a sprite

		// Initialise the player with an animation
		player = new Player();
		player.setMaxVelocity(0.2f);

		enemy = new Sprite[9];
		for(int i = 0; i < enemy.length; i++) {
			// Temporary enemy stuff
			mage = new Animation();
			mage.loadAnimationFromSheet("images/mageSheet.png", 4, 1, 100);
			enemy[i] = new Sprite(mage);
		}

		end = new Animation();
		end.loadAnimationFromSheet("images/end (Pressed) (64x64).png", 8, 1, 100);
		cup = new Sprite(end);

		initialiseGame();
	}

	/**
	 * You will probably want to put code to restart a game in a separate method so
	 * that you can call it to restart the game.
	 */
	public void initialiseGame() {
		gameEnd = false;
		debug = false;

		setSize(screenWidth, screenHeight);

		if (level.equals("level 1")) {
			tmap.loadMap("maps", "level1.txt");

			setVisible(true);

			player.setX(64);
			player.setY(192);
			player.setVelocityX(0);
			player.setVelocityY(0);
			player.show();

			enemy[0].setX(576);
			enemy[0].setY(160);
			enemy[0].setVelocityX(0.1f);
			enemy[0].setVelocityY(0);
			enemy[0].show();

			enemy[1].setX(128);
			enemy[1].setY(448);
			enemy[1].setVelocityX(0.1f);
			enemy[1].setVelocityY(0);
			enemy[1].show();

			enemy[2].setX(832);
			enemy[2].setY(480);
			enemy[2].setVelocityX(0.1f);
			enemy[2].setVelocityY(0);
			enemy[2].show();
			
			enemy[3].setX(832);
			enemy[3].setY(192);
			enemy[3].setVelocityX(0.1f);
			enemy[3].setVelocityY(0);
			enemy[3].show();
			
			enemy[4].setX(1216);
			enemy[4].setY(288);
			enemy[4].setVelocityX(0);
			enemy[4].setVelocityY(0.1f);
			enemy[4].show();

			enemy[5].setX(1088);
			enemy[5].setY(480);
			enemy[5].setVelocityX(0.1f);
			enemy[5].setVelocityY(0);
			enemy[5].show();

			enemy[6].setX(1440);
			enemy[6].setY(224);
			enemy[6].setVelocityX(0.1f);
			enemy[6].setVelocityY(0);
			enemy[6].show();

			enemy[7].setX(1600);
			enemy[7].setY(352);
			enemy[7].setVelocityX(0.1f);
			enemy[7].setVelocityY(0);
			enemy[7].show();
			
			enemy[8].setX(1344);
			enemy[8].setY(672);
			enemy[8].setVelocityX(0);
			enemy[8].setVelocityY(0.1f);
			enemy[8].show();
			
			cup.setX(1000);
			cup.setY(200);
			cup.setVelocityX(0);
			cup.setVelocityY(0);
			cup.hide();

			projectiles.clear();
		} else if (level.equals("level 2")) {
			tmap.loadMap("maps", "level2.txt");

			setVisible(true);
		} else if (level.equals("menu")) {

			player.setX(64);
			player.setY(200);
			player.setVelocityX(0);
			player.setVelocityY(0);
			player.show();
		}
	}

	/**
	 * Draw the current state of the game
	 */
	public void draw(Graphics2D g) {
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

		// Center the view on the player once it reaches the middle of the screen
		// To move the view to the bottom
		if (player.getY() + yo >= screenHeight / 2) {
			if (-yo >= tmap.getPixelHeight() - screenHeight) {
				// If the bottom edge of the map is reached, do not move further
				yo = -tmap.getPixelHeight() + screenHeight;
			} else {
				// Center on player
				yo = -((int) player.getY()) + (screenHeight / 2);
			}
		}

		// Center the view on the player once it reaches the middle of the screen
		// To move the view to the top
		if (player.getY() + yo <= screenHeight / 2) {
			if (yo >= 0) {
				// If the top edge of the map is reached, do not move further
				yo = 0;
			} else {
				// Center on player
				yo = -((int) player.getY()) + (screenHeight / 2);
			}
		}

		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Apply offsets to sprites then draw them
//		for (Sprite s : clouds) {
//			s.setOffsets(xo, yo);
//			s.draw(g);
//		}

		/**
		 * Draw differently based on the level
		 */
		if (level.equals("menu")) {
			xo = 0;
			yo = 0;
			player.setOffsets(xo, yo);
			player.setScale(2f);
			player.drawTransformed(g);
			player.playAnimation();

			g.setColor(colour1);
			g.fillRect(screenWidth - 200, 100, 150, 50);
			g.setColor(colour2);
			g.fillRect(screenWidth - 200, 200, 150, 50);

			g.setColor(Color.white);
			g.setFont(new Font("Monospaced", 2, 30));
			g.drawString("Level 1", screenWidth - 190, 133);
			g.drawString("Level 2", screenWidth - 190, 233);
		} else {
			// Apply offsets to player and draw
			player.setOffsets(xo, yo);
			player.draw(g);

			// Draw the enemies
			for (Sprite s : enemy) {
				s.setOffsets(xo, yo);
				s.draw(g);
			}

			cup.setOffsets(xo, yo);
			cup.draw(g);

			if (!projectiles.isEmpty()) {
				for (int i = 0; i < projectiles.size(); i++) {
					Projectile rock = projectiles.get(i);

					// Apply offsets to the projectiles and draw
					rock.setOffsets(xo, yo);
					rock.drawTransformed(g);
				}
			}

			// If the debugging is on, then draw the bounding boxes and circles
			if (debug) {
				g.setColor(Color.BLACK);
				player.drawBoundingBox(g);
				player.drawBoundingCircle(g);

				for (Sprite s : enemy) {
					s.drawBoundingBox(g);
					s.drawBoundingCircle(g);
				}

				for (Projectile proj : projectiles) {
					proj.drawBoundingBox(g);
					proj.drawBoundingCircle(g);
				}
			}

			// Apply offsets to tile map and draw it
			tmap.draw(g, xo, yo);

			if (player.isReloading()) {
				// Show reloading info
				String msg = String.format("Relaoding: %d", player.getReload() - player.getReloadTime());
				g.setColor(Color.darkGray);
				g.drawString(msg, 20, 70);
			} else {
				// Draw an apple per reamining apple in the clip
				for (int i = 0; i < player.getShots(); i++) {
					g.drawImage(loadImage("images/AppleSingle.png"), 30 + i * 20, 70, null);
				}
			}

		}
	}

	/**
	 * Update any sprites and check for collisions
	 * 
	 * @param elapsed The elapsed time between this call and the previous call of
	 *                elapsed
	 */
	public void update(long elapsed) {
		if (gameEnd) {
			player.update(elapsed);
			player.stop();

			for (Sprite s : enemy) {
				s.update(elapsed);
			}

			cup.update(elapsed);

			gameEndTime += elapsed;
			if (gameEndTime >= 5000) {
				level = "menu";
				gameEnd = false;
				gameEndTime = 0;
				initialiseGame();
			}
		} else if (level.equals("menu")) {
			player.update(elapsed);
		} else {
			// Now update the sprites animation and position
			if (player.getVelocityX() == 0 && player.getVelocityY() == 0) {
				player.pauseAnimationAtFrame(1);
			} else {
				player.playAnimation();
			}
			player.update(elapsed);

			for (Sprite s : enemy) {
				s.update(elapsed);
			}

			cup.update(elapsed);

			if (!projectiles.isEmpty()) {
				for (int i = 0; i < projectiles.size(); i++) {
					Projectile rock = projectiles.get(i);
					rock.update(elapsed);
					if (!rock.isExploding() && checkTileCollision(rock, tmap)) {
						rock.stop();
						rock.destroy(elapsed);
					}

					if (rock.isExploding() && rock.getExplodingTime() >= 7 * rock.getExplosionTimePerFrame()) {
						projectiles.remove(i);
					}
				}
			}

			// Then check for any collisions that may have occurred
			for (Sprite s : enemy) {
				if (s.isVisible() && boundingCircleCollision(player, s)) {
					JOptionPane.showMessageDialog(null, "Try Again!");
					initialiseGame();
				}
			}

			// Check for hits from projectiles onto the enemy sprite
			for (Projectile proj : projectiles) {
				for (Sprite s : enemy) {
					if (s.isVisible() && !proj.isExploding() && boundingCircleCollision(proj, s)) {
						// Move the projectile into the sprite a bit more before stopping it
						s.hide();
						proj.shiftX(proj.getVelocityX() * 100);
						proj.shiftY(proj.getVelocityY() * 100);
						proj.stop();

						proj.destroy(elapsed);
					}
					if (proj.isExploding() && proj.getExplodingTime() >= 7 * proj.getExplosionTimePerFrame()) {
						projectiles.remove(proj);
					}
				}
			}

			if (checkTileCollision(player, tmap)) {
				// Move the sprite out of the block
				if(player.getVelocityX() > 0) {
					player.shiftX(-2);
				} else if(player.getVelocityX() < 0) {
					player.shiftX(2);
				}
				if(player.getVelocityY() > 0) {
					player.shiftY(-2);
				} else if(player.getVelocityY() < 0) {
					player.shiftY(2);
				}
				// TODO fix the glitching through walls
				player.stop();
			}

			for (Sprite s : enemy) {
				if (checkTileCollision(s, tmap)) {
					// Move the sprite out of the block
					if(s.getVelocityX() > 0) {
						s.shiftX(-1);
					} else if(s.getVelocityX() < 0) {
						s.shiftX(1);
					}
					if(s.getVelocityY() > 0) {
						s.shiftY(-1);
					} else if(s.getVelocityY() < 0) {
						s.shiftY(1);
					}
					// TODO fix the glitching through walls
					// turn the sprite around
					s.setVelocity(-s.getVelocityX(), -s.getVelocityY());
				}
			}
		}
	}

	/**
	 * Override of the keyPressed event defined in GameCore to catch our own events
	 * 
	 * @param e The event that has been generated
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (level.equals("menu")) {
			if (key == KeyEvent.VK_ESCAPE)
				stop();
		} else {
			if (key == KeyEvent.VK_ESCAPE) {
				level = "menu";
				initialiseGame();
			}
			if (key == KeyEvent.VK_UP)
				// Compare negative velocity Y because when going up velocity Y is negative
				if (-player.getVelocityY() < player.getMaxVelocity())
					player.setVelocityY(player.getVelocityY() - 0.01f);

			if (key == KeyEvent.VK_DOWN) {
				if (player.getVelocityY() < player.getMaxVelocity())
					player.setVelocityY(player.getVelocityY() + 0.01f);
			}

			if (key == KeyEvent.VK_LEFT) {
				// Compare negative velocity X because when going left velocity X is negative
				if (-player.getVelocityX() < player.getMaxVelocity())
					player.setVelocityX(player.getVelocityX() - 0.01f);
			}

			if (key == KeyEvent.VK_RIGHT) {
				if (-player.getVelocityY() < player.getMaxVelocity())
					player.setVelocityX(player.getVelocityX() + 0.01f);
			}

			if (key == KeyEvent.VK_SPACE) {
				if (!player.isReloading()) {
					Point p = MouseInfo.getPointerInfo().getLocation();
					projectiles.add(new Projectile(player.getX(), player.getY(), p.x - xo, p.y - yo));
					if(!debug) {
						player.incrementShot();
					}
				}

			}

			if (key == KeyEvent.VK_COMMA) {
				debug = !debug;
				System.out.println("Debug is now " + debug);
			}

			if (key == KeyEvent.VK_S) {
				// Example of playing a sound as a thread
				Sound s = new Sound("sounds/caw.wav");
				s.start();
			}
		}
	}

	/**
	 * Checks bounding box for collisions
	 * 
	 * @param s1 The first sprite to check for a collision
	 * @param s2 The second sprite to check for a collision
	 * @return true if there is a collision between the two sprites' bounding box
	 */
	public boolean boundingBoxCollision(Sprite s1, Sprite s2) {
		return ((s1.getX() + s1.getImage().getWidth(null) * s1.getScale() > s2.getX())
				&& (s1.getX() < (s2.getX() + s2.getImage().getWidth(null) * s2.getScale()))
				&& ((s1.getY() + s1.getImage().getHeight(null) * s1.getScale() > s2.getY())
						&& (s1.getY() < s2.getY() + s2.getImage().getHeight(null) * s2.getScale())));
	}

	/**
	 * Checks bounding circle for collisions
	 * 
	 * @param s1 The first sprite to check for a collision
	 * @param s2 The second sprite to check for a collision
	 * @return true if there is a collision between the two sprites' bounding circle
	 */
	public boolean boundingCircleCollision(Sprite s1, Sprite s2) {
		return Math.sqrt(Math.pow((s2.getX() - s1.getX()), 2)
				+ Math.pow((s2.getY() - s1.getY()), 2)) <= (s1.getWidth() * s1.getScale()) / 2
						+ (s2.getWidth() * s2.getScale()) / 2;
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
			// Check for the cup
			if (ch == 'e') {
				tmap.setTileChar('.', xtile, ytile);
				cup.setPosition(sx, sy);
				cup.show();
				gameEnd = true;
			}

			if (s.getClass().equals(new Projectile().getClass())) {
				checkProjDestruction(ch, xtile, ytile);
			}
			return true;
		}

		// We need to consider the other corners of the sprite
		// The above looked at the top left position, let's look at the bottom left.
		xtile = (int) (sx / tileWidth);
		ytile = (int) ((sy + (s.getHeight() * s.getScale())) / tileHeight);
		ch = tmap.getTileChar(xtile, ytile);

		// If it's not empty space
		if (ch != '.') {
			// Check for the cup
			if (ch == 'e') {
				tmap.setTileChar('.', xtile, ytile);
				cup.setPosition(sx, sy);
				cup.show();
				gameEnd = true;
			}

			if (s.getClass().equals(new Projectile().getClass())) {
				checkProjDestruction(ch, xtile, ytile);
			}
			return true;
		}

		// Top right
		xtile = (int) ((sx + (s.getHeight() * s.getScale())) / tileWidth);
		ytile = (int) (sy / tileHeight);
		ch = tmap.getTileChar(xtile, ytile);

		// If it's not empty space
		if (ch != '.') {
			// Check for the cup
			if (ch == 'e') {
				tmap.setTileChar('.', xtile, ytile);
				cup.setPosition(sx, sy);
				cup.show();
				gameEnd = true;
			}

			if (s.getClass().equals(new Projectile().getClass())) {
				checkProjDestruction(ch, xtile, ytile);
			}
			return true;
		}

		// Bottom right
		xtile = (int) ((sx + (s.getHeight() * s.getScale())) / tileWidth);
		ytile = (int) ((sy + (s.getHeight() * s.getScale())) / tileHeight);
		ch = tmap.getTileChar(xtile, ytile);

		// If it's not empty space
		if (ch != '.') {
			// Check for the cup
			if (ch == 'e') {
				tmap.setTileChar('.', xtile, ytile);
				cup.setPosition(sx, sy);
				cup.show();
				gameEnd = true;
			}

			if (s.getClass().equals(new Projectile().getClass())) {
				checkProjDestruction(ch, xtile, ytile);
			}
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param ch
	 * @param x
	 * @param y
	 */
	public void checkProjDestruction(char ch, int x, int y) {
		if (ch == 't') {
			tmap.setTileChar('s', x, y);
		} else if (ch == 's') {
			tmap.setTileChar('c', x, y);
		} else if (ch == 'c') {
			tmap.setTileChar('.', x, y);
		}
	}

	public void keyReleased(KeyEvent e) {

		int key = e.getKeyCode();

		e.consume();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (level.equals("menu") && e.getX() > screenWidth - 200 && e.getX() < screenWidth - 50) {
			if (e.getY() > 100 && e.getY() < 150) {
				// Change the level and start the game
				level = "level 1";
				initialiseGame();
				// Restore the colour of the button
				colour1 = Color.black;
				// Restore the default size of the player
				player.setScale(1f);
			}
			if (e.getY() > 200 && e.getY() < 250) {
				// Change the level and start the game
				level = "level 2";
				initialiseGame();
				// Restore the colour of the button
				colour2 = Color.black;
				// Restore the default size of the player
				player.setScale(1f);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		if (level.equals("menu") && e.getX() > screenWidth - 200 && e.getX() < screenWidth - 50) {
			// If the mouse hovers over the level buttons, change their colours to grey
			if (e.getY() > 100 && e.getY() < 150) {
				colour1 = Color.gray;
			} else {
				colour1 = Color.black;
			}
			if (e.getY() > 200 && e.getY() < 250) {
				colour2 = Color.gray;
			} else {
				colour2 = Color.black;
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
