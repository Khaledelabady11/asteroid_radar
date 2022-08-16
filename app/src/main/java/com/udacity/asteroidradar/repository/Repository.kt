package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Network.ServiceApi
import com.udacity.asteroidradar.Network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.Room.AsteroidsDatabase
import com.udacity.asteroidradar.Room.asDatabaseModel
import com.udacity.asteroidradar.Room.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    val todayAsteroid:LiveData<List<Asteroid>> = Transformations.map(database.AsteroidsDao.getToday(start.format(
        DateTimeFormatter.ISO_DATE))){
        it.asDomainModel()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    val weeklyAsteroid:LiveData<List<Asteroid>> = Transformations.map(database.AsteroidsDao.getWeek(
        start.format(DateTimeFormatter.ISO_DATE),
        end.format(DateTimeFormatter.ISO_DATE)
    )){
        it.asDomainModel()
    }
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
              //  val today = Calendar.getInstance()
               // val afterSevenDays = Calendar.getInstance().also { it.add(Calendar.DAY_OF_YEAR,7) }
                val asteroids = ServiceApi.NetworkService.AsteroidsService.getAsteroids(API_KEY)
                val listResult = parseAsteroidsJsonResult(JSONObject(asteroids))
                database.AsteroidsDao.insertAll(*listResult.asDatabaseModel())
            } catch (e: Exception) {
            }
        }
    }
}