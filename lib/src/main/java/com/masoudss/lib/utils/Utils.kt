package com.masoudss.lib.utils

import android.content.Context
import android.util.TypedValue
import android.util.TypedValue.*

object Utils {

    @JvmStatic
    fun dp(context: Context?, dp: Int): Float {
        return applyDimension(COMPLEX_UNIT_DIP, dp.toFloat(), context!!.resources.displayMetrics)
    }

}