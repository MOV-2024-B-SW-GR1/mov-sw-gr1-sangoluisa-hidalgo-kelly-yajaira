package com.example.examen_iib

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "deber.db"
        private const val DATABASE_VERSION = 1

        // Table names
        private const val TABLE_ACTOR = "Actor"
        private const val TABLE_PELICULA = "Pelicula"

        // Common column names
        private const val COLUMN_ID = "id"

        // Actor Table - column names
        private const val COLUMN_NOMBRE_ACTOR = "nombre"
        private const val COLUMN_FECHA_NACIMIENTO = "fecha_nacimiento"
        private const val COLUMN_EDAD = "edad"
        private const val COLUMN_ALTURA = "altura"
        private const val COLUMN_DOBLE_ACCION = "tiene_doble_accion"

        // Pelicula Table - column names
        private const val COLUMN_NOMBRE_PELICULA = "nombre"
        private const val COLUMN_FECHA_ESTRENO = "fecha_estreno"
        private const val COLUMN_DURACION = "duracion"
        private const val COLUMN_CALIFICACION = "calificacion"
        private const val COLUMN_HISTORIA_REAL = "basado_historia_real"
        private const val COLUMN_ID_ACTOR = "id_actor"
        const val COLUMN_PELICULA_LATITUD = "latitud"
        const val COLUMN_PELICULA_LONGITUD = "longitud"

        // Date format
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createActorTable = ("CREATE TABLE $TABLE_ACTOR ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NOMBRE_ACTOR TEXT, "
                + "$COLUMN_FECHA_NACIMIENTO TEXT, "
                + "$COLUMN_EDAD INTEGER, "
                + "$COLUMN_ALTURA REAL, "
                + "$COLUMN_DOBLE_ACCION INTEGER)")

        val createPeliculaTable = ("CREATE TABLE $TABLE_PELICULA ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NOMBRE_PELICULA TEXT, "
                + "$COLUMN_FECHA_ESTRENO TEXT, "
                + "$COLUMN_DURACION INTEGER, "
                + "$COLUMN_CALIFICACION REAL, "
                + "$COLUMN_HISTORIA_REAL INTEGER, "
                + "$COLUMN_ID_ACTOR INTEGER, "
                + "$COLUMN_PELICULA_LATITUD REAL, "
                + "$COLUMN_PELICULA_LONGITUD REAL, "
                + "FOREIGN KEY($COLUMN_ID_ACTOR) REFERENCES $TABLE_ACTOR($COLUMN_ID))")


        db.execSQL(createActorTable)
        db.execSQL(createPeliculaTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTOR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PELICULA")
        onCreate(db)
    }

    // CRUD Operations for Actor
    fun addActor(actor: Actor): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOMBRE_ACTOR, actor.nombre)
        values.put(COLUMN_FECHA_NACIMIENTO, dateFormat.format(actor.fechaNacimiento))
        values.put(COLUMN_EDAD, actor.edad)
        values.put(COLUMN_ALTURA, actor.altura)
        values.put(COLUMN_DOBLE_ACCION, if (actor.tieneDobleAccion) 1 else 0)
        return db.insert(TABLE_ACTOR, null, values)
    }

    fun getActor(id: Int): Actor? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_ACTOR, null, "$COLUMN_ID=?", arrayOf(id.toString()),
            null, null, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val actor = Actor(
                idActor = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_ACTOR)),
                fechaNacimiento = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_NACIMIENTO)))!!,
                edad = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EDAD)),
                altura = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ALTURA)),
                tieneDobleAccion = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DOBLE_ACCION)) == 1
            )
            cursor.close()
            return actor
        }
        cursor?.close()
        return null
    }

    fun updateActor(actor: Actor): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOMBRE_ACTOR, actor.nombre)
        values.put(COLUMN_FECHA_NACIMIENTO, dateFormat.format(actor.fechaNacimiento))
        values.put(COLUMN_EDAD, actor.edad)
        values.put(COLUMN_ALTURA, actor.altura)
        values.put(COLUMN_DOBLE_ACCION, if (actor.tieneDobleAccion) 1 else 0)
        return db.update(TABLE_ACTOR, values, "$COLUMN_ID=?", arrayOf(actor.idActor.toString()))
    }

    fun deleteActor(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_ACTOR, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    // CRUD Operations for Pelicula
    fun addPelicula(pelicula: Pelicula): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOMBRE_PELICULA, pelicula.nombre)
        values.put(COLUMN_FECHA_ESTRENO, dateFormat.format(pelicula.fechaEstreno))
        values.put(COLUMN_DURACION, pelicula.duracion)
        values.put(COLUMN_CALIFICACION, pelicula.calificacion)
        values.put(COLUMN_HISTORIA_REAL, if (pelicula.basadoHistoriaReal) 1 else 0)
        values.put(COLUMN_ID_ACTOR, pelicula.id_Actor)
        values.put(COLUMN_PELICULA_LATITUD, pelicula.latitud)
        values.put(COLUMN_PELICULA_LONGITUD, pelicula.longitud)
        return db.insert(TABLE_PELICULA, null, values)
    }

    fun getPelicula(id: Int): Pelicula? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PELICULA, null, "$COLUMN_ID=?", arrayOf(id.toString()),
            null, null, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val pelicula = Pelicula(
                idPelicula = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_PELICULA)),
                fechaEstreno = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_ESTRENO)))!!,
                duracion = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURACION)),
                calificacion = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALIFICACION)),
                basadoHistoriaReal = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HISTORIA_REAL)) == 1,
                id_Actor = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_ACTOR)),
                latitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PELICULA_LATITUD)),
                longitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PELICULA_LONGITUD))
            )
            cursor.close()
            return pelicula
        }
        cursor?.close()
        return null
    }

    fun updatePelicula(pelicula: Pelicula): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOMBRE_PELICULA, pelicula.nombre)
        values.put(COLUMN_FECHA_ESTRENO, dateFormat.format(pelicula.fechaEstreno))
        values.put(COLUMN_DURACION, pelicula.duracion)
        values.put(COLUMN_CALIFICACION, pelicula.calificacion)
        values.put(COLUMN_HISTORIA_REAL, if (pelicula.basadoHistoriaReal) 1 else 0)
        values.put(COLUMN_ID_ACTOR, pelicula.id_Actor)
        return db.update(TABLE_PELICULA, values, "$COLUMN_ID=?", arrayOf(pelicula.idPelicula.toString()))
    }

    fun deletePelicula(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_PELICULA, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    // Get all Actors
    fun getAllActors(): List<Actor> {
        val actors = mutableListOf<Actor>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ACTOR", null)
        if (cursor.moveToFirst()) {
            do {
                val actor = Actor(
                    idActor = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_ACTOR)),
                    fechaNacimiento = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_NACIMIENTO)))!!,
                    edad = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EDAD)),
                    altura = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ALTURA)),
                    tieneDobleAccion = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DOBLE_ACCION)) == 1
                )
                actors.add(actor)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return actors
    }


    // Get all Movies of a Actor
    fun getPeliculasByActor(actorId: Int): List<Pelicula> {
        val peliculas = mutableListOf<Pelicula>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PELICULA WHERE $COLUMN_ID_ACTOR = ?", arrayOf(actorId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val pelicula = Pelicula(
                    idPelicula = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_PELICULA)),
                    fechaEstreno = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_ESTRENO)))!!,
                    duracion = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURACION)),
                    calificacion = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALIFICACION)),
                    basadoHistoriaReal = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HISTORIA_REAL)) == 1,
                    id_Actor = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_ACTOR)),
                    latitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PELICULA_LATITUD)),
                    longitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PELICULA_LONGITUD))
                )
                peliculas.add(pelicula)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return peliculas
    }
    fun clearAllActors() {
        val db = this.writableDatabase
        db.delete(TABLE_ACTOR, null, null)
        db.close()
    }
    @SuppressLint("Range")
    fun getPeliculaById(idPelicula: Int): Pelicula? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Pelicula WHERE idPelicula = ?", arrayOf(idPelicula.toString()))
        var pelicula: Pelicula? = null
        if (cursor.moveToFirst()) {
            pelicula = Pelicula(
                idPelicula = cursor.getInt(cursor.getColumnIndex("idPelicula")),
                nombre = cursor.getString(cursor.getColumnIndex("nombre")),
                fechaEstreno = Date(cursor.getLong(cursor.getColumnIndex("fechaEstreno"))),
                duracion = cursor.getInt(cursor.getColumnIndex("duracion")),
                calificacion = cursor.getDouble(cursor.getColumnIndex("calificacion")),
                basadoHistoriaReal = cursor.getInt(cursor.getColumnIndex("basado_historia_real")) > 0,
                id_Actor = cursor.getInt(cursor.getColumnIndex("id_actor")),
                latitud = cursor.getDouble(cursor.getColumnIndex("latitud")),
                longitud = cursor.getDouble(cursor.getColumnIndex("longitud"))
            )
        }
        cursor.close()
        return pelicula
    }


}
