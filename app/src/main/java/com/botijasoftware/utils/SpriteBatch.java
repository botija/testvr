package com.botijasoftware.utils;

import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import com.botijasoftware.utils.renderer.Renderer;

public class SpriteBatch implements Renderable {

	protected VertexBuffer mVertexBuffer;
	protected IndexBuffer mIndexBuffer;
	protected Texture mTexture = null;
	protected boolean mVisible = true;
	private final static int MAXSPRITES = 64;
	private final static int PVERTEX = 0;
	private final static int PTEXTCOORD = 1;
	private final static int PCOLOR = 2;
	private int count = 0;
    private int maxsprites = 0;
    public int maxspritecount = 0;
    public int drawcalls = 0;
    public int usedsprites = 0;
    public Sprite tmpSprite;



    public SpriteBatch() {
        this(MAXSPRITES);
    }
	
	public SpriteBatch(int nsprites) {

        maxsprites = nsprites;
		VertexBufferDeclaration vbd = new VertexBufferDeclaration();
		vbd.add(new VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_VERTEX,3,VertexBufferDefinition.ACCESS_DYNAMIC));
		vbd.add(new VertexBufferDefinition(VertexBufferDefinition.FLOAT, VertexBufferDefinition.DEF_TEXTURE_COORD,2,VertexBufferDefinition.ACCESS_DYNAMIC));
		vbd.add(new VertexBufferDefinition(VertexBufferDefinition.BYTE, VertexBufferDefinition.DEF_COLOR,4,VertexBufferDefinition.ACCESS_DYNAMIC));
		
		mVertexBuffer = Renderer.vbManager.requestVB(vbd, 4 * nsprites);
		mIndexBuffer = Renderer.ibManager.requestIB(GLES20.GL_TRIANGLES, GLES20.GL_UNSIGNED_SHORT, 6 * nsprites);


