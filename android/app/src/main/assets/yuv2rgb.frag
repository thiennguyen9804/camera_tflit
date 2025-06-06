precision mediump float;
varying mediump vec2 vtexcoord;
uniform lowp sampler2D samplerY;
uniform lowp sampler2D samplerU;
uniform lowp sampler2D samplerV;

void main() {
    mediump float y;
    mediump float u;
    mediump float v;
    lowp vec3 rgb;

    mat3 convmatrix = mat3(
        vec3(1.164,  1.164, 1.164),
        vec3(0.0,   -0.392, 2.017),
        vec3(1.596, -0.813, 0.0)
    );

    v = (texture(samplerV, vtexcoord)).r - (16.0 / 255.0);
    u = (texture2D(samplerU, vtexcoord).r - (128.0 / 255.0));
    v = (texture2D(samplerU, vtexcoord).r - (128.0 / 255.0));

    rgb = convmatrix * vec3(y, u, v);
    gl_FragColor = vec4(rgb, 1.0);
}
