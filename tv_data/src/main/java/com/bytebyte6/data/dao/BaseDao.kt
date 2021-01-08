package com.bytebyte6.data.dao

import androidx.room.*

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: Set<T>): List<Long>

    @Delete
    fun delete(data: T)

    @Delete
    fun delete(list: List<T>)

    @Update
    fun update(data: T)

    @Update
    fun update(list: List<T>)
}