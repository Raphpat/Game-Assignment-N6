import game2D.Animation;
import game2D.Sprite;

public class Player extends Sprite {

	private static String playerImage = "images/characterSheet.png";
	private static int MAX_SHOTS = 5;
	
	private int shots;
	private int reload = 500;
	private Animation anim = new Animation();
	
	public Player() {
		super();
		
		anim.loadAnimationFromSheet(playerImage, 4, 1, 100);
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
		if(shots == MAX_SHOTS) {
			shots = 0;
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

}
