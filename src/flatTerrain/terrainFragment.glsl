#version 330

flat in vec3 pass_colour;

out vec4 out_colour;


void main(void){

	out_colour = vec4(pass_colour, 1.0);

}