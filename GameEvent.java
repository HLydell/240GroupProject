/*
 *  GameEvents represent a change being made to the Game or Level.
 *  They are usually triggered by Buttons.
 *  Examples: Go to a Menu, Load a Level, Restart current Level
 */

public class GameEvent {

    private EventType eventType; // Type of event being triggered. Example: Go to Menu, Go to Level
    private int eventId; // Events can have an ID with additional information. Example: Level number

    public GameEvent(EventType eventType) {
        this.eventType = eventType;
        this.eventId = 0;
    }

    public GameEvent(EventType eventType, int eventId) {
        this.eventType = eventType;
        this.eventId = eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public int getEventId() {
        return eventId;
    }

    @Override
    public String toString() {
        return "GameEvent [eventType=" + eventType + ", eventId=" + eventId + "]";
    }
}
