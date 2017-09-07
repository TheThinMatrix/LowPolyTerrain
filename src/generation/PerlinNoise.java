package generation;

import java.util.Random;

/**
 * Generates the heights of the terrain. Check out tutorial 37 for more
 * info: https://youtu.be/qChQrNWU9Xw
 * 
 * @author Karl
 *
 */
public class PerlinNoise {

	private final int seed;
	private final float roughness;
	private final int octaves;
	private final float amplitude;

	public PerlinNoise(int seed, int octaves, float amplitude, float roughness) {
		this.seed = seed;
		this.octaves = octaves;
		this.amplitude = amplitude;
		this.roughness = roughness;
	}

	public PerlinNoise(int octaves, float amplitude, float roughness) {
		this.seed = new Random().nextInt(1000000000);
		this.octaves = octaves;
		this.amplitude = amplitude;
		this.roughness = roughness;
	}

	public int getSeed() {
		return seed;
	}

	public float getAmplitude() {
		return amplitude;
	}

	public float getPerlinNoise(int x, int y) {
		float total = 0;
		float d = (float) Math.pow(2, octaves - 1);
		for (int i = 0; i < octaves; i++) {
			float freq = (float) (Math.pow(2, i) / d);
			float amp = (float) Math.pow(roughness, i) * amplitude;
			total += getInterpolatedNoise(x * freq, y * freq) * amp;
		}
		return total;
	}

	private float getSmoothNoise(int x, int y) {
		float corners = (getNoise(x - 1, y - 1) + getNoise(x + 1, y - 1) + getNoise(x - 1, y + 1)
				+ getNoise(x + 1, y + 1)) / 16f;
		float sides = (getNoise(x - 1, y) + getNoise(x + 1, y) + getNoise(x, y - 1) + getNoise(x, y + 1)) / 8f;
		float center = getNoise(x, y) / 4f;
		return corners + sides + center;
	}

	private float getNoise(int x, int y) {
		return new Random(x * 49632 + y * 325176 + seed).nextFloat() * 2f - 1f;
	}

	private float getInterpolatedNoise(float x, float y) {
		int intX = (int) x;
		float fracX = x - intX;
		int intY = (int) y;
		float fracY = y - intY;

		float v1 = getSmoothNoise(intX, intY);
		float v2 = getSmoothNoise(intX + 1, intY);
		float v3 = getSmoothNoise(intX, intY + 1);
		float v4 = getSmoothNoise(intX + 1, intY + 1);
		float i1 = interpolate(v1, v2, fracX);
		float i2 = interpolate(v3, v4, fracX);
		return interpolate(i1, i2, fracY);
	}

	private float interpolate(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float) ((1f - Math.cos(theta)) * 0.5f);
		return a * (1 - f) + b * f;
	}

}
