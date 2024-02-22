package no.nav.pensjon.afp_offentlig.afp_offentlig_livsvarig_status_ruter

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.time.LocalDate

class AfpOffentligLivsvarigClient(
    webClientBuilder: WebClient.Builder,
    private val baseUrl: String,
) {
    private val webClient: WebClient = webClientBuilder.baseUrl(baseUrl).build()

    suspend fun hentAfpStatus(xRequestId: String, fnr: String, uttaksdato: LocalDate, tpnummer: Int) =
        webClient.get()
            .uri {
                it.path("/hentAfpStatus/{fnr}")
                    .queryParam("uttaksdato", uttaksdato)
                    .build(fnr)
            }
            .header("X-CORRELATION-ID", xRequestId)
            .header("X-TPID", tpnummer.toString())
            .retrieve()
            .awaitBody<HentAfpStatusResponse>()

    override fun toString(): String {
        return "AfpOffentligLivsvarigClient(baseUrl='$baseUrl')"
    }

    data class HentAfpStatusResponse(
        val tpId: String?,
        val fnr: String?,
        val statusAfp: String?,
        val virkningsdato: LocalDate?,
        val belop: Int?,
        val datoSistRegulert: LocalDate?,
    )
}
