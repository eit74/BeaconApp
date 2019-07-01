package de.rvwbk.eit74.beaconapp;

public class UserData {

    private static UserData instance;
    private String userID = "";
    private String nextTask = "";
    private String nextBeaconMM = "";

    private UserData(){}

    public static UserData getInstance(){
        if (instance == null)
            instance = new UserData();
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

    /**
     * Gets major-minor for next beacon
     * @return String
     */
    public String getNextBeaconMM() {
        return nextBeaconMM;
    }

    /**
     * Sets major-minor for next beacon
     * @param nextBeaconMM String
     */
    public void setNextBeaconMM(String nextBeaconMM) {
        this.nextBeaconMM = nextBeaconMM;
    }
}
