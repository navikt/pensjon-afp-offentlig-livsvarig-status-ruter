package no.nav.pensjon.afp_offentlig.afp_offentlig_livsvarig_status_ruter

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/afp-offentlig-status")
class AfpOffentligLivsvarigStautsRuterController(
    private val service: AfpOffentligLivsvarigService
) {

    @PostMapping("/status")
    fun hentAfpOffentligStatus(@RequestBody request: HentStatusRequest): HentStatusResponse {
        service.hentAfpOffentligLivsvarigStatus(
            fnr = request.fnr,
            onsketVirkningtidspunkt = request.onsketVirkningtidspunkt
        )
        return HentStatusResponse(null, SoknadDto(LocalDate.now()),null)
    }

    data class HentStatusRequest(
        val fnr: String,
        val onsketVirkningtidspunkt: LocalDate,
        val hjemmel: String,
        val tema: String,
        val type: String,
    )
    data class HentStatusResponse(
        val innvilget: InnvilgetDto?,
        val soknad: SoknadDto?,
        val manglendeApi: ManglendeApiDto?,
    )

    data class InnvilgetDto(
        val belop: Int,
        val tpNummer: Int,
        val startdato: LocalDate,
        val sluttdato: LocalDate?,
        val sistRegulert: LocalDate,
    )

    data class SoknadDto(
        val onsketVirkningsdato: LocalDate,
    )

    data class ManglendeApiDto(
        val onsketVirkningsdato: LocalDate
    )
}