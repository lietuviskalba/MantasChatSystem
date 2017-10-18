import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mantas_MSI on 18-Oct-17.
 */
public class PeopleData { // used for storing user names

    public static List<String> userNames = new ArrayList<>();
    public  static  List<Object> clientObjects = new ArrayList<>();
    public  static  int createAnIndex;

    public  void addName(String enteredName){
        userNames.add(enteredName);
    }

    public  void incrIndex(){
        createAnIndex += 1;
    }

    public  void displayUsers(){
        for (String name : userNames){
            System.out.println("Ze names: "+name);
        }
    }

    public  List<String> getUserNames(){

        return userNames;
    }



}
