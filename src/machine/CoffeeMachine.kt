package machine

import java.lang.IllegalStateException
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    val coffeeMachine = CoffeeMachine(400, 540, 120, 9, 550)
    var input = String()

    while (input != "exit") {
        input = scanner.next()
        println()
        coffeeMachine.act(input)
    }
}

class CoffeeMachine(waterMls: Int, milkMls: Int, beansGrs: Int, cups: Int, money: Int) {
    var waterMls = 0
    var milkMls = 0
    var beansGrs = 0
    var cups = 0
    var money = 0
    private var state: State = State.MAIN_MENU

    init {
        this.waterMls = if (waterMls < 0) 0 else waterMls
        this.milkMls = if (milkMls < 0) 0 else milkMls
        this.beansGrs = if (beansGrs < 0) 0 else beansGrs
        this.cups = if (cups < 0) 0 else cups
        this.money = if (money < 0) 0 else money
        runMainMenu()
    }


    fun act(input: String) {
        when (state) {
            State.CHOOSING_ACTION -> runEvent(input)
            State.CHOOSING_COFFEE -> chooseCoffee(input)
            else -> fillCoffeeMachineWithInput(input)
        }
    }

    private fun runMainMenu() {
        print("Write action (buy, fill, take, remaining, exit): ")
        state = State.CHOOSING_ACTION
    }

    private fun runEvent(event: String) {
        when (event) {
            "buy" -> runBuyEvent()
            "fill" -> runFillEvent()
            "take" -> runTakeEvent()
            "remaining" -> runRemainingEvent()
        }
    }

    private fun runBuyEvent() {
        print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
        state = State.CHOOSING_COFFEE
    }

    private fun chooseCoffee(input: String) {
        when (input) {
            "back" -> {}
            "1" -> makeCoffee(250, 0, 16, 4)
            "2" -> makeCoffee(350, 75, 20, 7)
            "3" -> makeCoffee(200, 100, 12, 6)
        }

        println()
        runMainMenu()
    }

    private fun makeCoffee(usedWaterMls: Int, usedMilkMls: Int, usedBeansGrs: Int, givenMoney: Int) {
        when {
            waterMls < usedWaterMls -> println("Sorry, not enough water!")
            milkMls < usedMilkMls -> println("Sorry, not enough milk!")
            beansGrs < usedBeansGrs -> println("Sorry, not enough coffee beans!")
            cups < 1 -> println("Sorry, not enough cups!")
            else -> {
                waterMls -= usedWaterMls
                milkMls -= usedMilkMls
                beansGrs -= usedBeansGrs
                cups--
                money += givenMoney
                println("I have enough resources, making you a coffee!")
            }
        }
    }

    private fun runFillEvent() {
        state = State.FILL_WATER
        print("Write how many ml of water do you want to add: ")
    }

    private fun fillCoffeeMachineWithInput(input: String) {
        when (state) {
            State.FILL_WATER -> {
                waterMls += input.toInt()
                state = State.FILL_MILK
                print("Write how many ml of milk do you want to add: ")
            }
            State.FILL_MILK -> {
                milkMls += input.toInt()
                state = State.FILL_BEANS
                print("Write how many grams of coffee beans do you want to add: ")
            }
            State.FILL_BEANS -> {
                beansGrs += input.toInt()
                state = State.FILL_CUPS
                print("Write how many disposable cups of coffee do you want to add: ")
            }
            State.FILL_CUPS -> {
                cups += input.toInt()

                runMainMenu()
            }
            else -> throw IllegalStateException()
        }
    }

    private fun runTakeEvent() {
        println("I gave you $$money")
        money = 0

        println()
        runMainMenu()
    }

    private fun runRemainingEvent() {
        println("The coffee machine has:")
        println("$waterMls of water")
        println("$milkMls of milk")
        println("$beansGrs of coffee beans")
        println("$cups of disposable cups")
        println("$$money of money")

        println()
        runMainMenu()
    }
}

enum class State {
    MAIN_MENU, CHOOSING_ACTION, CHOOSING_COFFEE, FILL_WATER, FILL_MILK, FILL_BEANS, FILL_CUPS
}
