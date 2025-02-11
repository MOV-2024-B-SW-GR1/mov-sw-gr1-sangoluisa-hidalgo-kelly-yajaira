package com.example.proyecto

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "GymReservation.db"
        private const val DATABASE_VERSION = 1

        // Table names
        private const val TABLE_USERS = "users"
        private const val TABLE_RESERVATIONS = "reservations"

        // Common column names
        private const val COLUMN_ID = "id"

        // Users Table - column names
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        // Reservations Table - column names
        private const val COLUMN_USER_ID_FK = "user_id"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_USERNAME TEXT, "
                + "$COLUMN_PASSWORD TEXT)")

        val createReservationTable = ("CREATE TABLE $TABLE_RESERVATIONS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_USER_ID_FK INTEGER, "
                + "$COLUMN_DATE TEXT, "
                + "$COLUMN_TIME TEXT, "
                + "FOREIGN KEY($COLUMN_USER_ID_FK) REFERENCES $TABLE_USERS($COLUMN_ID))")

        db.execSQL(createUserTable)
        db.execSQL(createReservationTable)
        populateSampleData()

    }

    private fun populateSampleData() {
        val userId1 = addUser("john_doe", "password123")
        val userId2 = addUser("jane_doe", "password456")

        addReservation(userId1.toInt(), "2025-02-12", "10:00 AM")
        addReservation(userId1.toInt(), "2025-02-13", "02:00 PM")
        addReservation(userId2.toInt(), "2025-02-12", "11:00 AM")
        addReservation(userId2.toInt(), "2025-02-14", "03:00 PM")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESERVATIONS")
        onCreate(db)
    }

    // CRUD Operations for Users
    fun addUser(username: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        return db.insert(TABLE_USERS, null, values).also {
            db.close()
        }
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?", arrayOf(username, password))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // CRUD Operations for Reservations
    fun addReservation(userId: Int, date: String, time: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID_FK, userId)
            put(COLUMN_DATE, date)
            put(COLUMN_TIME, time)
        }
        return db.insert(TABLE_RESERVATIONS, null, values).also {
            db.close()
        }
    }

    fun getReservations(userId: Int): List<Reservation> {
        val db = this.readableDatabase
        val reservations = mutableListOf<Reservation>()
        val cursor: Cursor = db.rawQuery("SELECT $COLUMN_ID, $COLUMN_USER_ID_FK, $COLUMN_DATE, $COLUMN_TIME FROM $TABLE_RESERVATIONS WHERE $COLUMN_USER_ID_FK = ?", arrayOf(userId.toString()))

        if (cursor.moveToFirst()) {
            val idIndex = 0
            val userIdIndex = 1
            val dateIndex = 2
            val timeIndex = 3

            do {
                val reservation = Reservation(
                    cursor.getInt(idIndex),
                    cursor.getInt(userIdIndex),
                    cursor.getString(dateIndex),
                    cursor.getString(timeIndex)
                )
                reservations.add(reservation)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return reservations
    }

}