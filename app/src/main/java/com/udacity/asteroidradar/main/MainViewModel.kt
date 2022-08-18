package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.network.AsteroidApiFilter
import com.udacity.asteroidradar.network.NetworkService
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.Room.getInstance
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class MainViewModel(application: Application) : AndroidViewModel(application)  {

    private val database = getInstance(application)
    private val repository = Repository(database)


    private val _image = MutableLiveData<PictureOfDay>()
    val image: LiveData<PictureOfDay>
        get() = _image

    private var _filterAsteroidDate = MutableLiveData(AsteroidApiFilter.SAVED)


    @RequiresApi(Build.VERSION_CODES.O)
    val asteroidListing = Transformations.switchMap(_filterAsteroidDate) {
        when (it) {
            AsteroidApiFilter.WEEK -> repository.weeklyAsteroid
            AsteroidApiFilter.TODAY -> repository.todayAsteroid
            else -> repository.asteroids
        }
    }
    init {
        viewModelScope.launch {
            repository.refreshAsteroids()
            PictureOfDay()

        }
//        changeFilter(AsteroidApiFilter.WEEK)

    }

    private val _goToDetails = MutableLiveData<Asteroid?>()

    val goToDetails: MutableLiveData<Asteroid?>
        get() = _goToDetails

    fun changeFilter(filter: AsteroidApiFilter) {
        this._filterAsteroidDate.value = filter
    }

    fun showAsteroidDetails(asteroid: Asteroid) {
        _goToDetails.value = asteroid
    }

    fun showAsteroidDetailsComplete() {
        _goToDetails.value = null
    }

    private fun PictureOfDay() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            try {
                _image.postValue(
                    NetworkService.AsteroidsService.getPictureOfTheDay(API_KEY)
                )
            } catch (err: Exception) {
            }
        }
    }

}