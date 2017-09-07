package display;

import org.lwjgl.opengl.ContextAttribs;

public class Context {

	private int[] openGlVersion;
	
	public Context(int version, int subVersion){
		this.openGlVersion = new int[]{version, subVersion};
	}

	public int[] getOpenGlVersion() {
		return openGlVersion;
	}

	public ContextAttribs getAttribs() {
		return new ContextAttribs(openGlVersion[0], openGlVersion[1]).withForwardCompatible(true).withProfileCore(true);
	}

}
