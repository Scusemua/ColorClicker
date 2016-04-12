package com.benrcarvergmail.colorclicker;

/**
 * Created by Benjamin on 4/12/2016.
 */
public class Highscore {
    private int mScore;
    private String mNickname;
    private String mUniqueUserId;

    /**
     * Constructor for the high mScore
     * @param s the user's mScore
     * @param n the user's mNickname
     * @param u the user's unique user id
     */
    public Highscore(int s, String n, String u) {
        mScore = s;
        mNickname = n;
        mUniqueUserId = u;
    }

    /**
     * Getter for the mScore of this high mScore
     * @return the high mScore value for this high mScore
     */
    public int getScore() {
        return mScore;
    }

    /**
     * Getter for the unique user id of this high mScore
     * @return the unique user id for this high mScore
     */
    public String getUniqueUserId() {
        return mUniqueUserId;
    }

    /**
     * Getter for the username of this high mScore
     * @return the username for this high mScore
     */
    public String getNickname() {
        return mNickname;
    }

    /**
     * Method to compare two Highscore objects
     * @param comp the Highscore we are comparing
     * @return 1 if this high mScore is greater than the argument, 0 if this high mScore is equal to
     * the argument, or -1 if this high mScore is less than the argument.
     */
    public int compareTo(Highscore comp) {
        if (this.mScore > comp.mScore) {
            return 1;
        } else if (this.mScore < comp.mScore) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Override the equals method from object
     * @param obj object that we are checking whether or not it equals "this" object (English pls)
     * @return boolean value representing whether or not this object and the parameter object are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!Highscore.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Highscore other = (Highscore) obj;
        if ((this.mNickname == null) ? (other.mNickname != null) : !this.mNickname.equals(other.mNickname)) {
            return false;
        }

        if ((this.mUniqueUserId == null) ? (other.mUniqueUserId != null) : !this.mUniqueUserId.equals(other.mUniqueUserId)) {
            return false;
        }

        if (this.mScore != other.mScore) {
            return false;
        }

        return true;
    }
}
