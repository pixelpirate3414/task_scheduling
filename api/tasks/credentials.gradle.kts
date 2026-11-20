import java.security.SecureRandom
import java.util.Base64
import java.util.Properties
import javax.crypto.KeyGenerator

val credentialsFile = layout.buildDirectory.file("credentials/credentials.properties")
val envFile = layout.projectDirectory.file(".env")

tasks.register("generateServiceCredentials") {
    group = "credentials"
    description = "Generates local PostgreSQL and JWT credentials"

    outputs.files(credentialsFile, envFile)

    doLast {
        val credentials = credentialsFile.get().asFile

        if (!credentials.exists() || !envFile.asFile.exists()) {
            credentials.parentFile.mkdirs()

            val random = SecureRandom()
            val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            fun randomString(length: Int) = buildString(length) {
                repeat(length) { append(alphabet[random.nextInt(alphabet.length)]) }
            }

            val jwtSecret = KeyGenerator.getInstance("HmacSHA256").run {
                init(256)
                Base64.getEncoder().encodeToString(generateKey().encoded)
            }

            val username = "task_${randomString(12)}"
            val password = randomString(32)

            Properties().apply {
                setProperty("username", username)
                setProperty("password", password)
                setProperty("jwt_secret", jwtSecret)
            }.store(credentials.outputStream(), "Local credentials")

            envFile.asFile.writeText(
                """
                    POSTGRES_USER=$username
                    POSTGRES_PASSWORD=$password
                    JWT_SECRET=$jwtSecret
                """.trimIndent()
            )
        }

        Properties().apply {
            credentials.inputStream().use(::load)
        }.run {
            logger.lifecycle("Credentials generated")
        }
    }
}
