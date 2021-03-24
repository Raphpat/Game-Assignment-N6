import game2D.Animation;
import game2D.Sprite;

/**
 * The Player class represents the player and handles the player's shooting,
 * reload and health
 * 
 * @author 2716761
 *
 */
public class Player extends Sprite {

	private static String playerImage = "images/characterSheet.png";
	private static int MAX_SHOTS = 5;
	private static int MAX_HEALTH = 3;

	private int shots = MAX_SHOTS;
	private int reload = 5000;
	private long reloadTime = 0;
	private boolean reloading = false;
	private Animation anim = new Animation();

	private int health = MAX_HEALTH;

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
	 * Reset the shots, refill the clip and put the counter back to 0
	 */
	public void resetShots() {
		shots = MAX_SHOTS;
		reloading = false;
		reloadTime = 0;
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

	/**
	 * Remove one HP of the player
	 */
	public void hit() {
		health--;
	}

	/**
	 * Add one HP to the player
	 */
	public void heal() {
		if (health < MAX_HEALTH) {
			health++;
		}
	}

	/**
	 * Restore the player to full HP
	 */
	public void healMax() {
		health = MAX_HEALTH;
	}

	/**
	 * @return the current HP of the player
	 */
	public int getHealth() {
		return health;
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
