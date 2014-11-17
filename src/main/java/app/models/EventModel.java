package app.models;

import java.sql.Timestamp;
    public class EventModel {
        private String action;
        private String element;
        private String child;
        private String value;
        private Timestamp clickTime;

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

        public Timestamp getClickTime() {
            return clickTime;
        }

        public void setClickTime(Timestamp clickTime) {
            this.clickTime = clickTime;
        }

        public String getChild() {
            return child;
        }

        public void setChild(String child) {
            this.child = child;
        }
    }