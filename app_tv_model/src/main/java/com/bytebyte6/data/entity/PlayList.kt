package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bytebyte6.base.randomColor
import com.bytebyte6.data.model.Card
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

@Parcelize
@Entity(indices = [Index(value = ["playlistName"], unique = true)])
@Keep
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0,
    var playlistName: String
) : Parcelable, Card {

    override val title: String
        get() = playlistName

    override val body: String
        get() = "playlistId: $playlistId"

    override val outline: Boolean
        get() = Random.Default.nextBoolean()

    override val color: Int
        get() = randomColor()

    override val radius: Int
        get() = 0
}