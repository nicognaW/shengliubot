plugins {
    kotlin("jvm") version "1.7.0"
    application
}

val ktor_version = "2.2.3"
group = "wang.nico"
version = "1.0-SNAPSHOT"

repositories {
    maven { setUrl("https://jitpack.io") }
    mavenCentral()
}

dependencies {
    implementation("com.aallam.openai:openai-client:3.2.2")
    implementation("io.github.crackthecodeabhi:kreds:0.8.1")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("io.ktor:ktor-client-jetty:$ktor_version")
    implementation("io.ktor:ktor-client-java:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-auth:$ktor_version")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")

    runtimeOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.7.0")
    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:1.7.0")

    val miraiVersion = "2.15.0-M1"
    api("net.mamoe", "mirai-core", miraiVersion)
    runtimeOnly("net.mamoe", "mirai-core", miraiVersion) // 运行时使用
    api("net.mamoe", "mirai-core-utils", miraiVersion)
    runtimeOnly("net.mamoe", "mirai-core-utils", miraiVersion) // 运行时使用
    implementation("ch.qos.logback:logback-classic:1.4.6")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation(kotlin("stdlib-jdk8"))
}


application {
    mainClass.set("MainKt")
}