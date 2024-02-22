package no.nav.pensjon.afp_offentlig.afp_offentlig_livsvarig_status_ruter

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody
import java.util.*

@Component
class SisteOrdningClient(
    webClientBuilder: WebClient.Builder,
    @Value("\${sisteOrdning.url}") baseUrl: String,
) {
    private val webClient = webClientBuilder
        .baseUrl(baseUrl)
        .build()

    suspend fun soekSisteOrdning(xRequestId: String, fnr: String): Int? =
        try {
            webClient.post()
                .uri("/soek")
                .header("X-CORRELATION-ID", xRequestId)
                .bodyValue(SisteOrdningSoekRequest(fnr = fnr))
                .retrieve()
                .awaitBody<SisteOrdningSoekResponse>()
                .tpnr
        } catch (e: WebClientResponseException) {
            if (e.statusCode.isSameCodeAs(HttpStatus.NOT_FOUND)) {
                null
            } else {
                throw e
            }
        }

    data class SisteOrdningSoekRequest(
        val fnr: String,
    )

    data class SisteOrdningSoekResponse(
        val tpnr: Int,
    )
}
