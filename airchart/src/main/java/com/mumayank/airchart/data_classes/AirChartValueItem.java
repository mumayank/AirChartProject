package com.mumayank.airchart.data_classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AirChartValueItem {

    @SerializedName("legendLabel")
    @Expose
    private String legendLabel;
    @SerializedName("values")
    @Expose
    private ArrayList<ArrayList<Float>> valuesList = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public AirChartValueItem() {
    }

    /**
     *
     * @param legendLabel
     * @param valuesList
     */
    public AirChartValueItem(String legendLabel, ArrayList<ArrayList<Float>> valuesList) {
        super();
        this.legendLabel = legendLabel;
        this.valuesList = valuesList;
    }

    public String getLegendLabel() {
        return legendLabel;
    }

    public void setLegendLabel(String legendLabel) {
        this.legendLabel = legendLabel;
    }

    public ArrayList<ArrayList<Float>> getValues() {
        return valuesList;
    }

    public void setValues(ArrayList<ArrayList<Float>> values) {
        this.valuesList = values;
    }

}