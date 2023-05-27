plugins{
    `maven-publish`
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

publishing {
    repositories {
//        maven {
//            name = "GitHubPackages"
//            url = uri("https://maven.pkg.github.com/iseki0/pomtools")
//            credentials {
//                username = project.findProperty("gpr.user") as? String ?: System.getenv("USERNAME")
//                password = project.findProperty("gpr.key") as? String ?: System.getenv("TOKEN")
//            }
//        }
        maven {
            name = "Central"
            url = if (version.toString().endsWith("SNAPSHOT")) {
                // uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
                uri("https://oss.sonatype.org/content/repositories/snapshots")
            } else {
                // uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
                uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            }
            credentials {
                username = properties["ossrhUsername"]?.toString() ?: System.getenv("OSSRH_USERNAME")
                password = properties["ossrhPassword"]?.toString() ?: System.getenv("OSSRH_PASSWORD")
            }
        }
    }

    publications {
        withType<MavenPublication>{
            pom {
                description.set("POM tools")
                url.set("https://github.com/iseki0/pomtools")
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        id.set("iseki0")
                        name.set("iseki zero")
                        email.set("iseki@iseki.space")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/iseki0/pomtools.git")
                    developerConnection.set("scm:git:https://github.com/iseki0/pomtools.git")
                    url.set("https://github.com/iseki0/pomtools")
                }
            }
        }
    }

}
