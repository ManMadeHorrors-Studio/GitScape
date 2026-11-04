package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.manmadehorrorsstudio.gitscape.GITHUB_REPOSITORIES_TABLE_NAME
import com.manmadehorrorsstudio.gitscape.SETTINGS_TABLE_NAME
import com.manmadehorrorsstudio.gitscape.TOKENS_TABLE_NAME
import com.manmadehorrorsstudio.gitscape.LLM_CONFIGURATION_TABLE_NAME

val migrationsList = arrayOf<Migration>(
    object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE settings_table_name ADD COLUMN themeStyle TEXT NOT NULL DEFAULT 'DEFAULT'"
            )
            db.execSQL(
                "ALTER TABLE settings_table_name RENAME TO $SETTINGS_TABLE_NAME"
            )
            db.execSQL(
                "ALTER TABLE github_repositories_table_name RENAME TO $GITHUB_REPOSITORIES_TABLE_NAME"
            )
        }
    },
    object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS $TOKENS_TABLE_NAME (
                `githubUserId` TEXT NOT NULL,
                `accessToken` TEXT NOT NULL,
                `refreshToken` TEXT NOT NULL,
                `accessTokenExpiresAt` INTEGER,
                `refreshTokenExpiresAt` INTEGER,
                PRIMARY KEY(`githubUserId`)
            )
            """.trimIndent()
            )
        }
    },
    object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS llm_favourites_table")
            db.execSQL("DROP TABLE IF EXISTS $LLM_CONFIGURATION_TABLE_NAME")
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS $LLM_CONFIGURATION_TABLE_NAME (
                `providerId` TEXT NOT NULL,
                `aiKey` TEXT,
                `selectedModelId` TEXT,
                `selected` INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY(`providerId`)
            )
            """.trimIndent()
            )

        }
    }
)