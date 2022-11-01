package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a log of alarm system events.
 * We use the Singleton Design Pattern to ensure that there is only
 * one model.EventLog in the system and that the system has global access
 * to the single instance of the model.EventLog.
 */
public class EventLog implements Iterable<model.Event> {
    /**
     * the only model.EventLog in the system (Singleton Design Pattern)
     */
    private static EventLog theLog;
    private Collection<model.Event> events;

    /**
     * Prevent external construction.
     * (Singleton Design Pattern).
     */
    private EventLog() {
        events = new ArrayList<model.Event>();
    }

    /**
     * Gets instance of model.EventLog - creates it
     * if it doesn't already exist.
     * (Singleton Design Pattern)
     *
     * @return instance of model.EventLog
     */
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }

        return theLog;
    }

    /**
     * Adds an event to the event log.
     *
     * @param e the event to be added
     */
    public void logEvent(model.Event e) {
        events.add(e);
    }

    /**
     * Clears the event log and logs the event.
     */
    public void clear() {
        events.clear();
        logEvent(new model.Event("model.Event log cleared."));
    }

    @Override
    public Iterator<model.Event> iterator() {
        return events.iterator();
    }
}
