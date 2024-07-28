package com.example.sokerihiiri.repository

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    // Muutetaan kalorit ja hiilarit kokonaisluvuiksi ja lisätään sarake kommentille.
    override fun migrate(db: SupportSQLiteDatabase) {
        // Luodaan väliaikainen taulu, johon kopioidaan vanhat tiedot.
        // Lopuksi poistetaan vanha taulu ja uudelleennimetään uusi taulu
        db.execSQL("CREATE TABLE meals_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, timestamp INTEGER NOT NULL, calories INTEGER NOT NULL, carbohydrates INTEGER NOT NULL, comment TEXT NOT NULL DEFAULT '')")
        db.execSQL("INSERT INTO meals_new (id, timestamp, calories, carbohydrates, comment) SELECT id, timestamp, CAST(calories AS INTEGER), CAST(carbohydrates AS INTEGER), '' FROM meals")
        db.execSQL("DROP TABLE meals")
        db.execSQL("ALTER TABLE meals_new RENAME TO meals")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    // Lisätään kommenttisarake mittaus- ja insuliinitapahtumiin

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE blood_sugar_measurements ADD COLUMN comment TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE insulin_injections ADD COLUMN comment TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    // Lisätään taulu "others" vapaamuotoiselle tapahtumalle

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE others (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, timestamp INTEGER NOT NULL, comment TEXT NOT NULL DEFAULT '')")
    }

}