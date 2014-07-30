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

import com.hp.gagawa.java.attributes.Attribute;

public abstract class Node {
	
	protected String tag_;
	protected ArrayList<Attribute> attributes_;
	protected Node parent_;
		
	protected Node(String tag){
		this.tag_ = tag;
		this.attributes_ = new ArrayList<Attribute>();
	}
	
	/**
	 * Gets the parent node
	 * @return the parent node
	 */
	public Node getParent(){
		return this.parent_;
	}
	
	/**
	 * Sets an element attribute
	 * @param name the attribute
	 * @param value the attribute value
	 */
	public void setAttribute(String name, String value){
		if(value != null){
			for(Attribute attribute: attributes_){
				if(attribute.getName().equals(name)){
					attribute.setValue(value);
					return;
				}
			}
			attributes_.add(new Attribute(name, value));
		}
	}
	
	/**
	 * Gets the value of the given attribute
	 * @param name the requested attribute name
	 * @return the value
	 */
	public String getAttribute(String name){
		for(Attribute attribute: attributes_){
			if(attribute.getName().equals(name)){
				return attribute.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Removes the given attribute
	 * @param name the attribute name
	 * @return whether the attribute was successfully removed
	 */
	public boolean removeAttribute(String name){
		for(Attribute attribute: attributes_){
			if(attribute.getName().equals(name)){
				return attributes_.remove(attribute);
			}
		}
		return false;
	}
	
	public void setParent(Node parent){
		this.parent_ = parent;
	}
	
	/**
	 * Write the HTML tag as a String
	 * @return HTML tag as String
	 */
	public String write(){
		StringBuffer b = new StringBuffer(writeOpen());
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
	
	protected String writeOpen(){
		StringBuffer b = new StringBuffer("<");
		b.append(tag_);
		for(Attribute attr: attributes_){
			b.append(attr.write());
		}
		b.append(">");
		return b.toString();
	}
	
	protected String writeClose(){
		StringBuffer b = new StringBuffer("</");
		b.append(tag_);
		b.append(">");
		return b.toString();
	}
		
}
