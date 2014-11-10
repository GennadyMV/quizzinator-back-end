package app.models;

import java.sql.Timestamp;
import javax.persistence.Embeddable;

@Embeddable
public class ClickModel {    
    private Timestamp clickTime;
    private String element;
    private String action;
}
