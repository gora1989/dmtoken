package dm.com.token;

/**
 * Created by echessa on 8/27/16.
 */
public class Item {

    private String title;
    private String phone;
    private String email;

    public Item() {
    }

    public Item(String title, String phone, String email) {
        this.title = title;
        this.phone = phone;
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
