#version 400 core

in vec3 position;
in vec2 texCoords;
in vec3 normal;

out vec2 o_texCoords;
out vec3 surfaceNormal;
out vec3 vectorTowardsLight[5];
out vec3 vectorTowardsCamera;
out float visibility;
out vec4 shadowCoords;

uniform mat4 transformMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[5];
uniform mat4 toShadowMapSpace;

const float fogDensity = 0.005;
const float fogGradient = 5.0;
const float shadowDistance = 400.0;     // Should be equal to SHADOW_DISTANCE in ShadowBox
const float transitionDistance = 10.0;  // Transition period

void main(void) {
    vec4 worldPosition = transformMatrix * vec4(position, 1.0);
    shadowCoords = toShadowMapSpace * worldPosition;

    vec4 positionRelativeToCamera = viewMatrix * worldPosition;

    gl_Position = projectionMatrix * positionRelativeToCamera;
    o_texCoords = texCoords;

    surfaceNormal = (transformMatrix * vec4(normal, 0.0)).xyz;
    for(int i=0; i<5; i++) {
         vectorTowardsLight[i] = lightPosition[i] - worldPosition.xyz;
    }
    vectorTowardsCamera = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    float distanceFromCamera = length(positionRelativeToCamera.xyz);
    visibility = 1;
    //visibility = exp(-pow((distanceFromCamera * fogDensity), fogGradient));
    //visibility = clamp(visibility, 0.0, 1.0);

    // How far into the transition period the vertex is
    distanceFromCamera = distanceFromCamera - (shadowDistance - transitionDistance);
    // Normalise, Clamp & Flip (Everything before is 1, everything after is 0)
    distanceFromCamera = distanceFromCamera / transitionDistance;
    shadowCoords.w = clamp(1.0 - distanceFromCamera, 0.0, 1.0);

}