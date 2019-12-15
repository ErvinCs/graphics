#version 400 core

in vec2 o_texCoords;
in vec3 surfaceNormal;
in vec3 vectorTowardsLight[5];
in vec3 vectorTowardsCamera;
in float visibility;

out vec4 o_color;

uniform sampler2D texSampler;
uniform vec3 lightColor[5];
uniform vec3 attenuation[5];
uniform float shineDamp;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void) {
    vec3 unitSurface       = normalize(surfaceNormal);
    vec3 unitTowardsCamera = normalize(vectorTowardsCamera);

    vec3 totalDiffuse  = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for(int i=0; i<5; i++) {
        float distFragToLight = length(vectorTowardsLight[i]);
        float attenFactor = attenuation[i].x + (attenuation[i].y * distFragToLight + (attenuation[i].z * distFragToLight * distFragToLight));
        vec3 unitTowardsLight = normalize(vectorTowardsLight[i]);
        float dotProduct = dot(unitSurface, unitTowardsLight);
        float brightness = max(dotProduct, 0.0);
        vec3 unitLightDirection = -unitTowardsLight;
        vec3 reflectedLightDirection = reflect(unitLightDirection, unitSurface);
        float specularFactor = dot(reflectedLightDirection, unitTowardsCamera);
        specularFactor = max(specularFactor, 0.0);
        float dampFactor = pow(specularFactor, shineDamp);
        totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attenFactor;
        totalSpecular = totalSpecular + (dampFactor * reflectivity * lightColor[i]) / attenFactor;
    }
    totalDiffuse = max(totalDiffuse, 0.2);

    vec4 textureColor = texture(texSampler, o_texCoords);
    if (textureColor.a < 0.5) {
        discard;
    }

    o_color = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
    //Mix object color with skyColor depending on the visibility
    o_color = mix(vec4(skyColor, 1.0), o_color, visibility);
}