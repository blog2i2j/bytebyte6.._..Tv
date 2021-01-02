package com.bytebyte6.base

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(it: Message) {
    Toast.makeText(
        this,
        it.get(this),
        if (it.longDuration) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    ).show()
}

fun Fragment.showToast(it: Message) {
    requireContext().showToast(it)
}

fun Activity.showToast(it: Message) {
    baseContext.showToast(it)
}

fun Context.showSnack(
    view: View,
    it: Message,
    listener: View.OnClickListener?
) {
    val bar = Snackbar.make(
        view,
        it.get(this),
        if (it.longDuration) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
    )
    if (it.actionId != 0) {
        bar.setAction(it.actionId, listener)
    }
    bar.show()
}

fun Fragment.showSnack(
    view: View,
    it: Message,
    listener: View.OnClickListener?
) {
    requireContext().showSnack(view, it, listener)
}

fun Activity.showSnack(
    view: View,
    it: Message,
    listener: View.OnClickListener?
) {
    baseContext.showSnack(view, it, listener)
}