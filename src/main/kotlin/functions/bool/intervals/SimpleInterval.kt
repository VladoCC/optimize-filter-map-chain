package functions.bool.intervals

import kotlin.math.max
import kotlin.math.min

abstract class SimpleInterval(val p1: Int, val p2: Int): Interval {
    override fun intersection(interval: SimpleInterval): SimpleInterval {
        return if (interval is NothingInterval) {
            NothingInterval
        } else if (p1 <= interval.p1 && p2 >= interval.p2) {
            interval
        } else if (p1 > interval.p1 && p2 < interval.p2) {
            this
        } else if(p2 > interval.p1 && p1 < interval.p2) {
            line(max(p1, interval.p1), min(p2, interval.p2))
        } else if (p2 == interval.p1) {
            point(p2)
        } else if (p1 == interval.p2) {
            point(p1)
        } else {
            NothingInterval
        }
    }

    override fun union(interval: SimpleInterval): Interval {
        return if (interval is EverythingInterval) {
            EverythingInterval
        } else if (p1 <= interval.p1 && p2 >= interval.p2) {
            this
        } else if (p1 >= interval.p1 && p2 <= interval.p2) {
            interval
        } else if((p2 == Int.MAX_VALUE || p2 + 1 >= interval.p1) &&
            (p1 == Int.MIN_VALUE|| p1 - 1 <= interval.p2)) {
            line(min(p1, interval.p1), max(p2, interval.p2))
        } else {
            CombinedInterval.fromTwoIntervals(this, interval)
        }
    }
}