package no.nav.pensjon.afp_offentlig.afp_offentlig_livsvarig_status_ruter

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/afp-offentlig-status")
class AfpOffentligLivsvarigStatusRuterController(
    private val service: AfpOffentligLivsvarigService
) {
    @PostMapping("/status")
    suspend fun hentAfpOffentligStatus(
        @RequestBody request: HentStatusRequest,
        @RequestHeader headers: Map<String, String>
    ): ResponseEntity<Any> {
        return headers["X-Request-ID"]
            ?.let { xRequestId ->
                ok(
                    service.hentAfpOffentligLivsvarigStatus(
                        xRequestId = xRequestId,
                        fnr = request.fnr,
                        onsketVirkningtidspunkt = request.onsketVirkningtidspunkt,
                    )
                )
            }
            ?: badRequest().body("X-Request-ID header må oppgis")
    }

    data class HentStatusRequest(
        val fnr: String,
        val onsketVirkningtidspunkt: LocalDate,
        val hjemmel: String,
        val tema: String,
        val type: String,
    )
}
