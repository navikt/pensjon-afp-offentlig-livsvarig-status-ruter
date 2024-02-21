package no.nav.pensjon.afp_offentlig.afp_offentlig_livsvarig_status_ruter

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody

@Component
class SisteOrdningClient(
    webClientBuilder: WebClient.Builder,
    @Value("\${sisteOrdning.url}") baseUrl: String,
) {
    private val webClient = webClientBuilder
        .baseUrl(baseUrl)
        .build()

    suspend fun soekSisteOrdning(fnr: String): Int? {
        try {
            return webClient.post()
                .uri("/soek")
                .bodyValue(SisteOrdningSoekRequest(fnr = fnr))
                .retrieve()
                .awaitBody<SisteOrdningSoekResponse>()
                .tpnr
        } catch (e: WebClientResponseException) {
            if (e.statusCode.isSameCodeAs(HttpStatus.NOT_FOUND)) {
                return null
            } else {
                throw e
            }
        }
    }

    data class SisteOrdningSoekRequest(
        val fnr: String,
    )

    data class SisteOrdningSoekResponse(
        val tpnr: Int,
    )
}
