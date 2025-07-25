import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.lang.Closure
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import java.net.HttpURLConnection
import java.net.URL

plugins {
    kotlin("jvm") version "1.8.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("com.github.ben-manes.versions") version "0.41.0"
    id("com.palantir.git-version") version "0.12.3"
    id("dev.s7a.gradle.minecraft.server") version "1.2.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jmailen.kotlinter") version "3.8.0"
}

val gitVersion: Closure<String> by extra

val pluginVersion: String by project.ext

repositories {
    mavenCentral()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://oss.sonatype.org/content/groups/public/")
}

val shadowImplementation: Configuration by configurations.creating
configurations["implementation"].extendsFrom(shadowImplementation)

dependencies {
    shadowImplementation(kotlin("stdlib"))
    compileOnly("org.spigotmc:spigot-api:$pluginVersion-R0.1-SNAPSHOT")
}

configure<BukkitPluginDescription> {
    main = "com.github.ringoame196_s_mcPlugin.Main"
    version = pluginVersion
    apiVersion = "1." + pluginVersion.split(".")[1]
    author = "ringoame196_s_mcPlugin"
    website = "https://github.com/ringoame196-s-mcPlugin"

    commands {
        register("dev") {
            description = "Development用のコマンド"
            permission = "op"
            usage = "/dev <reload>"
        }
    }
}

tasks.withType<ShadowJar> {
    configurations = listOf(shadowImplementation)
    archiveClassifier.set("")
    relocate("kotlin", "com.github.ringoame196_s_mcPlugin.libs.kotlin")
    relocate("org.intellij.lang.annotations", "com.github.ringoame196_s_mcPlugin.libs.org.intellij.lang.annotations")
    relocate("org.jetbrains.annotations", "com.github.ringoame196_s_mcPlugin.libs.org.jetbrains.annotations")
}

tasks.named("build") {
    dependsOn("shadowJar")
    // プラグインを特定のパスへ自動コピー
    val copyFilePath = "M:/TwitterServer/plugins/" // コピー先のフォルダーパス
    val copyFile = File(copyFilePath)
    if (copyFile.exists() && copyFile.isDirectory) {
        doFirst {
            copy {
                from(buildDir.resolve("libs/${project.name}.jar"))
                into(copyFile)
            }
        }
        doLast {
            val port = 25585
            val ip = "192.168.0.21"
            val apiUrl = "http://$ip:$port/plugin?name=${project.name}"

            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection

                // タイムアウト設定（重要）
                connection.connectTimeout = 2000  // 2秒でタイムアウト
                connection.readTimeout = 2000

                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    println("API Response: $response")
                } else {
                    println("Server responded with error code: ${connection.responseCode}")
                }

                connection.disconnect()
            } catch (e: java.net.ConnectException) {
                println("Warning: サーバーに接続できません（オフラインかもしれません）")
            } catch (e: java.net.SocketTimeoutException) {
                println("Warning: 接続がタイムアウトしました")
            } catch (e: Exception) {
                println("Warning: API通信で予期しないエラーが発生しました: ${e.message}")
            }
        }
    }
}

task<SetupTask>("setup")
