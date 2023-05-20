ext {
    set("kotlin.version", "1.8.20")
}

dependencies {
    val reactorVersion: String by project
    implementation("io.projectreactor:reactor-core:$reactorVersion")
}
