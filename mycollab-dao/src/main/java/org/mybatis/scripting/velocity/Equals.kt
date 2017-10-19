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
 * @since 5.1.1
 */
class Equals : Directive() {
    override fun getName(): String = "equals"

    override fun getType(): Int = DirectiveConstants.LINE

    @Throws(IOException::class, ResourceNotFoundException::class, ParseErrorException::class, MethodInvocationException::class)
    override fun render(context: InternalContextAdapter, writer: Writer, node: Node): Boolean {
        val value = node.jjtGetChild(0).value(context)
        when {
            value != null -> writer.write("='$value'")
            else -> writer.write("IS NULL")
        }
        return true
    }
}
