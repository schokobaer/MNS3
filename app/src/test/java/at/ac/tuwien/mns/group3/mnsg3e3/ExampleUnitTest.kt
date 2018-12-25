package at.ac.tuwien.mns.group3.mnsg3e3

import at.ac.tuwien.mns.group3.mnsg3e3.model.Location
import at.ac.tuwien.mns.group3.mnsg3e3.model.LocationReport
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun locationdiff() {
        val l1 = Location(47.565380, 9.752456, 0.0)
        val l2 = Location(47.565380, 10.0, 0.0)

        val report = LocationReport(l1, l2, null)
        val diff = report.difference
        Assert.assertEquals(diff, 18570.0, 10.0)
    }

    @Test
    fun locationdiff2() {
        val l1 = Location(47.565380, 9.752456, 0.0)
        val l2 = Location(47.565380, 9.752456, 0.0)

        val report = LocationReport(l1, l2, null)
        val diff = report.difference
        Assert.assertEquals(diff, 0.0, 10.0)
    }

    @Test
    fun locationdiff3() {
        val l1 = Location(5.0, 5.0, 0.0)
        val l2 = Location(10.0, 10.0, 0.0)

        val report = LocationReport(l1, l2, null)
        val diff = report.difference
        Assert.assertEquals(diff, 782779.0, 10.0)
    }
}
