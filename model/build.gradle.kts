plugins {
    `kotlin-convention`
    `dokka-convention`
    `maven-publish-convention`
}

//name = "pom-model"

dependencies {
    commonMainCompileOnly(libs.kotlinx.serialization.json)
    commonTestImplementation(libs.kotlinx.serialization.json)
}
