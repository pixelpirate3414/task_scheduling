import java.util.Properties

val postgresCredentialsFile = layout.buildDirectory.file("credentials/credentials.properties")

val postgresVersion = 18

tasks.register<Exec>("runPostgres") {
    group = "database"
    dependsOn("generateServiceCredentials")
    description = "Run local PostgreSQL"

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