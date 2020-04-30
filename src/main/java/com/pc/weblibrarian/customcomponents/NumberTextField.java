package com.pc.weblibrarian.customcomponents;

import com.pc.weblibrarian.views.viewsutil.RequiredValidationUtil;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.value.HasValueChangeMode;
import com.vaadin.flow.data.value.ValueChangeMode;

public class NumberTextField extends GeneratedVaadinTextField<NumberTextField, Integer>
        implements HasSize, HasValidation, HasValueChangeMode, HasPrefixAndSuffix, InputNotifier, KeyNotifier,
        CompositionNotifier, HasAutocomplete, HasAutocapitalize, HasAutocorrect
{
    
    private static final long serialVersionUID = 1L;
    
    private ValueChangeMode currentMode;
    
    private boolean isConnectorAttached;
    
    /**
     * Constructs an empty {@code TextField}.
     */
    public NumberTextField() {
        super(0, 0, false);
        setValueChangeMode(ValueChangeMode.ON_CHANGE);
        
        getElement().setAttribute("type", "number");
        setPattern("[0-9]*");
        setPreventInvalidInput(true);
    }
    
    /**
     * Constructs an empty {@code TextField} with the given label.
     *
     * @param label the text to set as the label
     */
    public NumberTextField(String label) {
        this();
        setLabel(label);
    }
    
    /**
     * Constructs an empty {@code TextField} with the given label and placeholder
     * text.
     *
     * @param label       the text to set as the label
     * @param placeholder the placeholder text to set
     */
    public NumberTextField(String label, String placeholder) {
        this(label);
        setPlaceholder(placeholder);
    }
    
    /**
     * Constructs a {@code TextField} with the given label, an initial value and
     * placeholder text.
     *
     * @param label        the text to set as the label
     * @param initialValue the initial value
     * @param placeholder  the placeholder text to set
     *
     * @see #setValue(Object)
     * @see #setPlaceholder(String)
     */
    public NumberTextField(String label, Integer initialValue, Integer placeholder) {
        this(label);
        setValue(initialValue);
        setPlaceholder(String.valueOf(placeholder));
    }
    
    /**
     * Constructs an empty {@code TextField} with a value change listener.
     *
     * @param listener the value change listener
     *
     * @see #addValueChangeListener(ValueChangeListener)
     */
    public NumberTextField(ValueChangeListener<? super ComponentValueChangeEvent<NumberTextField, Integer>> listener) {
        this();
        addValueChangeListener(listener);
    }
    
    /**
     * Constructs an empty {@code TextField} with a label and a value change
     * listener.
     *
     * @param label    the text to set as the label
     * @param listener the value change listener
     * @see #setLabel(String)
     * @see #addValueChangeListener(ValueChangeListener)
     */
    public NumberTextField(String label,
                           ValueChangeListener<? super ComponentValueChangeEvent<NumberTextField, Integer>> listener) {
        this(label);
        addValueChangeListener(listener);
    }
    
    /**
     * Constructs an empty {@code TextField} with a label,a value change listener
     * and an initial value.
     *
     * @param label        the text to set as the label
     * @param initialValue the initial value
     * @param listener     the value change listener
     *
     * @see #setLabel(String)
     * @see #setValue(Object)
     * @see #addValueChangeListener(ValueChangeListener)
     */
    public NumberTextField(String label, Integer initialValue,
                           ValueChangeListener<? super ComponentValueChangeEvent<NumberTextField, Integer>> listener) {
        this(label);
        setValue(initialValue);
        addValueChangeListener(listener);
    }
    
    /**
     * {@inheritDoc}
     * <p>
     * The default value is {@link ValueChangeMode#ON_CHANGE}.
     */
    @Override
    public ValueChangeMode getValueChangeMode() {
        return currentMode;
    }
    
    @Override
    public void setValueChangeMode(ValueChangeMode valueChangeMode) {
        currentMode = valueChangeMode;
        setSynchronizedEvent(ValueChangeMode.eventForMode(valueChangeMode, "value-changed"));
    }
    
    @Override
    public String getErrorMessage() {
        return super.getErrorMessageString();
    }
    
    @Override
    public void setErrorMessage(String errorMessage) {
        super.setErrorMessage(errorMessage);
    }
    
    @Override
    public boolean isInvalid() {
        return isInvalidBoolean();
    }
    
    @Override
    public void setInvalid(boolean invalid) {
        super.setInvalid(invalid);
    }
    
    @Override
    public void setLabel(String label) {
        super.setLabel(label);
    }
    
    /**
     * String used for the label element.
     *
     * @return the {@code label} property from the webcomponent
     */
    public String getLabel() {
        return getLabelString();
    }
    
    @Override
    public void setPlaceholder(String placeholder) {
        super.setPlaceholder(placeholder);
    }
    
    /**
     * A hint to the user of what can be entered in the component.
     *
     * @return the {@code placeholder} property from the webcomponent
     */
    public String getPlaceholder() {
        return getPlaceholderString();
    }
    
    @Override
    public void setAutofocus(boolean autofocus) {
        super.setAutofocus(autofocus);
    }
    
    /**
     * Specify that this control should have input focus when the page loads.
     *
     * @return the {@code autofocus} property from the webcomponent
     */
    public boolean isAutofocus() {
        return isAutofocusBoolean();
    }
    
    /**
     * Maximum number of characters (in Unicode code points) that the user can
     * enter.
     *
     * @param maxLength the maximum length
     */
    public void setMaxLength(int maxLength) {
        super.setMaxlength(maxLength);
    }
    
    /**
     * Maximum number of characters (in Unicode code points) that the user can
     * enter.
     *
     * @return the {@code maxlength} property from the webcomponent
     */
    public int getMaxLength() {
        return (int) getMaxlengthDouble();
    }
    
    /**
     * Minimum number of characters (in Unicode code points) that the user can
     * enter.
     *
     * @param minLength the minimum length
     */
    public void setMinLength(int minLength) {
        super.setMinlength(minLength);
    }
    
    /**
     * Minimum number of characters (in Unicode code points) that the user can
     * enter.
     *
     * @return the {@code minlength} property from the webcomponent
     */
    public int getMinLength() {
        return (int) getMinlengthDouble();
    }
    
    /**
     * Specifies that the user must fill in a value.
     *
     * @return the {@code required} property from the webcomponent
     */
    public boolean isRequired() {
        return isRequiredBoolean();
    }
    
    /**
     * <p>
     * Specifies that the user must fill in a value.
     * </p>
     * NOTE: The required indicator will not be visible, if there is no
     * {@code label} property set for the textfield.
     *
     * @param required the boolean value to set
     */
    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
    }
    
    /**
     * When set to <code>true</code>, user is prevented from typing a value that
     * conflicts with the given {@code pattern}.
     *
     * @return the {@code preventInvalidInput} property from the webcomponent
     */
    public boolean isPreventInvalidInput() {
        return isPreventInvalidInputBoolean();
    }
    
    @Override
    public void setPreventInvalidInput(boolean preventInvalidInput) {
        super.setPreventInvalidInput(preventInvalidInput);
    }
    
    @Override
    public void setPattern(String pattern) {
        super.setPattern(pattern);
    }
    
    /**
     * A regular expression that the value is checked against. The pattern must
     * match the entire value, not just some subset.
     *
     * @return the {@code pattern} property from the webcomponent
     */
    public String getPattern() {
        return getPatternString();
    }
    
    /**
     * Message to show to the user when validation fails.
     *
     * @return the {@code title} property from the webcomponent
     */
    public String getTitle() {
        return super.getTitleString();
    }
    
    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }
    
    @Override
    public Integer getEmptyValue() {
        return 0;
    }
    
    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        super.setRequiredIndicatorVisible(requiredIndicatorVisible);
        if (!isConnectorAttached) {
            RequiredValidationUtil.attachConnector(this);
            isConnectorAttached = true;
        }
        RequiredValidationUtil.updateClientValidation(requiredIndicatorVisible,
                                                      this);
    }
    
    public void centerAlign() {
        addThemeName("align-center");
    }
    
    public void addPlusMinus() {
        centerAlign();
        Button pluss = new Button(new Icon(VaadinIcon.PLUS));
        pluss.setThemeName("tertiary-inline");
        pluss.addClickListener(c -> setValue(getValue() + 1));
        pluss.setTabIndex(8);
        setSuffixComponent(pluss);
        
        Button minuss = new Button(new Icon(VaadinIcon.MINUS));
        minuss.setThemeName("tertiary-inline");
        minuss.addClickListener(c -> {
            if (getValue() > 0) {
                setValue(getValue() - 1);
            }
        });
        setPrefixComponent(minuss);
    }
}