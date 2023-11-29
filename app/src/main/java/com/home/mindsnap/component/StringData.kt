package com.home.mindsnap.component

import android.content.Context

class StringData(private val resID : Int = -1, val message : String?, private vararg val param : String) {

    fun isResourceMessage() : Boolean {
        return resID != -1
    }

    fun getResourceMessage(context: Context) : String {
        return if (!isResourceMessage())
            ""
        else
            if (param.isEmpty()) {
                context.getString(resID)
            }
            else {
                context.getString(resID, param)
            }
    }
}