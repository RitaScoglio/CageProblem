data class Cycle(var vertices:MutableList<Int>){
    var cycle : List<Int>
    var length : Int

    init {
        cycle = vertices
        vertices = vertices.sorted().toMutableList()
        length = vertices.size
    }

    override fun toString(): String {
        return cycle.toString()
    }
}