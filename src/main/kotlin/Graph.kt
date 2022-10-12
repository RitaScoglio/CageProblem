import java.lang.Math.min
import java.util.LinkedList

class Graph {
    constructor(input: List<List<Int>>){
        createOriginalGraph(input)
        girth = computeGirth(createAdjacencyList())
    }
    constructor(vertices: MutableList<Vertex>, edges: Set<Edge>){
        this.vertices.addAll(vertices)
        this.edges.addAll(edges)
        girth = computeGirth(createAdjacencyList())
    }

    val vertices = mutableListOf<Vertex>()
    var edges = mutableSetOf<Edge>()
    var newVertices = mutableListOf<Vertex>()
    var girth: Int

    private fun createAdjacencyList(newEdges: List<Edge> = listOf()): List<MutableList<Int>> {
        val adjacencyList: List<MutableList<Int>> = List(vertices.size) { mutableListOf() }
        for(edge in edges.plus(newEdges)){
            adjacencyList[edge.vertex1.name].add(edge.vertex2.name)
            adjacencyList[edge.vertex2.name].add(edge.vertex1.name)
        }
        return adjacencyList
    }

    fun createOriginalGraph(input: List<List<Int>>) {
        //creates vertices from 1 to the number of all vertices
        vertices.addAll((0 until input.size).map { Vertex(it) }.toList())
        //creates edges based on vertices and adjacency list
        vertices.map { v1 -> input[v1.name].map { v2 -> edges.add(Edge(v1, vertices[v2]))}}
    }

    fun addNewVertices(
        skipEdge: Edge,
        numberOfNewVertices: Int
    ) {

        var currentNewVertex = vertices.size
        //adding new vertices to edges
        vertices.addAll((currentNewVertex until vertices.size+numberOfNewVertices).map { Vertex(it) }.toList())
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
        if(skipEdge.vertex1.name != -1)
            newEdges.add(skipEdge)
        edges = newEdges
    }

    fun getCombinationsOfEdges(): MutableSet<List<Edge>> {
        val possibleEdges = combinationsOfVertices()
        //set to save possible compositions of edges
        val allCompositions = mutableSetOf<List<Edge>>()
        //for each edge in possible edges, we get the rest of the compositions to it
        for (indexOfEdge in 0..possibleEdges.size-(newVertices.size/2)) {
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
                    if (newGirth == girth+1)
                        allCompositions.add(compositionOfEdges.toList())
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
            } while (compositionOfEdges.isNotEmpty())
        }
        return allCompositions
    }

    //compute all possible edges - pairs of vertices
    fun combinationsOfVertices(): MutableList<Edge> {
        val possibleEdges = mutableListOf<Edge>()
        for (v1 in vertices.size-newVertices.size until vertices.size) {
            for (v2 in v1 + 1 until vertices.size) {
                possibleEdges.add(Edge(vertices[v1], vertices[v2]))
            }
        }
        return possibleEdges
    }

    private fun computeGirth(adjacencyList: List<MutableList<Int>>): Int {
        var shortestCycle = Integer.MAX_VALUE
        var distance = MutableList(vertices.size){Integer.MAX_VALUE}
        for(i in 0 until vertices.size){
            distance = MutableList(vertices.size){Integer.MAX_VALUE}
            val parent = MutableList(vertices.size){Integer.MIN_VALUE}
            distance[i] = 0
            val queue = LinkedList<Int>()
            queue.add(i)
            while (!queue.isEmpty()){
                val first = queue.poll()
                for(child in adjacencyList[first]){
                    if(distance[child] == Integer.MAX_VALUE){
                        distance[child] = 1 + distance[first]
                        parent[child] = first
                        queue.add(child)
                    } else if (parent[first] != child && parent[child] != first){
                        shortestCycle = min(shortestCycle, distance[first]+distance[child]+1)
                    }
                }
            }
        }
        if (shortestCycle == Integer.MAX_VALUE)
            return -1
        else
            return shortestCycle
    }
}