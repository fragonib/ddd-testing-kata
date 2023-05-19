package clean.the.forest.area.application

import clean.the.forest.area.infrastructure.AreaRepository
import clean.the.forest.area.infrastructure.WeatherProvider
import clean.the.forest.area.model.Area
import clean.the.forest.area.model.GeoPos
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll


class ReportWeatherOfParticularAreaUseCaseTest extends Specification {

    AreaRepository areaRepository = Mock()
    WeatherProvider weatherProvider = Mock()

    @Subject
    def sut = new ReportWeatherOfParticularAreaUseCase(areaRepository, weatherProvider)

    @Unroll
    def 'checkability status of area "#areaName" should be "#expectedCheckability"'() {

        given: "External dependencies stubbed"
        areaRepository.findByName(areaName) >> Mono.just(
                new Area(areaName, expectedLat, expectedLon, expectedCountry)
        )

        weatherProvider.byGeoPosition(new GeoPos(expectedLat, expectedLon)) >> Mono.just(expectedWeather)

        when: "request weather by area name"
        def weatherReport = sut.report(areaName).block()

        then: "repost should has expected area and weather condition"
        weatherReport.area == new Area(areaName, expectedLat, expectedLon, expectedCountry)
        weatherReport.weatherCondition == expectedWeather

        where:
        areaName      || expectedLat | expectedLon | expectedCountry | expectedWeather | expectedCheckability
        "Calderona"   || 39.67       | -0.43       | "ES"            | "Clouds"        | false
        "Mariola"     || 38.72       | -0.53       | "ES"            | "Clear"         | true
        "Penyagolosa" || 40.23       | -0.29       | "ES"            | "Drizzle"       | false

    }

}
