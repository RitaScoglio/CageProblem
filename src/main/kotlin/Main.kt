import java.io.File

//[[1,2,3], [0,2,4], [0,1,5],[0,4,5], [1,3,5],[2,3,4]]

fun main() {
    val input: String? = readLine()
    val adjList = input!!.filter { it != ' ' }.split("],[").map { s -> s.filter { it != ']' && it != '[' }.split(",") }
    val main = Main(adjList)
    main.run()
}

class Main(input: List<List<String>>) {

   /* init{
        input.map { it.map { s -> s.toInt() } }.also { this.input = it }
    }*/

    /*
       input graph as adjacency list
       TODO make input from console
        */
    //val input = listOf(listOf(2, 3, 4), listOf(1, 3, 4), listOf(1, 2, 4), listOf(1, 2, 3)) //square
    private val input = listOf(listOf(1,2,3), listOf(0,2,4), listOf(0,1,5), listOf(0,4,5), listOf(1,3,5), listOf(2,3,4)) //prism
    //945 originally, 78 after girth = 4-> 48, 5-> 30
    //var input: List<List<Int>>
    private lateinit var originalGraph : Graph
    private lateinit var newGraphs : MutableList<Graph>

    fun run(){
        getGraphFromInput()
        getNewGraphs()
        println("New Graphs (size): ${newGraphs.size}")
        printToFile()
    }

    private fun printToFile() {
        File("somefile.txt").bufferedWriter().use { out ->
            newGraphs.forEach { graph -> val builder = StringBuilder()
                graph.edges.forEach { e -> builder.append("${e.vertex1.name} ${e.vertex2.name}  ${e.vertex2.name} ${e.vertex1.name}  ") }
                builder.delete(builder.length-2, builder.length)
                out.write(builder.toString())
                out.newLine()
            }
        }
    }

    private fun getGraphFromInput(){
        //creating original graph object from inputted parameters
        originalGraph = Graph(input)
    }

    private fun getNewGraphs(){
        newGraphs = mutableListOf()
        val edgesToSkip: List<Edge>
        var numberOfNewVertices = originalGraph.vertices.size / 2 * 3

        //for even number of edges, we need to skip one edge from splitting and create one less new vertex
        if (originalGraph.edges.size % 2 == 1) {
            edgesToSkip = originalGraph.edges.toList()
            numberOfNewVertices -= 1
        } else
            edgesToSkip = listOf(Edge(Vertex(-1), Vertex(-1)))

        //creating new graph from the original graph
        for (skip in edgesToSkip) {
            val newGraph = Graph(originalGraph.vertices, originalGraph.edges)
            newGraph.addNewVertices(skip, numberOfNewVertices)
            val edgesCombinations = newGraph.getCombinationsOfEdges()
            println("Combinations of Edges (size): ${edgesCombinations.size}")
            newGraphs.addAll(createNewGraphs(newGraph.edges, newGraph.vertices, edgesCombinations))
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