import java.io.File

//[[1,2,3],[4,5,6],[7,8,9],[10,0,1], [2,3,7],[10,9,8], [4,2,5], [10,6,3], [10,6,5], [4,1,8], [7,9,1]]
//[[1,2,3], [0,2,4], [0,1,5],[0,4,5], [1,3,5],[2,3,4]]
//[[1,2,3, 4,5], [0,2,4,6,7], [0,1,5,9,10],[0,4,5,11,15], [1,3,5,12,14],[12,11,13, 14, 6], [10,8,9, 2, 3], [13,14,15, 7, 8],[10,14,15, 11, 10], [9,7,8, 12, 13],[12,13,14, 3, 4]]
//[[1,2,3],[0,2,3],[1,0,3],[0,1,2]]
//[[2,1,7],[2,0,3],[0,1,3],[1,2,4],[3,5,6],[4,6,7],[5,7,4],[6,0,5]]
//petersen [[5, 6, 4], [4, 7, 8], [7, 9, 5], [6, 8, 9], [1, 9, 0], [0, 8, 2], [0, 3, 7], [1, 2, 6], [1, 3, 5], [2, 3, 4]]
//[[2,6,9],[2,5,7],[0,1,3],[2,4,8],[3,5,6],[1,9,4],[4,7,0],[1,8,6],[3,9,7],[0,5,8]]
//[[6, 7, 1], [8, 9, 0], [6, 8, 10], [7, 11, 12], [9, 11, 13], [10, 12, 13], [0, 2, 11], [0, 3, 10], [1, 2, 13], [1, 4, 12], [2, 5, 7], [3, 4, 6], [3, 5, 9], [4, 5, 8]]
//[ [ 6, 11, 3 ], [ 4, 8, 7 ], [ 9, 10, 5 ], [ 9, 8, 0 ], [ 11, 10, 1 ], [ 6, 7, 2 ], [ 0, 5, 9 ], [ 10, 5, 1 ], [ 3, 1, 11 ], [ 2, 3, 6 ], [ 4, 2, 7 ], [ 0, 4, 8 ] ]
//[ [ 2, 4, 7 ], [ 6, 8, 9 ], [ 0, 5, 7 ], [ 7, 10, 11 ], [ 5, 0, 9 ], [ 9, 2, 4 ], [ 1, 8, 10 ], [ 0, 2, 3 ], [ 1, 6, 11 ], [ 1, 4, 5 ], [ 11, 6, 3 ], [ 10, 8, 3 ] ]
//heawood -
// [ [ 7, 8, 9 ], [ 7, 10, 11 ], [ 7, 12, 13 ], [ 8, 10, 12 ], [ 8, 11, 13 ], [ 9, 10, 13 ], [ 9, 11, 12 ], [ 0, 1, 2 ], [ 0, 3, 4 ], [ 0, 5, 6 ],[ 1, 3, 5 ], [ 1, 4, 6 ], [ 2, 3, 6 ], [ 2, 4, 5 ] ]
// mcgee -
//[ [ 16, 4, 2 ], [ 22, 3, 2 ], [ 1, 0, 23 ], [ 1, 5, 17 ], [ 12, 0, 15 ],[ 3, 7, 14 ], [ 14, 10, 13 ], [ 5, 9, 12 ], [ 10, 12, 22 ], [ 7, 11, 23 ],[ 6, 8, 21 ], [ 9, 13, 20 ], [ 8, 4, 7 ], [ 11, 15, 6 ], [ 16, 6, 5 ],[ 13, 17, 4 ], [ 14, 18, 0 ], [ 19, 15, 3 ], [ 16, 20, 19 ],[ 21, 17, 18 ], [ 18, 22, 11 ], [ 23, 19, 10 ], [ 8, 20, 1 ], [ 2, 21, 9 ] ]
// n(3,11)-
//[ [ 31, 49, 84 ], [ 42, 60, 110 ], [ 50, 80, 90 ], [ 43, 59, 97 ], [ 42, 67, 75 ], [ 48, 94, 103 ], [ 39, 71, 77 ], [ 37, 69, 108 ], [ 56, 91, 111 ], [ 59, 70, 101 ], [ 62, 89, 102 ], [ 38, 53, 74 ], [ 19, 62, 75 ], [ 21, 54, 108 ], [ 24, 53, 107 ], [ 48, 85, 109 ], [ 18, 38, 40 ], [ 18, 42, 72 ], [ 16, 17, 81 ], [ 12, 74, 100 ], [ 41, 54, 88 ], [ 13, 27, 100 ], [ 41, 53, 82 ], [ 29, 41, 60 ], [ 14, 47, 103 ], [ 39, 83, 106 ], [ 45, 78, 86 ], [ 21, 44, 48 ], [ 51, 55, 71 ], [ 23, 32, 109 ], [ 79, 94, 96 ], [ 0, 47, 51 ], [ 29, 64, 84 ], [ 66, 92, 107 ], [ 45, 52, 106 ], [ 64, 87, 98 ], [ 44, 92, 110 ], [ 7, 68, 107 ], [ 11, 16, 98 ], [ 6, 25, 87 ], [ 16, 76, 85 ], [ 20, 22, 23 ], [ 1, 4, 17 ], [ 3, 88, 105 ], [ 27, 36, 61 ], [ 26, 34, 60 ], [ 69, 90, 96 ], [ 24, 31, 52 ], [ 5, 15, 27 ], [ 0, 81, 99 ], [ 2, 75, 91 ], [ 28, 31, 56 ], [ 34, 47, 104 ], [ 11, 14, 22 ], [ 13, 20, 56 ], [ 28, 95, 110 ], [ 8, 51, 54 ], [ 76, 88, 89 ], [ 78, 94, 102 ], [ 3, 9, 73 ], [ 1, 23, 45 ], [ 44, 80, 99 ], [ 10, 12, 84 ], [ 66, 70, 91 ], [ 32, 35, 69 ], [ 77, 101, 102 ], [ 33, 63, 83 ], [ 4, 103, 105 ], [ 37, 86, 93 ], [ 7, 46, 64 ], [ 9, 63, 109 ], [ 6, 28, 93 ], [ 17, 101, 108 ], [ 59, 74, 95 ], [ 11, 19, 73 ], [ 4, 12, 50 ], [ 40, 57, 104 ], [ 6, 65, 82 ], [ 26, 58, 111 ], [ 30, 81, 83 ], [ 2, 61, 82 ], [ 18, 49, 79 ], [ 22, 77, 80 ], [ 25, 66, 79 ], [ 0, 32, 62 ], [ 15, 40, 93 ], [ 26, 68, 97 ], [ 35, 39, 105 ], [ 20, 43, 57 ], [ 10, 57, 92 ], [ 2, 46, 104 ], [ 8, 50, 63 ], [ 33, 36, 89 ], [ 68, 71, 85 ], [ 5, 30, 58 ], [ 55, 73, 96 ], [ 30, 46, 95 ], [ 3, 86, 99 ], [ 35, 38, 111 ], [ 49, 61, 97 ], [ 19, 21, 106 ], [ 9, 65, 72 ], [ 10, 58, 65 ], [ 5, 24, 67 ], [ 52, 76, 90 ], [ 43, 67, 87 ], [ 25, 34, 100 ], [ 14, 33, 37 ], [ 7, 13, 72 ], [ 15, 29, 70 ], [ 1, 36, 55 ], [ 8, 78, 98 ] ]
//mcgee excess 2 -
//[ [ 4, 16, 24 ], [ 2, 3, 22 ], [ 1, 24, 25 ], [ 1, 5, 17 ], [ 0, 12, 15 ], [ 3, 7, 14 ], [ 10, 14, 25 ], [ 5, 9, 12 ], [ 10, 12, 22 ], [ 7, 11, 23 ],[ 6, 8, 21 ], [ 9, 13, 20 ], [ 4, 7, 8 ], [ 11, 15, 25 ], [ 5, 6, 16 ], [ 4, 13, 17 ], [ 0, 14, 18 ], [ 3, 15, 19 ], [ 16, 19, 20 ], [ 17, 18, 21 ], [ 11, 18, 22 ], [ 10, 19, 23 ], [ 1, 8, 20 ], [ 9, 21, 24 ],[ 0, 2, 23 ], [ 2, 6, 13 ] ]
//mcgee excess 4-
//[ [ 4, 16, 24 ], [ 3, 22, 26 ], [ 25, 26, 27 ], [ 1, 5, 17 ], [ 0, 12, 15 ], [ 3, 7, 14 ], [ 10, 14, 25 ], [ 5, 9, 27 ], [ 10, 12, 22 ], [ 7, 11, 23 ], [ 6, 8, 21 ], [ 9, 13, 20 ], [ 4, 8, 27 ], [ 11, 15, 25 ], [ 5, 6, 16 ], [ 4, 13, 17 ], [ 0, 14, 18 ], [ 3, 15, 19 ], [ 16, 19, 20 ],  [ 17, 18, 21 ], [ 11, 18, 22 ], [ 10, 19, 23 ], [ 1, 8, 20 ], [ 9, 21, 24 ], [ 0, 23, 26 ], [ 2, 6, 13 ], [ 1, 2, 24 ], [ 2, 7, 12 ] ]
//petersen excess 2-
//[ [ 9, 10, 11 ], [ 2, 5, 10 ], [ 1, 3, 11 ], [ 2, 4, 8 ], [ 3, 5, 6 ], [ 1, 4, 9 ], [ 4, 7, 10 ], [ 6, 8, 11 ],[ 3, 7, 9 ], [ 0, 5, 8 ], [ 0, 1, 6 ], [ 0, 2, 7 ] ]
//petersen exces 2-
//[ [ 9, 10, 11 ], [ 2, 5, 10 ], [ 1, 3, 11 ], [ 2, 4, 8 ], [ 3, 5, 6 ], [ 1, 4, 9 ], [ 4, 7, 11 ], [ 6, 8, 10 ], [ 3, 7, 9 ], [ 0, 5, 8 ], [ 0, 1, 7 ], [ 0, 2, 6 ] ]
//mcgee excess 6_1-
//[ [ 16, 24, 28 ], [ 3, 22, 26 ], [ 25, 26, 27 ], [ 1, 5, 17 ], [ 12, 15, 29 ], [ 3, 7, 14 ], [ 10, 25, 28 ], [ 5, 9, 27 ], [ 12, 22, 28 ], [ 7, 11, 23 ],[ 6, 21, 29 ], [ 9, 13, 20 ], [ 4, 8, 27 ], [ 11, 15, 25 ], [ 5, 16, 29 ], [ 4, 13, 17 ], [ 0, 14, 18 ], [ 3, 15, 19 ], [ 16, 19, 20 ], [ 17, 18, 21 ], [ 11, 18, 22 ], [ 10, 19, 23 ], [ 1, 8, 20 ],[ 9, 21, 24 ], [ 0, 23, 26 ], [ 2, 6, 13 ], [ 1, 2, 24 ], [ 2, 7, 12 ], [ 0, 6, 8 ], [ 4, 10, 14 ] ]
//mcgee excess 6_2-
//[ [ 16, 24, 28 ], [ 3, 22, 26 ], [ 25, 26, 27 ], [ 1, 5, 17 ], [ 12, 15, 28 ], [ 3, 7, 14 ], [ 10, 14, 25 ], [ 5, 9, 29 ], [ 10, 12, 22 ], [ 7, 11, 23 ],[ 6, 8, 21 ], [ 9, 13, 20 ], [ 4, 8, 27 ], [ 11, 15, 25 ], [ 5, 6, 28 ], [ 4, 13, 17 ], [ 0, 18, 29 ], [ 3, 15, 19 ], [ 16, 19, 20 ], [ 17, 18, 21 ], [ 11, 18, 22 ], [ 10, 19, 23 ], [ 1, 8, 20 ],[ 9, 21, 24 ], [ 0, 23, 26 ], [ 2, 6, 13 ], [ 1, 2, 24 ], [ 2, 12, 29 ], [ 0, 4, 14 ], [ 7, 16, 27 ] ]
//mcgee excess 6_3-
//[ [ 16, 24, 28 ], [ 3, 22, 26 ], [ 25, 26, 27 ], [ 1, 5, 17 ], [ 12, 15, 29 ], [ 3, 7, 14 ], [ 10, 14, 25 ], [ 5, 9, 27 ], [ 10, 12, 22 ], [ 7, 11, 28 ],[ 6, 8, 21 ], [ 9, 20, 29 ], [ 4, 8, 27 ], [ 15, 25, 28 ], [ 5, 6, 16 ], [ 4, 13, 17 ], [ 0, 14, 18 ], [ 3, 15, 19 ], [ 16, 19, 20 ], [ 17, 18, 21 ], [ 11, 18, 22 ], [ 10, 19, 23 ], [ 1, 8, 20 ],[ 21, 24, 29 ], [ 0, 23, 26 ], [ 2, 6, 13 ], [ 1, 2, 24 ], [ 2, 7, 12 ], [ 0, 9, 13 ], [ 4, 11, 23 ] ]
//mcgee excess 6_4-
//[ [ 4, 24, 28 ], [ 3, 22, 28 ], [ 25, 26, 27 ], [ 1, 5, 17 ], [ 0, 12, 15 ], [ 3, 7, 14 ], [ 10, 14, 25 ], [ 5, 9, 27 ], [ 10, 12, 22 ], [ 7, 11, 23 ],[ 6, 8, 21 ], [ 9, 13, 20 ], [ 4, 8, 27 ], [ 11, 15, 25 ], [ 5, 6, 16 ], [ 4, 13, 17 ], [ 14, 18, 29 ], [ 3, 15, 19 ], [ 16, 19, 20 ], [ 17, 18, 21 ], [ 11, 18, 28 ], [ 10, 19, 23 ], [ 1, 8, 29 ],[ 9, 21, 24 ], [ 0, 23, 26 ], [ 2, 6, 13 ], [ 2, 24, 29 ], [ 2, 7, 12 ], [ 0, 1, 20 ], [ 16, 22, 26 ] ]
//mcgee excess 6_5-
//[ [ 4, 24, 28 ], [ 3, 22, 26 ], [ 25, 27, 29 ], [ 1, 5, 17 ], [ 0, 12, 15 ], [ 3, 7, 14 ], [ 10, 14, 25 ], [ 5, 9, 27 ], [ 10, 12, 22 ], [ 7, 11, 23 ],[ 6, 8, 21 ], [ 9, 13, 20 ], [ 4, 8, 27 ], [ 11, 15, 25 ], [ 5, 6, 16 ], [ 4, 13, 17 ], [ 14, 18, 28 ], [ 3, 15, 19 ], [ 16, 19, 29 ], [ 17, 18, 21 ], [ 11, 22, 28 ], [ 10, 19, 23 ], [ 1, 8, 20 ],[ 9, 21, 24 ], [ 0, 23, 26 ], [ 2, 6, 13 ], [ 1, 24, 29 ], [ 2, 7, 12 ], [ 0, 16, 20 ], [ 2, 18, 26 ] ]
//mcgee excess 6_6-
//[ [ 4, 24, 28 ], [ 3, 22, 26 ], [ 25, 26, 29 ], [ 1, 5, 17 ], [ 0, 12, 15 ], [ 3, 7, 14 ], [ 10, 14, 25 ], [ 5, 9, 27 ], [ 10, 12, 22 ], [ 7, 11, 23 ],[ 6, 8, 21 ], [ 9, 13, 20 ], [ 4, 8, 27 ], [ 11, 15, 25 ], [ 5, 6, 16 ], [ 4, 13, 17 ], [ 14, 18, 28 ], [ 3, 15, 19 ], [ 16, 19, 29 ], [ 17, 18, 21 ], [ 11, 22, 28 ], [ 10, 19, 23 ], [ 1, 8, 20 ],[ 9, 21, 24 ], [ 0, 23, 26 ], [ 2, 6, 13 ], [ 1, 2, 24 ], [ 7, 12, 29 ], [ 0, 16, 20 ], [ 2, 18, 27 ] ]
fun main() {
    val main = Main()
    while (true) {
        print("Put adjacency list or other: ")
        val input: String? = readLine()

        when (input) {
            "exit" -> break
            "splitEdges" -> main.splitEdges()
            "splitTwoEdges" -> main.splitTwoEdges()
            "excision" -> main.exciseVertices()
            else -> main.getGraphFromInput(input!!)
        }
    }
}

