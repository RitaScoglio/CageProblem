import java.lang.Math.min
import java.util.LinkedList

class Graph {
    constructor(input: List<List<Int>>) {
        createOriginalGraph(input)
        this.adjacencyList = createAdjacencyList()
        //println(adjacencyList)
        girth = computeGirth(adjacencyList)
        println("Girth: $girth")
        cycles = computeAllCycles(adjacencyList)
        val filtered = cycles.filter { it.length == girth }
        println("Cycles of girth size: ${filtered}")
        println("Number of cycles: ${filtered.size}")
    }

    constructor(vertices: MutableList<Vertex>, edges: Set<Edge>) {
        this.vertices.addAll(vertices)
        this.edges.addAll(edges)
        this.adjacencyList = createAdjacencyList()
        girth = computeGirth(adjacencyList)
        println("Girth: $girth")
        cycles = computeAllCycles(adjacencyList)
        val filtered = cycles.filter { it.length == girth }
        println("Cycles of girth size: ${filtered}")
        println("Number of cycles: ${filtered.size}")
        //println(cycles)
    }

    constructor(graph: Graph) {
        this.vertices.addAll(graph.vertices)
        this.edges.addAll(graph.edges)
        this.girth = graph.girth
        this.cycles = graph.cycles
    }

    lateinit var adjacencyList: List<MutableList<Int>>
    var vertices = mutableListOf<Vertex>()
    var edges = mutableSetOf<Edge>()
    var newVertices = mutableListOf<Vertex>()
    var girth: Int
    var cycles: List<Cycle>

    private fun createAdjacencyList(newEdges: List<Edge> = listOf()): List<MutableList<Int>> {
        val adjacency: List<MutableList<Int>> = List(vertices.size) { mutableListOf() }
        for (edge in edges.plus(newEdges)) {
            adjacency[edge.vertex1.name].add(edge.vertex2.name)
            adjacency[edge.vertex2.name].add(edge.vertex1.name)
        }
        return adjacency
    }

    fun createOriginalGraph(input: List<List<Int>>) {
        //creates vertices from 1 to the number of all vertices
        vertices.addAll((0 until input.size).map { Vertex(it) }.toList())
        //creates edges based on vertices and adjacency list
        vertices.map { v1 -> input[v1.name].map { v2 -> edges.add(Edge(v1, vertices[v2])) } }
    }

    fun addNewVertices(
        skipEdge: Edge,
        numberOfNewVertices: Int
    ) {

        var currentNewVertex = vertices.size
        //adding new vertices to edges
        vertices.addAll((currentNewVertex until vertices.size + numberOfNewVertices).map { Vertex(it) }.toList())
        val newEdges = mutableSetOf<Edge>()
        //create splitted edges
        edges.map { e ->
            if (e != skipEdge) {
                newEdges.add(Edge(e.vertex1, vertices[currentNewVertex]))
                newEdges.add(Edge(e.vertex2, vertices[currentNewVertex]))
                newVertices.add(vertices[currentNewVertex])
                currentNewVertex++
            }
        }
        //adding the edge we skip from splitting
        if (skipEdge.vertex1.name != -1)
            newEdges.add(skipEdge)
        edges = newEdges
    }

    fun getCombinationsOfEdges(searchedGirth:Int): List<Edge> {
        val possibleEdges = combinationsOfVertices()
        //set to save possible compositions of edges
        //val allCompositions = mutableSetOf<List<Edge>>()
        //save the needed graph
        var graph = listOf<Edge>()
        //for each edge in possible edges, we get the rest of the compositions to it
        for (indexOfEdge in 0..possibleEdges.size - (newVertices.size / 2)) {
            //current composition of edges
            val compositionOfEdges = mutableListOf<Edge>()
            //indexes od edges in possibleEdges
            val indexes = mutableListOf<Int>()
            //already used vertices for the edge's composition - to avoid degree bigger than 3
            val usedVertices = mutableListOf<Vertex>()
            compositionOfEdges.add(possibleEdges[indexOfEdge])
            indexes.add(indexOfEdge)
            usedVertices.add(possibleEdges[indexOfEdge].vertex1)
            usedVertices.add(possibleEdges[indexOfEdge].vertex2)
            var startIndex = indexOfEdge
            do {
                //start index is always 1 bigger than the index of last edge in list
                var currentIndex = startIndex + 1
                do {
                    if (!compositionOfEdges.contains(possibleEdges[currentIndex]) &&
                        !usedVertices.contains(possibleEdges[currentIndex].vertex1) &&
                        !usedVertices.contains(possibleEdges[currentIndex].vertex2)
                    ) {
                        //add new edge to composition
                        compositionOfEdges.add(possibleEdges[currentIndex])
                        indexes.add(currentIndex)
                        usedVertices.add(possibleEdges[currentIndex].vertex1)
                        usedVertices.add(possibleEdges[currentIndex].vertex2)
                    }
                    currentIndex++
                } while (compositionOfEdges.size < newVertices.size / 2 && currentIndex < possibleEdges.size)
                //add new composition to all
                if (compositionOfEdges.size == newVertices.size / 2) {
                    val adjacencyList = createAdjacencyList(compositionOfEdges.toList())
                    val newGirth = computeGirth(adjacencyList)
                    if (newGirth == searchedGirth) {
                        graph = compositionOfEdges.toList()
                        break
                    }
                }
                //remove last or last 2 edges from the composition to get a new
                //if the last edge in composition was also last in possibleEdges, we distract two as there is no more possibilities for the last
                val toRemove = if (indexes.last() == possibleEdges.size - 1) 2 else 1
                //save startIndex from the last deleted edge
                startIndex = indexes[indexes.size - toRemove]
                for (n in 1..toRemove) {
                    indexes.removeLast()
                    compositionOfEdges.removeLast()
                    usedVertices.removeLast()
                    usedVertices.removeLast()
                }
            } while (compositionOfEdges.isNotEmpty() && graph.isEmpty())
        }
        return graph
    }

