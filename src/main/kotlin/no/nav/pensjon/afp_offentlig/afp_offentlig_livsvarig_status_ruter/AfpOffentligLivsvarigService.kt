package no.nav.pensjon.afp_offentlig.afp_offentlig_livsvarig_status_ruter

import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AfpOffentligLivsvarigService(
    private val clientMap: Map<Int, AfpOffentligLivsvarigClient>,
) {
    suspend fun hentAfpOffentligLivsvarigStatus(xRequestId: String, fnr: String, tpNr: Set<Int>, onsketVirkningtidspunkt: LocalDate): HentStatusResponse {
        return tpNr.map { tpNr ->
            val client = clientMap[tpNr]
            if (client != null) {
                val hentAfpStatus = client
                    .hentAfpStatus(
                        xRequestId = xRequestId,
                        fnr = fnr,
                        uttaksdato = onsketVirkningtidspunkt,
                        tpnummer = tpNr,
                    )

                when (hentAfpStatus.statusAfp) {
                    "SOKT" -> return HentStatusResponse(
                        soknad = SoknadDto(
                            hentAfpStatus.virkningsdato
                                ?: throw IllegalArgumentException("Svar fra ordning mangler 'virkningsdato' for afp offentlig søknad"),
                            tpNummer = hentAfpStatus.tpId?.toInt()
                                ?: throw IllegalArgumentException("Svar fra ordning mangler 'tpId' for afp offentlig søknad"),
                        ),
                    )

                    "INNVILGET" -> return HentStatusResponse(
                        innvilget = InnvilgetDto(
                            belop = hentAfpStatus.belop
                                ?: throw IllegalArgumentException("Svar fra ordning mangler 'belop' for innvilget afp offentlig"),
                            tpNummer = hentAfpStatus.tpId?.toInt()
                                ?: throw IllegalArgumentException("Svar fra ordning mangler 'tpId' for innvilget afp offentlig"),
                            startdato = hentAfpStatus.virkningsdato
                                ?: throw IllegalArgumentException("Svar fra ordning mangler 'virkningsdato' for innvilget afp offentlig"),
                            sistRegulert = hentAfpStatus.datoSistRegulert
                                ?: throw IllegalArgumentException("Svar fra ordning mangler 'datoSistRegulert' for innvilget afp offentlig"),
                        )
                    )

                    "AVSLAG", "IKKE_SOKT" -> return HentStatusResponse()
                    else -> throw IllegalArgumentException("Fikk ukjent status ${hentAfpStatus.statusAfp}")
                }
            } else {
                HentStatusResponse(
                    manglendeApi = ManglendeApiDto(
                        tpNummer = tpNr,
                    ),
                )
            }
        }.run {
            firstOrNull { it.innvilget != null }
                ?: firstOrNull { it.soknad != null }
                ?: firstOrNull { it.manglendeApi != null }
                ?: first()
        }
    }

    data class HentStatusResponse(
        val innvilget: InnvilgetDto? = null,
        val soknad: SoknadDto? = null,
        val manglendeApi: ManglendeApiDto? = null,
    )


    data class InnvilgetDto(
        val belop: Int,
        val tpNummer: Int,
        val startdato: LocalDate,
        val sistRegulert: LocalDate,
    )

    data class SoknadDto(
        val onsketVirkningsdato: LocalDate,
        val tpNummer: Int,
    )

    @Suppress("unused")
    class ManglendeApiDto(
        val tpNummer: Int,
    )
}

