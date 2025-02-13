package dev.datlag.tolgee.common

import dev.datlag.tolgee.Tolgee
import dev.datlag.tolgee.TolgeeApple
import io.ktor.client.*
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.cache.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlin.coroutines.CoroutineContext

/**
 * The default platform-specific HTTP client configured for Darwin-based platforms (e.g., iOS, macOS).
 *
 * This client includes the following configurations:
 * - Redirection following (`followRedirects = true`) to handle HTTP redirects automatically.
 * - Caching support through the HTTP cache plugin (`install(HttpCache)`).
 *
 * It serves as the default HTTP client for operations requiring network interactions when targeting Darwin platforms.
 */
internal actual val platformHttpClient: HttpClient = HttpClient(Darwin) {
    followRedirects = true
    install(HttpCache)
}

/**
 * Creates a platform-specific instance of the Tolgee localization tool.
 *
 * @param config Configuration object used to initialize the Tolgee instance.
 * @return A platform-specific Tolgee instance.
 */
internal actual fun createPlatformTolgee(config: Tolgee.Config): Tolgee {
    return TolgeeApple(config)
}

/**
 * Provides the platform-specific CoroutineContext for performing network-related operations.
 *
 * This context ensures that network tasks are executed on an appropriate thread
 * or dispatcher, depending on the platform implementation.
 */
internal actual val platformNetworkContext: CoroutineContext
    get() = Dispatchers.IO