plugins {
    kotlin("jvm") version "1.7.0"
    java

    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "com.github.raink1208"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
}

bukkit {
    main = "com.github.raink1208.snowballfight.Main"
    apiVersion = "1.19"

    commands {
        register("snowballfight") {
            description = "雪合戦管理用コマンド"
            aliases = listOf("sbf")
            permission = "snowballfightgame.command"
        }
        register("snowballfightgame") {
            description = "雪合戦ゲーム用コマンド"
            aliases = listOf("sbfg")
            permission = "snowballfightgame.command"
        }
    }
    permissions {
        register("snowballfightgame.command")
    }
}

tasks.jar {
    archiveFileName.set("${project.name}.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}