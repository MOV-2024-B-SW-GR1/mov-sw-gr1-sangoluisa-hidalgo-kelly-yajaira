package org.example

import java.util.*

fun main() {
//variables mutable e inmutables
    val inmutable: String = "Kelly val"
    //inmutable = "hola" // nos dara error ya que no se puede cambiar

    var mutable: String = "Kelly var"
    println(mutable)

    mutable = "cambiado" //como puede mutar se puede cambia
    println(mutable)


//-------------------------------------------------------------//
//duckt typing

    val ejemploVariable = "String Kelly"
    ejemploVariable.trim() //se puede hacer esto ya que identifica que es un string

    val edadEjemplo = 12
    //  ejemploVariable = edadEjemplo //dara error ya que se identifica que es int.

//-------------------------------------------------------------//
// Variables Primitivas
    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean = true
    val fechaNacimiento: Date = Date()

//-------------------------------------------------------------//
//switch case o when
    val estadoCivilWhen = "C"
    when (estadoCivilWhen) {         //cuando el estado civil es:
        ("C") -> {
            println("Casado")       //C entonces es casado
        }
        "S" -> {
            println("Soltero")      //S entonces es casado
        }
        else -> {
            println("No sabemos")   //OTROS entonces no sabemos
        }
    }
//-------------------------------------------------------------//
//if else de una linea -> parecido al ternario ya que no existe
    val esSoltero = (estadoCivilWhen == "S")
    val coqueteo = if (esSoltero) "Si" else "No"  //if else chiquito

//llamada a la funcion - la funcion esta al ultimo
    imprimirNombre("Kelly Funcion")

//llamada a la funcion calcularSueldo
    calcularSueldo(10.00)
    calcularSueldo(10.00,15.00,20.00)
    calcularSueldo(10.00,bonoEspecial = 20.00)
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00)

//llamada a las clases creadas:
    val sumaA = Suma(1,1)
    val sumaB = Suma(null, 1)
    val sumaC = Suma(1, null)
    val sumaD = Suma(null, null)

    sumaA.sumar()
    sumaB.sumar()
    sumaC.sumar()
    sumaD.sumar()

    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)
//-------------------------------------------------------------//
//ARREGLOS
//ARREGLOS ESTATICOS
    val arregloEstatico: Array<Int> = arrayOf<Int>(1,2,3)
    //no se puede editar
    println(arregloEstatico)

//ARREGLOS DINAMICOS
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(
        1,2,3,4,5,6,7,8,9,10
    )
    println(arregloDinamico)
    //Se pueden agregar, eliminar elementos
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)

//-------------------------------------------------------------//
//ITERACION DE ARREGLOS
    // FOR EACH = > Unit
    // Iterar un arreglo
    val respuestaForEach: Unit = arregloDinamico
        .forEach { valorActual: Int -> //  - > = >
            println("Valor actual: ${valorActual}");
        }


    // "IT" (en ingles "eso") significa el elemento iterado
    arregloDinamico.forEach{ println("Valor Actual (it): ${it}")}


    // MAP -> MUTA(Modifica cambia) el arreglo
    // 1) Enviamos el nuevo valor de la iteracion
    // 2) Nos devuelve un NUEVO ARREGLO con valores
    // de las iteraciones
    val respuestaMap: List<Double> = arregloDinamico
        .map { valorActual: Int ->
            return@map valorActual.toDouble() + 100.00
        }
    println(respuestaMap)

    val respuestaMapDos = arregloDinamico.map{ it + 15 }
    println(respuestaMapDos)

    // Filter - > Filtrar el ARREGLO
    // 1) Devolver una expresion (TRUE o FALSE)
    // 2) Nuevo arreglo FILTRADO
    val respuestaFilter: List<Int> = arregloDinamico
        .filter{ valorActual:Int ->
            // Expresion o CONDICION
            val mayoresACinco: Boolean = valorActual > 5
            return@filter mayoresACinco
        }

    val respuestaFilterDos =arregloDinamico.filter{ it <=5 }
    println(respuestaFilter)
    println(respuestaFilterDos)

//-------------------------------------------------------------//
// ANY O ALL - PARA VERFIICAR SI CUMPLE DEVUELVE BOOLEANO
    // OR -> ANY (Alguno Cumple?)
    val respuestaAny: Boolean = arregloDinamico
        .any{ valorActual:Int ->
            return@any (valorActual > 5)
        }
    println(respuestaAny) // True

    // And -> ALL (Todos cumplen?)
    val respuestaAll: Boolean = arregloDinamico
        .all{ valorActual:Int ->
            return@all (valorActual > 5)
        }
    println(respuestaAll) // False
}


