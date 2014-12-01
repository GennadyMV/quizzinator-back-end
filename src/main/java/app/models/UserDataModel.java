/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.models;

import java.util.ArrayList;

/**
 *
 * @author ilmarihu
 */
public class UserDataModel {
    private ArrayList<UserData> data;
    
    public UserDataModel() {
        data = new ArrayList<UserData>();
    }
    
    public void addData(UserData userdata) {
        data.add(userdata);
    }
    
    public UserData[] getData() {
        UserData[] userdata = new UserData[data.size()];
        
        for(int i=0;i<data.size();i++){
            userdata[i] = data.get(i);
        }
        return userdata;
    }

    public static class UserData {

        private String username;
        private Long numberOfAnswers;
        private boolean hasImproved;
        
        public UserData(String user, Long answers, boolean hasImproved) {
            username = user;
            numberOfAnswers = answers;
            this.hasImproved = hasImproved;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Long getNumberOfAnswers() {
            return numberOfAnswers;
        }

        public void setNumberOfAnswers(Long numberOfAnswers) {
            this.numberOfAnswers = numberOfAnswers;
        }

        public boolean isHasImproved() {
            return hasImproved;
        }

        public void setHasImproved(boolean hasImproved) {
            this.hasImproved = hasImproved;
        }

    }

}
