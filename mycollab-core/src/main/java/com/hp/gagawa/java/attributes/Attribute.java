/**
(c) Copyright 2008 Hewlett-Packard Development Company, L.P.

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

package com.hp.gagawa.java.attributes;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Attribute {
	private String name;
	private String value;
	
	public Attribute(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	public Attribute(String name){
		this.name = name;
		this.value = null;
	}
	
	public String write(){
		StringBuffer b = new StringBuffer(" ");
		b.append(name);
		if(value != null){
			b.append("=\"");
			b.append(value);
			b.append("\"");
		}
		return b.toString();
	}
	
	@Override
	public String toString(){
		return this.write();
	}

}
