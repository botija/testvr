attribute vec3 aVertexPosition;
attribute vec4 aVertexColor;
attribute vec2 aVertexTextureCoord;
uniform mat4 uMVMatrix;
uniform mat4 uPMatrix;
varying vec2 st;
varying vec4 color;

void main() {  
	st = aVertexTextureCoord;
	color = aVertexColor;
	gl_Position = uPMatrix * uMVMatrix * vec4(aVertexPosition, 1.0);
	//gl_Position = ftransform();
}