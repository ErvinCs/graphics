#version 400 core

in vec3 position;
in vec2 texCoords;

out vec2 o_texCoords;

uniform mat4 transformMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;


void main(void) {
    gl_Position = projectionMatrix * viewMatrix * transformMatrix * vec4(position, 1.0);
    o_texCoords = texCoords;
}