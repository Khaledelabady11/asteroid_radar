package com.udacity.asteroidradar.Network

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Room.DatabaseAsteroid

data class NetworkAsteroid(val id: Long,
                           val codename: String,
                           val closeApproachDate: String,
                           val absoluteMagnitude: Double,
                           val estimatedDiameter: Double,
                           val relativeVelocity: Double,
                           val distanceFromEarth: Double,
                           val isPotentiallyHazardous: Boolean)
fun ArrayList<Asteroid>.asDatabaseAsteroid(): Array<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid(
            it.id,
            it.codename,
            it.closeApproachDate,
            it.absoluteMagnitude,
            it.estimatedDiameter,
            it.relativeVelocity,
            it.distanceFromEarth,
            it.isPotentiallyHazardous
        )
    }.toTypedArray()

}