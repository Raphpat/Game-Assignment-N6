import game2D.*;

/**
 * The Projectile class that will be what the sprites shoot at each other.
 * 
 * @author 2716761
 *
 */
public class Projectile extends Sprite {

	private static String projImage = "images/Apple.png";
	private static String explodingImage = "images/Desappearing (96x96).png";
	private static String soundFile = "sounds/splat.wav";
	
	private Sound sound;

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
	public Projectile(float x, float y, Velocity v) {
		super();
		exploding = false;
		sound = new Sound(soundFile);

		Animation anim = new Animation();

		//anim.addFrame(new ImageIcon(projImage).getImage(), 500);
		anim.loadAnimationFromSheet(projImage, 17, 1, 100);
		
		setAnimation(anim);

		//setScale(0.5f);
		setPosition(x, y);
		//setRotation(45);
		System.out.println("Dx: " + v.getdx() + " Dy: " + v.getdy());
		setVelocityX((float) v.getdx());
		setVelocityY((float) v.getdy());
	}

	/**
	 * A dummy constructor
	 */
	public Projectile() {
		super();
	}

	public void destroy(long elapsed) {
		Animation anim = new Animation();
		anim.loadAnimationFromSheet(explodingImage, 7, 1, explosionTimePerFrame);
		sound.start();

		setAnimation(anim);
		explodingTime = elapsed;
		exploding = true;
		// Offset to center the explosion on the projectile
		shiftX(-40);
		shiftY(-40);
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
