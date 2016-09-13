import java.io.*;
import java.util.ArrayList;

/**
 * Created by sanak on 23.03.2016.
 */
public class Users {
    int tryNumber;
    public User currentUser;
    public ArrayList <User> users;

    Users(){
        currentUser = null;
        users = new <User> ArrayList();
    }

    public void addUser(String name, String password){
        User user = new User(name, password);
        users.add(user);
    }

    public void addUser(String name){
        User user = new User(name, "1");
        users.add(user);
    }

    public User searchUser(String name){
        for (User user : users) {
            if (user.name.equals(name)){
                return user;
            }
        }
        return null;
    }

    public boolean logIn(String name, String password){
        for (User user : users) {
            if (user.name.equals(name) && user.password.equals(password)){
                this.currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void logOut(){
        this.currentUser = null;
    }

    public void showUserList(){
        for (User user : users) {
            System.out.println(user.name + "    —    блокировка: " + user.isBlocked + ",    ограничения: " + user.restrictions);
        }
    }

    public void blockUser(String name){
        if("admin".equals(currentUser.name)){
            for (User user : users) {
                if (user.name.equals(name)){
                    user.isBlocked = !user.isBlocked;
                }
            }
        }
    }

    public void setRestrictions(String name){
        if("admin".equals(currentUser.name)){
            for (User user : users) {
                if (user.name.equals(name)){
                    user.restrictions = !user.restrictions;
                }
            }
        }
    }

    public void readFromDB() throws IOException {
        File file = new File("users.txt");

        if (!file.exists()){
            file.createNewFile();
            addUser("admin", "admin");
            writeInDb();
        } else{
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            String s, name, password;
            int beginIndex;
            int endIndex;
            boolean isBlocked, restrictions;

            while ((s = in.readLine()) != null){
                endIndex = s.indexOf(" ");
                name = s.substring(0, endIndex);

                beginIndex = endIndex + 1;
                endIndex = s.indexOf(" ", beginIndex);
                password = s.substring(beginIndex, endIndex);

                beginIndex = endIndex + 1;
                endIndex = s.indexOf(" ", beginIndex);
                if ("true".equals(s.substring(beginIndex, endIndex))){
                    isBlocked = true;
                } else isBlocked = false;

                beginIndex = endIndex + 1;
                endIndex = s.length();
                if ("true".equals(s.substring(beginIndex, endIndex))){
                    restrictions = true;
                } else restrictions = false;

                User user = new User(name, password, isBlocked, restrictions);
                users.add(user);
            }

            in.close();
        }
    }

    public void writeInDb() throws IOException {
        File file = new File("users.txt");
        if(!file.exists()){
            file.createNewFile();
        }
        PrintWriter out = new PrintWriter(file.getAbsoluteFile());
        for (User user : users) {
            out.println(user.name + " " + user.password + " " + user.isBlocked + " " + user.restrictions);
        }
        out.close();
    }
}
