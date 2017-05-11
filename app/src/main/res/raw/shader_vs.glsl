attribute vec3 aVertexPosition;
attribute vec4 aVertexColor;
attribute vec3 aVertexNormal;
attribute vec2 aVertexTextureCoord;
uniform mat4 uMVPMatrix;
varying vec2 st;
varying vec4 color;

void main() {  
	st = aVertexTextureCoord;
	color = aVertexColor;
	gl_Position = uMVPMatrix * vec4(aVertexPosition, 1.0);
	//gl_Position = ftransform();
}