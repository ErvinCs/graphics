#version 400 core

in vec2 o_texCoords;
in vec3 surfaceNormal;
in vec3 vectorTowardsLight[5];
in vec3 vectorTowardsCamera;
in float visibility;
in vec4 shadowCoords;

out vec4 o_color;

uniform sampler2D backgroundTex;
uniform sampler2D rTex;
uniform sampler2D gTex;
uniform sampler2D bTex;
uniform sampler2D blendMap;
uniform sampler2D shadowMap;

uniform vec3 lightColor[5];
uniform vec3 attenuation[5];
uniform float shineDamp;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void) {
    float objectNearestToLight = texture(shadowMap, shadowCoords.xy).r;     // r - stores the depth information
    float lightFactor = 1.0;
    if (shadowCoords.z > objectNearestToLight) {        // If the Z of the terrain is behind an object (greater than) then shadow it
        lightFactor = 1.0 - (shadowCoords.w * 0.4);     // Fade out shadows at a distance
    }

    vec4 blendMapColor = texture(blendMap, o_texCoords);
    float backgroundTexAmount = 1 - (blendMapColor.r + blendMapColor.r + blendMapColor.r);
    vec2 tiledCoords = o_texCoords * 40.0;

    vec4 backgroundTexColor = texture(backgroundTex, tiledCoords) * backgroundTexAmount;
    vec4 rTexColor = texture(rTex, tiledCoords) * blendMapColor.r;
    vec4 gTexColor = texture(gTex, tiledCoords) * blendMapColor.g;
    vec4 bTexColor = texture(bTex, tiledCoords) * blendMapColor.b;
    vec4 totalColor = backgroundTexColor + rTexColor + gTexColor + bTexColor;

    vec3 unitSurface      = normalize(surfaceNormal);
    vec3 unitTowardsCamera= normalize(vectorTowardsCamera);

    vec3 totalDiffuse  = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for(int i=0; i<5; i++) {
        vec3 unitTowardsLight = normalize(vectorTowardsLight[i]);
        float distFragToLight = length(vectorTowardsLight[i]);
        float attenFactor = attenuation[i].x + (attenuation[i].y * distFragToLight + (attenuation[i].z * distFragToLight * distFragToLight));
        float dotProduct = dot(unitSurface, unitTowardsLight);
        float brightness = max(dotProduct, 0.2);
        vec3 unitLightDirection      = -unitTowardsLight;
        vec3 reflectedLightDirection = reflect(unitLightDirection, unitSurface);
        float specularFactor = dot(reflectedLightDirection, unitTowardsCamera);
        specularFactor = max(specularFactor, 0.0);
        float dampFactor = pow(specularFactor, shineDamp);
        totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attenFactor;
        totalSpecular = totalSpecular + (dampFactor * reflectivity * lightColor[i]) / attenFactor;
    }
    totalDiffuse = max(totalDiffuse, 0.2) * lightFactor;

    o_color = vec4(totalDiffuse, 1.0) * totalColor + vec4(totalSpecular, 1.0);
    //Mix object color with skyColor depending on the visibility
    o_color = mix(vec4(skyColor, 1.0), o_color, visibility);
}