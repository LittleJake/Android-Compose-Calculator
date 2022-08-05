package net.littlejake.calculator

class Calc {
    var isInit: Boolean = true
    var former: Double = 0.0
    var current: Double = 0.0
    var result: Double = 0.0

    var oper: String = ""

    fun eval(): String {
        return try {
            if (isInit && oper != "C") {
                result = current
                former = result
                current = 0.0
                isInit = false
            } else {
                when (oper) {
                    "+" -> { result = former + current }
                    "-" -> { result = former - current }
                    "×" -> { result = former * current }
                    "÷" -> { result = former / current }
                    "C" -> { reset() }
                    else -> { result = former }
                }
                former = result
                current = 0.0
                isInit = true
            }
            result.toString()
        } catch (e: Exception) {
            reset()
            e.toString()
        }
    }

    private fun reset(){
        isInit = true
        former = 0.0
        current = 0.0
        oper = ""
        result = 0.0
    }
}