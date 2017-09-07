package main;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import utils.Colour;

/**
 * A load of configs for the application.
 * 
 * @author Karl
 *
 */
public class Configs {

	public static final int FPS_CAP = 100;

	public static final float COLOUR_SPREAD = 0.45f;
	public static final Colour[] TERRAIN_COLS = new Colour[] { new Colour(201, 178, 99, true),
			new Colour(135, 184, 82, true), new Colour(80, 171, 93, true), new Colour(120, 120, 120, true),
			new Colour(200, 200, 210, true) };

	public static Vector3f LIGHT_POS = new Vector3f(0.3f, -1f, 0.5f);
	public static Colour LIGHT_COL = new Colour(1f, 0.8f, 0.8f);
	public static Vector2f LIGHT_BIAS = new Vector2f(0.3f, 0.8f);

	public static final int TERRAIN_SIZE = 100;

	public static final float AMPLITUDE = 10;
	public static final float ROUGHNESS = 0.35f;
	public static final int OCTAVES = 3;

}
