plugins {
    jacoco
}

tasks.withType<JacocoReport> {
    reports {
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
    dependsOn(tasks.findByName("test"))
}

tasks.withType<Test> {
    finalizedBy(tasks.findByName("jacocoTestReport"))
}
