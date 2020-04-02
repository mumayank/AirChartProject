
# AirChart
[![](https://jitpack.io/v/mumayank/AirChartProject.svg)](https://jitpack.io/#mumayank/AirChartProject)

## Setup

Add this line in your `root build.gradle` at the end of repositories:

```kotlin
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
  ```
Add this line in your `app build.gradle`:
```kotlin
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
dependencies {
  implementation 'com.github.mumayank:AirChartProject:LATEST_VERSION'
}
```
where LATEST_VERSION is [![](https://jitpack.io/v/mumayank/AirChartProject.svg)](https://jitpack.io/#mumayank/AirChartProject)

## Usage

Charts are nothing but graphical representation of the data. As Android app developers, most of the times, we get this data from the REST APIs. And due to the time taken in getting a response from an API, we need to show a loading UI to the app user. Thus, this library takes care of:

 - Show loading UI while the data loads
 - Allowing the dev to directly supply JSON received in the API response to show a chart
 - Allowing the dev to set chart parameters via code

___

### Set chart data from REST API
If your REST APIs are returning the correctly-formatted JSONObject for a chart, you can simply pass it to the library. The library will take care of the rest, making the Android developer's job almost negligible. In this case, you only have to include this one line of code:

```kotlin
AirChart(activity, viewGroup).showBarChart(jsonString)
```
Here, 

 - `activity` is the reference to the activity
 - `viewGroup` is the layout where you want the chart to appear
 - `jsonString` is the JSON received from the REST API

___

### Show loading UI
As stated above, if you want the library to show loading progress, you can simply split the declaration and the function call:

- Before making the REST API call, write this code:
```kotlin
		val airChart = AirChart(activity, viewGroup)
```

- After receiving response, write this code:
```kotlin
		airChart.showBarChart(jsonString)
```
___

### Set chart data using code

```kotlin
airChart.showBarChart(object: AirChartBar.IBar {  
  
	override fun getTitle(): String {  
	    return "title of the chart"  
	}  
  
	override fun getXAxisTitle(): String {  
	    return "this is X-axis"  
	}  
  
	override fun getXAxisLabels(): ArrayList<String> {  
	    return arrayListOf("Student1", "Student2", "Student3")  
	}  
  
	override fun getYLeftAxisTitle(): String {  
	    return "this is Y-axis"  
	}  
  
    override fun getYLeftAxisValues(): ArrayList<Value> {  
        return arrayListOf(  
            Value(  
                "marks in sem 1",  
                arrayListOf(50.0, 40.0, 49.5)  
            ),  
            Value(  
                "marks in sem 2",  
                arrayListOf(100.0, 30.0, 31.0)  
            )  
        )  
    }  
  
    override fun getColors(): ArrayList<String> {  
        return arrayListOf("#ffa726", "#2196f3")  
    }  
  
	/*  
	Additional optional functions that can be overridden on demand:     
	*/  
  
	override fun getSubTitle(): String? {  
	      return "this is subtitle of the chart"  
	}  
  
	override fun getDecimalFormatPattern(): String? {  
	    return "0.#"  
	}  
  
    override fun getAdditionalValues(): java.util.ArrayList<AdditionalValue>? {  
        return arrayListOf(  
            AdditionalValue("Key1", "Value1"),  
            AdditionalValue("Key2", "Value2"),  
            AdditionalValue("Key3", "Value3")  
        )  
    }  
  
	override fun getIsHorizontal(): Boolean {  
	    return false  
	}  
  
	override fun getCustomViewLayoutResId(): Int? {  
	    return R.layout.custom_view  
	}  
  
	override fun getIsAnimationRequired(): Boolean? {  
	    return true  
	}  
  
	override fun onValueSelected(e: Entry?) {  
	    // do something  
	}  
  
	override fun onNoValueSelected() {  
	    // do something  
	}  
  
})
```

Note:
 - Set `title` or `subTitle` as an empty string (`""`) to hide them from the view
 - If any `JSONArray` has no values, send an empty array (`[]`)
 - `getYLeftAxisValues` accepts an `arrayList` of `Value`. If you want a simple bar chart, put only 1 `Value` object in the `arrayList`. If you want a grouped bar chart, put multiple `Value` objects in the `arrayList`.
 - If you want a `HorizontalBarChart`, simply return `true` in `getIsHorizontal`

___

### JSON format (this info is for the backend dev)

    {
      "title": "Student records",
      "xAxisTitle": "Projects",
      "xAxisLabels": [
        "Narendra",
        "Rahul",
        "Prakash",
        "Sonia",
        "Lalu"
      ],
      "yLeftAxisTitle": "Progress Value",
      "yLeftAxisValues": [
        {
          "legendLabel": "Scope",
          "values": [
            24.3,
            12.1,
            13.5,
            12.7,
            17.8
          ]
        },
        {
          "legendLabel": "Taken up",
          "values": [
            13.1,
            5.9,
            2.4,
            9.3,
            5.3
          ]
        }
      ],
      "colors": [
        "#2196f3",
        "#ff9800"
      ],
      "subTitle": "Subtitle of the chart",
      "decimalFormatPattern": "0.0",
      "additionalValues": [
        {
          "key": "Key1",
          "value": "Value1"
        },
        {
          "key": "Key2",
          "value": "Value2"
        }
      ],
      "isHorizontal": false,
      "isAnimationRequired": true
    }

Note:

 - All fields are compulsory
 - Set `title` or `subTitle` as an empty string (`""`) to hide them from the view
 - If any `JSONArray` has no values, send an empty array (`[]`)

___
#### Java POJO classes of the above JSON
#### Bar.java

    import com.google.gson.annotations.Expose;  
    import com.google.gson.annotations.SerializedName;  
      
    import java.util.ArrayList;  
      
    public class Bar {  
      
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
        @SerializedName("isHorizontal")  
        @Expose  
      private Boolean isHorizontal;  
        @SerializedName("isAnimationRequired")  
        @Expose  
      private Boolean isAnimationRequired;  
      
        /**  
     * No args constructor for use in serialization */  public Bar() {  
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
      * @param isHorizontal  
      */  
      public Bar(String title, String xAxisTitle, ArrayList<String> xAxisLabels, String yLeftAxisTitle, ArrayList<Value> yLeftAxisValues, ArrayList<String> colors, String subTitle, String decimalFormatPattern, ArrayList<AdditionalValue> additionalValues, Boolean isHorizontal, Boolean isAnimationRequired) {  
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
            this.isHorizontal = isHorizontal;  
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
      
        public Boolean getIsHorizontal() {  
            return isHorizontal;  
        }  
      
        public void setIsHorizontal(Boolean isHorizontal) {  
            this.isHorizontal = isHorizontal;  
        }  
      
        public Boolean getIsAnimationRequired() {  
            return isAnimationRequired;  
        }  
      
        public void setIsAnimationRequired(Boolean isAnimationRequired) {  
            this.isAnimationRequired = isAnimationRequired;  
        }  
      
    }


#### Value.java

    import com.google.gson.annotations.Expose;  
    import com.google.gson.annotations.SerializedName;  
      
    import java.util.ArrayList;  
      
    public class Value {  
      
        @SerializedName("legendLabel")  
        @Expose  
      private String legendLabel;  
        @SerializedName("values")  
        @Expose  
      private ArrayList<Double> values = null;  
      
        /**  
     * No args constructor for use in serialization */  public Value() {  
        }  
      
        /**  
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

#### AdditionalValue.java

    import com.google.gson.annotations.Expose;  
    import com.google.gson.annotations.SerializedName;  
      
    public class AdditionalValue {  
      
        @SerializedName("key")  
        @Expose  
      private String key;  
        @SerializedName("value")  
        @Expose  
      private String value;  
      
        /**  
     * No args constructor for use in serialization */  public AdditionalValue() {  
        }  
      
        /**  
     * @param value  
      * @param key  
      */  
      public AdditionalValue(String key, String value) {  
            super();  
            this.key = key;  
            this.value = value;  
        }  
      
        public String getKey() {  
            return key;  
        }  
      
        public void setKey(String key) {  
            this.key = key;  
        }  
      
        public String getValue() {  
            return value;  
        }  
      
        public void setValue(String value) {  
            this.value = value;  
        }  
      
    }

___
Please star this repository if it has helped you :)
