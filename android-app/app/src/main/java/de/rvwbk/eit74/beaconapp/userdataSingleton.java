package de.rvwbk.eit74.beaconapp;

public class userdataSingleton {

    public static userdataSingleton instance;
    public String userID = "";
    public String nextTask = "";

    public userdataSingleton(){}

    public static userdataSingleton getInstance(){
        if (instance == null)
            instance = new userdataSingleton();
        return instance;
    }

    public String getUserID(){
        return userID;
    }

    public void setUserID(String id){
        this.userID = id;
    }

    public String getNextTask() {
        return nextTask;
    }

    public void setNextTask(String nextTask) {
        this.nextTask = nextTask;
    }
}
