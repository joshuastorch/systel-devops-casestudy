package com.example.demo.data

import java.io.File
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.atan2
import kotlin.math.sqrt

data class Bahnhof(
    val DS100: String,
    val name: String,
    val long: Double,
    val lat: Double
) {
    /**
     * Calculate the distance between this station and another one by their geo coordinates
     */
    fun calculateDistance(other: Bahnhof): Double {
        // http://www.movable-type.co.uk/scripts/latlong.html

        val phi1 = lat * PI / 180
        val phi2 = other.lat * PI / 180
        val deltaPhi = (lat - other.lat) * PI / 180
        val deltaLambda = (long - other.long) * PI / 180

        val a = sin(deltaPhi/2) * sin(deltaPhi/2) + cos(phi1) * cos(phi2) * sin(deltaLambda/2) * sin(deltaLambda/2)
        val c = 2 * atan2(sqrt(a), sqrt(1-a))

        // circumference of the earth
        val r = 6371e3

        return c * r
    }

    companion object {
        val stations: List<Bahnhof> = File("D_Bahnhof_2020_alle.CSV").readText(Charsets.UTF_8).let { data ->
            val lines = data.split("\n")
            val header = lines.first().split(";")
            val indexDS100 = header.indexOf("DS100")
            val indexLat = header.indexOf("Breite")
            val indexLong = header.indexOf("Laenge")
            val indexName = header.indexOf("NAME")
            val indexVerkehr = header.indexOf("Verkehr")

            lines.subList(1, lines.size).mapNotNull { line ->
                val csvFields = line.split(";")
                if (csvFields.size < header.size) {
                    return@mapNotNull null
                }
                if (csvFields[indexVerkehr] == "FV") {
                    Bahnhof(
                        csvFields[indexDS100],
                        csvFields[indexName],
                        csvFields[indexLong].replace(",", ".").toDouble(),
                        csvFields[indexLat].replace(",", ".").toDouble()
                    )
                } else {
                    null
                }
            }
        }
    }
}
