#version 400 core

in vec3 position;
in vec2 texCoords;
in vec3 normal;

out vec2 o_texCoords;
out vec3 surfaceNormal;
out vec3 vectorTowardsLight;
out vec3 vectorTowardsCamera;

uniform mat4 transformMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;


void main(void) {
    vec4 worldPosition = transformMatrix * vec4(position, 1.0);

    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    o_texCoords = texCoords * 40.0;

    surfaceNormal = (transformMatrix * vec4(normal, 0.0)).xyz;
    vectorTowardsLight = lightPosition - worldPosition.xyz;
    vectorTowardsCamera = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
}