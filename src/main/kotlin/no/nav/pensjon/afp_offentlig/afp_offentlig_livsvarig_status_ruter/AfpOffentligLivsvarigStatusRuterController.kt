package no.nav.pensjon.afp_offentlig.afp_offentlig_livsvarig_status_ruter

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
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
                        tpNr = request.tpNr.apply { if (isEmpty()) { throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Må ha minst ett tpnr å sjekke") } },
                        onsketVirkningtidspunkt = request.onsketVirkningtidspunkt,
                    )
                )
            }
            ?: badRequest().body("X-Request-ID header må oppgis")
    }

    data class HentStatusRequest(
        val fnr: String,
        val tpNr: Set<Int>,
        val onsketVirkningtidspunkt: LocalDate,
        val hjemmel: String,
        val tema: String,
        val type: String,
    )
}
