package com.botijasoftware.utils

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.ArrayList
import java.util.Comparator

class Timer {

    private val events = ArrayList<Event>()

    inner class Event(var ms: Int, private val `object`: Any, private val method: Method, private val args: Array<Any>) {

        inner class CustomComparator : Comparator<Event> {
            override fun compare(e1: Event, e2: Event): Int {
                return e1.ms - e2.ms
            }
        }

        fun elapsedTime(elapsedms: Int) {
            ms -= elapsedms
        }

        fun timedOut(): Boolean {
            return ms <= 0
        }

        fun remainintMs(): Int {
            return ms
        }

        fun run() {
            try {
                method.invoke(`object`, *args)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }

        }
    }


    fun update(time: Float) {
        val mselapsed = (time * 1000.0f).toInt()

        var i = 0
        while (i < events.size) {
            val e = events[i]
            e.elapsedTime(mselapsed)
            if (e.timedOut()) {
                e.run()
                events.removeAt(i--)
            }
            i++

        }
    }

    fun schedule(ms: Int, receiver: Any, method: Method, args: Array<Any>) {
        val event = Event(ms, receiver, method, args)

        for (i in events.indices) {
            val e = events[i]
            if (event.ms < e.ms) {
                events.add(i, event)
                return
            }
        }

        events.add(event)
    }

    fun clear() {
        events.clear()
    }

}