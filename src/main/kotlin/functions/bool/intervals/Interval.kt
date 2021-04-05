package functions.bool.intervals

import functions.bool.BooleanExpression
import kotlin.math.ceil
import kotlin.math.floor

interface Interval {
    fun intersection(interval: SimpleInterval): Interval
    fun union(interval: SimpleInterval): Interval
    fun toExpression(): BooleanExpression<*>

    fun line(p1: Int, p2: Int): SimpleInterval {
        if (p1 == p2) {
            return point(p1)
        }
        val minPoint: Int
        val maxPoint: Int
        if (p1 > p2) {
            maxPoint = p1
            minPoint = p2
        } else {
            maxPoint = p2
            minPoint = p1
        }
        if (minPoint == Int.MIN_VALUE) {
            if (maxPoint == Int.MAX_VALUE) {
                return EverythingInterval
            }
            return NegativeInfInterval(maxPoint)
        } else if (maxPoint == Int.MAX_VALUE) {
            return PositiveInfInterval(minPoint)
        }
        return LineInterval(minPoint, maxPoint)
    }

    fun point(point: Int): SimpleInterval {
        return PointInterval(point)
    }
}