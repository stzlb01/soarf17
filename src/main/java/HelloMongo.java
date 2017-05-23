import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Scanner;

public class HelloMongo {

    private MongoClient   mongoClient;
    private MongoDatabase soarf17;

    HelloMongo() {
        this.mongoClient = new MongoClient(Configuration.DB_SERVER, Configuration.DB_PORT);
        this.soarf17 = this.mongoClient.getDatabase(Configuration.DB_NAME);
    }

    void insertDocument(int id, String name) {
        Document doc = new Document();
        doc.append("id", id);
        doc.append("name", name);
        MongoCollection employees = this.soarf17.getCollection("employees");
        employees.insertOne(doc);
    }

    void printDocuments() {
        MongoCollection employees = this.soarf17.getCollection("employees");
        MongoCursor<Document> cursor = employees.find().iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            System.out.println(doc);
        }
    }

    void printSpecifiedID() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter an ID: ");
        int id = Integer.parseInt(sc.nextLine());
        MongoCollection employees = this.soarf17.getCollection("employees");
        MongoCursor<Document> cursor = employees.find().iterator();
        int count=0;
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            int newId = doc.getInteger("id");
            if (id == newId) {
                System.out.println(doc.get("name") + " has that ID!");
                count++;
                break;
            }
        }
        if (count ==0) {
            System.out.println("There are no employees with that ID!");
        }

    }

    void done() {
        this.mongoClient.close();
    }

    public static void main(String[] args) {
        try {
            HelloMongo helloMongo = new HelloMongo();
//            Scanner sc = new Scanner(System.in);
//            System.out.println("Enter an id: ");
//            int id = Integer.parseInt(sc.nextLine());
//            System.out.println("Enter a name: ");
//            String name = sc.nextLine();
//            helloMongo.insertDocument(id, name);
//            helloMongo.printDocuments();
            helloMongo.printSpecifiedID();
            helloMongo.done();
        }
        catch (Exception ex) {
            System.out.println("Ops, something went wrong: " + ex);
        }
    }
}