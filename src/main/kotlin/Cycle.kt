data class Cycle(var cycle:MutableList<Int>){
    var length: Int
    var edges:List<Edge>
    var vertices:List<Int>

    init {
        length = cycle.size
        vertices = cycle.sorted()
        val pom = cycle.toMutableList()
        pom.add(cycle.first())
        edges = getEdges(pom)
    }

    private fun getEdges(path: List<Int>): List<Edge> = path.zipWithNext().map { pair -> Edge(Vertex(pair.first + 1), Vertex(pair.second + 1)) }
            .sortedWith(compareBy<Edge> { edge -> edge.vertex1.name }.thenBy { it.vertex2.name })

    override fun equals(other: Any?): Boolean {
        if (other !is Cycle) return false
        return edges == other.edges
    }

    override fun hashCode(): Int {
        return edges.map { (v1, v2)-> (11*v1.name)+(11*v2.name) }.reduce{a, b->a+11*b}
    }

    override fun toString(): String {
        return cycle.toString()
    }
}