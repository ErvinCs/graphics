#version 400

in vec3 texCoords;
out vec4 o_Color;

uniform samplerCube cubeMap;

void main(void){
    o_Color = texture(cubeMap, texCoords);
}