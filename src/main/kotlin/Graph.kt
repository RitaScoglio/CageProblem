import java.lang.Math.min
import java.util.*

class Graph {
    constructor(input: List<List<Int>>) {
        createOriginalGraph(input)
        this.adjacencyList = createAdjacencyList()
        //println(adjacencyList)
        girth = computeGirth(adjacencyList)
        //println("Girth: $girth")

        val allFilteredCycles = mutableListOf<List<Cycle>>()
        for (a in 0..vertices.size - girth) {
            for (size in girth..vertices.size - a) {
                val cycles = computeCyclesSize(adjacencyList, size)
                allFilteredCycles.add(cycles.filter { it.length == size })
            }
        }
        //for (a in 0..vertices.size-girth) {
        val edgesInAllCycles = mutableListOf<List<Edge>>()
        for (size in girth..vertices.size) {
            //cycles = computeCyclesSize(adjacencyList, size)
            //val filtered = cycles.filter { it.length == size }
            println("Cycles of size ${size}: ${allFilteredCycles[size - girth]}")
            allFilteredCycles[size - girth].forEach { cycle ->
                println("${cycle.edges} ${cycle.vertices}")
                //println(cycle)
                //allFilteredCycles[size - girth].forEach { otherCycle ->
                    //println("\t${cycle.cycle.intersect(otherCycle.cycle)}")
                //}
            }
            edgesInAllCycles.add(edgesInCycle(allFilteredCycles[size - girth]))
            println("Number of cycles ${size}: ${allFilteredCycles[size - girth].size}")
        }
        val all = mutableListOf<Pair<Edge, List<Int>>>()
        for (e in edges) {
            val edge = Edge(Vertex(e.vertex1.name + 1), Vertex(e.vertex2.name + 1))
            val occurences: List<Int> = edgesInAllCycles.map { list -> Collections.frequency(list, edge) }
            all.add(Pair(edge, occurences))
        }
        all.forEach { println(it) }
        /*val parts = all.groupBy { it.second.subList(0, vertices.size-girth) }
        for (part in parts.keys.sortedBy { it.sum() }) {
            println("${part}:")
            for (p in parts.get(part)!!) {
                println("\t${p}")
            }
        }
        println(parts.size)*/
        // }
    }

    private fun edgesInCycle(filtered: List<Cycle>): MutableList<Edge> {
        val allEdgesInCycle = mutableListOf<Edge>()
        for (cycle in filtered) {
            var v = cycle.cycle.first()
            var old = cycle.cycle as MutableList
            old.add(v)
            allEdgesInCycle.addAll(
                old.zipWithNext().map { pair -> Edge(Vertex(pair.first + 1), Vertex(pair.second + 1)) })
        }

        val e = mutableListOf<String>()
        for (item in allEdgesInCycle.distinct()) {
            e.add(item.toGAP())
        }
        //println(e)
        return allEdgesInCycle
    }

    private fun edgesOccurences(filtered: List<Cycle>) {
        val allEdgesInCycle = mutableListOf<Edge>()
        for (cycle in filtered) {
            var v = cycle.cycle.first()
            var old = cycle.cycle as MutableList
            old.add(v)
            allEdgesInCycle.addAll(
                old.zipWithNext().map { pair -> Edge(Vertex(pair.first + 1), Vertex(pair.second + 1)) })
        }
        val e = mutableListOf<String>()
        val o = mutableListOf<Int>()
        for (item in allEdgesInCycle.distinct()) {
            e.add(item.toGAP())
            o.add(Collections.frequency(allEdgesInCycle, item))

        }
        if (!e.isEmpty()) {
            println("edges${filtered[0].length}:= ${e};")
            println("occur${filtered[0].length}:= ${o};")
        }
    }

    constructor(vertices: MutableList<Vertex>, edges: Set<Edge>) {
        this.vertices.addAll(vertices)
        this.edges.addAll(edges)
        this.adjacencyList = createAdjacencyList()
        girth = computeGirth(adjacencyList)
        //println("Girth: $girth")
        cycles = computeAllCycles(adjacencyList)
        val filtered = cycles.filter { it.length == girth }
        //println("Cycles of girth size: ${filtered}")
        //println("Number of cycles: ${filtered.size}")
        //println(cycles)
    }

    constructor(graph: Graph) {
        this.vertices.addAll(graph.vertices)
        this.edges.addAll(graph.edges)
        this.girth = graph.girth
        this.cycles = graph.cycles
    }

    fun isInitialized(): Boolean {
        return (vertices.isNotEmpty() && edges.isNotEmpty())
    }

