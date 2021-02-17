package clean.the.forest.weather.application

import clean.the.forest.weather.infraestructure.AreaRepository
import clean.the.forest.weather.model.Area
import spock.lang.Specification
import spock.lang.Unroll


class WeatherOfParticularAreaUseCaseSpec extends Specification {

    @Unroll
    def 'weather data of the location "#areaName" should be "#expectedWeather"'() {

        given:
        WeatherOfParticularAreaUseCase sut = new WeatherOfParticularAreaUseCase(new AreaRepository())

        when:
        def weatherReport = sut.report(areaName)

        then:
        weatherReport.area == new Area(areaName, expectedLat, expectedLon, expectedCountry)
        weatherReport.weatherCondition == expectedWeather

        where:
        areaName    || expectedLat | expectedLon | expectedCountry || expectedWeather
        "Ipiñaburu" || 43.07       | -2.75       | "ES"            || "Clouds"
        "Ibarra"    || 43.05       | -2.57       | "ES"            || "Clear"
        "Zegama"    || 42.97       | -2.29       | "ES"            || "Drizzle"
    }

}
