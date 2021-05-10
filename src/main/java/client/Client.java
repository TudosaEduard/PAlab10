package client;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private final String name;
    public List<Client> friends = new ArrayList<>();
    public List<String> mail = new ArrayList<>();

    public Client(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Client> getFriends() {
        return friends;
    }

    public List<String> getMail() {
        return mail;
    }

}