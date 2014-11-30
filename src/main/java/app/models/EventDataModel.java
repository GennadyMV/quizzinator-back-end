package app.models;

import java.sql.Timestamp;
import java.util.List;

/**
 * data about tracked events from user's web browser
 */
public class EventDataModel {
    private String user;
    private Long quizId;
    
    private List<Event> events;
    
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    
    /**
     * this is a single action performed by user
     * inner classes must be static for jackson to deserialize them
     */
    public static class Event {
        private String action;
        private String element;
        private String child;
        private String value;
        private Timestamp actionTime;
        
        public Event() {}

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getElement() {
            return element;
        }

        public void setElement(String element) {
            this.element = element;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Timestamp getActionTime() {
            return actionTime;
        }

        public void setActionTime(Timestamp actionTime) {
            this.actionTime = actionTime;
        }

        public String getChild() {
            return child;
        }

        public void setChild(String child) {
            this.child = child;
        }
    }
}