    //compute all possible edges - pairs of vertices
    fun combinationsOfVertices(): MutableList<Edge> {
        val possibleEdges = mutableListOf<Edge>()
        for (v1 in 0 until newVertices.size-1) {
            for (v2 in v1 + 1 until newVertices.size) {
                possibleEdges.add(Edge(newVertices[v1], newVertices[v2]))
            }
        }
        return possibleEdges
    }

    private fun computeGirth(adjacencyList: List<MutableList<Int>>): Int {
        var shortestCycle = Integer.MAX_VALUE
        var distance = MutableList(vertices.size) { Integer.MAX_VALUE }
        for (i in 0 until vertices.size) {
            distance = MutableList(vertices.size) { Integer.MAX_VALUE }
            val parent = MutableList(vertices.size) { Integer.MIN_VALUE }
            distance[i] = 0
            val queue = LinkedList<Int>()
            queue.add(i)
            while (!queue.isEmpty()) {
                val first = queue.poll()
                for (child in adjacencyList[first]) {
                    if (distance[child] == Integer.MAX_VALUE) {
                        distance[child] = 1 + distance[first]
                        parent[child] = first
                        queue.add(child)
                    } else if (parent[first] != child && parent[child] != first) {
                        shortestCycle = min(shortestCycle, distance[first] + distance[child] + 1)
                    }
                }
            }
        }
        if (shortestCycle == Integer.MAX_VALUE)
            return -1
        else
            return shortestCycle
    }

    private fun computeAllCycles(adjacencyList: List<MutableList<Int>>): List<Cycle> {
        val cycles = mutableSetOf<Cycle>()
        //for (i in 0 until vertices.size) {
        //distance = MutableList(vertices.size){Integer.MAX_VALUE}
        //val parent = MutableList(vertices.size){Integer.MIN_VALUE}
        //vertex and current path to it
        val queue = LinkedList<Pair<Int, MutableList<Int>>>()
        queue.add(Pair(0, mutableListOf<Int>()))
        while (!queue.isEmpty()) {
            val (vertex, path) = queue.poll()
            //vertex already in path?
            if (path.contains(vertex)) {
                cycles.add(getCycle(path, vertex))
            } else { //add to path and send to children
                val newPath = path.toMutableList()
                newPath.add(vertex)
                for (child in adjacencyList[vertex]) {
                    queue.add(Pair(child, newPath))
                }
            }
            // }
        }
        return cycles.filter { it.length > 2 }
    }

    private fun getCycle(path: MutableList<Int>, vertex: Int): Cycle {
        return Cycle(path.subList(path.indexOf(vertex), path.size))
    }

    fun deleteVerticesAndEdges() {
        val cycleToDelete: MutableList<Int> = cycles.filter { it.length == girth }[0].vertices
        val newListOfVertices: List<Vertex> = vertices.filter { it.name !in cycleToDelete }
        val newMapping = mutableMapOf<Vertex, Vertex>()
        var i = 0
        newListOfVertices.map { x -> newMapping.put(x, vertices[i]); i++ }
        val newEdges = mutableListOf<Edge>()
        val notCompleteVertices = mutableListOf<Vertex>()
        for (edge in edges){
            if(edge.vertex1 !in newMapping.keys && edge.vertex2 !in newMapping.keys) continue
            else if (edge.vertex1 !in newMapping.keys) notCompleteVertices.add(newMapping.get(edge.vertex2)!!)
            else if (edge.vertex2 !in newMapping.keys) notCompleteVertices.add(newMapping.get(edge.vertex1)!!)
            else newEdges.add(Edge(newMapping.get(edge.vertex1)!!, newMapping.get(edge.vertex2)!!))
        }
        vertices.clear()
        vertices.addAll(newMapping.values)
        edges.clear()
        edges.addAll(newEdges)
        newVertices.clear()
        newVertices.addAll(notCompleteVertices)
    }
}
