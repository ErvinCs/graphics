#version 400 core

in vec2 o_texCoords;

out vec4 o_color;

uniform sampler2D texSampler;

void main(void) {
    o_color = texture(texSampler, o_texCoords);
}