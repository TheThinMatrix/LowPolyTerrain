package display;

import java.nio.ByteBuffer;

import utils.MyFile;

public class WindowBuilder {
	
	private static final String DEFAULT_TITLE = "Equinox Engine";
	private static final String ICON16 = "icon16.png";
	private static final String ICON32 = "icon32.png";
	private static final String ICON128 = "icon128.png";
	
	private final int width;
	private final int height;
	private final int fpsCap;
	
	private String title = DEFAULT_TITLE;
	private ByteBuffer[] icon = null;
	private boolean vSync = false;
	private boolean antialiasing = false;
	private boolean fullScreen = false;

	protected WindowBuilder(int width, int height, int fpsCap){
		this.width = width;
		this.height = height;
		this.fpsCap = fpsCap;
	}
	
	public WindowBuilder withIcon(MyFile iconFolder){
		MyFile icon16File = new MyFile(iconFolder, ICON16);
		MyFile icon32File = new MyFile(iconFolder, ICON32);
		MyFile icon128File = new MyFile(iconFolder, ICON128);
		this.icon = IconLoader.load(icon16File, icon32File, icon128File);
		return this;
	}
	
	public WindowBuilder setTitle(String title){
		this.title = title;
		return this;
	}
	
	public WindowBuilder withVSync(boolean vSync){
		this.vSync = vSync;
		return this;
	}
	
	public WindowBuilder antialias(boolean antialias){
		this.antialiasing = antialias;
		return this;
	}
	
	public WindowBuilder fullScreen(boolean full){
		this.fullScreen = full;
		return this;
	}
	
	public Window create(){
		return new Window(new Context(3,3), this);
	}

	protected int getWidth() {
		return width;
	}
	
	protected int getHeight(){
		return height;
	}

	protected int getFpsCap() {
		return fpsCap;
	}

	protected String getTitle() {
		return title;
	}

	protected ByteBuffer[] getIcon() {
		return icon;
	}

	protected boolean hasIcon(){
		return icon != null;
	}

	protected boolean isvSync() {
		return vSync;
	}

	protected boolean isAntialiasing() {
		return antialiasing;
	}

	protected boolean isFullScreen() {
		return fullScreen;
	}
	
	
	
	
}
