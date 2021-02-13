package com.bytebyte6.data

import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.model.Category
import com.bytebyte6.data.model.Language
import okio.internal.commonToUtf8String
import java.io.File
import java.io.InputStream

object M3u {
    private const val logoPattern = "(?<=tvg-logo=\").*?(?=\")"
    private const val namePattern = "(?<=tvg-name=\").*?(?=\")"
    private const val languagePattern = "(?<=tvg-language=\").*?(?=\")"
    private const val countryPattern = "(?<=tvg-country=\").*?(?=\")"
    private const val categoryPattern = "(?<=group-title=\").*?(?=\")"
    private val logoRegex = Regex(logoPattern)
    private val nameRegex = Regex(namePattern)
    private val langRegex = Regex(languagePattern)
    private val countryRegex = Regex(countryPattern)
    private val categoryRegex = Regex(categoryPattern)

    fun getTvs(inputStream: InputStream): List<Tv> {
        val m3uString = inputStream.readBytes().commonToUtf8String()
        return getTvs(m3uString)
    }

    fun getTvs(m3uFile: File): List<Tv> {
        val m3uString = m3uFile.readBytes().commonToUtf8String()
        return getTvs(m3uString)
    }

    private fun getTvs(m3uString: String): MutableList<Tv> {
        val contains = m3uString.contains("tvg-id")
        val nameAndUrlList = if (contains) {
            m3uString.split("#EXTINF:-1 ").toMutableList()
        } else {
            m3uString.split("#EXTINF:-1 ,").toMutableList()
        }
        nameAndUrlList.removeAt(0)
        return if (contains) {
            getTvsByTvg(nameAndUrlList)
        } else {
            getTvsNormal(nameAndUrlList)
        }
    }

    private fun getTvsNormal(nameAndUrlList: MutableList<String>): MutableList<Tv> {
        val tvs = mutableListOf<Tv>()
        for (str in nameAndUrlList) {
            val nameAndUrl = str.split("\n")
            tvs.add(Tv(name = nameAndUrl[0], url = nameAndUrl[1]))
        }
        return tvs
    }

    private fun getTvsByTvg(nameAndUrlList: MutableList<String>): MutableList<Tv> {
        val tvs = mutableListOf<Tv>()
        for (str in nameAndUrlList) {
            val url = if (str.contains("#EXTVLCOPT")) {
                str.split("\n")[2]
            } else {
                str.split("\n")[1]
            }
            val logo = logoRegex.find(str)?.value ?: ""
            val name = nameRegex.find(str)?.value ?: str.split(",")[1]
            val lang = langRegex.find(str)?.value ?: ""
            val langList = if (lang.contains(";")) {
                val result = mutableListOf<Language>()
                val languages = lang.split(";")
                languages.forEach {
                    result.add(Language(it))
                }
                result
            } else {
                mutableListOf(Language(lang))
            }
            val country = countryRegex.find(str)?.value ?: Country.UNKOWN
            val category = categoryRegex.find(str)?.value ?: Category.OTHER
            tvs.add(
                Tv(
                    url = url,
                    logo = logo,
                    name = name,
                    language = langList,
                    country = Country(code = country),
                    category = category
                )
            )
        }
        return tvs
    }
}