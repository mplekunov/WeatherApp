package com.application.weatherapp.model.weather.statistics

class Direction(
    val value: Float,
    val unit: DirectionUnit
)

enum class DirectionUnit {
    DEGREES,
    NONE
}

//override fun toString(): String {
//    return when(unit) {
//        Directions.NONE -> "No Wind"
//        Directions.NORTH -> "North"
//        Directions.EAST -> "East"
//        Directions.NORTH_EAST -> "North-East"
//        Directions.NORTH_WEST -> "North-West"
//        Directions.SOUTH -> "South"
//        Directions.SOUTH_EAST -> "South-East"
//        Directions.SOUTH_WEST -> "South-West"
//        Directions.WEST -> "West"
//    }
//
//
//}

enum class Directions {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST,
    NONE
}