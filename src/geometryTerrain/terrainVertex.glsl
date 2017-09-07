#version 330

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec4 in_colour;

out vec3 vertexColour;

void main(void){

	gl_Position = vec4(in_position, 1.0);
	vertexColour = in_colour.rgb;

}