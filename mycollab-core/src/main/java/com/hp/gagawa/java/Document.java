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
THE SOFTWARE.
*/

package com.hp.gagawa.java;

import com.hp.gagawa.java.elements.Body;
import com.hp.gagawa.java.elements.Doctype;
import com.hp.gagawa.java.elements.Head;
import com.hp.gagawa.java.elements.Html;


public class Document{

	private Doctype doctype;
	private Html html;
	public Head head;
	public Body body;
	
	public Document(DocumentType spec) {
		doctype = new Doctype(spec);
		html = new Html();
		head = new Head();
		body = new Body();
		html.appendChild(head);
		html.appendChild(body);
	}
	
	public String write(){
		StringBuffer b = new StringBuffer();
		b.append(doctype.write());
		b.append(html.write());
		return b.toString();
	}
	
	@Override
	public String toString(){
		return this.write();
	}

}
