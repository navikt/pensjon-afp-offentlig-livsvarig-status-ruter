package no.nav.pensjon.afp_offentlig.afp_offentlig_livsvarig_status_ruter

import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AfpOffentligLivsvarigService(
    private val sisteOrdningClient: SisteOrdningClient,
    private val clientMap: Map<Int, AfpOffentligLivsvarigClient>,
) {
    suspend fun hentAfpOffentligLivsvarigStatus(fnr: String, onsketVirkningtidspunkt: LocalDate): HentStatusResponse {
        val sisteOrdning: Int? = sisteOrdningClient.soekSisteOrdning(fnr)

        return if (sisteOrdning != null) {
            val client = clientMap[sisteOrdning]
            if (client != null) {
                val hentAfpStatus = client
                    .hentAfpStatus(fnr, onsketVirkningtidspunkt, sisteOrdning)

                when (hentAfpStatus.statusAfp) {
                    "SOKT" -> return HentStatusResponse(
                        soknad = SoknadDto(hentAfpStatus.virkningsdato)
                    )

                    "INNVILGET" -> return HentStatusResponse(
                        innvilget = InnvilgetDto(
                            belop = hentAfpStatus.belop,
                            tpNummer = hentAfpStatus.tpId.toInt(),
                            startdato = hentAfpStatus.virkningsdato,
                            sistRegulert = hentAfpStatus.datoSistRegulert,
                        )
                    )

                    "AVSLAG", "IKKE_SOKT" -> return HentStatusResponse()
                    else -> throw IllegalArgumentException("Fikk ukjent status ${hentAfpStatus.statusAfp}")
                }
            } else {
                HentStatusResponse(
                    manglendeApi = ManglendeApiDto(),
                )
            }
        } else {
            HentStatusResponse(
                manglerSisteOrdning = ManglerSisteOrdningDto()
            )
        }
    }

    data class HentStatusResponse(
        val innvilget: InnvilgetDto? = null,
        val soknad: SoknadDto? = null,
        val manglendeApi: ManglendeApiDto? = null,
        val manglerSisteOrdning: ManglerSisteOrdningDto? = null,
    )


    data class InnvilgetDto(
        val belop: Int,
        val tpNummer: Int,
        val startdato: LocalDate,
        val sistRegulert: LocalDate,
    )

    data class SoknadDto(
        val onsketVirkningsdato: LocalDate,
    )

    class ManglendeApiDto
    class ManglerSisteOrdningDto

}

