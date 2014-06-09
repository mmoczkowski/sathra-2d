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
package eu.sathra.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class TreeNode<T> {

	private List<TreeNode<T>> mChildren = new ArrayList<TreeNode<T>>();
	private TreeNode<T> mParent;
	private T mValue;
	
	private class DepthFirstIterator implements Iterator<T> {
		
		private Stack<TreeNode<T>> mStack = new Stack<TreeNode<T>>();
		
		public DepthFirstIterator(TreeNode<T> root) {
			mStack.push(root);
		}

		@Override
		public boolean hasNext() {
			return !mStack.isEmpty();
		}

		@Override
		public T next() {
			TreeNode<T> current = mStack.pop();
	
			mStack.addAll(current.getChildren());

			return current.getValue();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException ();
		}
		
	}
	
	public TreeNode() {
		this(null);
	}
	
	public TreeNode(T value) {
		mValue = value;
	}
	
	public void setValue(T value) {
		mValue = value;
	}
	
	public T getValue() {
		return mValue;
	}
	
	public void addChild(TreeNode<T> child) {
		if(child.getParent() != null)
			child.getParent().removeChild(child);

		child.mParent = this;
		mChildren.add(child);
	}
	
	public void addChildren(TreeNode<T>[] children) {
		for(TreeNode<T> node : children)
			addChild(node);
	}
	
	public void removeChild(TreeNode<T> child) {
		child.mParent = null;
		mChildren.remove(child);
	}
	
	public TreeNode<T> getParent() {
		return mParent;
	}
	
	public boolean hasChildren() {
		return !mChildren.isEmpty();
	}
	
	public int childCount() {
		return mChildren.size();
	}
	
	public TreeNode<T> getChild(int c) {
		return mChildren.get(c);
	}
	
	public List<TreeNode<T>> getChildren() {
		return Collections.unmodifiableList(mChildren);
	}
	
	public Iterator<T> getDepthFirstIterator() {
		return new DepthFirstIterator(this);
	}	
}