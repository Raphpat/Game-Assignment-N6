
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import game2D.*;

// Game demonstrates how we can override the GameCore class
// to create our own 'game'. We usually need to implement at
// least 'draw' and 'update' (not including any local event handling)
// to begin the process. You should also add code to the 'init'
// method that will initialise event handlers etc. By default GameCore
// will handle the 'Escape' key to quit the game but you should
// override this with your own event handler.

/**
 * @author 2716761
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
	Animation end;

	Player player = null;
	Mage[] enemy = null;
	Sprite cup = null;
	Sprite[] heart = null;
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	ArrayList<Projectile> enemyProjectiles = new ArrayList<Projectile>();

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

		// Initialise the player with an animation
		player = new Player();
		player.setMaxVelocity(0.2f);

		// Create the enemies
		enemy = new Mage[14];
		for (int i = 0; i < enemy.length; i++) {
			enemy[i] = new Mage();
		}

		heart = new Sprite[3];
		for (int i = 0; i < heart.length; i++) {
			Animation anim = new Animation();
			anim.addFrame(loadImage("images/heart.png"), 500);
			heart[i] = new Sprite(anim);
		}

		end = new Animation();
		end.loadAnimationFromSheet("images/end (Pressed) (64x64).png", 8, 1, 100);
		end.pauseAt(0);
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
		player.resetShots();
		player.healMax();

		setTitle("Knight Splat: " + level);
		setSize(screenWidth, screenHeight);

		// The following is just positionning of sprites, skip to line ~ 290

		if (level.equals("level 1")) {
			tmap.loadMap("maps", "level1.txt");

			setVisible(true);

			player.setX(96);
			player.setY(192);
			player.setVelocityX(0);
			player.setVelocityY(0);
			player.show();
			
			cup.setX(1888);
			cup.setY(640);
			cup.setVelocityX(0);
			cup.setVelocityY(0);
			cup.show();

			heart[0].setX(584);
			heart[0].setY(544);
			heart[0].setVelocityX(0);
			heart[0].setVelocityY(0);
			heart[0].show();

			heart[1].setX(1096);
			heart[1].setY(448);
			heart[1].setVelocityX(0);
			heart[1].setVelocityY(0);
			heart[1].show();

			heart[2].setX(1992);
			heart[2].setY(160);
			heart[2].setVelocityX(0);
			heart[2].setVelocityY(0);
			heart[2].show();

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

			enemy[9].hide();
			enemy[10].hide();
			enemy[11].hide();
			enemy[12].hide();
			enemy[13].hide();

			projectiles.clear();
		} else if (level.equals("level 2")) {
			tmap.loadMap("maps", "level2.txt");

			player.setX(1312);
			player.setY(96);
			player.setVelocityX(0);
			player.setVelocityY(0);
			player.show();
			
			cup.setX(160);
			cup.setY(96);
			cup.setVelocityX(0);
			cup.setVelocityY(0);
			cup.show();

			heart[0].setX(2216);
			heart[0].setY(390);
			heart[0].setVelocityX(0);
			heart[0].setVelocityY(0);
			heart[0].show();

			heart[1].setX(488);
			heart[1].setY(420);
			heart[1].setVelocityX(0);
			heart[1].setVelocityY(0);
			heart[1].show();

			heart[2].setX(360);
			heart[2].setY(230);
			heart[2].setVelocityX(0);
			heart[2].setVelocityY(0);
			heart[2].show();

			enemy[0].setX(96);
			enemy[0].setY(64);
			enemy[0].setVelocityX(0);
			enemy[0].setVelocityY(0.1f);
			enemy[0].show();

			enemy[1].setX(256);
			enemy[1].setY(128);
			enemy[1].setVelocityX(0);
			enemy[1].setVelocityY(0.1f);
			enemy[1].show();

			enemy[2].setX(544);
			enemy[2].setY(96);
			enemy[2].setVelocityX(0.1f);
			enemy[2].setVelocityY(0);
			enemy[2].show();

			enemy[3].setX(256);
			enemy[3].setY(288);
			enemy[3].setVelocityX(0);
			enemy[3].setVelocityY(0.1f);
			enemy[3].show();

			enemy[4].setX(256);
			enemy[4].setY(416);
			enemy[4].setVelocityX(0);
			enemy[4].setVelocityY(0.1f);
			enemy[4].show();

			enemy[5].setX(704);
			enemy[5].setY(96);
			enemy[5].setVelocityX(0);
			enemy[5].setVelocityY(0.1f);
			enemy[5].show();

			enemy[6].setX(960);
			enemy[6].setY(256);
			enemy[6].setVelocityX(0.1f);
			enemy[6].setVelocityY(0);
			enemy[6].show();

			enemy[7].setX(1120);
			enemy[7].setY(192);
			enemy[7].setVelocityX(0);
			enemy[7].setVelocityY(0.1f);
			enemy[7].show();

			enemy[8].setX(1216);
			enemy[8].setY(384);
			enemy[8].setVelocityX(0.1f);
			enemy[8].setVelocityY(0);
			enemy[8].show();

			enemy[9].setX(1504);
			enemy[9].setY(320);
			enemy[9].setVelocityX(0.1f);
			enemy[9].setVelocityY(0);
			enemy[9].show();

			enemy[10].setX(1792);
			enemy[10].setY(160);
			enemy[10].setVelocityX(0.1f);
			enemy[10].setVelocityY(0);
			enemy[10].show();

			enemy[11].setX(1792);
			enemy[11].setY(416);
			enemy[11].setVelocityX(0.1f);
			enemy[11].setVelocityY(0);
			enemy[11].show();

			enemy[12].setX(2336);
			enemy[12].setY(416);
			enemy[12].setVelocityX(0);
			enemy[12].setVelocityY(0.1f);
			enemy[12].show();

			enemy[13].setX(2336);
			enemy[13].setY(192);
			enemy[13].setVelocityX(0);
			enemy[13].setVelocityY(0.1f);
			enemy[13].show();

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

		g.drawImage(loadImage("images/Nebula Blue.png"), xo / 2, yo / 2, null);

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
			
			// Draw the victory cup
			cup.setOffsets(xo, yo);
			cup.draw(g);

			// Draw the enemies
			for (Mage s : enemy) {
				s.setOffsets(xo, yo);
				s.draw(g);
			}

			// Draw the hearts
			for (Sprite s : heart) {
				s.setOffsets(xo, yo);
				s.draw(g);
			}

			if (!projectiles.isEmpty()) {
				for (int i = 0; i < projectiles.size(); i++) {
					Projectile rock = projectiles.get(i);

					// Apply offsets to the projectiles and draw
					rock.setOffsets(xo, yo);
					rock.drawTransformed(g);
				}
			}

			if (!enemyProjectiles.isEmpty()) {
				for (int i = 0; i < enemyProjectiles.size(); i++) {
					Projectile rock = enemyProjectiles.get(i);

					// Apply offsets to the projectiles and draw
					rock.setOffsets(xo, yo);
					rock.drawTransformed(g);
				}
			}

			// If the debugging is on, then draw the bounding boxes and circles
			if (debug) {
				g.setColor(Color.white);
				player.drawBoundingBox(g);
				player.drawBoundingCircle(g);

				for (Mage s : enemy) {
					s.drawBoundingBox(g);
					s.drawBoundingCircle(g);
				}

				cup.drawBoundingBox(g);
				cup.drawBoundingCircle(g);

				for (Projectile proj : projectiles) {
					proj.drawBoundingBox(g);
					proj.drawBoundingCircle(g);
				}
			}

			// Apply offsets to tile map and draw it
			tmap.draw(g, xo, yo);

			if (player.isReloading()) {
				// Show reloading info
				String msg = String.format("Reloading: %d", player.getReload() - player.getReloadTime());
				g.setColor(Color.white);
				g.drawString(msg, 20, 70);
			} else {
				// Draw an apple per reamining apple in the clip
				for (int i = 0; i < player.getShots(); i++) {
					g.drawImage(loadImage("images/AppleSingle.png"), 30 + i * 20, 70, null);
				}
			}

			for (int i = player.getHealth(); i > 0; i--) {
				g.drawImage(loadImage("images/heart.png"), 10 + i * 20, 90, null);
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

			// Before moving the player, check the next tile collision. If there is going to
			// be one, revert to previous coordinates
			if (!debug && checkFutureTileCollision(player, tmap)) {
				player.setPosition(player.getPreviousX(), player.getPreviousY());
				player.stop();
			}
			player.update(elapsed);

			for (Sprite s : enemy) {
				s.update(elapsed);
			}

			for (Sprite s : heart) {
				s.update(elapsed);
			}

			cup.update(elapsed);

			// Tilemap collisions for projectiles
			if (!projectiles.isEmpty()) {
				for (int i = 0; i < projectiles.size(); i++) {
					Projectile rock = projectiles.get(i);
					rock.update(elapsed);
					if (!rock.isExploding() && checkTileCollision(rock, tmap)) {
						rock.stop();
						rock.destroy(elapsed, Math.sqrt(
								Math.pow(rock.getX() - player.getX(), 2) + Math.pow(rock.getY() - player.getY(), 2)));
					}

					if (rock.isExploding() && rock.getExplodingTime() >= 7 * rock.getExplosionTimePerFrame()) {
						projectiles.remove(i);
					}
				}
			}

			// Handle player collisions with hearts
			for (Sprite s : heart) {
				if (s.isVisible() && boundingBoxCollision(player, s)) {
					if (player.getHealth() < player.getMaxHealth()) {
						s.hide();
						player.heal();
					}
				}
			}
			
			// Handle player collision with cup
			if(boundingCircleCollision(player, cup)) {
				gameEnd = true;
				cup.playAnimation();
			}

			// Tilemap collisions for enemy projectiles
			if (!enemyProjectiles.isEmpty()) {
				for (int i = 0; i < enemyProjectiles.size(); i++) {
					Projectile rock = enemyProjectiles.get(i);
					rock.update(elapsed);
					if (!rock.isExploding() && checkTileCollision(rock, tmap)) {
						rock.stop();
						rock.destroy(elapsed, Math.sqrt(
								Math.pow(rock.getX() - player.getX(), 2) + Math.pow(rock.getY() - player.getY(), 2)));
					}

					if (rock.isExploding() && rock.getExplodingTime() >= 7 * rock.getExplosionTimePerFrame()) {
						enemyProjectiles.remove(i);
					}
				}
			}

			for (Mage s : enemy) {
				// Check for tile collisions between enemies and player
				if (!debug && s.isVisible() && boundingCircleCollision(player, s)) {
					new Sound("sounds/dead.wav").start();
					JOptionPane.showMessageDialog(null, "Try Again!");
					level = "menu";
					initialiseGame();
				}
				// The enemies can shoot
				if (s.isVisible() && !s.isReloading()) {
					double angle = Math.atan2(s.getY() - player.getY(), s.getX() - player.getX()) + Math.PI;
					Velocity v = new Velocity(0.2, angle);
					enemyProjectiles.add(new Projectile(s.getX(), s.getY(), v));
					s.setReloading(true);
				}
			}

			// Check for hits from projectiles onto the enemy sprite
			for (Projectile proj : projectiles) {
				for (Mage s : enemy) {
					if (s.isVisible() && !proj.isExploding() && boundingCircleCollision(proj, s)) {
						// Move the projectile into the sprite a bit more before stopping it
						s.hide();
						proj.shiftX(proj.getVelocityX() * 100);
						proj.shiftY(proj.getVelocityY() * 100);
						proj.stop();

						proj.destroy(elapsed, Math.sqrt(
								Math.pow(proj.getX() - player.getX(), 2) + Math.pow(proj.getY() - player.getY(), 2)));
					}
					if (proj.isExploding() && proj.getExplodingTime() >= 7 * proj.getExplosionTimePerFrame()) {
						projectiles.remove(proj);
					}
				}
			}

			// Check for hits from enemy projectiles onto the player sprite
			for (Projectile proj : enemyProjectiles) {
				if (!proj.isExploding() && boundingCircleCollision(proj, player)) {
					// Move the projectile into the sprite a bit more before stopping it
					proj.shiftX(proj.getVelocityX() * 100);
					proj.shiftY(proj.getVelocityY() * 100);
					proj.stop();

					proj.destroy(elapsed, Math
							.sqrt(Math.pow(proj.getX() - player.getX(), 2) + Math.pow(proj.getY() - player.getY(), 2)));

					if (!debug) {
						// Remove one HP
						player.hit();

						if (player.getHealth() <= 0) {
							new Sound("sounds/dead.wav").start();
							JOptionPane.showMessageDialog(null, "Try Again!");
							level = "menu";
							initialiseGame();
						} else {
							new Sound("sounds/hit.wav").start();
						}
					}
				}
				if (proj.isExploding() && proj.getExplodingTime() >= 7 * proj.getExplosionTimePerFrame()) {
					enemyProjectiles.remove(proj);
				}
			}

//			// Check all the player and enemies tile collisions
//			if (checkFutureTileCollision(player, tmap)) {
//				// Move the sprite out of the block
//				if (player.getVelocityX() > 0) {
//					player.shiftX(-2);
//				} else if (player.getVelocityX() < 0) {
//					player.shiftX(2);
//				}
//				if (player.getVelocityY() > 0) {
//					player.shiftY(-2);
//				} else if (player.getVelocityY() < 0) {
//					player.shiftY(2);
//				}
//				// TODO fix the glitching through walls
//				player.stop();
//			}

			for (Mage s : enemy) {
				if (checkTileCollision(s, tmap)) {
					// Move the sprite out of the block
					if (s.getVelocityX() > 0) {
						s.shiftX(-1);
					} else if (s.getVelocityX() < 0) {
						s.shiftX(1);
					}
					if (s.getVelocityY() > 0) {
						s.shiftY(-1);
					} else if (s.getVelocityY() < 0) {
						s.shiftY(1);
					}
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
			if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W)
				// Compare negative velocity Y because when going up velocity Y is negative
				if (-player.getVelocityY() < player.getMaxVelocity())
					player.setVelocityY(player.getVelocityY() - 0.01f);

			if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
				if (player.getVelocityY() < player.getMaxVelocity())
					player.setVelocityY(player.getVelocityY() + 0.01f);
			}

			if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
				// Compare negative velocity X because when going left velocity X is negative
				if (-player.getVelocityX() < player.getMaxVelocity())
					player.setVelocityX(player.getVelocityX() - 0.01f);
			}

			if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
				if (-player.getVelocityY() < player.getMaxVelocity())
					player.setVelocityX(player.getVelocityX() + 0.01f);
			}

			if (key == KeyEvent.VK_SPACE) {
				if (!player.isReloading()) {
					Point p = MouseInfo.getPointerInfo().getLocation();
					double angle = Math.atan2(player.getY() - (p.y - yo), player.getX() - (p.x - xo)) + Math.PI;
					Velocity v = new Velocity(0.3, angle);
					projectiles.add(new Projectile(player.getX(), player.getY(), v));
					if (!debug) {
						player.incrementShot();
					}
				}
			}

			if (key == KeyEvent.VK_COMMA) {
				debug = !debug;
				System.out.println("Debug is now " + debug);
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
//			// Check for the cup
//			if (ch == 'e' && s.getClass().equals(new Player().getClass())) {
//				tmap.setTileChar('.', xtile, ytile);
//				cup.setPosition(sx, sy);
//				cup.show();
//				gameEnd = true;
//			}

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
//			// Check for the cup
//			if (ch == 'e' && s.getClass().equals(new Player().getClass())) {
//				tmap.setTileChar('.', xtile, ytile);
//				cup.setPosition(sx, sy);
//				cup.show();
//				gameEnd = true;
//			}

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
//			if (ch == 'e' && s.getClass().equals(new Player().getClass())) {
//				tmap.setTileChar('.', xtile, ytile);
//				cup.setPosition(sx, sy);
//				cup.show();
//				gameEnd = true;
//			}

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
//			// Check for the cup
//			if (ch == 'e' && s.getClass().equals(new Player().getClass())) {
//				tmap.setTileChar('.', xtile, ytile);
//				cup.setPosition(sx, sy);
//				cup.show();
//				gameEnd = true;
//			}

			if (s.getClass().equals(new Projectile().getClass())) {
				checkProjDestruction(ch, xtile, ytile);
			}
			return true;
		}

		return false;
	}

	//
	/**
	 * Figure out where the next move is and then authorise
	 * 
	 * @param s    The Sprite to check collisions for
	 * @param tmap The tile map to check
	 * @return true if there was a tile collision
	 */
	public boolean checkFutureTileCollision(Sprite s, TileMap tmap) {
		// Take a note of a sprite's current position
		float sx = s.getX() + s.getVelocityX();
		float sy = s.getY() + s.getVelocityY();

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
			return true;
		}

		// We need to consider the other corners of the sprite
		// The above looked at the top left position, let's look at the bottom left.
		xtile = (int) (sx / tileWidth);
		ytile = (int) ((sy + (s.getHeight() * s.getScale())) / tileHeight);
		ch = tmap.getTileChar(xtile, ytile);

		// If it's not empty space
		if (ch != '.') {
			return true;
		}

		// Top right
		xtile = (int) ((sx + (s.getHeight() * s.getScale())) / tileWidth);
		ytile = (int) (sy / tileHeight);
		ch = tmap.getTileChar(xtile, ytile);

		// If it's not empty space
		if (ch != '.') {
			return true;
		}

		// Bottom right
		xtile = (int) ((sx + (s.getHeight() * s.getScale())) / tileWidth);
		ytile = (int) ((sy + (s.getHeight() * s.getScale())) / tileHeight);
		ch = tmap.getTileChar(xtile, ytile);

		// If it's not empty space
		if (ch != '.') {
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

	}
}
