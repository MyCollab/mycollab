package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class DefaultGenericSearchPanel<S extends SearchCriteria> extends GenericSearchPanel<S> {
    private static final long serialVersionUID = 1L;

    private MHorizontalLayout header;
    private ComponentContainer headerText;
    protected boolean canSwitchToAdvanceLayout;

    public DefaultGenericSearchPanel() {
        this(true);
    }

    public DefaultGenericSearchPanel(boolean canSwitchToAdvanceLayout) {
        this.canSwitchToAdvanceLayout = canSwitchToAdvanceLayout;
    }

    @Override
    public void attach() {
        super.attach();
        moveToBasicSearchLayout();
    }

    abstract protected SearchLayout<S> createBasicSearchLayout();

    abstract protected SearchLayout<S> createAdvancedSearchLayout();

    abstract protected ComponentContainer buildSearchTitle();

    protected Component buildExtraControls() {
        return null;
    }

    protected ComponentContainer constructHeader() {
        if (header == null) {
            headerText = buildSearchTitle();
            if (headerText != null) {
                MHorizontalLayout rightComponent = new MHorizontalLayout();
                header = new MHorizontalLayout().withFullWidth().withMargin(new MarginInfo(true, false, true, false));

                header.with(headerText, rightComponent).withAlign(headerText, Alignment.MIDDLE_LEFT)
                        .withAlign(rightComponent, Alignment.MIDDLE_RIGHT).expand(headerText);
            }

            Component extraControls = buildExtraControls();
            if (extraControls != null) {
                addHeaderRight(extraControls);
            }
            return header;
        } else {
            return header;
        }
    }

    public void setTotalCountNumber(Integer countNumber) {
        if (headerText instanceof HeaderWithFontAwesome) {
            ((HeaderWithFontAwesome) headerText).appendToTitle(UserUIContext.getMessage(GenericI18Enum.OPT_TOTAL_VALUE, countNumber));
        }
    }

    protected SearchLayout<S> moveToBasicSearchLayout() {
        SearchLayout<S> layout = createBasicSearchLayout();
        setCompositionRoot(layout);
        return layout;
    }

    protected SearchLayout<S> moveToAdvancedSearchLayout() {
        SearchLayout<S> layout = createAdvancedSearchLayout();
        setCompositionRoot(layout);
        return layout;
    }
}