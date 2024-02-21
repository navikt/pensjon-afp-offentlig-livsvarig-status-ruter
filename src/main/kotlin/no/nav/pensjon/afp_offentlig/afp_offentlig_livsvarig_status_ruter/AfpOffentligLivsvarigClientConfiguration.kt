package no.nav.pensjon.afp_offentlig.afp_offentlig_livsvarig_status_ruter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class AfpOffentligLivsvarigClientConfiguration(
    private val hentAfpStatusApiProperties: HentAfpStatusApiProperties,
) {
    @Bean
    fun afpOffentligStatusKlienter() = hentAfpStatusApiProperties.tilbydere.entries.associate { it.value.ordning to AfpOffentligLivsvarigClient(WebClient.builder(), it.value.url) }
}
