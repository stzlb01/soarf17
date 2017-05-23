import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

public class HelloBoth {

    private MongoClient mongoClient;
    private MongoDatabase soarf17;
    private Twitter twitter;
    private String screenName;


    HelloBoth(String[] args) throws TwitterException {
        this.mongoClient = new MongoClient(Configuration.DB_SERVER, Configuration.DB_PORT);
        this.soarf17 = this.mongoClient.getDatabase(Configuration.DB_NAME);
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

    void insertDocument() throws TwitterException {
        User user = twitter.showUser(screenName);
        Document doc = new Document();
        doc.append("name", user.getName());
        doc.append("screenName", user.getScreenName());
        doc.append("description", user.getDescription());
        doc.append("dateCreated", user.getCreatedAt());
        doc.append("location", user.getLocation());
        doc.append("numberOfFollowers", user.getFollowersCount());
        doc.append("numberOfFriends", user.getFriendsCount());
        doc.append("numberOfTweets", user.getStatusesCount());
        doc.append("protection", user.isProtected());
        MongoCollection users = this.soarf17.getCollection("users");
        users.insertOne(doc);
    }

    void printDocuments() {
        MongoCollection users = this.soarf17.getCollection("users");
        MongoCursor<Document> cursor = users.find().iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            System.out.println(doc);
        }
    }

    void done() {
        this.mongoClient.close();
    }

    public static void main(String[] args) {
        try {
            HelloBoth helloBoth = new HelloBoth(args);
            helloBoth.insertDocument();
            helloBoth.printDocuments();
            helloBoth.done();


        } catch (Exception e) {
            System.out.println("Oops something went wrong!");
        }


    }
}
