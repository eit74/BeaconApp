package de.rvwbk.eit74.beaconapp;

public class userdataSingleton {

    public static userdataSingleton instance;
    public int userID = 0;
    public int nextTask = 0;

    public userdataSingleton(){}

    public static userdataSingleton getInstance(){
        if (instance == null)
            instance = new userdataSingleton();
        return instance;
    }

    public int getUserID(){
        return userID;
    }

    public void setUserID(int id){
        this.userID = id;
    }

    public int getNextTask() {
        return nextTask;
    }

    public void setNextTask(int nextTask) {
        this.nextTask = nextTask;
    }
}
