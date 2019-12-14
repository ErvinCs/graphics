#version 400 core

in vec2 o_texCoords;
in vec3 surfaceNormal;
in vec3 vectorTowardsLight;
in vec3 vectorTowardsCamera;
in float visibility;

out vec4 o_color;

uniform sampler2D texSampler;
uniform vec3 lightColor;
uniform float shineDamp;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void) {
    vec3 unitSurface      = normalize(surfaceNormal);
    vec3 unitTowardsLight = normalize(vectorTowardsLight);

    float dotProduct = dot(unitSurface, unitTowardsLight);
    float brightness = max(dotProduct, 0.2);
    vec3 diffuse = brightness * lightColor;

    vec3 unitTowardsCamera       = normalize(vectorTowardsCamera);
    vec3 unitLightDirection      = -unitTowardsLight;
    vec3 reflectedLightDirection = reflect(unitLightDirection, unitSurface);

    float specularFactor = dot(reflectedLightDirection, unitTowardsCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampFactor = pow(specularFactor, shineDamp);

    vec3 finalSpecular = dampFactor * reflectivity * lightColor;

    o_color = vec4(diffuse, 1.0) * texture(texSampler, o_texCoords) + vec4(finalSpecular, 1.0);
    //Mix object color with skyColor depending on the visibility
    o_color = mix(vec4(skyColor, 1.0), o_color, visibility);
}