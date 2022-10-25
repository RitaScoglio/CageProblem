import java.io.File
//[[1,2,3],[4,5,6],[7,8,9],[10,0,1], [2,3,7],[10,9,8], [4,2,5], [10,6,3], [10,6,5], [4,1,8], [7,9,1]]
//[[1,2,3], [0,2,4], [0,1,5],[0,4,5], [1,3,5],[2,3,4]]
//[[1,2,3, 4,5], [0,2,4,6,7], [0,1,5,9,10],[0,4,5,11,15], [1,3,5,12,14],[12,11,13, 14, 6], [10,8,9, 2, 3], [13,14,15, 7, 8],[10,14,15, 11, 10], [9,7,8, 12, 13],[12,13,14, 3, 4]]
//[[1,2,3],[0,2,3],[1,0,3],[0,1,2]]
//[[2,1,7],[2,0,3],[0,1,3],[1,2,4],[3,5,6],[4,6,7],[5,7,4],[6,0,5]]
//[[6, 7, 1], [8, 9, 0], [6, 8, 10], [7, 11, 12], [9, 11, 13], [10, 12, 13], [0, 2, 11], [0, 3, 10], [1, 2, 13], [1, 4, 12], [2, 5, 7], [3, 4, 6], [3, 5, 9], [4, 5, 8]]
fun main() {
    while (true) {
        print("Put adjacency list or exit: ")
        val input: String? = readLine()
        if (input == "exit")
            break
        val adjList =
            input!!.filter { it != ' ' }.split("],[").map { s -> s.filter { it != ']' && it != '[' }.split(",") }
        val main = Main(adjList)
        main.run()
    }
}

class Main(input: List<List<String>>) {

    init{
        input.map { it.map { s -> s.toInt() } }.also { this.input = it }
    }

    /*
       input graph as adjacency list
       TODO make input from console
        */
    //val input = listOf(listOf(2, 3, 4), listOf(1, 3, 4), listOf(1, 2, 4), listOf(1, 2, 3)) //square
    //private val input = listOf(listOf(1,2,3), listOf(0,2,4), listOf(0,1,5), listOf(0,4,5), listOf(1,3,5), listOf(2,3,4)) //prism
    //945 originally, 78 after girth = 4-> 48, 5-> 30
    var input: List<List<Int>>
    private lateinit var originalGraph : Graph
    private lateinit var newGraph : Graph

    fun run(){
        println("---Original Graph---")
        getGraphFromInput()
        println("---Splitting---")
        getNewGraphs()
        if (this::newGraph.isInitialized) {
            println("Graph adjacency list: ${newGraph.adjacencyList}")
            //println("New Graphs (size): ${newGraphs.size}")
            printToFile()
            println("---Excision---")
            excision()
            println("Graph adjacency list: ${newGraph.adjacencyList}")
        }
        println("----------")
    }

    private fun excision() {
        newGraph.deleteVerticesAndEdges()
        val edgesCombinations = newGraph.getCombinationsOfEdges(newGraph.girth+1)
        if (edgesCombinations.size == 0)
            println("No graphs with girth ${newGraph.girth+1} or bigger")
        //println("Combinations of Edges (size): ${edgesCombinations.size}")
        else {
            newGraph = Graph(newGraph.vertices, newGraph.edges.plus(edgesCombinations))
        }
    }

    private fun printToFile() {
        File("somefile.txt").bufferedWriter().use { out ->
            /*newGraphs.forEach { graph -> val builder = StringBuilder()
                graph.edges.forEach { e -> builder.append("${e.vertex1.name} ${e.vertex2.name}  ${e.vertex2.name} ${e.vertex1.name}  ") }
                builder.delete(builder.length-2, builder.length)
                out.write(builder.toString())
                out.newLine()
            }
        }*/
            out.write(newGraph.cycles.toString())
            out.newLine()
        }
    }

    private fun getGraphFromInput(){
        //creating original graph object from inputted parameters
        originalGraph = Graph(input)
    }

    private fun getNewGraphs(){
        val edgesToSkip: List<Edge>
        var numberOfNewVertices = originalGraph.edges.size

        //for even number of edges, we need to skip one edge from splitting and create one less new vertex
        if (originalGraph.edges.size % 2 == 1) {
            edgesToSkip = originalGraph.edges.toList()
            numberOfNewVertices -= 1
        } else
            edgesToSkip = listOf(Edge(Vertex(-1), Vertex(-1)))

        //creating new graph from the original graph
        for (skip in edgesToSkip) {
            val nGraph = Graph(originalGraph)
            //println("Skip Edge: $skip")
            nGraph.addNewVertices(skip, numberOfNewVertices)
            val edgesCombinations = nGraph.getCombinationsOfEdges(nGraph.girth+1)
            if (edgesCombinations.size == 0)
                println("No graphs with girth ${nGraph.girth+1} or bigger")
            //println("Combinations of Edges (size): ${edgesCombinations.size}")
            else {
                newGraph = Graph(nGraph.vertices, nGraph.edges.plus(edgesCombinations))
                println("Skipped edge: ${skip}")
                break
            }
        }
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
}