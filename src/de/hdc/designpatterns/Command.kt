package de.hdc.designpatterns

import java.util.ArrayList

/** The Command interface  */
sealed class Command {
  abstract fun execute()
  abstract fun undo()
}

/** The Command for turning on the light - ConcreteCommand #1  */
class FlipUpCommand(private val theLight: Light) : Command() {
  override fun execute() = theLight.turnOn()
  override fun undo() = theLight.turnOff()
  override fun toString(): String {
    return "FlipUp"
  }
}

/** The Command for turning off the light - ConcreteCommand #2  */
class FlipDownCommand(private val theLight: Light) : Command() {
  override fun execute() = theLight.turnOff()
  override fun undo() = theLight.turnOn()
  override fun toString(): String {
    return "FlipDown"
  }

}

/** The Invoker class  */
class Switch {
  private val history = ArrayList<Command>()

  fun storeAndExecute(cmd: Command) {
    this.history.add(cmd) // optional
    cmd.execute()
  }

  fun pop(): Command {
    val last = history.last()
    last.undo()
    history.remove(last)
    return last
  }

  override fun toString(): String {
    return history.joinToString(", ")
  }
}

/** The Receiver class  */
class Light {
  private var on: Boolean = false

  fun turnOn() {
    on = true
  }

  fun turnOff() {
    on = false
  }

  override fun toString(): String {
    return "Light(${if (on) "ON" else "OFF"})"
  }
}

/* The test class or client */
object PressSwitch {
  @JvmStatic
  fun main(arguments: Array<String>) {
    val lamp = Light()

    val switchUp = FlipUpCommand(lamp)
    val switchDown = FlipDownCommand(lamp)

    val mySwitch = Switch()

    while (true) {
      println("Lamp: $lamp   History: $mySwitch")
      print("Input action 'on'/'off'/'undo': ")
      val a = readLine()
      println()
      when (a) {
        "on" -> mySwitch.storeAndExecute(switchUp) as Any
        "off" -> mySwitch.storeAndExecute(switchDown)
        "undo" -> mySwitch.pop()
        else -> {
          System.exit(0)
        }
      }.exhaustive
    }
  }
}