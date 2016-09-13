/**
 * Created by sanak on 17.03.2016.
 */
public class User {
    String name;
    String password;
    boolean isBlocked;
    boolean restrictions;

    User(String name, String password){
        this.name = name;
        this.password = password;
        this.isBlocked = false;
        this.restrictions = false;
    }

    User(String name, String password, boolean isBlocked, boolean limitation){
        this.name = name;
        this.password = password;
        this.isBlocked = isBlocked;
        this.restrictions = limitation;
    }

    public boolean isBlocked(){
        return this.isBlocked;
    }

    public boolean changePassword(String password, String newPassword){
        if(this.password.equals(password)){
            this.password = newPassword;
            return true;
        }else {
            return false;
        }
    }

    public boolean checkPassword(String password){
        if(password.matches("(.*[A-z]+.*)")&& password.matches(".*[/*'-'+%]+.*")&& password.matches(".*[\\p{Punct}]+.*")){
            return true;
        }else {
            return false;
        }
    }

    public static void main(String[] args) {
        User user = new User("name", "4545");
        System.out.println(user.checkPassword(",%a"));
    }
}
