package clean.the.forest.area.application

import clean.the.forest.area.infrastructure.AreaRepository
import clean.the.forest.area.infrastructure.WeatherProvider
import clean.the.forest.area.model.Area
import clean.the.forest.area.model.GeoPos
import clean.the.forest.shared.testing.TestClassification
import org.junit.jupiter.api.Tag
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Unroll


@Tag(TestClassification.UNIT)
class ReportCheckabilityOfParticularAreaUseCaseTest extends Specification {

    @Unroll
    def 'checkability status of area "#areaName" should be "#expectedCheckability"'() {

        given: "External dependencies stubbed"
        AreaRepository areaRepository = Stub()
        areaRepository.findByName(areaName) >> Mono.just(
                new Area(areaName, expectedLat, expectedLon, expectedCountry)
        )

        WeatherProvider weatherProvider = Stub()
        weatherProvider.reportWeatherByGeoPos(new GeoPos(expectedLat, expectedLon)) >> Mono.just(expectedWeather)

        when: "request weather by area name"
        def sut = new ReportCheckabilityOfParticularAreaUseCase(areaRepository, weatherProvider)
        def weatherReport = sut.report(areaName).block()

        then: "repost should has expected area and weather condition"
        weatherReport.area == new Area(areaName, expectedLat, expectedLon, expectedCountry)
        weatherReport.weatherCondition == expectedWeather

        where:
        areaName    || expectedLat | expectedLon | expectedCountry | expectedWeather | expectedCheckability
        "Ipiñaburu" || 43.07       | -2.75       | "ES"            | "Clouds"        | false
        "Ibarra"    || 43.05       | -2.57       | "ES"            | "Clear"         | true
        "Zegama"    || 42.97       | -2.29       | "ES"            | "Drizzle"       | false

    }

}
