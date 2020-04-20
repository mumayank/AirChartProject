package com.mumayank.airchart.data_classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Line {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("xAxisTitle")
    @Expose
    private String xAxisTitle;

    @SerializedName("xAxisLabels")
    @Expose
    private ArrayList<String> xAxisLabels = null;

    @SerializedName("yLeftAxisTitle")
    @Expose
    private String yLeftAxisTitle;

    @SerializedName("yLeftAxisValues")
    @Expose
    private ArrayList<Value> yLeftAxisValues = null;

    //todo should we include yRightAxisValues ??

    @SerializedName("colors")
    @Expose
    private ArrayList<String> colors = null;

    @SerializedName("subTitle")
    @Expose
    private String subTitle;

    @SerializedName("decimalFormatPattern")
    @Expose
    private String decimalFormatPattern;

    @SerializedName("additionalValues")
    @Expose
    private ArrayList<AdditionalValue> additionalValues = null;

    @SerializedName("isAnimationRequired")
    @Expose
    private Boolean isAnimationRequired;

    /**
     * No args constructor for use in serialization
     */
    public Line() {
    }

    /**
     * @param yLeftAxisValues
     * @param xAxisLabels
     * @param subTitle
     * @param yLeftAxisTitle
     * @param xAxisTitle
     * @param isAnimationRequired
     * @param title
     * @param decimalFormatPattern
     * @param additionalValues
     * @param colors
     */
    public Line(String title, String xAxisTitle, ArrayList<String> xAxisLabels, String yLeftAxisTitle, ArrayList<Value> yLeftAxisValues,
                ArrayList<String> colors, String subTitle, String decimalFormatPattern, ArrayList<AdditionalValue> additionalValues,
                Boolean isAnimationRequired) {
        super();
        this.title = title;
        this.xAxisTitle = xAxisTitle;
        this.xAxisLabels = xAxisLabels;
        this.yLeftAxisTitle = yLeftAxisTitle;
        this.yLeftAxisValues = yLeftAxisValues;
        this.colors = colors;
        this.subTitle = subTitle;
        this.decimalFormatPattern = decimalFormatPattern;
        this.additionalValues = additionalValues;
        this.isAnimationRequired = isAnimationRequired;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXAxisTitle() {
        return xAxisTitle;
    }

    public void setXAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
    }

    public ArrayList<String> getXAxisLabels() {
        return xAxisLabels;
    }

    public void setXAxisLabels(ArrayList<String> xAxisLabels) {
        this.xAxisLabels = xAxisLabels;
    }

    public String getYLeftAxisTitle() {
        return yLeftAxisTitle;
    }

    public void setYLeftAxisTitle(String yLeftAxisTitle) {
        this.yLeftAxisTitle = yLeftAxisTitle;
    }

    public ArrayList<Value> getYLeftAxisValues() {
        return yLeftAxisValues;
    }

    public void setYLeftAxisValues(ArrayList<Value> yLeftAxisValues) {
        this.yLeftAxisValues = yLeftAxisValues;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDecimalFormatPattern() {
        return decimalFormatPattern;
    }

    public void setDecimalFormatPattern(String decimalFormatPattern) {
        this.decimalFormatPattern = decimalFormatPattern;
    }

    public ArrayList<AdditionalValue> getAdditionalValues() {
        return additionalValues;
    }

    public void setAdditionalValues(ArrayList<AdditionalValue> additionalValues) {
        this.additionalValues = additionalValues;
    }

    public Boolean getIsAnimationRequired() {
        return isAnimationRequired;
    }

    public void setIsAnimationRequired(Boolean isAnimationRequired) {
        this.isAnimationRequired = isAnimationRequired;
    }

}
