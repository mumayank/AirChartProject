package com.mumayank.airchart.data_classes;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("legendLabel")
    @Expose
    private String legendLabel;
    @SerializedName("values")
    @Expose
    private ArrayList<Double> values = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Value() {
    }

    /**
     *
     * @param legendLabel
     * @param values
     */
    public Value(String legendLabel, ArrayList<Double> values) {
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

    public ArrayList<Double> getValues() {
        return values;
    }

    public void setValues(ArrayList<Double> values) {
        this.values = values;
    }

}
