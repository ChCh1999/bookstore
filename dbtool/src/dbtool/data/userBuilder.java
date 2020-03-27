package dbtool.data;

public class userBuilder {
    user user_init = new user();

    public userBuilder setAccount(String account) {
        user_init.account = account;
        return this;
    }

    public userBuilder setName(String name) {
        user_init.name = name;
        return this;
    }

    public userBuilder setPassword(String password) {
        user_init.password = password;
        return this;
    }

    public userBuilder setAddress(String address) {
        user_init.address = address;
        return this;
    }

    public userBuilder setProvince(String province) {
        user_init.province = province;
        return this;
    }

    public userBuilder setCity(String city) {
        user_init.city = city;
        return this;
    }

    public userBuilder setInfo(String info) {
        user_init.info = info;
        return this;
    }

    public userBuilder setMoney(float money) {
        user_init.money = money;
        return this;
    }

    public user getUser() {
        if (user_init.account != null && user_init.password != null)
            return user_init;
        else
            return null;
    }
}
