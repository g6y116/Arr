package kr.dongwon.arr.base

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun log(message: String) {
    Log.d("@@@", message)
}

fun toast(context: Context, message: Int) {
    Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
}

fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private fun getString(context: Context, str: Any): String {
    return when (str) {
        is String -> str
        is Int -> context.getString(str)
        else -> ""
    }
}

fun booleanToString(data: Boolean): String {
    return if (data) "Y" else "N"
}

fun stringToBoolean(data: String): Boolean {
    return data == "Y"
}

fun getNowTimeStamp(): String {
    return SimpleDateFormat("yyyyMMddhhmmss", Locale.KOREA).apply {
        timeZone = TimeZone.getTimeZone("KST")
    }.format(Date())
}

fun showAlert(
    context: Context,
    title: Any? = null,
    message: Any? = null,
    positive: Pair<Any, () -> Any>? = null,
    negative: Pair<Any, () -> Any>? = null,
    isCancelable: Boolean = false,
) {
    AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth).apply {
        if (title != null) setTitle(getString(context, title))
        if (message != null) setMessage(getString(context, message))
        if (positive != null) setPositiveButton(getString(context, positive.first)) { _, _ -> positive.second() }
        if (negative != null) setNegativeButton(getString(context, negative.first)) { _, _ -> negative.second() }
        setCancelable(isCancelable)
        setOnDismissListener { }
    }.show()
}