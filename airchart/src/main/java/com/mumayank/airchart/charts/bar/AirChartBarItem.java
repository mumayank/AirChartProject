package com.mumayank.airchart.charts.bar;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mumayank.airchart.data_classes.AirChartAdditionalData;
import com.mumayank.airchart.data_classes.AirChartValueItem;

public class AirChartBarItem {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subTitle")
    @Expose
    private String subTitle;
    @SerializedName("decimalFormatPattern")
    @Expose
    private String decimalFormatPattern;
    @SerializedName("xLabel")
    @Expose
    private String xLabel;
    @SerializedName("xLabels")
    @Expose
    private List<String> xLabels = null;
    @SerializedName("yLeftLabel")
    @Expose
    private String yLeftLabel;
    @SerializedName("yLeftItems")
    @Expose
    private List<AirChartValueItem> airChartValueItems = null;
    @SerializedName("colors")
    @Expose
    private List<String> colors = null;
    @SerializedName("additionalDatas")
    @Expose
    private List<AirChartAdditionalData> airChartAdditionalData = null;
    @SerializedName("isAnimationRequired")
    @Expose
    private Boolean isAnimationRequired;

    /**
     * No args constructor for use in serialization
     *
     */
    public AirChartBarItem() {
    }

    /**
     *
     * @param airChartAdditionalData
     * @param subTitle
     * @param xLabels
     * @param airChartValueItems
     * @param isAnimationRequired
     * @param title
     * @param yLeftLabel
     * @param decimalFormatPattern
     * @param xLabel
     * @param colors
     */
    public AirChartBarItem(String title, String subTitle, String decimalFormatPattern, String xLabel, List<String> xLabels, String yLeftLabel, List<AirChartValueItem> airChartValueItems, List<String> colors, List<AirChartAdditionalData> airChartAdditionalData, Boolean isAnimationRequired) {
        super();
        this.title = title;
        this.subTitle = subTitle;
        this.decimalFormatPattern = decimalFormatPattern;
        this.xLabel = xLabel;
        this.xLabels = xLabels;
        this.yLeftLabel = yLeftLabel;
        this.airChartValueItems = airChartValueItems;
        this.colors = colors;
        this.airChartAdditionalData = airChartAdditionalData;
        this.isAnimationRequired = isAnimationRequired;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getXLabel() {
        return xLabel;
    }

    public void setXLabel(String xLabel) {
        this.xLabel = xLabel;
    }

    public List<String> getXLabels() {
        return xLabels;
    }

    public void setXLabels(List<String> xLabels) {
        this.xLabels = xLabels;
    }

    public String getYLeftLabel() {
        return yLeftLabel;
    }

    public void setYLeftLabel(String yLeftLabel) {
        this.yLeftLabel = yLeftLabel;
    }

    public List<AirChartValueItem> getYLeftItems() {
        return airChartValueItems;
    }

    public void setYLeftItems(List<AirChartValueItem> airChartValueItems) {
        this.airChartValueItems = airChartValueItems;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<AirChartAdditionalData> getAirChartAdditionalData() {
        return airChartAdditionalData;
    }

    public void setAirChartAdditionalData(List<AirChartAdditionalData> airChartAdditionalData) {
        this.airChartAdditionalData = airChartAdditionalData;
    }

    public Boolean getIsAnimationRequired() {
        return isAnimationRequired;
    }

    public void setIsAnimationRequired(Boolean isAnimationRequired) {
        this.isAnimationRequired = isAnimationRequired;
    }

}
