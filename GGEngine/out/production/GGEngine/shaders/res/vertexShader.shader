#version 400 core

in vec3 position;
in vec2 texCoords;
in vec3 normal;

out vec2 o_texCoords;
out vec3 surfaceNormal;
out vec3 vectorTowardsLight;
out vec3 vectorTowardsCamera;
out float visibility;

uniform mat4 transformMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform float useSimulatedLighting;

const float fogDensity = 0.005;
const float fogGradient = 5.0;

void main(void) {
    vec4 worldPosition = transformMatrix * vec4(position, 1.0);
    vec4 positionRelativeToCamera = viewMatrix * worldPosition;

    //MVP Matrix
    gl_Position = projectionMatrix * positionRelativeToCamera;
    o_texCoords = texCoords;

    vec3 actualNormal = normal;
    if (useSimulatedLighting > 0.5) {
        actualNormal = vec3(0.0, 1.0, 0.0);
    }

    surfaceNormal = (transformMatrix * vec4(actualNormal, 0.0)).xyz;
    vectorTowardsLight = lightPosition - worldPosition.xyz;
    vectorTowardsCamera = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    float distanceFromCamera = length(positionRelativeToCamera.xyz);
    visibility = exp(-pow((distanceFromCamera * fogDensity), fogGradient));
    visibility = clamp(visibility, 0.0, 1.0);
}