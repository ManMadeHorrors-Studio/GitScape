
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.hilt)
}

private val keysProperties = Properties().apply {
    val keysFile = rootProject.file("keys.properties")
    if (keysFile.exists()) {
        keysFile.inputStream().use(::load)
    }
}

private fun getSecretValue(key: String): String {
    return keysProperties.getProperty(key)
        ?: System.getenv(key)
        ?: error(
            """
            Missing configuration value: $key
            
            Either:
            - define '$key' inside keys.properties
            - or expose it as an environment variable (e.g. Jenkins Credentials).
            """.trimIndent()
        )
}

android {
    namespace = "com.manmadehorrorsstudio.gitscape"
    compileSdk {
        version = release(36)
    }

    val releaseSigningConfig = "release"

    defaultConfig {
        applicationId = "com.manmadehorrorsstudio.gitscape"
        minSdk = 30
        //noinspection OldTargetApi
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        signingConfigs {
            create(releaseSigningConfig) {
                storeFile = file(getSecretValue("KEYSTORE_FILE"))
                storePassword = getSecretValue("KEYSTORE_PASSWORD")
                keyAlias = getSecretValue("KEY_ALIAS")
                keyPassword = getSecretValue("KEY_PASSWORD")
            }
        }

        buildConfigField(
            "String",
            "RELEASE_SIGNING_CONFIG",
            "\"$releaseSigningConfig\""
        )

        val GITHUB_PAT = "GITHUB_PAT"
        val AI_KEY = "AI_KEY"

        buildConfigField(
            "String",
            GITHUB_PAT,
            "\"${getLocalProperty(GITHUB_PAT)}\""
        )
        buildConfigField(
            "String",
            AI_KEY,
            "\"${getLocalProperty(AI_KEY)}\""
        )
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            signingConfig = signingConfigs.getByName(releaseSigningConfig)
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    packaging {
        resources.excludes += "META-INF/INDEX.LIST"
        resources.excludes += "META-INF/DEPENDENCIES"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splash)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.retrofit)
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)
    implementation(libs.retrofit.converter.moshi)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.coil.compose)

    implementation(libs.koog.agents)
    implementation(libs.koogHttpClientKtor)
    // Uncomment the implementation for the LLM provider you want to use
    implementation(libs.koog.google)
    /*implementation(libs.koog.openai)
    implementation(libs.koog.anthropic)
    implementation(libs.koog.deepseek)
    implementation(libs.koog.openrouter)
    implementation(libs.koog.mistralai)
    implementation(libs.koog.dashscope)
    implementation(libs.koog.ollama)*/

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.koog.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

private fun getLocalProperty(propertyName: String): String? {
    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")

    return if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { lpf ->
            properties.load(lpf)
        }
        properties.getProperty(propertyName)
    } else
        null
}