class Main() {
    /*
       input graph as adjacency list
       TODO make input from console
        */
    //val input = listOf(listOf(2, 3, 4), listOf(1, 3, 4), listOf(1, 2, 4), listOf(1, 2, 3)) //square
    //private val input = listOf(listOf(1,2,3), listOf(0,2,4), listOf(0,1,5), listOf(0,4,5), listOf(1,3,5), listOf(2,3,4)) //prism
    //945 originally, 78 after girth = 4-> 48, 5-> 30
    private var Graphs: MutableList<Graph> = mutableListOf()
    fun getGraphFromInput(input: String) {
        println("---Graph---")
        val adjList =
            input.filter { it != ' ' }.split("],[").map { s -> s.filter { it != ']' && it != '[' }.split(",") }
                .map { it.map { s -> s.toInt() } }
        //creating original graph object from inputted parameters
        pushToGraphs(listOf(Graph(adjList)))
    }
    fun splitEdges() {
        if (Graphs.isNotEmpty()) {
            println("---Splitting---")
            //newGraph = Graph(originalGraph.vertices, originalGraph.edges)
            getGraphsFromSplitting(Graphs.first().girth + 1)
            printToFile("split.txt")
        }
    }

    fun splitTwoEdges() {
        println("---Splitting Two Edges---")
        val newGraphs = mutableListOf<Graph>()
        for (graph in Graphs) {
            val listOfPairs: MutableList<List<Edge>> = graph.combinationsOfPairsOfEdges()
            val originGraph = Graph(graph)
            for (edges in listOfPairs) {
                newGraphs.addAll(getNewGraphs(originGraph, originGraph.edges.filter { it !in edges }, originGraph.girth))
            }
        }
        pushToGraphs(newGraphs)
        printToFile("splitTwoEdges-${Graphs.first().vertices.size}.txt")
    }

