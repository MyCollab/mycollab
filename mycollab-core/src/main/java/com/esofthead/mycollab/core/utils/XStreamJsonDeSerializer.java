package com.esofthead.mycollab.core.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
public class XStreamJsonDeSerializer {
	private static final XStream xstream;

	static {
		xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.setMode(XStream.NO_REFERENCES);
	}

	public static String toJson(Object o) {
		return xstream.toXML(o);
	}

	public static Object fromJson(String json) {
		return xstream.fromXML(json);
	}
}
