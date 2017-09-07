#version 330

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in vec3 vertexColour[];

out vec3 finalColour;

uniform vec3 lightDirection;
uniform vec3 lightColour;
uniform vec2 lightBias;

uniform mat4 projectionViewMatrix;

vec3 calculateLighting(vec3 normal){
	float brightness = max(dot(-lightDirection, normal), 0.0);
	return (lightColour * lightBias.x) + (brightness * lightColour * lightBias.y);
}

vec3 calcTriangleNormal(){
	vec3 tangent1 = gl_in[1].gl_Position.xyz - gl_in[0].gl_Position.xyz;
	vec3 tangent2 = gl_in[2].gl_Position.xyz - gl_in[0].gl_Position.xyz;
	vec3 normal = cross(tangent1, tangent2);
	return normalize(normal);
}

void main(void){

	vec3 normal = calcTriangleNormal();
	vec3 lighting = calculateLighting(normal);
	
	for(int i=0;i<3;i++){
		gl_Position = projectionViewMatrix * gl_in[i].gl_Position;
		//vertexColour[i] colour be used here, but then the colours would still be interpolated.
		finalColour = vertexColour[0] * lighting;
		EmitVertex();
	}
	
	EndPrimitive();

}