    private fun getGraphsFromSplitting(neededGirth: Int){
        val edgesToSkip: List<Edge>

        //for even number of edges, we need to skip one edge from splitting and create one less new vertex
        if (Graphs.first().edges.size % 2 == 1) {
            edgesToSkip = Graphs.first().edges.toList()
        } else
            edgesToSkip = listOf(Edge(Vertex(-1), Vertex(-1)))

        val originGraph = Graph(Graphs.first())
        val newGraphs = mutableListOf<Graph>()
        //creating new graph from the original graph
        for (skip in edgesToSkip) {
            newGraphs.addAll(getNewGraphs(originGraph, listOf(skip), neededGirth))
        }
        pushToGraphs(newGraphs)
    }

    private fun getNewGraphs(originGraph: Graph, skippedEdges: List<Edge>, neededGirth: Int): MutableList<Graph> {
        val nGraph = Graph(originGraph)
        val newGraphs = mutableListOf<Graph>()
        nGraph.addNewVertices(
            nGraph.edges.filter { it !in skippedEdges },
            skippedEdges,
            nGraph.edges.size - skippedEdges.size
        )
        val edgesCombinations = nGraph.getCombinationsOfEdges(neededGirth)
        if (edgesCombinations.size == 0)
            println("No graphs with girth ${neededGirth} or bigger")
        else {
            for (comb in edgesCombinations) {
                newGraphs.add(Graph(nGraph.vertices, nGraph.edges.plus(comb)))
            }
        }
        return newGraphs
    }

