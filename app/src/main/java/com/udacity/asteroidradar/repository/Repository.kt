package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.network.NetworkService
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.Room.AsteroidsDatabase
import com.udacity.asteroidradar.Room.asDatabaseModel
import com.udacity.asteroidradar.Room.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate

class Repository(private val database: AsteroidsDatabase) {
    @RequiresApi(Build.VERSION_CODES.O)
    private val start = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val end = LocalDate.now().plusDays(7)
    // from DB
    val asteroids:LiveData<List<Asteroid>> = Transformations.map(database.AsteroidsDao.getAllAsteroid()){
        it.asDomainModel()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    val todayAsteroid:LiveData<List<Asteroid>> = Transformations.map(database.AsteroidsDao.getToday(start.toString())){
        it.asDomainModel()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    val weeklyAsteroid:LiveData<List<Asteroid>> = Transformations.map(database.AsteroidsDao.getWeek(
        start.toString(),
        end.toString()
    )){
        it.asDomainModel()
    }



    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
            // insert Data
                val asteroids = NetworkService.AsteroidsService.getAsteroids(API_KEY)
                val listResult = parseAsteroidsJsonResult(JSONObject(asteroids))
                database.AsteroidsDao.insertAll(*listResult.asDatabaseModel())
            } catch (e: Exception) {
            }
        }
    }
}