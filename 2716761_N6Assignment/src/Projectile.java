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

		Animation anim = new Animation();

		anim.addFrame(new ImageIcon(projImage).getImage(), 500);

		setAnimation(anim);

		setScale(0.5f);
		setPosition(x, y);
		setRotation(45);
		setVelocityX((tx - x) / 1000);
		setVelocityY((ty - y) / 1000);
	}

}
