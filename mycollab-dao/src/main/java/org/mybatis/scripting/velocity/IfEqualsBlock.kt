package org.mybatis.scripting.velocity

import org.apache.velocity.context.InternalContextAdapter
import org.apache.velocity.exception.MethodInvocationException
import org.apache.velocity.exception.ParseErrorException
import org.apache.velocity.exception.ResourceNotFoundException
import org.apache.velocity.runtime.directive.Directive
import org.apache.velocity.runtime.directive.DirectiveConstants
import org.apache.velocity.runtime.parser.node.Node
import java.io.IOException
import java.io.Writer

/**
 * @author MyCollab Ltd
 * @since 7.0.2
 */
class IfEqualsBlock : Directive() {

    /*
     * (non-Javadoc)
     *
     * @see org.apache.velocity.runtime.directive.Directive#getName()
     */
    override fun getName(): String = "ifequals"

    /*
     * (non-Javadoc)
     *
     * @see org.apache.velocity.runtime.directive.Directive#getType()
     */
    override fun getType(): Int = DirectiveConstants.BLOCK

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.velocity.runtime.directive.Directive#render(org.apache.velocity
     * .context.InternalContextAdapter, java.io.Writer,
     * org.apache.velocity.runtime.parser.node.Node)
     */
    @Throws(IOException::class, ResourceNotFoundException::class, ParseErrorException::class, MethodInvocationException::class)
    override fun render(context: InternalContextAdapter, writer: Writer, node: Node): Boolean {
        val param0 = node.jjtGetChild(0).value(context)
        val param1 = node.jjtGetChild(1).value(context)
        if (param0 == param1) {
            val content = node.jjtGetChild(2)
            content.render(context, writer)
        }
        return true
    }

}
