package rendering;

import shaders.ShaderProgram;
import shaders.UniformMatrix;
import shaders.UniformVec2;
import shaders.UniformVec3;
import utils.MyFile;

public class TerrainShader extends ShaderProgram{
	
	protected UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");
	protected UniformVec3 lightDirection = new UniformVec3("lightDirection");
	protected UniformVec3 lightColour = new UniformVec3("lightColour");
	protected UniformVec2 lightBias = new UniformVec2("lightBias");

	public TerrainShader(MyFile vertexFile, MyFile fragmentFile) {
		super(vertexFile, fragmentFile);
		super.storeAllUniformLocations(projectionViewMatrix, lightDirection, lightColour, lightBias);
	}
	
	public TerrainShader(MyFile vertexFile, MyFile geometryFile, MyFile fragmentFile) {
		super(vertexFile, geometryFile, fragmentFile);
		super.storeAllUniformLocations(projectionViewMatrix, lightDirection, lightColour, lightBias);
	}


}
