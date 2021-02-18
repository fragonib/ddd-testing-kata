package clean.the.forest.weather.infrastructure.config

import clean.the.forest.weather.application.WeatherOfParticularAreaUseCase
import clean.the.forest.weather.infrastructure.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value


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
    fun weatherOfParticularAreaUseCase(
        areaRepository: AreaRepository, weatherProvider: WeatherProvider
    ): WeatherOfParticularAreaUseCase {
        return WeatherOfParticularAreaUseCase(areaRepository, weatherProvider)
    }

}