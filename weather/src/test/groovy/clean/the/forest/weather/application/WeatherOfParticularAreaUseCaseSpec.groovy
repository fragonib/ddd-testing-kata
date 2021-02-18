package clean.the.forest.weather.application

import clean.the.forest.weather.infraestructure.AreaRepository
import clean.the.forest.weather.infraestructure.WeatherProvider
import clean.the.forest.weather.model.Area
import clean.the.forest.weather.model.GeoPos
import clean.the.forest.weather.shared.TestClassification
import org.junit.jupiter.api.Tag
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Unroll


@Tag(TestClassification.UNIT)
class WeatherOfParticularAreaUseCaseSpec extends Specification {

    @Unroll
    def 'weather data of the location named "#areaName" should be "#expectedWeather"'() {

        given: "External weather provider is stubbed"
        AreaRepository areaRepository = new AreaRepository()

        WeatherProvider weatherProvider = Stub()
        weatherProvider.reportWeatherByGeoPos(new GeoPos(expectedLat, expectedLon)) >> Mono.just(expectedWeather)

        WeatherOfParticularAreaUseCase sut =
                new WeatherOfParticularAreaUseCase(areaRepository, weatherProvider)

        when: "request weather by area name"
        def weatherReport = sut.report(areaName).block()

        then: "repost should has expected area and weather condition"
        weatherReport.area == new Area(areaName, expectedLat, expectedLon, expectedCountry)
        weatherReport.weatherCondition == expectedWeather

        where:
        areaName    || expectedLat | expectedLon | expectedCountry | expectedWeather
        "Ipiñaburu" || 43.07       | -2.75       | "ES"            | "Clouds"
        "Ibarra"    || 43.05       | -2.57       | "ES"            | "Clear"
        "Zegama"    || 42.97       | -2.29       | "ES"            | "Drizzle"
    }

}
