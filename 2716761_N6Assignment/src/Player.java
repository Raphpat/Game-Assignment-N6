import game2D.Animation;
import game2D.Sprite;

public class Player extends Sprite {

	private static String playerImage = "images/characterSheet.png";
	private static int MAX_SHOTS = 5;

	private int shots;
	private int reload = 5000;
	private long reloadTime = 0;
	private boolean reloading = false;
	private Animation anim = new Animation();
	private boolean death = false;

	public Player() {
		super();

		anim.loadAnimationFromSheet(playerImage, 4, 1, 100);
		setAnimation(anim);
	}

	/**
	 * @return the shots
	 */
	public int getShots() {
		return shots;
	}

	/**
	 * Increments the shot counter
	 */
	public void incrementShot() {
		if (shots == MAX_SHOTS) {
			shots = 0;
			reloading = true;
		} else {
			shots++;
		}
	}

	/**
	 * @param i pause the animation at frame
	 */
	public void pauseAt(int i) {
		anim.pauseAt(i);

	}

	/**
	 * Play the animation
	 */
	public void play() {
		anim.play();
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
