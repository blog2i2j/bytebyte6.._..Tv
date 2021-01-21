package com.bytebyte6.usecase.work

import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.image.SearchImage

class TvLogoSearch(private val dao: TvDao,private val searchImage: SearchImage)  {
    fun doThatShit() {
        dao.getTvs()
            .filter {
                it.logo.isEmpty()
            }
            .forEach {
                if (it.name.isNotEmpty()) {
                    val logo = searchImage.search(it.name)
                    if (logo.isNotEmpty()) {
                        it.logo = logo
                        dao.insert(it)
                    }
                }
            }
    }
}