    lateinit var adjacencyList: List<MutableList<Int>>
    var vertices = mutableListOf<Vertex>()
    var edges = mutableSetOf<Edge>()
    var newVertices = mutableListOf<Vertex>()
    var girth: Int
    lateinit var cycles: List<Cycle>

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
        edgesToSplit: List<Edge>,
        skippedEdges: List<Edge>,
        numberOfNewVertices: Int
    ) {

        var currentNewVertex = vertices.size
        //adding new vertices to edges
        vertices.addAll((currentNewVertex until vertices.size + numberOfNewVertices).map { Vertex(it) }.toList())
        val newEdges = mutableSetOf<Edge>()
        //create splitted edges
        edges.map { e ->
            if (e in edgesToSplit) {
                newEdges.add(Edge(e.vertex1, vertices[currentNewVertex]))
                newEdges.add(Edge(e.vertex2, vertices[currentNewVertex]))
                newVertices.add(vertices[currentNewVertex])
                currentNewVertex++
            }
        }
        //adding the edge we skip from splitting
        if (Edge(Vertex(-1), Vertex(-1)) !in skippedEdges)
            newEdges.addAll(skippedEdges)
        edges = newEdges
    }

    fun getCombinationsOfEdges(searchedGirth: Int): MutableList<List<Edge>> {
        val possibleEdges = removeUsed(combinationsOfVertices())
        //set to save possible compositions of edges
        //val allCompositions = mutableSetOf<List<Edge>>()
        //save the needed graph
        var graph = mutableListOf<List<Edge>>()
        //for each edge in possible edges, we get the rest of the compositions to it
        for (indexOfEdge in 0..possibleEdges.size - (newVertices.size / 2)) {
            //current composition of edges
            val compositionOfEdges = mutableListOf<Edge>()
            //indexes od edges in possibleEdges
            val indexes = mutableListOf<Int>()
            //already used vertices for the edge's composition - to avoid degree bigger than 3
            val usedVertices = mutableListOf<Vertex>()
            var startIndex = -1
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
                    if (newGirth >= searchedGirth) {
                        graph.add(compositionOfEdges.toList())
                    }
                }
                if (possibleEdges.size > newVertices.size / 2) {
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
                } else {
                    compositionOfEdges.clear()
                }
            } while (compositionOfEdges.isNotEmpty() && graph.isEmpty())
        }
        return graph
    }

    private fun removeUsed(combinations: MutableList<Edge>): MutableList<Edge> {
        val possible = mutableListOf<Edge>()
        for (comb in combinations) {
            if (comb !in edges)
                possible.add(comb)
        }
        return possible
    }

    //compute all possible edges - pairs of vertices
    fun combinationsOfVertices(): MutableList<Edge> {
        val possibleEdges = mutableListOf<Edge>()
        for (v1 in 0 until newVertices.size - 1) {
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
            //}
        }
        return cycles.filter { it.length > 2 }
    }

    private fun computeCyclesSize(adjacencyList: List<MutableList<Int>>, size: Int): List<Cycle> {
        val cycles = mutableListOf<Cycle>()
        for (i in 0 until vertices.size) {
            //distance = MutableList(vertices.size){Integer.MAX_VALUE}
            //val parent = MutableList(vertices.size){Integer.MIN_VALUE}
            //vertex and current path to it
            val queue = LinkedList<Pair<Int, MutableList<Int>>>()
            queue.add(Pair(i, mutableListOf<Int>()))
            while (!queue.isEmpty()) {
                val (vertex, path) = queue.poll()
                if (path.size <= size) {
                    //vertex already in path?
                    if (path.contains(vertex)) {
                        getCycle(path, vertex).also { newCycle ->
                            if (newCycle !in cycles && newCycle.length >2)
                                cycles.add(newCycle)
                        }
                    } else { //add to path and send to children
                        val newPath = path.toMutableList()
                        newPath.add(vertex)
                        for (child in adjacencyList[vertex]) {
                            queue.add(Pair(child, newPath))
                        }
                    }
                }
            }
        }
        return cycles
    }

    private fun getCycle(path: MutableList<Int>, vertex: Int): Cycle = Cycle(path.subList(path.indexOf(vertex), path.size))

    fun deleteVerticesAndEdges() {
        val cycles: List<Cycle> = cycles.filter { it.length == girth }
        var cycleToDelete: MutableList<Int> = mutableListOf()
        if (cycles.size > 100) {
            val sublist = cycles.subList(0, 2)
            for (list in sublist) {
                cycleToDelete.addAll(list.cycle)
            }
        } else {
            cycleToDelete = cycles[0].cycle as MutableList<Int>
        }
        cycleToDelete = cycleToDelete.distinct() as MutableList<Int>
        val newListOfVertices: List<Vertex> = vertices.filter { it.name !in cycleToDelete }
        val newMapping = mutableMapOf<Vertex, Vertex>()
        var i = 0
        newListOfVertices.map { x -> newMapping.put(x, vertices[i]); i++ }
        val newEdges = mutableListOf<Edge>()
        val notCompleteVertices = mutableListOf<Vertex>()
        for (edge in edges) {
            if (edge.vertex1 !in newMapping.keys && edge.vertex2 !in newMapping.keys) continue
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

    fun combinationsOfPairsOfEdges(): MutableList<List<Edge>> {
        val listOfPairs = mutableListOf<List<Edge>>()
        val listOfEdges = edges.toList()
        for (e1 in 0 until listOfEdges.size - 1) {
            for (e2 in e1 + 1 until listOfEdges.size) {
                listOfPairs.add(listOf(listOfEdges[e1], listOfEdges[e2]))
            }
        }
        return listOfPairs
    }
}
