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
	 */
	public Projectile(int x, int y) {
		super();
		
		Animation anim = new Animation();
		
		anim.addFrame(new ImageIcon(projImage).getImage(), 500);

		setAnimation(anim);
		
        setScale(0.2f);
        setRotation(0.1f);
        setPosition(x, y);

        }
	

}
