package dev.datlag.tolgee

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.systemLocale
import dev.datlag.tolgee.common.createPlatformTolgee
import dev.datlag.tolgee.common.platformHttpClient
import dev.datlag.tolgee.common.platformNetworkContext
import io.ktor.client.*
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

open class Tolgee(
    open val config: Config
) {

    @JvmOverloads
    open fun getTranslation(
        key: String,
        locale: Locale = systemLocale,
        vararg args: Any
    ): String? {
        return null
    }

    @ConsistentCopyVisibility
    data class Config internal constructor(
        val apiKey: String,
        val apiUrl: String = DEFAULT_API_URL,
        val projectId: String? = null,
        val network: Network = Network()
    ) {

        class Builder {
            lateinit var apiKey: String

            var apiUrl: String = DEFAULT_API_URL
                set(value) {
                    field = value.trim().ifBlank { null } ?: DEFAULT_API_URL
                }

            var projectId: String? = null
                set(value) {
                    field = value?.trim()?.ifBlank { null }
                }

            var network: Network = Network()

            fun apiKey(apiKey: String) = apply {
                this.apiKey = apiKey
            }

            fun apiUrl(url: String) = apply {
                this.apiUrl = url
            }

            fun projectId(projectId: String?) = apply {
                this.projectId = projectId
            }

            fun network(network: Network) = apply {
                this.network = network
            }

            fun network(builder: Network.Builder.() -> Unit) = apply {
                this.network = Network.Builder().apply(builder).build()
            }

            fun build(): Config = Config(
                apiKey = apiKey,
                apiUrl = apiUrl,
                projectId = projectId,
            )
        }

        @ConsistentCopyVisibility
        data class Network internal constructor(
            val client: HttpClient = platformHttpClient,
            val context: CoroutineContext = platformNetworkContext
        ) {

            class Builder {
                var client: HttpClient = platformHttpClient
                var context: CoroutineContext = platformNetworkContext

                fun client(client: HttpClient) = apply {
                    this.client = client
                }

                fun context(context: CoroutineContext) = apply {
                    this.context = context
                }

                fun build(): Network = Network(
                    client = client,
                    context = context
                )
            }
        }

        companion object {
            internal const val DEFAULT_API_URL = "https://app.tolgee.io/v2/"
        }
    }

    companion object {
        @JvmStatic
        fun init(config: Config) = createPlatformTolgee(config)

        @JvmStatic
        fun init(builder: Config.Builder.() -> Unit) = init(Config.Builder().apply(builder).build())
    }
}