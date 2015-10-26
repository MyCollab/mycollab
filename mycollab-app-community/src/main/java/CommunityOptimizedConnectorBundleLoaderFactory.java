/**
 * This file is part of mycollab-app-community.
 *
 * mycollab-app-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-app-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-app-community.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.vaadin.server.widgetsetutils.ConnectorBundleLoaderFactory;
import com.vaadin.shared.ui.Connect;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd
 * @since 5.1.0
 */
public class CommunityOptimizedConnectorBundleLoaderFactory extends
        ConnectorBundleLoaderFactory {
    private Set<String> eagerConnectors = new HashSet<String>();

    {
        eagerConnectors.add(com.vaadin.client.ui.ui.UIConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.customlayout.CustomLayoutConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.passwordfield.PasswordFieldConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.textfield.TextFieldConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.csslayout.CssLayoutConnector.class.getName());
        eagerConnectors.add(com.ejt.vaadin.loginform.shared.LoginFormConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.checkbox.CheckBoxConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.button.ButtonConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.extensions.javascriptmanager.JavaScriptManagerConnector.class.getName());
    }

    @Override
    protected Connect.LoadStyle getLoadStyle(JClassType connectorType) {
        if (eagerConnectors.contains(connectorType.getQualifiedBinaryName())) {
            return Connect.LoadStyle.EAGER;
        } else {
            // Loads all other connectors immediately after the initial view has
            // been rendered
            return Connect.LoadStyle.DEFERRED;
        }
    }
}
