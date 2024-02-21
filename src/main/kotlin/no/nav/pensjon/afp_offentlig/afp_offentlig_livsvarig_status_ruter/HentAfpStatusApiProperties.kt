package no.nav.pensjon.afp_offentlig.afp_offentlig_livsvarig_status_ruter

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "hent-afp-status-api")
data class HentAfpStatusApiProperties(
    var tilbydere: Map<String, AfpStatusApi> = emptyMap()
) {
    data class AfpStatusApi(
        val ordning: Int,
        val url: String,
    )
}
