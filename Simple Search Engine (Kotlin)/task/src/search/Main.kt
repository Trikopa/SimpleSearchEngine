package search

import java.io.File

fun ask(message: String) = println(message).run { readln() }

typealias Mapper = MutableMap<String,MutableList<Int>>

typealias DataList = List<MutableList<Int>?>

fun main(args: Array<String>) {
    SearchEngine(args[1])
}

class SearchEngine(filename: String) {
    private val dataList = File(filename).readLines()

    private fun invertedQuery(): Mapper {
        val map: Mapper = mutableMapOf()
        dataList.forEachIndexed {index, s ->
            s.split(" ").forEach { if (map[it] != null) map[it]!!.add(index) else map[it] = mutableListOf(index) }
        }
        return map
    }

    private fun query(dataList: List<String>, map: Mapper) {
        val strategy = ask("\nSelect a matching strategy: ALL, ANY, NONE")
        val data = ask("\nEnter a name or email to search all matching people.")
        val mapList = map.values.flatten()
        val filteredMapList = filterQuery(mapList, data, strategy)
        if (filteredMapList.isEmpty()) {
            println("No matching people found.")
        } else {
            println("\n${filteredMapList.toSet().size} persons found:")
            filteredMapList.toSet().forEach { println(dataList[it]) }
        }
    }

    private fun String.any(query: List<String>): Boolean {
        query.forEach { if(this.contains(it,true)) return true }
        return false
    }

    private fun filterQuery(dataMap: List<Int>, query: String, strategy: String): List<Int> {
        return when(strategy) {
            "ANY" ->  dataMap.filter { dataList[it].any(query.split(" ")) }
            "ALL" ->  dataMap.filter { dataList[it].contains(query, true) }
            "NONE" -> dataMap.filter { !dataList[it].any(query.split(" ")) }
            else -> dataMap
        }
    }

    private fun printPeople(dataList: List<String>) {
        println("=== List of people ===")
        dataList.forEach { println(it) }
    }

    private fun game() {
        while (true) {
            println("\n=== Menu ===\n" +
                    "1. Find a person\n" +
                    "2. Print all people\n" +
                    "0. Exit")
            when (readln()) {
                "0" -> {
                    println("Bye!")
                    break
                }
                "1" -> query(dataList,invertedQuery())
                "2" -> printPeople(dataList)
                else -> println("Incorrect option! Try again.")

            }
        }
    }

    init {
        game()
    }
}