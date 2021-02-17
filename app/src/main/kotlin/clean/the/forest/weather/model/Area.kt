package clean.the.forest.weather


typealias AreaName = String

data class Area(
    val name: AreaName,
    val position: GeoPos,
    val country: Country
) {
    constructor(name: AreaName, lat: Double, lon: Double, countryCode: String) :
            this(name, GeoPos(lat, lon), Country(countryCode))
}

data class GeoPos(
    val lat: Double,
    val lon: Double
)

data class Country(
    val code: String
)