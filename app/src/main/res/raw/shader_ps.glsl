precision highp float;
varying vec2 st;
varying vec4 color;
uniform sampler2D texture0;

void main() {
	vec4 c = texture2D(texture0, st);
	gl_FragColor = c * color;
	//gl_FragColor = vec4(st.x, st.y , 0.0f, 1.0f);
}