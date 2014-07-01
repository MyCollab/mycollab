package com.esofthead.mycollab.template.velocity;

import java.io.Reader;
import java.io.Writer;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
@Component
public class TemplateEngineImpl implements TemplateEngine {

	@Autowired
	private VelocityEngine voEngine;

	@Override
	public void evaluate(TemplateContext context, Writer writer,
			String message, Reader reader) {
		voEngine.evaluate(context.getVelocityContext(), writer, "log", reader);
	}

}
