package game2D;

import java.io.*;

/**
 * Plays a sound quieter depending on the given distance, which is the distance
 * between where the sound is happenning to the player
 * 
 * @author 2716761
 *
 */
public class DistanceFilterStream extends FilterInputStream {

	private double distance;

	DistanceFilterStream(InputStream in, double dist) {
		super(in);
		distance = dist;
	}

	// Get a value from the array 'buffer' at the given 'position'
	// and convert it into short big-endian format
	public short getSample(byte[] buffer, int position) {
		return (short) (((buffer[position + 1] & 0xff) << 8) | (buffer[position] & 0xff));
	}

	// Set a short value 'sample' in the array 'buffer' at the
	// given 'position' in little-endian format
	public void setSample(byte[] buffer, int position, short sample) {
		buffer[position] = (byte) (sample & 0xFF);
		buffer[position + 1] = (byte) ((sample >> 8) & 0xFF);
	}

	public int read(byte[] sample, int offset, int length) throws IOException {
		// Get the number of bytes in the data stream
		int bytesRead = super.read(sample, offset, length);
		// TODO tune how distance affects sound here, the smaller the distance the louder the sound.
		distance /= 5;
		// Make sure that no sound is less than one so that the later divsion doesn't increase the volume
		if(distance < 1) {
			distance = 1;
		}
		// Modify the volume by the distance
		float volume = (float) (1.0f / distance);
		short amp = 0;

		// Loop through the sample 2 bytes at a time
		for (int p = 0; p < bytesRead; p = p + 2) {
			// Read the current amplitutude (volume)
			amp = getSample(sample, p);
			// Reduce it by the relevant volume factor
			amp = (short) ((float) amp * volume);
			// Set the new amplitude value
			setSample(sample, p, amp);
		}
		return length;

	}
}
