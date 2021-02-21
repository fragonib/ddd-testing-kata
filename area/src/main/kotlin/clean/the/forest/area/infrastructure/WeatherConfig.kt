package clean.the.forest.area.infrastructure

import clean.the.forest.area.application.ReportCheckabilityOfParticularAreaUseCase
import clean.the.forest.area.application.ReportWeatherOfAllKnownAreasUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class WeatherConfig {

    @Bean
    fun inMemoryAreaRepository(): InMemoryAreaRepository {
        return InMemoryAreaRepository()
    }

    @Bean
    fun openWeatherProvider(@Value("\${OPENWEATHER_APIKEY}") apiKey: String): WeatherProvider {
        return OpenWeatherProvider(apiKey)
    }

    @Bean
    fun reportOfAllKnownAreasUseCase(
        areaRepository: AreaRepository,
        weatherProvider: WeatherProvider
    ): ReportWeatherOfAllKnownAreasUseCase {
        return ReportWeatherOfAllKnownAreasUseCase(areaRepository, weatherProvider)
    }

    @Bean
    fun reportOfParticularAreaUseCase(
        areaRepository: AreaRepository,
        weatherProvider: WeatherProvider
    ): ReportCheckabilityOfParticularAreaUseCase {
        return ReportCheckabilityOfParticularAreaUseCase(areaRepository, weatherProvider)
    }

}