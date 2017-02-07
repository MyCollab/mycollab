/**
(c) Copyright 2009 Hewlett-Packard Development Company, L.P.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/

package com.hp.gagawa.java;

import java.util.ArrayList;

/**
 * Represents a node that can have children
 * @author friedrch
 *
 */
public class FertileNode extends Node {
	
	public ArrayList<Node> children;
	
	protected FertileNode(String tag){
		super(tag);
		this.children = new ArrayList<>();
	}
	
	/**
	 * Gets the list of child nodes
	 * @return a list of children
	 */
	public ArrayList<Node> getChildren(){
		return children;
	}
	
	/**
	 * Gets the child node at the given index
	 * @param index child node index
	 * @return the child node
	 */
	public Node getChild(int index){
		return children.get(index);
	}
	
	/**
	 * Writes the node's tag and all of its children's tags
	 */
	@Override
	public String write(){
		StringBuffer b = new StringBuffer(writeOpen());
		
		if(children != null && children.size() > 0){
			for(Node child: children){
				b.append(child.write());
			}
		}
		b.append(writeClose());
		return b.toString();
	}
	
	/**
	 * A convenient way of calling write().
	 * This method calls this.write().
	 */
	@Override
	public String toString(){
		return this.write();
	}
	
}
