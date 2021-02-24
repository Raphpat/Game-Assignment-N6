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
	public Projectile(int mouseX, int mouseY) {
		//System.out.println("Building a proj");
		Animation anim = new Animation();
		
		anim.addFrame(new ImageIcon(projImage).getImage(), 500);
		//System.out.println("Building a proj 2");
		setAnimation(anim);
		
        setScale(0.2f);
        setRotation(0.1f);
        setPosition(mouseX, mouseY);
        //System.out.println("Building a proj 3");
        }
	

}
