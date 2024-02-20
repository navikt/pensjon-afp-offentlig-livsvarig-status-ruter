package no.nav.pensjon.afp_offentlig.afp_offentlig_livsvarig_status_ruter

import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AfpOffentligLivsvarigService(
    private val client: AfpOffentligLivsvarigClient
) {

    companion object {
        //TODO: application properties
        private const val SPK = "3010"
        private const val KLP = "3200"
        private const val OPF = "3820"
        private const val GABLER = "4160"

    }

    fun hentAfpOffentligLivsvarigStatus(fnr: String, onsketVirkningtidspunkt: LocalDate) {

        when (val sisteOrdning = client.hentSisteOrdning(fnr)) {
            SPK -> {
                client.hentAfpStatusSPK(
                    fnr = fnr,
                    uttaksdato = onsketVirkningtidspunkt,
                    sisteOrdning = sisteOrdning
                )
            }
            KLP -> {
                client.hentAfpStatusKLP(
                    fnr = fnr,
                    uttaksdato = onsketVirkningtidspunkt,
                    sisteOrdning = sisteOrdning
                )
            }
            OPF -> {
                client.hentAfpStatusOPF(
                    fnr = fnr,
                    uttaksdato = onsketVirkningtidspunkt,
                    sisteOrdning = sisteOrdning
                )
            }
            GABLER -> {
                client.hentAfpStatusGABLER(
                    fnr = fnr,
                    uttaksdato = onsketVirkningtidspunkt,
                    sisteOrdning = sisteOrdning
                )
            }

            else -> null
        }


    }

}