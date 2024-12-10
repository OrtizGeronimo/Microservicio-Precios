package precio.precios.security;


import com.google.gson.annotations.SerializedName;
import precio.precios.utils.gson.GsonTools;


public class User {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("login")
    public String login;
    @SerializedName("permissions")
    public String[] permissions;

    public static User fromJson(String json) {
        return GsonTools.gson().fromJson(json, User.class);
    }
}