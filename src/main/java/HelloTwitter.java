/*
 * SOAR Project Fall 2017
 * Advisor: Thyago Mota
 * Student:
 * Description: Simple tests to learn Twitter4J and Twitter's Search API
 */

import twitter4j.*;
import twitter4j.auth.AccessToken;

public class HelloTwitter {

    private Twitter twitter;
    private String screenName;


    HelloTwitter(String[] args) throws TwitterException {
        if (args[0] != null) {
            screenName = args[0];
        }
        else {
            screenName = twitter.getScreenName();
        }
        setCredentials();
    }

    void setCredentials() {
        this.twitter = TwitterFactory.getSingleton();
        this.twitter.setOAuthConsumer(Configuration.CONSUMER_KEY, Configuration.CONSUMER_SECRET);
        AccessToken accessToken = new AccessToken(Configuration.ACCESS_TOKEN_KEY, Configuration.ACCESS_TOKEN_SECRET);
        this.twitter.setOAuthAccessToken(accessToken);
    }

    void printHomeTimeLine() throws TwitterException {
        ResponseList<Status> statusResponseList = this.twitter.getUserTimeline(screenName);
        for (Status status: statusResponseList)
            System.out.println(status.getText());
    }



    void printFollowers() throws TwitterException {

        long cursor = -1;
        PagableResponseList<User> statusResponseList;
        do {
            statusResponseList = this.twitter.getFollowersList(screenName, cursor, 20);
            for (User user : statusResponseList) {
                System.out.println(user.getScreenName());
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }
        while ((cursor = statusResponseList.getNextCursor()) != 0);

    }



    void printFollowingList() throws TwitterException {
        long cursor = -1;
        PagableResponseList<User> statusResponseList;
        do {
            statusResponseList = this.twitter.getFriendsList(screenName, cursor, 20);
            for (User user : statusResponseList) {
                System.out.println(user.getScreenName());
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }
        while ((cursor = statusResponseList.getNextCursor()) != 0);

    }

    void printProfile() throws TwitterException {
        User user = twitter.showUser(screenName);
        System.out.println(user.getName() + " Profile Information");
        System.out.println("Screen Name: " + user.getScreenName());
        System.out.println("Description: " + user.getDescription());
        System.out.println("Date created: " + user.getCreatedAt());
        System.out.println("Location: " + user.getLocation());
        System.out.println("# of followers: " + user.getFollowersCount());
        System.out.println("# of friends: " + user.getFriendsCount());
        System.out.println("# of tweets: " + user.getStatusesCount());
        System.out.println("Protection: " + user.isProtected());
    }

    
    void printFollowersFollowedBack() throws TwitterException {
        long cursor = -1;
        PagableResponseList<User> listOfFollowers;
        PagableResponseList<User> listOfFollowing;
        do {
            listOfFollowers = this.twitter.getFollowersList(screenName, cursor, 20);
            listOfFollowing = this.twitter.getFriendsList(screenName, cursor, 20);
            for (User user : listOfFollowing) {
                if (listOfFollowers.contains(user)) {
                    System.out.println(user.getScreenName());
                }
            }
            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException e) {

            }
        }
        while ((cursor = listOfFollowing.getNextCursor()) != 0);

    }


    public static void main(String[] args) {
        try {
            HelloTwitter helloTwitter = new HelloTwitter(args);
            //helloTwitter.printHomeTimeLine();
            //helloTwitter.printFollowers();
            //helloTwitter.printFollowingList();
            //helloTwitter.printProfile();
            helloTwitter.printFollowersFollowedBack();
        }
        catch (Exception ex) {
            System.out.println("Ops, something went wrong: " + ex);
        }
    }
}