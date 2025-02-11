package com.example.deber02_iib

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class Actor(
    var nombre: String?,
    var fechaNacimiento: Date,
    var edad: Int,
    var altura: Double,
    var tieneDobleAccion: Boolean
) : Parcelable {
    override fun toString(): String {
        return "$nombre - $altura"
    }
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        Date(parcel.readLong()),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte()
    ) {
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeLong(fechaNacimiento.time)
        parcel.writeInt(edad)
        parcel.writeDouble(altura)
        parcel.writeByte(if (tieneDobleAccion) 1 else 0)
    }
    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<Actor> {
        override fun createFromParcel(parcel: Parcel): Actor {
            return Actor(parcel)
        }
        override fun newArray(size: Int): Array<Actor?> {
            return arrayOfNulls(size)
        }
    }
}