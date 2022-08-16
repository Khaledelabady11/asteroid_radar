package com.udacity.asteroidradar.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface Dao {

    @Query("SELECT * FROM databaseasteroid ORDER BY closeApproachDate Asc")
    fun getAllAsteroid(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM databaseasteroid WHERE closeApproachDate = :date ORDER BY closeApproachDate ASC")
    fun getToday(date:String):LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM databaseasteroid WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate DESC")
    fun getWeek(startDate:String,endDate:String):LiveData<List<DatabaseAsteroid>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: DatabaseAsteroid)
}