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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import eu.sathra.ai.Task;
import eu.sathra.ai.context.AIContext;
import eu.sathra.ai.context.JexlContext;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.physics.Body;
import eu.sathra.scene.animation.Animation;

/***
 * Base class for all scene nodes. Scene node is a part of hierarchical scene
 * graph. Each node can have arbitrary number of children (also scene nodes).
 * The transformation applied to a node is relative to it's parent. If a parent
 * is not visible, the children also won't be drawn.
 * 
 * @author Milosz Moczkowski
 * 
 */
// TODO: findChildrenById, iterable
public class SceneNode implements Iterable<SceneNode> {

	public static final int COLLIDE_WITH_ALL_MASK = Integer.MAX_VALUE;
	public static final int COLLIDE_WITH_NONE_MASK = 0;

	private String mId;
	private boolean mIsVisible = true;
	private boolean mIsAIEnabled = true;
	private SceneNode mParent;
	private Body mBody;
	private Task mAITask;
	private AIContext mAIContext;
	private Set<SceneNode> mChildren = new LinkedHashSet<SceneNode>();
	private Set<SceneNode> mChildrenToRemove = new LinkedHashSet<SceneNode>(); 
	private SceneNode[] mChildrenCopy = new SceneNode[0];
	private Animation mCurrentAnimation;
	private Transform mTransform = new Transform();
	private Transform mAbsoluteTransform = new Transform();
	private Object mUserData = null;
	private int mCollisionMask = COLLIDE_WITH_ALL_MASK;

	public SceneNode() {
		this(null, new Transform(), true, null, null, null, null);
	}

	public SceneNode(Transform transform) {
		this(null, transform, true, null, null, null, null);
	}

	@Deserialize({ "id", "transform", "is_visible", "animation", "children",
			"body", "ai" })
	@Defaults({ Deserialize.NULL, Deserialize.NULL, "true", Deserialize.NULL,
			Deserialize.NULL, Deserialize.NULL, Deserialize.NULL })
	public SceneNode(String id, Transform t, boolean isVisible,
			Animation animation, SceneNode[] children, Body body, Task ai) {

		setId(id);
		setBody(body);
		setVisible(isVisible);
		setAIContext(new JexlContext());// new BeanShellContext());//new
										// RhinoContext(this));

		if (animation != null)
			startAnimation(animation);

		if (children != null)
			addChildren(children);

		if (t != null)
			setTransform(t);

		setAI(ai); // AI needs to be added last!
	}

	/**
	 * Returns this node string id. This can be used to identify nodes and
	 * obtaining references to deserialized nodes. See: {@see
	 * eu.sathra.scene.SceneNode#findChildById(String)}
	 * 
	 * @return
	 */
	public String getId() {
		return mId;
	}

	public void setId(String id) {
		mId = id;
	}

	/**
	 * Gets bitmask used for calculating path and collisions.
	 * 
	 * @return Collision mask.
	 */
	public int getCollisionMask() {
		return mCollisionMask;
	}

	/**
	 * Sets bitmask used for calculating path and collisions.
	 * 
	 * @param mask
	 */
	public void setCollisionMask(int mask) {
		this.mCollisionMask = mask;
		// TODO: body.setmask
	}

	/**
	 * Get this node's parent
	 * 
	 * @return Node's parent or null if it has no parent.
	 */
	public SceneNode getParent() {
		return mParent;
	}

	/**
	 * Set a parent of this node. Equivalent of calling
	 * {@code parent.addChild(node); }
	 * 
	 * @param parent
	 */
	public void setParent(SceneNode parent) {
		parent.addChild(this);
	}

	/**
	 * Add node to this child.
	 * 
	 * @param child
	 */
	public void addChild(SceneNode child) {
		if (child.mParent != null) {
			child.mParent.removeChild(child);
		}

		mChildren.add(child);
		child.mParent = this;

		updateChildrenCopy();
	}

	public void addChildren(SceneNode[] children) {
		for (SceneNode child : children) {
			addChild(child);
		}

		updateChildrenCopy();
	}

	public void removeChild(SceneNode child) {
		mChildrenToRemove.add(child);
	}

	/**
	 * Return a copy of an array containing this node's children.
	 * 
	 * @return
	 */
	public SceneNode[] getChildren() {
		return mChildrenCopy;
	}

	/**
	 * Searches recursively for a node with a given id.
	 * 
	 * @param id
	 * @return
	 */
	public SceneNode findChildById(String id) {
		for (SceneNode child : mChildren) {
			if (child.getId() == null && id == null)
				return child;
			else if (child.getId() != null && child.getId().equals(id)) {
				return child;
			}

			SceneNode subChild = child.findChildById(id);

			if (subChild != null) {
				return subChild;
			}
		}

		return null;
	}

