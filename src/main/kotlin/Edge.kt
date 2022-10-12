data class Edge(var vertex1:Vertex, var vertex2:Vertex){
    init {
        if (vertex1.name > vertex2.name) {
            val vertex = vertex1
            vertex1 = vertex2
            vertex2 = vertex
        }
    }

    override fun toString(): String {
        return "($vertex1, $vertex2)"
    }
}