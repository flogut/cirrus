package de.hgv.cirrus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CirrusApplication {
    companion object {
        var serverPath = ""
    }
}

fun main(args: Array<String>) {
//    CirrusApplication.serverPath = args[0]
    CirrusApplication.serverPath = "C:\\Users\\Florian\\Desktop\\server-data"

    runApplication<CirrusApplication>(*args)
}
