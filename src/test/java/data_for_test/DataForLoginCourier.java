package data_for_test;

public class DataForLoginCourier {
    private String login;
    private String password;

    public DataForLoginCourier(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public DataForLoginCourier() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
