import org.springframework.boot.gradle.plugin.SpringBootPlugin

ext {
    set("kotlin.version", libs.versions.kotlin.get())
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation(libs.bundles.reactor)
}
