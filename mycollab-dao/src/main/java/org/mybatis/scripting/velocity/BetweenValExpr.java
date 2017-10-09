package org.mybatis.scripting.velocity;

import com.mycollab.db.arguments.BetweenValuesSearchField;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class BetweenValExpr extends Directive {

    /*
     * (non-Javadoc)
     *
     * @see org.apache.velocity.runtime.directive.Directive#getName()
     */
    public String getName() {
        return "betweenvalexpr";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.velocity.runtime.directive.Directive#getType()
     */
    public int getType() {
        return BLOCK;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.velocity.runtime.directive.Directive#render(org.apache.velocity
     * .context.InternalContextAdapter, java.io.Writer,
     * org.apache.velocity.runtime.parser.node.Node)
     */
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException,
            ParseErrorException, MethodInvocationException {
        Object value = node.jjtGetChild(0).value(context);
        if (value instanceof BetweenValuesSearchField) {
            Node content = node.jjtGetChild(1);
            content.render(context, writer);
        }
        return true;
    }
}
