package clean.the.forest.weather.infrastructure

import clean.the.forest.weather.application.ReportParticularAreaUseCase
import clean.the.forest.weather.application.ReportAllKnownAreasUseCase
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
    ): ReportAllKnownAreasUseCase {
        return ReportAllKnownAreasUseCase(areaRepository, weatherProvider)
    }

    @Bean
    fun reportOfParticularAreaUseCase(
        areaRepository: AreaRepository,
        weatherProvider: WeatherProvider
    ): ReportParticularAreaUseCase {
        return ReportParticularAreaUseCase(areaRepository, weatherProvider)
    }

}