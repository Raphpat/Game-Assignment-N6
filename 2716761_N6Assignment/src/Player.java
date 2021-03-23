import game2D.Animation;
import game2D.Sprite;

public class Player extends Sprite {

	private static String playerImage = "images/characterSheet.png";
	private static int MAX_SHOTS = 5;

	private int shots = MAX_SHOTS;
	private int reload = 5000;
	private long reloadTime = 0;
	private boolean reloading = false;
	private Animation anim = new Animation();

	public Player() {
		super();

		anim.loadAnimationFromSheet(playerImage, 4, 1, 100);
		setAnimation(anim);
	}

	/**
	 * @return the shots left in the player's clip
	 */
	public int getShots() {
		return shots;
	}

	/**
	 * Decrements the shot left counter
	 */
	public void incrementShot() {
		if (shots == 1) {
			shots = MAX_SHOTS;
			reloading = true;
		} else {
			shots--;
		}
	}
	
	/**
	 * @return total reload time of the player, in miliseconds
	 */
	public int getReload() {
		return reload;
	}
	
	/**
	 * @return how far into the reload the player is
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
