import java.security.SecureRandom
import java.util.Properties

val postgresCredentialsFile = layout.buildDirectory.file("postgres/credentials.properties")
val envFile = layout.projectDirectory.file(".env")

val postgresVersion = 18

tasks.register("generatePostgresCredentials") {
    group = "database"
    description = "Generates and logs credentials for the PostgreSQL container."

    val credentialsFileProvider = postgresCredentialsFile
    outputs.file(credentialsFileProvider)

    val envFile = envFile
    outputs.file(envFile)

    doLast {
        val credentials = credentialsFileProvider.get().asFile
        if (!credentials.exists()) {
            credentials.parentFile.mkdirs()
            val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            val random = SecureRandom()
            fun randomValue(length: Int) = (1..length)
                .map { alphabet[random.nextInt(alphabet.length)] }
                .joinToString("")

            val username = "task_${randomValue(12)}"
            val password = randomValue(32)

            Properties().apply {
                setProperty("username", username)
                setProperty("password", password)
            }.store(credentials.outputStream(), "Local PostgreSQL credentials")

            envFile.asFile.writeText(
                """
                    POSTGRES_USER=$username
                    POSTGRES_PASSWORD=$password
                """.trimIndent()
            )
        }

        val values = Properties().apply { credentials.inputStream().use(::load) }
        logger.lifecycle("PostgreSQL username: ${values.getProperty("username")}")
        logger.lifecycle("PostgreSQL password: ${values.getProperty("password")}")
    }
}

tasks.register<Exec>("runPostgres") {
    group = "database"
    description = "Starts PostgreSQL in Docker using the generated credentials."
    dependsOn("generatePostgresCredentials")

    val credentialsFileProvider = postgresCredentialsFile
    val pgVersion = postgresVersion

    inputs.file(credentialsFileProvider)

    doFirst {
        val values = Properties().apply {
            credentialsFileProvider.get().asFile.inputStream().use(::load)
        }

        commandLine(
            "docker", "run", "--detach", "--name", "task-scheduler-postgres",
            "--publish", "5432:5432",
            "--env", "POSTGRES_DB=task_scheduler",
            "--env", "POSTGRES_USER=${values.getProperty("username")}",
            "--env", "POSTGRES_PASSWORD=${values.getProperty("password")}",
            "postgres:$pgVersion"
        )
    }
}