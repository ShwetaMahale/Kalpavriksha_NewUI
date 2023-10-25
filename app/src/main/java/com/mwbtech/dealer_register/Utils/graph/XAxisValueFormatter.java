package com.mwbtech.dealer_register.Utils.graph;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class XAxisValueFormatter extends ValueFormatter {

   private String[] values;

   public XAxisValueFormatter(String[] values) {
       this.values = values;
   }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return this.values[(int) value];
    }
}