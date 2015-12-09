package com.botijasoftware.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;

public class Timer {

    private ArrayList<Event> events = new ArrayList<Event>();

    public class Event {

        public class CustomComparator implements Comparator<Event> {
            @Override
            public int compare(Event e1, Event e2) {
                return (e1.ms - e2.ms);
            }
        }

        private Object object;
        private Object[] args;
        private Method method;

        private int ms;

        public Event(int ms, Object receiver, Method method, Object[] args) {
            this.ms = ms;
            this.object = receiver;
            this.method = method;
            this.args = args;
        }

        public void elapsedTime(int elapsedms) {
            ms -= elapsedms;
        }

        public boolean timedOut(){
            return (ms <= 0);
        }

        public int remainintMs() {
            return ms;
        }

        public void run() {
            try {
                method.invoke(object, args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }



    public void update(float time) {
        int mselapsed = (int) (time * 1000.0f);

        for (int i =0; i < events.size();i++) {
            Event e = events.get(i);
            e.elapsedTime(mselapsed);
            if (e.timedOut()) {
                e.run();
                events.remove(i--);
            }

        }
    }

    public void schedule(int ms, Object receiver, Method method, Object[] args) {
        Timer.Event event = new Event(ms, receiver, method, args);

        for (int i=0; i< events.size(); i++) {
            Event e = events.get(i);
            if (event.ms < e.ms) {
                events.add(i, event);
                return;
            }
        }

        events.add(event);
    }

    public void clear() {
        events.clear();
    }

}