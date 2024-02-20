package no.nav.pensjon.afp_offentlig.afp_offentlig_livsvarig_status_ruter

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class AfpOffentligLivsvarigClient {

    fun hentSisteOrdning(fnr: String) : String {
        return "3010" //todo: returner bare tp-nr?
    }

    fun hentAfpStatusSPK(fnr: String, uttaksdato: LocalDate, sisteOrdning: String) {

    }
    fun hentAfpStatusKLP(fnr: String, uttaksdato: LocalDate, sisteOrdning: String) {

    }
    fun hentAfpStatusOPF(fnr: String, uttaksdato: LocalDate, sisteOrdning: String) {

    }
    fun hentAfpStatusGABLER(fnr: String, uttaksdato: LocalDate, sisteOrdning: String) {

    }

}