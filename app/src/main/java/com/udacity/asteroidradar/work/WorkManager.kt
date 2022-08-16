package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Room.getInstance
import com.udacity.asteroidradar.repository.Repository
import retrofit2.HttpException

class RefreshDataWork(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    companion object {
        const val WORKER_NAME = "RefreshDataWork"
    }
    override suspend fun doWork(): Result {
        val database = getInstance(applicationContext)
        val repository = Repository(database)

        return try {
            repository.refreshAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}

