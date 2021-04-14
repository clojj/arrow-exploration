import arrow.core.*
import com.capraro.kalidation.constraints.function.inValues
import com.capraro.kalidation.constraints.function.notBlank
import com.capraro.kalidation.dsl.constraints
import com.capraro.kalidation.dsl.property
import com.capraro.kalidation.dsl.validationSpec
import arrow.core.computations.either.eager as doEither

data class Foo(val bar: String)

fun main() {

    val spec = validationSpec {
        constraints<Foo> {
            property(Foo::bar) {
                notBlank()
                inValues("GREEN", "WHITE", "RED")
            }
        }
    }

    val foo = Foo("GREE")

    val result = doEither<Nel<String>, String> {
        // TODO return validated value (data class) instead of Boolean ?
        val validFoo: Boolean = spec.validate(foo).mapLeft { Nel.fromListUnsafe(it.map { validationResult -> validationResult.message }) }.bind()
        val s1 = someFun("$validFoo").bind()
        s1
    }
    println(result)
}

fun validated(): Validated<Nel<String>, String> = "s0".valid()
fun someFun(s0: String): Either<Nel<String>, String> = "$s0 s1".right()
fun someBad(s0: String): Validated<Nel<String>, String> = "input $s0 ...catched exception message".nel().invalid()