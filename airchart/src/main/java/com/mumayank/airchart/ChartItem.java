package com.mumayank.airchart;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChartItem {

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
    private List<YLeftItem> yLeftItems = null;
    @SerializedName("colors")
    @Expose
    private List<String> colors = null;
    @SerializedName("additionalDatas")
    @Expose
    private List<AdditionalData> additionalDatas = null;
    @SerializedName("isAnimationRequired")
    @Expose
    private Boolean isAnimationRequired;

    /**
     * No args constructor for use in serialization
     *
     */
    public ChartItem() {
    }

    /**
     *
     * @param additionalDatas
     * @param subTitle
     * @param xLabels
     * @param yLeftItems
     * @param isAnimationRequired
     * @param title
     * @param yLeftLabel
     * @param decimalFormatPattern
     * @param xLabel
     * @param colors
     */
    public ChartItem(String title, String subTitle, String decimalFormatPattern, String xLabel, List<String> xLabels, String yLeftLabel, List<YLeftItem> yLeftItems, List<String> colors, List<AdditionalData> additionalDatas, Boolean isAnimationRequired) {
        super();
        this.title = title;
        this.subTitle = subTitle;
        this.decimalFormatPattern = decimalFormatPattern;
        this.xLabel = xLabel;
        this.xLabels = xLabels;
        this.yLeftLabel = yLeftLabel;
        this.yLeftItems = yLeftItems;
        this.colors = colors;
        this.additionalDatas = additionalDatas;
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

    public List<YLeftItem> getYLeftItems() {
        return yLeftItems;
    }

    public void setYLeftItems(List<YLeftItem> yLeftItems) {
        this.yLeftItems = yLeftItems;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<AdditionalData> getAdditionalDatas() {
        return additionalDatas;
    }

    public void setAdditionalDatas(List<AdditionalData> additionalDatas) {
        this.additionalDatas = additionalDatas;
    }

    public Boolean getIsAnimationRequired() {
        return isAnimationRequired;
    }

    public void setIsAnimationRequired(Boolean isAnimationRequired) {
        this.isAnimationRequired = isAnimationRequired;
    }

}
