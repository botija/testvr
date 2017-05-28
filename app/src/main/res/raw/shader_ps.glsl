precision highp float;
varying vec2 st;
varying vec4 color;
uniform sampler2D texture0;

void main() {
	vec4 c = texture2D(texture0, st);
	gl_FragColor = c + color;
}