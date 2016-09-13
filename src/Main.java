import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Created by sanak on 03.03.2016.
 */
public class Main {
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_VERSION = System.getProperty("os.version");
    public static final String USER_NAME = System.getProperty("user.name");
    public static final String WIN_DIR = System.getenv("windir");

    public static final double WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

    public static void main(String []argc) throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Scanner scan = new Scanner(System.in);

        String hostname = "Unknown";

        InetAddress addr;
        addr = InetAddress.getLocalHost();
        hostname = addr.getHostName();
        System.out.println("Enter secret key:");
        String userKey = scan.next();
        String globalInfo = OS_NAME + OS_VERSION + USER_NAME + WIN_DIR + WIDTH + hostname + userKey;
        String hashCode = "" + globalInfo.hashCode();


        WinRegistry.createKey(WinRegistry.HKEY_CURRENT_USER, "HmExample");
        String value = WinRegistry.readString (
                WinRegistry.HKEY_CURRENT_USER,
                "Software\\JavaSoft\\Prefs\\/Sanak",
                "/S/I/G/N/A/T/U/R/E"
        );
        if(value.equals(hashCode)){

            Users users = new Users();
            users.readFromDB();

            Scanner in = new Scanner(System.in);
            int choose;
            String name;
            String password;
            String newpassword;

            while(true){
                System.out.println("\n1. Add user\n2. Sign in\n3. About\n4. Exit");
                choose = in.nextInt();

                switch(choose){

                    case 1:
                        System.out.println("Login: ");
                        name = in.next();
                        System.out.println("Pass: ");
                        password = in.next();
                        System.out.println("Repeat pass: ");
                        String repPassword = in.next();
                        if (repPassword.equals(password)) {
                            if (users.searchUser(name) == null) {
                                users.addUser(name, password);
                                users.writeInDb();
                            } else {
                                System.out.println("User already exist");
                            }
                        } else{
                            System.out.println("Passwords don't match");
                        }
                        break;

                    case 2:
                        System.out.println("Login: ");
                        name = in.next();
                        System.out.println("Pass: ");
                        password = in.next();
                        if(users.searchUser(name) != null){
                            if(!users.searchUser(name).isBlocked()){
                                if(users.logIn(name, password)){

                                    System.out.println("Hello, " + name );
                                    if("admin".equals(name)){
                                        while(choose != 6){
                                            System.out.println("\n1. Change pass \n2. Show users list\n3. Add user\n4. Block/unblock user \n5. Turn on limitations\n6. Log out");
                                            choose = in.nextInt();

                                            switch(choose){

                                                case 1:
                                                    System.out.println("Old pass: ");
                                                    password = in.next();
                                                    System.out.println("New pass: ");
                                                    newpassword = in.next();
                                                    System.out.println("Repeat new pass: ");
                                                    String newRepPassword = in.next();
                                                    if (newRepPassword.equals(newpassword)) {
                                                        if (!users.currentUser.restrictions || users.currentUser.checkPassword(newpassword)) {
                                                            if (users.currentUser.changePassword(password, newpassword)) {
                                                                System.out.println("\nPass changed\n");
                                                            } else {
                                                                System.out.print("\nINCORRECT old pass\n");
                                                            }
                                                        } else {
                                                            System.out.println("Password must contain alphabet, punctuation and arithmetic symbols");
                                                        }
                                                    } else System.out.println("Passwords don't match");

                                                    users.writeInDb();
                                                    break;

                                                case 2:
                                                    System.out.println();
                                                    users.showUserList();
                                                    break;

                                                case 3:
                                                    System.out.println("Login: ");
                                                    name = in.next();
                                                    if(users.searchUser(name) == null){
                                                        users.addUser(name);
                                                        users.writeInDb();
                                                    }else {
                                                        System.out.println("User already exist");
                                                    }
                                                    break;

                                                case 4:
                                                    System.out.println("Enter user's login: ");
                                                    name = in.next();
                                                    if (Objects.equals(name, "admin")) {
                                                        System.out.println("Admin can't be blocked!");
                                                        break;
                                                    }
                                                    users.blockUser(name);
                                                    users.writeInDb();
                                                    break;

                                                case 5:
                                                    System.out.println("Enter user's login: ");
                                                    name = in.next();
                                                    users.setRestrictions(name);
                                                    users.writeInDb();
                                                    break;

                                                case 6:
                                                    users.logOut();
                                                    break;
                                            }
                                        }
                                    }else{
                                        do{
                                            System.out.println("\n1. Change pass \n2. Log out");
                                            choose = in.nextInt();

                                            switch(choose){
                                                case 1:
                                                    System.out.println("Old pass: ");
                                                    password = in.next();
                                                    System.out.println("New pass: ");
                                                    newpassword = in.next();
                                                    System.out.println("Repeat new pass: ");
                                                    String newRepPassword = in.next();
                                                    if (newRepPassword.equals(newpassword)) {
                                                        if (!users.currentUser.restrictions || users.currentUser.checkPassword(newpassword)) {
                                                            if (users.currentUser.changePassword(password, newpassword)) {
                                                                System.out.println("\nPass changed\n");
                                                            } else {
                                                                System.out.print("\nINCORRECT old pass\n");
                                                            }
                                                        } else {
                                                            System.out.println("Password must contain alphabet, punctuation and arithmetic symbols");
                                                        }
                                                    } else System.out.println("Passwords don't match");
                                                    users.writeInDb();
                                                    break;

                                                case 2:
                                                    users.logOut();
                                                    break;
                                            }
                                        } while(choose != 2);
                                    }
                                }else{
                                    users.tryNumber++;
                                    if(users.tryNumber == 3){
                                        System.out.println("Your limit of attempts expired");
                                        exit(1);
                                    }
                                    System.out.println("Wrong data\nTry one more time!(attempts left: "+(3-users.tryNumber)+")");
                                }
                            }else{
                                System.out.println("User is blocked");
                            }
                        } else{
                            System.out.println("User doesn't exist");
                        }
                        break;
                    case 3:
                        System.out.println("Developer: Sanak Alex, FB-32\nPassword must contain alphabet, punctuation and arithmetic symbols");
                        break;
                    case 4:
                        exit(1);
                }

            }
        }else{
            System.out.println("INCORRECT key. Exit");
        }
    }
}
