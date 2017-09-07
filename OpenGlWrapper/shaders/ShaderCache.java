package shaders;

import java.util.HashMap;
import java.util.Map;

public class ShaderCache {
	
	private Map<Integer,Boolean> booleanUniforms = new HashMap<Integer,Boolean>();
	private Map<Integer,Float> floatUniforms = new HashMap<Integer,Float>();
	
	public boolean needsUpdating(Integer location, boolean value){
		Boolean currentValue = booleanUniforms.get(location);
		if(currentValue==null||currentValue!=value){
			booleanUniforms.put(location, value);
			return true;
		}else{
			return false;
		}
		
	}
	
	public boolean needsUpdating(int location, float value){
		Float currentValue = floatUniforms.get(location);
		if(currentValue==null||currentValue!=value){
			floatUniforms.put(location, value);
			return true;
		}else{
			return false;
		}
	}

}
