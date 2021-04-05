package functions.bool.intervals

import functions.bool.BooleanExpression
import functions.bool.OrExpression

class CombinedInterval(interval: SimpleInterval): Interval {

    var intervals= mutableListOf(interval)

    constructor(interval: Interval):
            this(if (interval is CombinedInterval) interval.intervals[0] else interval as SimpleInterval) {
        if (interval is CombinedInterval) {
            for (i in (1 until interval.intervals.size)) {
                unionInternal(interval.intervals[i])
            }
        }
    }

    private constructor(interval1: SimpleInterval, interval2: SimpleInterval): this(interval1) {
        intervals.add(interval2)
    }

    companion object {
        internal fun fromTwoIntervals(interval1: SimpleInterval, interval2: SimpleInterval): CombinedInterval {
            return CombinedInterval(interval1, interval2)
        }
    }

    fun intersection(interval: Interval): Interval {
        return when (interval) {
            is SimpleInterval -> {
                intersection(interval)
            }
            is CombinedInterval -> {
                interval.intervals.forEach {
                    intersectionInternal(it)
                }
                simplify()
            }
            else -> {
                interval
            }
        }
    }

    override fun intersection(interval: SimpleInterval): Interval {
        intersectionInternal(interval)
        return simplify()
    }

    private fun intersectionInternal(interval: SimpleInterval) {
        if (interval is NothingInterval) {
            intervals.clear()
        } else {
            val newList = mutableListOf<SimpleInterval>()
            intervals.forEach {
                val intersection = it.intersection(interval)
                if (intersection !is NothingInterval) {
                    newList.add(intersection)
                }
            }
            intervals = newList
        }
    }

    fun union(interval: Interval): Interval {
        return when (interval) {
            is SimpleInterval -> {
                union(interval)
            }
            is CombinedInterval -> {
                interval.intervals.forEach {
                    unionInternal(it)
                }
                simplify()
            }
            else -> {
                interval
            }
        }
    }

    override fun union(interval: SimpleInterval): Interval {
        unionInternal(interval)
        return simplify()
    }

    private fun unionInternal(interval: SimpleInterval) {
        if (interval is EverythingInterval) {
            intervals = mutableListOf(interval)
        } else {
            val newList = mutableListOf<SimpleInterval>()
            var combined = interval
            for (it in intervals) {
                val result = it.union(combined)
                if (result is CombinedInterval) {
                    newList.add(it)
                } else if (result is EverythingInterval) {
                    newList.clear()
                    combined = EverythingInterval
                    break
                } else {
                    combined = result as SimpleInterval
                }
            }
            newList.add(combined)
            intervals = newList
        }
    }

    private fun simplify(): Interval {
        return when (intervals.size) {
            0 -> NothingInterval
            1 -> intervals[0]
            else -> {
                this
            }
        }
    }

    override fun toExpression(): BooleanExpression<*> {
        var result = intervals[0].toExpression()
        for (i in (1 until intervals.size)) {
            result = OrExpression(result, intervals[i].toExpression())
        }
        return result
    }
}