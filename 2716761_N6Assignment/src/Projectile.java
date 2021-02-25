import javax.swing.ImageIcon;

import game2D.*;

/**
 * The Projectile class that will be what the sprites shoot at each other.
 * 
 * @author 2716761
 *
 */
public class Projectile extends Sprite {

	private static String projImage = "images/rock.png";

	private long explodingTime;
	private boolean exploding;
	private int explosionTimePerFrame = 100;

	/**
	 * Will construct a rock projectile
	 * 
	 * @param x  origin position
	 * @param y  origin position
	 * @param tx target position
	 * @param ty target position
	 */
	public Projectile(float x, float y, int tx, int ty) {
		super();
		exploding = false;

		Animation anim = new Animation();

		anim.addFrame(new ImageIcon(projImage).getImage(), 500);

		setAnimation(anim);

		setScale(0.5f);
		setPosition(x, y);
		setRotation(45);
		setVelocityX((tx - x) / 1000);
		setVelocityY((ty - y) / 1000);
	}

	public void destroy(long elapsed) {
		Animation anim = new Animation();
		anim.loadAnimationFromSheet("images/Desappearing (96x96).png", 7, 1, explosionTimePerFrame);

		setAnimation(anim);
		explodingTime = elapsed;
		exploding = true;
	}

	/**
	 * @return the time at which the exploding started
	 */
	public long getExplodingTime() {
		return explodingTime;
	}

	/**
	 * @return the duration of each image in the animation
	 */
	public int getExplosionTimePerFrame() {
		return explosionTimePerFrame;
	}

	/**
	 * @return true if the projectile is currently exploding
	 */
	public boolean isExploding() {
		return exploding;
	}

	/**
	 * Updates this Sprite's Animation and its position based on the elapsedTime.
	 * 
	 * @param The time that has elapsed since the last call to update
	 */
	public void update(long elapsedTime) {
		super.update(elapsedTime);
		if(isExploding()) {
			explodingTime += elapsedTime;
		}
	}

}
