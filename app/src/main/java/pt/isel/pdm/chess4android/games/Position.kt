package pt.isel.pdm.chess4android.games


class Position(var x: Int, var y: Int) {
    /**
     * Method equals and hashcode were overridden because two positions with same x and y need to be equal.
     * Both of the methods were create with the help of IntelliJ.
     * */


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        /**
         * The reason why 31 was used can be seen in the following excerpt from the book Joshua Bloch's Effective Java:
         *    The value 31 was chosen because it is an odd prime.
         *    If it were even and the multiplication overflowed, information would be lost, as multiplication by 2 is equivalent to shifting.
         *    The advantage of using a prime is less clear, but it is traditional.
         *    A nice property of 31 is that the multiplication can be replaced by a shift and a subtraction for better performance: 31 * i == (i << 5) - i.
         *    Modern VMs do this sort of optimization automatically.
         * */
        return 31 * x + y
    }
}