//-------------------------------------------------------------//
//Sintaxis de las funciones
//funcion con un parametro
    fun imprimirNombre (nombre:String):Unit{ //el unit es el void, si no devuelve nada, no es necesario ponerla
        //se puede crear funciones dentro de otras funciones
        fun otraFuncionAdentro(){
            println("otra funcion adentro") //el alcance es el scope de esa funcion
        }
        //Template Strings
        println("Nombre: ${nombre}") //para cuando es de mas de una variable
        println("Nombre: $nombre") // de una sola variable

        println("Nombre: ${nombre + nombre}") //uso con llaves concatenadas
        println("Nombre (con llaves) : ${nombre.uppercase()}") //uso con llaves (funciones)
        println("Forma Incorrecta sin llaves: $nombre.uppercase()") //INCORRECTO

        otraFuncionAdentro()
    }

//funcion con varios parametros
    fun calcularSueldo( //fun calcularSueldo( sueldo:Double,tasa: Double = 12.00, bonoEspecial:Double? = null):Double{
        sueldo:Double, // Reguerido
        tasa: Double = 12.00, // Opcional (defecto)
        bonoEspecial:Double? = null // Opciongl (nullable)
        // Variable? -> "?" Es Nullable (osea que puede en algun momento ser nulo)
    ):Double {
        // Int -> Int? (nullable)
        // String -> String? (nullable)
        // Date -> Date? (nullable)
        if(bonoEspecial == null){
            return sueldo *(100/tasa)
        }else{
            return sueldo * (100 / tasa) * bonoEspecial
        }
    }

//-------------------------------------------------------------//
//clases

//SINTAXIS JAVA
    abstract class NumerosJava{
        protected val numeroUno:Int
        private val numeroDos: Int

        constructor(
            uno:Int,
            dos: Int
        ){
            this.numeroUno = uno
            this.numeroDos = dos
            println("Inicializando")
        }
    }

//SINTAXIS KOTLIN
    abstract class Numeros(
        // Caso 1) Parametro normal
        // uno:Int , (parametro (sin modificador acceso))

        // Caso 2) Parametro y propiedad (atributo) (protected)
        // private var uno: Int (propiedad "instancia.uno")

        protected val numeroUno: Int, // instancia.numeroUno
        protected val numeroDos: Int, // instancia.numeroDos
        parametroNoUsadoNoPropiedadDeLaClase: Int? = null
    ){
        init{ // bloque constructor primario OPCIONAL
            this.numeroUno
            this.numeroDos
            println("Inicializando Clase Numeros")
        }
    }

    class Suma( // Constructor primario  -> class Suma( unoParametro: Int, dosParametro: Int,): Numeros(unoParametro, dosParametro){
        unoParametro: Int, // Parametro
        dosParametro: Int, // Parametros
    ): Numeros( // Clase papa, Numeros (extendiendo)
        unoParametro,
        dosParametro
    ){
        //modificadores de acceso
        public val soyPublicoExplicito: String = "variable publica"
        val soyPublicoImplicito: String = "variable publica sin tener la palabra public"

        init { // bloque constructor primario
            //variables heredadas del padre
            this.numeroUno
            this.numeroDos
            numeroUno // this. OPCIONAL  [propiedades, metodos]
            numeroDos // this. OPCIONAL [propiedades, metodos]
            //variables que hemos colocado nuevas
            this.soyPublicoImplicito
            soyPublicoExplicito
        }

    //constructores secundarios
    //Para recibir valores nulos

    constructor(
        // Constructor secundario
        uno: Int?, // Entero nullable
        dos: Int,
    ):this(
        if (uno == null) 0 else uno,
        dos
    ){
        // Bloque de codigo de constructor secundario OPCIONAL
    }

    constructor ( // Constructor secundorio
        uno: Int,
        dos: Int?, // Entero nullable
    ):this(
        uno,
        if(dos == null) 0 else dos
    )

    constructor(
        // Constructor secundario
        uno: Int?, // Entero nulLable
        dos: Int?, // Entero nullable
    ):this(
        if (uno == null) 0 else uno,
        if (dos == null) 0 else dos
    )


    //FUNCIONES Y COMPANY OBJECT

    fun sumar ():Int{
        val total = numeroUno + numeroDos
        agregarHistorial(total)
        return total
    }

    companion object { // Comparte entre todas las instanciAs, similar oL STATIC
        // funciones, variables
        // NombreClase.metodo, NombreClase.funcion =>
        // Suma.pi
        val pi = 3.14
        // Suma.elevarAlCuadrado

        fun elevarAlCuadrado(num:Int):Int{ return num * num }
        val historialSumas = arrayListOf<Int>()

        fun agregarHistorial(valorTotalSuma:Int) { // Suma.agregarHistorial
            historialSumas.add(valorTotalSuma)
        }
    }
}