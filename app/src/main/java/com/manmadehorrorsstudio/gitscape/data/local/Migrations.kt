package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.manmadehorrorsstudio.gitscape.GITHUB_REPOSITORIES_TABLE_NAME
import com.manmadehorrorsstudio.gitscape.SETTINGS_TABLE_NAME
import com.manmadehorrorsstudio.gitscape.TOKENS_TABLE_NAME


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
                `refreshToken` TEXT,
                `accessTokenExpiresAt` INTEGER,
                `refreshTokenExpiresAt` INTEGER,
                PRIMARY KEY(`githubUserId`)
            )
            """.trimIndent()
            )
        }
    }
)