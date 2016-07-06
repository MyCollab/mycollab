/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.apache.velocity.tools.generic.directive;

import com.mycollab.db.arguments.OneValueSearchField;
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
public class OneValExpr extends Directive {

    /*
     * (non-Javadoc)
     *
     * @see org.apache.velocity.runtime.directive.Directive#getName()
     */
    public String getName() {
        return "onevalexpr";
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
        if (value instanceof OneValueSearchField) {
            Node content = node.jjtGetChild(1);
            content.render(context, writer);
        }
        return true;
    }
}