	@SuppressLint("WrongCall")
	public void onDraw(GL10 gl, long time, long delta) {
		if (isVisible()) {

			// Update absolute transform
			if (getParent() != null)
				mAbsoluteTransform.set(getParent().getAbsoluteTransform());
			else
				mAbsoluteTransform.clear();

			mAbsoluteTransform.add(getTransform());

			// Apply animation
			if (mCurrentAnimation != null) {
				mCurrentAnimation.animate(delta, time, mTransform);
			}

			gl.glPushMatrix();
			// gl.glTranslatef(getX(), getY(), 0);
			gl.glScalef(getScaleX(), getScaleY(), 0);
			gl.glTranslatef(getX() / getAbsoluteScaleX(), getY()
					/ getAbsoluteScaleY(), 0);

			// TODO: rotation
			// gl.glRotatef(0, 0, 0, 0);

			gl.glRotatef(this.getTransform().getRotation(), 0, 0, 1);

			// Draw yourself
			draw(gl, time, delta);

			// Draw children
			SceneNode[] childrenCopy = mChildrenCopy;

			for (SceneNode child : childrenCopy) {
				child.onDraw(gl, time, delta);
			}

			gl.glPopMatrix();

			// Update AI
			if (mAITask != null && isAIEnabled())
				mAITask.execute(mAIContext, time, delta);
		}
		
		mChildren.removeAll(mChildrenToRemove);

		if(!mChildrenToRemove.isEmpty()) {
			updateChildrenCopy();
		}
		
		mChildrenToRemove.clear();
	}

	public void setMatrix(Matrix matrix) {
		float array[] = new float[9];

		matrix.getValues(array);

		setPosition(array[2], array[5]);
		setScale(array[0], array[4]);
	}

	public void setTransform(Transform t) {
		mTransform.set(t);

		if (mBody != null)
			mBody.setTransform(t);
	}

	public Transform getTransform() {
		return mBody == null ? mTransform : mBody.getTransform();
	}

	public Transform getAbsoluteTransform() {
		return mAbsoluteTransform;
	}

	public void setPosition(float x, float y) {
		mTransform.setX(x);
		mTransform.setY(y);

		if (mBody != null)
			mBody.setPosition(x, y);
	}

	public float getX() {
		return getTransform().getX();
	}

	public float getY() {
		return getTransform().getY();
	}

	public void setScale(float x, float y) {
		mTransform.setScaleX(x);
		mTransform.setScaleY(y);
	}

	public float getScaleX() {
		return mTransform.getScaleX();
	}

	public float getScaleY() {
		return mTransform.getScaleY();
	}

	public float getAbsoluteScaleX() {
		return mAbsoluteTransform.getScaleX();
	}

	public float getAbsoluteScaleY() {
		return mAbsoluteTransform.getScaleY();
	}

	public void setVisible(boolean isVisible) {
		mIsVisible = isVisible;
	}

	public boolean isVisible() {
		return mIsVisible;
	}
	
	public boolean isAIEnabled() {
		return mIsAIEnabled;
	}
	
	public void setAIEnabled(boolean enabled) {
		mIsAIEnabled = enabled;
	}

	public void startAnimation(Animation animation) {
		mCurrentAnimation = animation;
		mCurrentAnimation.start();
	}

	/**
	 * Starts current animation if node has one.
	 */
	public void startAnimation() {
		if (mCurrentAnimation != null)
			mCurrentAnimation.start();
	}

	public Animation getAnimation() {
		return mCurrentAnimation;
	}

	public void setBody(Body body) {
		if (mBody != null)
			mBody.setSceneNode(null);

		mBody = body;

		if (mBody != null)
			mBody.setSceneNode(this);
	}

	public Body getBody() {
		return mBody;
	}

	public float getAbsoluteX() {
		return mAbsoluteTransform.getX();
	}

	public float getAbsoluteY() {
		return mAbsoluteTransform.getY();
	}

	public void setAI(Task ai) {
		mAITask = ai;

		attachAITask();
	}

	public AIContext getAIContext() {
		return mAIContext;
	}

	public void setAIContext(AIContext context) {
		mAIContext = context;
		mAIContext.setOwner(this);

		attachAITask();
	}

	public Task getAI() {
		return mAITask;
	}

	public void setUserData(Object data) {
		mUserData = data;
	}

	public Object getUserData() {
		return mUserData;
	}

	protected void draw(GL10 gl, long time, long delta) {
		// noop
	}

	private void updateChildrenCopy() {
		mChildrenCopy = mChildren.toArray(new SceneNode[mChildren.size()]);
	}

	private void attachAITask() {
		if (mAITask != null) {
			Iterator<Task> iter = mAITask.getDepthFirstIterator();
			while (iter.hasNext()) {
				Task myTask = iter.next();
				myTask.onAttach(getAIContext());
			}
		}
	}

	@Override
	public Iterator<SceneNode> iterator() {
		return mChildren.iterator();
	}
}
