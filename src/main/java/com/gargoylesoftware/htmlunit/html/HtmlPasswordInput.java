/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.html.impl.SelectionDelegate;

/**
 * Wrapper for the HTML element "input".
 *
 * @version $Revision: 5789 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class HtmlPasswordInput extends HtmlInput implements SelectableTextInput {

    private static final long serialVersionUID = -1074283471317076942L;

    private String valueAtFocus_;

    private final SelectionDelegate selectionDelegate_ = new SelectionDelegate(this);

    private final DoTypeProcessor doTypeProcessor_ = new DoTypeProcessor() {
        private static final long serialVersionUID = -1938284467263013958L;
        @Override
        void typeDone(final String newValue, final int newCursorPosition) {
            setAttribute("value", newValue);
            setSelectionStart(newCursorPosition);
            setSelectionEnd(newCursorPosition);
        }
    };

    /**
     * Creates an instance.
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlPasswordInput(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isSubmittableByEnter() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void select() {
        selectionDelegate_.select();
    }

    /**
     * {@inheritDoc}
     */
    public String getSelectedText() {
        return selectionDelegate_.getSelectedText();
    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        return getValueAttribute();
    }

    /**
     * {@inheritDoc}
     */
    public void setText(final String text) {
        setValueAttribute(text);
    }

    /**
     * {@inheritDoc}
     */
    public int getSelectionStart() {
        return selectionDelegate_.getSelectionStart();
    }

    /**
     * {@inheritDoc}
     */
    public void setSelectionStart(final int selectionStart) {
        selectionDelegate_.setSelectionStart(selectionStart);
    }

    /**
     * {@inheritDoc}
     */
    public int getSelectionEnd() {
        return selectionDelegate_.getSelectionEnd();
    }

    /**
     * {@inheritDoc}
     */
    public void setSelectionEnd(final int selectionEnd) {
        selectionDelegate_.setSelectionEnd(selectionEnd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doType(final char c, final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        doTypeProcessor_.doType(getValueAttribute(), getSelectionStart(), getSelectionEnd(),
            c, shiftKey, ctrlKey, altKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void focus() {
        super.focus();
        // store current value to trigger onchange when needed at focus lost
        valueAtFocus_ = getValueAttribute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void removeFocus() {
        super.removeFocus();
        if (!valueAtFocus_.equals(getValueAttribute())) {
            executeOnChangeHandlerIfAppropriate(this);
        }
        valueAtFocus_ = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new HtmlPasswordInput(getNamespaceURI(), getQualifiedName(), getPage(), getAttributesMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue) {
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue);
        if (qualifiedName.equals("value") && getPage() instanceof HtmlPage) {
            setSelectionStart(attributeValue.length());
            setSelectionEnd(attributeValue.length());
        }
    }
}
