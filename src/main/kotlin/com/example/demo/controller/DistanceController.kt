package com.example.demo.controller

import com.example.demo.data.Bahnhof
import com.example.demo.data.ResponseData
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import kotlin.math.round

@RestController
@RequestMapping("/api/v1")
class DistanceController {
    /**
     * Calculate the distance between two stations identified by their DS100
     */
    @GetMapping("/distance/{from}/{to}")
    fun distance(@PathVariable from: String, @PathVariable to: String): ResponseData {
        val startStation = Bahnhof.stations.find { it.DS100 == from } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val destStation = Bahnhof.stations.find { it.DS100 == to } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val distance = startStation.calculateDistance(destStation)

        return ResponseData(startStation.name, destStation.name, round(distance/1000).toLong(), "km")
    }
}
