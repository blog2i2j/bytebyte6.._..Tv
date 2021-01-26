package com.bytebyte6.base

import android.widget.ImageView
import com.bumptech.glide.Glide

interface ImageClearHelper {
    val images: MutableSet<ImageView>
    fun clear()
}

class GlideClearHelper : ImageClearHelper {
    override val images: MutableSet<ImageView> = mutableSetOf()
    override fun clear() {
        images.forEach {
            Glide.with(it.context).clear(it)
        }
        images.clear()
    }
}