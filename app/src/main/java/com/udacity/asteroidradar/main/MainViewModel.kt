package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Network.AsteroidApiFilter
import com.udacity.asteroidradar.Network.ServiceApi
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.Room.getInstance
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


enum class AsteroidStatus { LOADING, DONE, ERROR }

class MainViewModel(application: Application) : AndroidViewModel(application)  {
    private val database = getInstance(application)
    private val filter = MutableLiveData(AsteroidApiFilter.TODAY)
    private val repository = Repository(database)
  //  private val asteroidFilterType = MutableLiveData<AsteroidApiFilter>()
    private val _image = MutableLiveData<PictureOfDay>()
    val image: LiveData<PictureOfDay>
        get() = _image

    private var _filterAsteroidDate = MutableLiveData(AsteroidApiFilter.TODAY)


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
        changeFilter(AsteroidApiFilter.TODAY)

    }

    private val _goToDetails = MutableLiveData<Asteroid?>()

    val goToDetails: MutableLiveData<Asteroid?>
        get() = _goToDetails


    fun showAsteroidDetails(asteroid: Asteroid) {
        _goToDetails.value = asteroid
    }

    fun showAsteroidDetailsComplete() {
        _goToDetails.value = null
    }
    fun changeFilter(filter: AsteroidApiFilter) {
        this.filter.value = filter
    }
    private fun PictureOfDay() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            try {
                _image.postValue(
                    ServiceApi.NetworkService.AsteroidsService.getPictureOfTheDay(API_KEY)
                )
            } catch (err: Exception) {
                Log.e("refreshPictureOfDay", err.printStackTrace().toString())
            }
        }
    }

}