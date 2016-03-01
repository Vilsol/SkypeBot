package io.mazenmc.skypebot.engine.bot

public data class CommandInternal(val name: String, val admin: Boolean, val alias: Array<String>,
                                  val command: Boolean, val exact: Boolean) {
    public fun name(): String {
        return name
    }

    public fun admin(): Boolean {
        return admin
    }

    public fun alias(): Array<String> {
        return alias
    }

    public fun command(): Boolean {
        return command
    }

    public fun exact(): Boolean {
        return exact
    }
}

public class CommandBuilder(val name: String) {
    var admin: Boolean = false
    var alias: Array<String> = emptyArray()
    var command: Boolean = true
    var exact: Boolean = true

    public fun admin(admin: Boolean): CommandBuilder {
        this.admin = admin
        return this
    }

    public fun alias(alias: Array<String>): CommandBuilder {
        this.alias = alias
        return this
    }

    public fun command(command: Boolean): CommandBuilder {
        this.command = command
        return this
    }

    public fun exact(exact: Boolean): CommandBuilder {
        this.exact = exact
        return this
    }

    public fun internal(): CommandInternal {
        return CommandInternal(name, admin, alias, command, exact)
    }
}