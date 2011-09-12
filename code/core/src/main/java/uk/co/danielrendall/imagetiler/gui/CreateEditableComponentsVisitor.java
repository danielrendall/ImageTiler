/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of ImageTiler.
 *
 * ImageTiler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ImageTiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ImageTiler.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.co.danielrendall.imagetiler.gui;

import uk.co.danielrendall.imagetiler.annotations.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

/**
 * @author Daniel Rendall
 */
public class CreateEditableComponentsVisitor implements FieldVisitor {

    private final List<JComponent> components;
    public final int SliderMinimum = 0;
    public final int SliderMaximum = 100;

    public CreateEditableComponentsVisitor() {
        components = new ArrayList<JComponent>();
    }

    public void visit(final StringField sField) {
        final JTextField tf = new JTextField();
        tf.setText(sField.get());
        tf.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String text = tf.getText();
                sField.set(text);
                String newText = sField.get();
                if (!text.equals(newText)) {
                    tf.setText(newText);
                }
            }
        });
        addComponent(sField, tf);
    }

    public void visit(final BooleanField bField) {
        final JCheckBox cb = new JCheckBox();
        cb.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    bField.set(true);
                    if (!bField.get()) {
                        cb.setSelected(false);
                    }
                } else {
                    bField.set(false);
                    if (bField.get()) {
                        cb.setSelected(true);
                    }
                }
            }
        });
        addComponent(bField, cb);
    }

    public void visit(final DoubleField dField) {
        final JSlider s = new JSlider(JSlider.HORIZONTAL, SliderMinimum, SliderMaximum, getScaledValueForSlider(dField, dField.defaultValue()));
        s.setPaintLabels(true);
        Hashtable<Integer, JLabel> ht = new Hashtable<Integer, JLabel>();
        ht.put(new Integer(SliderMinimum), new JLabel(Double.toString(dField.minValue())));
        ht.put(new Integer(SliderMaximum), new JLabel(Double.toString(dField.maxValue())));
        s.setLabelTable(ht);
        s.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                if (!s.getValueIsAdjusting()) {
                    int value = s.getValue();
                    double fieldValue = getDescaledValueForModel(dField, value);
                    dField.set(fieldValue);
                    double newValue = dField.get();
                    if (newValue != value) {
                        s.setValue(getScaledValueForSlider(dField, newValue));
                    }
                }
            }
        });
        addComponent(dField, s);
    }

    public void visit(final IntegerField iField) {
        final JSlider s = new JSlider(JSlider.HORIZONTAL, iField.minValue(), iField.maxValue(), iField.defaultValue());
        s.setPaintLabels(true);
        s.setPaintTicks(true);
        Hashtable<Integer, JLabel> ht = new Hashtable<Integer, JLabel>();
        ht.put(new Integer(iField.minValue()), new JLabel(Integer.toString(iField.minValue())));
        ht.put(new Integer(iField.maxValue()), new JLabel(Integer.toString(iField.maxValue())));
        s.setLabelTable(ht);
        s.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                if (!s.getValueIsAdjusting()) {
                    int value = s.getValue();
                    iField.set(value);
                    int newValue = iField.get();
                    if (newValue != value) {
                        s.setValue(newValue);
                    }
                }
            }
        });
        addComponent(iField, s);
    }

    public List<JComponent> getComponents() {
        return components;
    }

    private void addComponent(AnnotatedField aField, JComponent comp) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(aField.name()), BorderLayout.NORTH);
        comp.setToolTipText(aField.description());
        panel.add(comp, BorderLayout.CENTER);
        components.add(panel);
    }

    private int getScaledValueForSlider(DoubleField field, double fieldValue) {
        double normalizedFieldValue = fieldValue - field.minValue();
        return (int) ((double)SliderMinimum + ((normalizedFieldValue / field.getRange()) * (double)(SliderMaximum - SliderMinimum)));
    }

    private double getDescaledValueForModel(DoubleField field, int sliderValue) {
        double normalizedSliderValue = (double) sliderValue - (double) SliderMinimum;
        return field.minValue() + (normalizedSliderValue / (double) (SliderMaximum - SliderMinimum)) * field.getRange();
    }

}
