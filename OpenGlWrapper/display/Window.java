package display;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class Window {
	
	private static final int MIN_HEIGHT = 700;

	private final int fpsCap;

	private DisplayMode resolution;
	private boolean fullScreen;
	private float aspectRatio;

	private List<DisplayMode> availableResolutions = new ArrayList<DisplayMode>();

	protected Window(Context context, WindowBuilder settings) {
		this.fpsCap = settings.getFpsCap();
		try {
			getSuitableFullScreenModes();
			DisplayMode resolution = getStartResolution(settings);
			Display.setInitialBackground(1, 1, 1);
			this.aspectRatio = (float) resolution.getWidth() / resolution.getHeight();
			setResolution(resolution, settings.isFullScreen());
			if (settings.hasIcon()) {
				Display.setIcon(settings.getIcon());
			}
			Display.setVSyncEnabled(settings.isvSync());
			Display.setTitle(settings.getTitle());
			Display.create(new PixelFormat().withDepthBits(24).withSamples(4), context.getAttribs());
			GL11.glViewport(0, 0, resolution.getWidth(), resolution.getHeight());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public float getAspectRatio(){
		return aspectRatio;
	}
	
	public DisplayMode getResolution(){
		return resolution;
	}
	
	public boolean isFullScreen(){
		return fullScreen;
	}
	
	public List<DisplayMode> getAvailableResolutions(){
		return availableResolutions;
	}

	public void setResolution(DisplayMode resolution, boolean fullscreen) {
		try {
			Display.setDisplayMode(resolution);
			this.resolution = resolution;
			if (fullscreen && resolution.isFullscreenCapable()) {
				Display.setFullscreen(true);
				this.fullScreen = fullscreen;
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		Display.sync(fpsCap);
		Display.update();
	}

	public boolean isCloseRequested() {
		return Display.isCloseRequested();
	}

	public void destroy() {
		Display.destroy();
	}

	public static WindowBuilder newWindow(int width, int height, int fpsCap) {
		return new WindowBuilder(width, height, fpsCap);
	}

	private void getSuitableFullScreenModes() throws LWJGLException {
		DisplayMode[] resolutions = Display.getAvailableDisplayModes();
		DisplayMode desktopResolution = Display.getDesktopDisplayMode();
		for (DisplayMode resolution : resolutions) {
			if (isSuitableFullScreenResolution(resolution, desktopResolution)) {
				availableResolutions.add(resolution);
			}
		}
	}

	private boolean isSuitableFullScreenResolution(DisplayMode resolution, DisplayMode desktopResolution) {
		if (resolution.getBitsPerPixel() == desktopResolution.getBitsPerPixel()) {
			if (resolution.getFrequency() == desktopResolution.getFrequency()) {
				float desktopAspect = (float) desktopResolution.getWidth() / desktopResolution.getHeight();
				float resAspect = (float) resolution.getWidth() / resolution.getHeight();
				float check = resAspect / desktopAspect;
				if (check > 0.95f && check < 1.05f) {
					return resolution.getHeight() > MIN_HEIGHT;
				}
			}
		}
		return false;
	}

	private DisplayMode getFullScreenDisplayMode(int width, int height) {
		for (DisplayMode potentialMode : availableResolutions) {
			if (potentialMode.getWidth() == width && potentialMode.getHeight() == height) {
				return potentialMode;
			}
		}
		return null;
	}

	private DisplayMode getStartResolution(WindowBuilder settings) {
		if (settings.isFullScreen()) {
			DisplayMode fullScreenMode = getFullScreenDisplayMode(settings.getWidth(), settings.getHeight());
			if (fullScreenMode != null) {
				return fullScreenMode;
			}
			settings.fullScreen(false);
		}
		return new DisplayMode(settings.getWidth(), settings.getHeight());

	}

}
