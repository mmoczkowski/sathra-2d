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
package eu.sathra.scene;

import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;

/**
 * Class representing transformation info (position, scale and rotation). This
 * class is muttable!
 * 
 * @author Milosz Moczkowski
 * 
 */
public class Transform {

	private float mX;
	private float mY;
	private float mScaleX;
	private float mScaleY;
	private float mRotation;

	public Transform() {
		this(0, 0, 0, 1, 1);
	}

	public Transform(float x, float y) {
		this(x, y, 0, 1, 1);
	}

	public Transform(float x, float y, float angle) {
		this(x, y, angle, 1, 1);
	}
	
	public Transform(Transform other) {
		set(other);
	}

	@Deserialize({ "x", "y", "angle", "scale_x", "scale_y" })
	@Defaults({ "0", "0", "0", "1", "1" })
	public Transform(float x, float y, float rotation, float scaleX,
			float scaleY) {
		set(x, y, rotation, scaleX, scaleY);
	}

	public float getX() {
		return mX;
	}

	public void setX(float x) {
		mX = x;
	}

	public float getY() {
		return mY;
	}

	public void setY(float y) {
		mY = y;
	}

	public void setPosition(float x, float y) {
		mX = x;
		mY = y;
	}

	public float getRotation() {
		return mRotation;
	}

	public float getScaleX() {
		return mScaleX;
	}

	public void setScaleX(float scale) {
		mScaleX = scale;
	}

	public float getScaleY() {
		return mScaleY;
	}

	public void setScaleY(float scale) {
		mScaleY = scale;
	}

	public void setScale(float x, float y) {
		mScaleX = Math.min(x, 0);
		mScaleY = Math.min(y, 0);
	}

	public void setRotation(float rotation) {
		mRotation = rotation;
	}

	public void clear() {
		set(0, 0, 0, 1, 1);
	}

	public Transform set(Transform other) {
		set(other.getX(), other.getY(), other.getRotation(), other.getScaleX(),
				other.getScaleY());
		
		return this;
	}

	public void set(float x, float y, float rotation, float scaleX, float scaleY) {
		setX(x);
		setY(y);
		setScaleX(scaleX);
		setScaleY(scaleY);
		setRotation(rotation);
	}

	public Transform add(Transform other) {
		set(getX() + other.getX(), 
			getY() + other.getY(),
			getRotation() + other.getRotation(),
			getScaleX() * other.getScaleX(),
			getScaleY() * other.getScaleY());

		return this;
	}
	
	public Transform substract(Transform other) {
		set(getX() - other.getX(), 
				getY() - other.getY(),
				getRotation() - other.getRotation(),
				getScaleX() / other.getScaleX(),
				getScaleY() / other.getScaleY());

			return this;
	}

	public Transform multiply(float scalar) {
		set(getX() * scalar, 
			getY() * scalar, 
			getRotation() * scalar, 
			getScaleX() * scalar, 
			getScaleY() * scalar);
		
		return this;
	}

	@Override
	public String toString() {
		return String.format("[Position: {%f, %f}, Rotation: %f, Scale: %f]",
				getX(), getY(), getRotation(), getScaleX(), getScaleY());
	}
}
