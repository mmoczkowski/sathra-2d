/*******************************************************************************
 * Copyright 2014 SATHRA Milosz Moczkowski, milosz.moczkowski@sathra.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package eu.sathra.resources;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import eu.sathra.SathraActivity;

public class TextureLoader implements ResourceLoader<Texture> {

	@Override
	public Texture load(String path) throws IOException {
		final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; 
		
        InputStream input = SathraActivity
				.getCurrentSathra().getAssets().open(path);
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		input.close();
		
		// Load with opengl
		final int[] textureHandle = new int[1];

		GLES20.glGenTextures(1, textureHandle, 0);

		if (textureHandle[0] != 0) {
			// Bind to the texture in OpenGL
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

			// GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

			// Set filtering
			// GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
			// GLES20.GL_TEXTURE_MIN_FILTER,
			// GLES20.GL_LINEAR_MIPMAP_LINEAR);
			// GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
			// GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

			//GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

			// Recycle the bitmap, since its data has been loaded into OpenGL.
			bitmap.recycle();

			return new Texture(textureHandle[0], bitmap.getWidth(),
					bitmap.getHeight());
		} else {
			throw new IOException("Error loading texture: " + path);
		}
	}

	@Override
	public void unload(Texture resource) {
		GLES20.glDeleteTextures(1, new int[] { resource.getHandle() }, 0);
	}

}
