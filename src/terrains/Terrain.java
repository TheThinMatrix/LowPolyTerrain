package terrains;

import openglObjects.Vao;
import rendering.ICamera;
import rendering.Light;
import rendering.TerrainRenderer;

public class Terrain {
	
	private final Vao vao;
	private final int vertexCount;
	private final TerrainRenderer renderer;
	
	public Terrain(Vao vao, int vertexCount, TerrainRenderer renderer){
		this.vao = vao;
		this.vertexCount = vertexCount;
		this.renderer = renderer;
	}
	
	public int getVertexCount(){
		return vertexCount;
	}
	
	public Vao getVao(){
		return vao;
	}
	
	public void render(ICamera camera, Light light){
		renderer.render(this, camera, light);
	}
	
	public void delete(){
		vao.delete(true);
	}

}
