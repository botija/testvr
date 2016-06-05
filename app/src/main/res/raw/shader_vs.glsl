attribute vec3 aVertexPosition;
attribute vec4 aVertexColor;
attribute vec3 aVertexNormal;
attribute vec2 aVertexTextureCoord;
uniform mat4 uMVMatrix;
uniform mat4 uPMatrix;
uniform mat3 uNormalMatrix;
uniform mat3 vLightPosition;
varying vec3 normal;
varying vec3 lightdir;
varying vec2 st;
varying vec4 color;

void main() {  
	normal = uNormalMatrix * aVertexNormal;
   vec4 vPosition4 = uMVMatrix * aVertexPosition;
   vec3 vPosition3 = VPosition4.xyz / VPosition.w;
   lightdir = normalize(vLightPosition - vPosition3);
	st = aVertexTextureCoord;
	color = aVertexColor;
	gl_Position = uPMatrix * uMVMatrix * vec4(aVertexPosition, 1.0);
}