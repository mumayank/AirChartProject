package com.mumayank.airchart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChartItem {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("decimalFormatPattern")
    @Expose
    private String decimalFormatPattern;
    @SerializedName("xLabel")
    @Expose
    private String xLabel;
    @SerializedName("xLabels")
    @Expose
    private ArrayList<String> xLabels = null;
    @SerializedName("yLeftLabel")
    @Expose
    private String yLeftLabel;
    @SerializedName("yLeftItems")
    @Expose
    private ArrayList<YLeftItem> yLeftItems = null;
    @SerializedName("colors")
    @Expose
    private ArrayList<String> colors = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ChartItem() {
    }

    /**
     *
     * @param xLabels
     * @param yLeftItems
     * @param title
     * @param yLeftLabel
     * @param decimalFormatPattern
     * @param xLabel
     * @param colors
     */
    public ChartItem(String title, String decimalFormatPattern, String xLabel, ArrayList<String> xLabels, String yLeftLabel, ArrayList<YLeftItem> yLeftItems, ArrayList<String> colors) {
        super();
        this.title = title;
        this.decimalFormatPattern = decimalFormatPattern;
        this.xLabel = xLabel;
        this.xLabels = xLabels;
        this.yLeftLabel = yLeftLabel;
        this.yLeftItems = yLeftItems;
        this.colors = colors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecimalFormatPattern() {
        return decimalFormatPattern;
    }

    public void setDecimalFormatPattern(String decimalFormatPattern) {
        this.decimalFormatPattern = decimalFormatPattern;
    }

    public String getXLabel() {
        return xLabel;
    }

    public void setXLabel(String xLabel) {
        this.xLabel = xLabel;
    }

    public ArrayList<String> getXLabels() {
        return xLabels;
    }

    public void setXLabels(ArrayList<String> xLabels) {
        this.xLabels = xLabels;
    }

    public String getYLeftLabel() {
        return yLeftLabel;
    }

    public void setYLeftLabel(String yLeftLabel) {
        this.yLeftLabel = yLeftLabel;
    }

    public ArrayList<YLeftItem> getYLeftItems() {
        return yLeftItems;
    }

    public void setYLeftItems(ArrayList<YLeftItem> yLeftItems) {
        this.yLeftItems = yLeftItems;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

}
