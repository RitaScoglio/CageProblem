import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    val main = Math()
   /* val elapsed: kotlin.time.Duration = measureTime {
        val list = List(10) { it + 1 }
        val pairs = main.getAllPairs(list)
        val comb = main.generateAllCombinations(pairs.size, list.size / 2)
        main.getCorrectCombinations(pairs, comb)
    }
    println(elapsed)
    val ela: kotlin.time.Duration = measureTime {
        main.newApproach(10)
    }
    println(ela)*/
    val pse: kotlin.time.Duration = measureTime {
        main.newNewApproach(6)
    }
    println(pse)
}

class Math {

    fun newNewApproach(numberOfVertices: Int){
        val possibleEdges = mutableListOf<Edge>()
        for (i in 1 until numberOfVertices) {
            for (j in i + 1..numberOfVertices) {
                possibleEdges.add(Edge(Vertex(i), Vertex(j)))
            }
        }
        println(possibleEdges)
        val allCompositions = mutableSetOf<List<Edge>>()
        for (indexOfEdge in 0..possibleEdges.size-(numberOfVertices/2)) {
            val compositionOfEdges = mutableListOf<Edge>()
            val indexes = mutableListOf<Int>()
            val usedVertices = mutableListOf<Vertex>()
            compositionOfEdges.add(possibleEdges[indexOfEdge])
            indexes.add(indexOfEdge)
            usedVertices.add(possibleEdges[indexOfEdge].vertex1)
            usedVertices.add(possibleEdges[indexOfEdge].vertex2)
            var startIndex = indexOfEdge

            do {
                var currentIndex = startIndex + 1
                do {
                    if (!compositionOfEdges.contains(possibleEdges[currentIndex]) &&
                        !usedVertices.contains(possibleEdges[currentIndex].vertex1) &&
                        !usedVertices.contains(possibleEdges[currentIndex].vertex2)
                    ) {
                        compositionOfEdges.add(possibleEdges[currentIndex])
                        indexes.add(currentIndex)
                        usedVertices.add(possibleEdges[currentIndex].vertex1)
                        usedVertices.add(possibleEdges[currentIndex].vertex2)
                    }
                    currentIndex++
                } while (compositionOfEdges.size < numberOfVertices / 2 && currentIndex < possibleEdges.size)

                if (compositionOfEdges.size == numberOfVertices / 2)
                    allCompositions.add(compositionOfEdges.toList())
                val toRemove = if (indexes.last() == possibleEdges.size - 1) 2 else 1
                startIndex = indexes[indexes.size - toRemove]
                for (n in 1..toRemove) {
                    indexes.removeLast()
                    compositionOfEdges.removeLast()
                    usedVertices.removeLast()
                    usedVertices.removeLast()
                }
            } while (!compositionOfEdges.isEmpty())
        }
        println(allCompositions)
        println(allCompositions.size)
    }


    fun newApproach(numVertices: Int){
        val listOfIndex = mutableListOf<Char>()
        for (ch in 1..numVertices){
            listOfIndex.add(ch.toChar())
        }
        val perm = allPermutations(listOfIndex)
        println(perm.size)
        res = perm.map { str -> str.chunked(2)
            .map { pair -> Edge(Vertex(pair[0].toInt()), Vertex(pair[1].toInt())) }
            .sortedBy { it.vertex1.name }}.toMutableSet()
        println(res.size)
        println(res)
        /*for (p in res){
            for(ch in p){
                print(ch.toInt())
                print(", ")
            }
            print("||")
        }*/
    }

    var res = mutableSetOf<List<Edge>>()
    fun allPermutations(list: List<Char>): Set<List<Char>> {
        if (list.isEmpty()) return setOf(emptyList())

        val result: MutableSet<List<Char>> = mutableSetOf()
        for (i in 0 until list.size) {
            allPermutations(list - list[i]).forEach{
                    item -> result.add(item + list[i])
            }
        }
        return result
    }

    fun getAllPairs(list: List<Int>): MutableList<List<Int>> {
        val pairs = mutableListOf<List<Int>>()
        for (i in 0 until list.size-1){
            for (j in i+1 until list.size){
                pairs.add(listOf(list[i], list[j]))
            }
        }
        return pairs
    }

    fun generateAllCombinations(n: Int, r: Int): List<List<Int>> {
        val combinations: MutableList<List<Int>> = mutableListOf()
        helper(combinations, MutableList(r){0}, 0, n - 1, 0)
        return combinations
    }

    private fun helper(combinations: MutableList<List<Int>>, data: MutableList<Int>, start: Int, end: Int, index: Int) {
        if (index == data.size) {
            val combination = data.toMutableList()
            combinations.add(combination)
        } else if (start <= end) {
            data[index] = start
            helper(combinations, data, start + 1, end, index + 1)
            helper(combinations, data, start + 1, end, index)
        }
    }

    fun getCorrectCombinations(pairs: MutableList<List<Int>>, comb: List<List<Int>>) {
        val b= comb.map { c ->  val tmp = c.map { i -> pairs[i] }.flatten()
            if (tmp.size != tmp.toSet().size) 0 else tmp.chunked(2)}.filter { it != 0 }
        println(b.size)
    }
}