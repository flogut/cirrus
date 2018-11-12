package de.hgv.cirrus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CirrusApplication {
    companion object {
        var serverPath: String = ""
    }
}

fun main(args: Array<String>) {
    val serverPath = args.getOrNull(0)
    if (serverPath == null) {
        println("Nutzung:  java -jar server.jar <Ordner, in dem die hochgeladenen Bilder gespeichert werden>")
        return
    }
    CirrusApplication.serverPath = serverPath
//    CirrusApplication.serverPath = "C:\\Users\\Florian\\Desktop\\server-data"

    runApplication<CirrusApplication>(*args)
}