        for (int v = 0, i = 0; v < nsprites*4;v+=4,i+=6) {
            mIndexBuffer.put(i,v);
            mIndexBuffer.put(i+1,v+1);
            mIndexBuffer.put(i+2,v+2);
            mIndexBuffer.put(i+3,v+1);
            mIndexBuffer.put(i+4,v+2);
            mIndexBuffer.put(i+5,v+3);
        }

	}
	
	public void DrawSprite(GL10 gl, Sprite sprite) {
		Texture t = sprite.mTexture;

        if (mTexture == null) {
            mTexture = t;
        }
		
		//same texture --> to vb
		if (mTexture.mID == t.mID) {
			putSpriteOnVB(sprite);
			if (count >= maxsprites) {
				drawVB(gl);
				count = 0;
			}
		}
		else { //diferent texture --> draw vb, and push new sprite to empty vb
			drawVB(gl);
			count = 0;
            mTexture = t;
			putSpriteOnVB(sprite);
		}
	}

    public void DrawSprite(GL10 gl, Sprite sprite, ColorRGBAb color) {
        Texture t = sprite.mTexture;

        if (mTexture == null) {
            mTexture = t;
        }

        //same texture --> to vb
        if (mTexture.mID == t.mID) {
            putSpriteOnVB(sprite, color);
            if (count >= maxsprites) {
                drawVB(gl);
                count = 0;
            }
        }
        else { //diferent texture --> draw vb, and push new sprite to empty vb
            drawVB(gl);
            count = 0;
            mTexture = t;
            putSpriteOnVB(sprite, color);
        }
    }

    private void putSpriteOnVB(Sprite sprite, ColorRGBAb color) {
        putSpriteOnVB(sprite, sprite.mScale, sprite.mRotation, color, sprite.mFlip);
    }

    private void putSpriteOnVB(Sprite sprite) {
        putSpriteOnVB(sprite, sprite.mScale, sprite.mRotation, sprite.mColor, sprite.mFlip);
    }
    
	private void putSpriteOnVB(Sprite sprite, Vector2 scale, float rotation, ColorRGBAb color, boolean flip) {

        usedsprites++;

		Rectanglef rect = sprite.mRectangle;

		float ohw = (rect.width/2.0f);
		float ohh = (rect.height/2.0f);
		float x = rect.X + ohw;
		float y = rect.Y + ohh;

        float hw = ohw * scale.X;
        float hh = ohh * scale.Y;
		
        // |cos -sin| * [x y]  --> xr = x * cos - y * sin
        // |sin  cos|              yr = x * sin + y * cos


        float sin = (float)Math.sin(rotation);
        float cos = (float)Math.cos(rotation);

        float x0 = - hw;
        float x1 = + hw;
        float y0 = - hh;
        float y1 = + hh;

		float data [] = {x + x0 * cos - y0 * sin, y + x0 * sin + y0 * cos, 0.0f,
                x + x1 * cos - y0 * sin, y + x1 * sin + y0 * cos, 0.0f,
                x + x0 * cos - y1 * sin, y + x0 * sin + y1 * cos, 0.0f,
                x + x1 * cos - y1 * sin, y + x1 * sin + y1 * cos, 0.0f};

        //float data [] = {x0, y0, 0.0f , x1, y0, 0.0f, x0, y1, 0.0f, x1, y1, 0.0f};

		
		ArrayBuffer vb = mVertexBuffer.getBuffer(PVERTEX);
		vb.position(count * 12);
		vb.put(data);
		vb.position(0);
		
		TextureCoords coords = sprite.mTexture.getTextureCoords();
		
		if (flip) {
			coords.flipVertical();
		}
		
		float st [] = { coords.s0, coords.t0, coords.s1, coords.t0, coords.s0, coords.t1, coords.s1, coords.t1 };
		vb = mVertexBuffer.getBuffer(PTEXTCOORD);
		vb.position(count * 8);
		vb.put(st);
		vb.position(0);
		
		byte r = color.R;
		byte g = color.G;
		byte b = color.B;
		byte a = color.A;
		
		byte cdata [] = { r,g,b,a,r,g,b,a,r,g,b,a,r,g,b,a };
		
		//mVertexBuffer.getBuffer(2).put(cdata);
		vb = mVertexBuffer.getBuffer(PCOLOR);
		vb.position(count * 16);
		vb.put(cdata);
		vb.position(0);

        count++;
	}
	
	private void drawVB(GL10 gl) {
		//GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.getID() );
        //Renderer.BindTexture(Renderer.TEXTURE0, mTexture.getID());
        Renderer.BindTexture(Renderer.TEXTURE0, mTexture.getID());
		
		Renderer.pushModelViewMatrix();
		Renderer.modelview.loadIdentity();
		
		Renderer.loadModelViewMatrix();
		mIndexBuffer.setSize(count*6); // two triangles per sprite
		mVertexBuffer.Draw(gl, mIndexBuffer);

		Renderer.popModelViewMatrix();
		//Renderer.modelview.loadMatrix();

        drawcalls++;
        if (count > maxspritecount)
            maxspritecount = count;
		
	}

    @Override
    public void Update(float time) {

    }

    @Override
    public void LoadContent(GL10 gl, ResourceManager resources) {

    }

    public void Draw(GL10 gl) {

        if (count > 0) {
            drawVB(gl);

        }
		
	}
	
	public void end(GL10 gl) {

        if (count > 0) {
            drawVB(gl);
            count = 0;
            mTexture = null;
        }
		//disable sprite shader
		//clean vb
	}
	
	public void begin(GL10 gl) {
        count = 0;
        mTexture = null;

        maxspritecount = 0;
        drawcalls = 0;
        usedsprites = 0;
		//enable sprite shader
		//prepare vb
	}


    public void flush(GL10 gl) {
        if (count > 0) {
            drawVB(gl);
            count = 0;
            mTexture = null;
        }
    }

    public void freeContent(GL10 gl, ResourceManager resources) {
        Renderer.vbManager.freeVB(mVertexBuffer);
        Renderer.ibManager.freeIB(mIndexBuffer);
    }

    public void move(float x, float y) {}

    public void scale(float x, float y) {}


    public void DrawText(GL10 gl, Font font, String text, float x, float y) {
        DrawText(gl, font, text, x, y, null);
    }

    public void DrawText(GL10 gl, Font font, String text, float x, float y, ColorRGBAb color) {
        float fontsize = (float) font.getFontSize();
        float fontsizesep = fontsize * 3/7;

        if (tmpSprite != null) {
            tmpSprite.setRectangle(new Rectanglef(0,0,fontsize,fontsize));
            tmpSprite.setTexture(font.getTexture());
        }
        else {

            tmpSprite = new Sprite(new Rectanglef(0,0,fontsize,fontsize), font.getTexture());
        }

        int textLen = text.length();

        if (color != null)
            tmpSprite.setColor(color);
        else
            tmpSprite.setColor(ColorRGBAb.WHITE);

        int ascii = 0;

        for (int i = 0; i< textLen; i++) {

            ascii = text.codePointAt(i);
            tmpSprite.setTexture(font.getTexture(ascii));
            tmpSprite.getRectangle().setPosition(x+ i*fontsizesep , y);
            DrawSprite(gl, tmpSprite);

        }
    }


    public Vector2 getTextSize(Font font, String text) {
        float fontsize = (float) font.getFontSize();
        float fontsizesep = fontsize * 3/7;
        return new Vector2(fontsizesep*text.length(), fontsize);
    }

}