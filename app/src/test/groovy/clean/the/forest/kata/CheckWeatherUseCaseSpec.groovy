package clean.the.forest.kata

import spock.lang.Specification
import spock.lang.Unroll


class CheckWeatherUseCaseSpec extends Specification {

    @Unroll
    def 'weather data of the location "#areaName" should be "#expectedWeather"'() {

        given:
        CheckWeatherUseCase sut = new CheckWeatherUseCase()

        when:
        def area = new Area(areaName, new GeoPos(lat, lon), new Country(countryCode))
        def weatherReport = sut.weatherReport(area)

        then:
        weatherReport.area == area
        weatherReport.weather == expectedWeather

        where:
        areaName    | lat   | lon   | countryCode || expectedWeather
        "Ipiñaburu" | 43.07 | -2.75 | "ES"        || "Clouds"
        "Ibarra"    | 43.05 | -2.57 | "ES"        || "Clear"
        "Zegama"    | 42.97 | -2.29 | "ES"        || "Drizzle"
    }

}
