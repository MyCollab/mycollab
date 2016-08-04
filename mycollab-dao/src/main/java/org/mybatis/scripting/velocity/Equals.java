package org.mybatis.scripting.velocity;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class Equals extends Directive {
    @Override
    public String getName() {
        return "equals";
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException,
            ParseErrorException, MethodInvocationException {
        Object value = node.jjtGetChild(0).value(context);
        if (value != null) {
            writer.write(String.format("='%s'", value));
        } else {
            writer.write("IS NULL");
        }
        return true;
    }
}
