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
package org.mybatis.scripting.velocity;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class TrimExtDirective extends Directive {

    @Override
    public String getName() {
        return "trimext";
    }

    @Override
    public final int getType() {
        return BLOCK;
    }

    @Override
    public final boolean render(InternalContextAdapter ica, Writer writer, Node node) throws IOException,
            ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        Params p = getParams(ica, node);
        if (p == null) {
            return false;
        } else {
            return render(p, writer);
        }
    }

    public boolean render(final Params params, final Writer writer) throws IOException,
            ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        int leftIndex = 0;
        int rightIndex = params.maxBody;
        if (!params.prefixOverrides.isEmpty()) {
            final String LEFT = params.body.substring(0, params.maxPrefixLength < params.maxBody ? params.maxPrefixLength
                                    : params.maxBody).toUpperCase(Locale.ENGLISH);
            FastLinkedList<String>.Node n = params.prefixOverrides.start();
            while (n != null) {
                if (LEFT.startsWith(n.data)) {
                    leftIndex = n.data.length();
                    break;
                }
                n = n.next;
            }
        }
        if (!params.suffixOverrides.isEmpty()) {
            final String RIGHT = params.body.substring(rightIndex - params.maxSuffixLength).toUpperCase(Locale.ENGLISH);
            FastLinkedList<String>.Node n = params.suffixOverrides.start();
            while (n != null) {
                if (RIGHT.endsWith(n.data)) {
                    rightIndex = rightIndex - n.data.length();
                    break;
                }
                n = n.next;
            }
        }
        writer.append(params.prefix).append(' ');
        writer.append(params.body, leftIndex, rightIndex).append(' ');
        writer.append(params.suffix);
        return true;
    }

    protected static final class Params {
        String prefix = "";
        String suffix = "";
        FastLinkedList<String> prefixOverrides = new FastLinkedList<String>();
        FastLinkedList<String> suffixOverrides = new FastLinkedList<String>();
        String body = "";
        int maxPrefixLength = 0;
        int maxSuffixLength = 0;
        int maxBody = 0;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            if (body == null) {
                this.body = "";
            } else {
                this.body = body.trim();
            }
            maxBody = this.body.length();
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public FastLinkedList<String> getPrefixOverrides() {
            return prefixOverrides;
        }

        public void setPrefixOverrides(String list) {
            maxPrefixLength = fromStringList(list, '|', prefixOverrides);
        }

        public FastLinkedList<String> getSuffixOverrides() {
            return suffixOverrides;
        }

        public void setSuffixOverrides(String list) {
            maxSuffixLength = fromStringList(list, '|', suffixOverrides);
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

    }

    protected Params getParams(final InternalContextAdapter context, final Node node) throws IOException,
            ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        final Params params = new Params();
        final int nodes = node.jjtGetNumChildren();
        for (int i = 0; i < nodes; i++) {
            Node child = node.jjtGetChild(i);
            if (child != null) {
                if (!(child instanceof ASTBlock)) {
                    if (i == 0) {
                        params.setPrefix(String.valueOf(child.value(context)));
                    } else if (i == 1) {
                        params.setPrefixOverrides(String.valueOf(child.value(context)).toUpperCase(Locale.ENGLISH));
                    } else if (i == 2) {
                        params.setSuffix(String.valueOf(child.value(context)));
                    } else if (i == 3) {
                        params.setSuffixOverrides(String.valueOf(child.value(context)).toUpperCase(Locale.ENGLISH));
                    } else {
                        break;
                    }
                } else {
                    StringWriter blockContent = new StringWriter();
                    child.render(context, blockContent);
                    if ("".equals(blockContent.toString().trim())) {
                        return null;
                    }
                    params.setBody(blockContent.toString().trim());
                    break;
                }
            }
        }
        return params;
    }

    private static int fromStringList(final String list, final char sep, final FastLinkedList<String> fll) {
        int max = 0;
        if (list != null) {
            final int n = list.length();
            int i = 0;
            while (i < n) {
                int r = list.indexOf(sep, i);
                if (i < r) {
                    fll.add(list.substring(i, r));
                    int len = r - i;
                    if (len > max) {
                        max = len;
                    }
                    i = r + 1;
                } else {
                    break;
                }
            }
            if (i < n) {
                fll.add(list.substring(i));
                int len = n - i;
                if (len > max) {
                    max = len;
                }
            }
        }
        return max;
    }

}
