package schichtplanhgl.repository.hash

interface HashRepository {
    fun hash(raw: String): String

}