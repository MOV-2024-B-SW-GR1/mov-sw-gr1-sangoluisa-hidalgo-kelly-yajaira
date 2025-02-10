package com.example.sw2024bgr1_kysh

class BBaseDatosMemoria {
    companion object {
        val arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador.add(BEntrenador(1,"Kelly","a@a.com"))
            arregloBEntrenador.add(BEntrenador(2,"Yajaira","b@b.com"))
            arregloBEntrenador.add(BEntrenador(3,"Ashton","c@c.com"))
        }
    }
}