    fun exciseVertices() {
        if (Graphs.isNotEmpty()) {
            //println("Graph adjacency list: ${newGraph.adjacencyList}")
            //println("New Graphs (size): ${newGraphs.size}")
            println("---Excision---")
            val newGraphs = mutableListOf<Graph>()
            for (graph in Graphs) {
                newGraphs.addAll(excision(graph, graph.girth + 1))
            }
            pushToGraphs(newGraphs)
        }
        printToFile("excision.txt")
    }

    private fun excision(graph: Graph, neededGirth: Int): MutableList<Graph> {
        val newGraphs = mutableListOf<Graph>()
        graph.deleteVerticesAndEdges()
        val edgesCombinations = graph.getCombinationsOfEdges(neededGirth)
        if (edgesCombinations.size == 0)
            println("No graphs with girth ${neededGirth} or bigger")
        //println("Combinations of Edges (size): ${edgesCombinations.size}")
        else {
            for (comb in edgesCombinations) {
                newGraphs.add(Graph(graph.vertices, graph.edges.plus(comb)))
            }
        }
        return newGraphs
    }

    private fun printToFile(s: String) {
        File(s).printWriter().use { out ->
            Graphs.forEach { graph ->
                val builder = StringBuilder()
                graph.edges.forEach { e -> builder.append("${e.vertex1.name} ${e.vertex2.name}  ${e.vertex2.name} ${e.vertex1.name}  ") }
                builder.delete(builder.length - 2, builder.length)
                out.write(builder.toString())
                out.println()
            }
            out.close()
        }
        //out.write(newGraph.cycles.toString())
        //out.newLine()
        //}
    }

    private fun createNewGraphs(
        edges: MutableSet<Edge>,
        vertices: MutableList<Vertex>,
        combinations: MutableSet<List<Edge>>
    ): MutableList<Graph> {
        val newGraphs = mutableListOf<Graph>()
        combinations.forEach { comb -> newGraphs.add(Graph(vertices, edges.plus(comb))) }
        return newGraphs
    }

    private fun printGraph(graph: Graph) {
        println(graph.vertices)
        println(graph.edges)
    }

    fun pushToGraphs(graphs:List<Graph>){
        Graphs.clear()
        Graphs.addAll(graphs)
    }

}