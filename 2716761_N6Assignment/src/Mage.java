import game2D.Animation;
import game2D.Sprite;

public class Mage extends Sprite {

	private static String playerImage = "images/mageSheet.png";
	
	private int reload = 3000;
	private long reloadTime = 0;
	private boolean reloading = false;
	private Animation anim = new Animation();
	
	public Mage() {
		super();

		anim.loadAnimationFromSheet(playerImage, 4, 1, 100);
		setAnimation(anim);
	}
	
	/**
	 * Reset the shots, put the counter back to 0
	 */
	public void resetShots() {
		reloading = false;
		reloadTime = 0;
	}
	
	/**
	 * @return total reload time of the mage, in miliseconds
	 */
	public int getReload() {
		return reload;
	}
	
	/**
	 * @return how far into the reload the mage is
	 */
	public long getReloadTime() {
		return reloadTime;
	}

	/**
	 * @return the reloading
	 */
	public boolean isReloading() {
		return reloading;
	}

	/**
	 * @param reloading the reloading to set
	 */
	public void setReloading(boolean reloading) {
		this.reloading = reloading;
	}
	
	public void update(long elapsedTime) {
		super.update(elapsedTime);
		if (reloading) {
			reloadTime += elapsedTime;
		} 
		// Allow shooting again
		if (reloadTime >= reload) {
			reloading = false;
			reloadTime = 0;
		}
	}
}
