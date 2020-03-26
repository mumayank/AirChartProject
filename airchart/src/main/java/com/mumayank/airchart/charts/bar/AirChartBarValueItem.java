package com.mumayank.airchart.charts.bar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AirChartBarValueItem {

    @SerializedName("legendLabel")
    @Expose
    private String legendLabel;
    @SerializedName("values")
    @Expose
    private ArrayList<Float> values = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public AirChartBarValueItem() {
    }

    /**
     *
     * @param legendLabel
     * @param values
     */
    public AirChartBarValueItem(String legendLabel, ArrayList<Float> values) {
        super();
        this.legendLabel = legendLabel;
        this.values = values;
    }

    public String getLegendLabel() {
        return legendLabel;
    }

    public void setLegendLabel(String legendLabel) {
        this.legendLabel = legendLabel;
    }

    public ArrayList<Float> getValues() {
        return values;
    }

    public void setValues(ArrayList<Float> values) {
        this.values = values;
    }

}