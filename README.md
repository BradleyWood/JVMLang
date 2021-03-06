# Raven-Lang

## What is this?

This is an experimental programming language built for the JVM platform. The main
goal of this project is to create a fast jvm language that lacks the verbosity
of java while maintaining interoperability. Antlr is used to perform parsing
and the ASM bytecode manipulation framework is used for bytecode generation.
The runtime environment performs all type checking and coercions as well
as dynamic linking.


### [Web Demo](http://bradleyjwood.me/raven)

View multiple examples or try to create your own program using
the web demo. The web demo also comes with a REPL (read-eval-print-loop)
utility.

## Build

```
git clone https://github.com/BradleyWood/Raven-Lang.git
```

```
mvn install
```

## Project Layout

- [raven-core](raven-core/src/main/java/org/raven/core) - the core runtime environment

- [raven-compiler](raven-compiler/src/main/java/org/raven) - code compilation

- [raven-maven-plugin](raven-maven-plugin/src/main/java/org/raven/maven) - maven compatibility - compilation and test compilation

- [raven-stdlib](raven-stdlib/src/main/raven/raven) - raven stdlib

- [raven-cli](raven-cli/src/main/java/org/raven) - cli

- [raven-example](example) - example maven projects


## Examples

#### Interoperability

```kotlin
import javax.swing.JOptionPane

fun main() {
    JOptionPane.showMessageDialog(null, "Hello World.")
}
```

#### Defer

Function calls can be deferred in a last in first out order. Parameters
are evaluated immediately and are stored until the deferred statement is executed.

```kotlin
fun main() {
    for (i range 0 to 10) {
    	defer println(i)
    }
    defer println("World!")
    defer print("Hello, ")
}
```

#### When

```kotlin
fun func(name, param) = when(name) {
    "println" -> println(param)
    "sin"     -> Math.sin(param)
    "cos"     -> Math.cos(param)
    "PI"      -> Math.PI / param
    else      -> {}
}

fun main() {
    println("Sin(PI) = " + func("cos", Math.PI))
}
```

#### Go routines


```go
go aFunction();
```

[View more examples](https://github.com/BradleyWood/TlDemo)


## Tests

Many Raven tests are written in Raven and are compiled using the
[raven-maven-plugin](raven-maven-plugin/src/main/java/org/raven/maven).
To achieve Junit compatibility we add an annotation processor for the
@Test annotation to ensure that test methods are compiled as non-static
void methods.

#### Example

```kotlin
import org.junit.Assert
import org.junit.Test

@Test
fun testAddition() {
    var a = 100
    var b = 200
    Assert.assertEquals(300, a + b)
}
```
