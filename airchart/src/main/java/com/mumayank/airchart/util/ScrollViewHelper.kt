package com.mumayank.airchart.util

import android.widget.LinearLayout
import android.widget.ScrollView

class ScrollViewHelper {
    companion object {

        fun isScrollViewScrollable(scrollView: ScrollView, child: LinearLayout): Boolean {
            return scrollView.height < child.height + scrollView.paddingTop + scrollView.paddingBottom
        }

    }
}