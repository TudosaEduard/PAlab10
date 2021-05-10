package client;

import java.util.List;

public class Command {

    public List<Client> clients;

    public Command(List<Client> clients){
        this.clients = clients;
    }

    public void register(String name){
        Client client = new Client(name);
        clients.add(client);
    }

    public boolean login(String name){
        for (Client client: clients) {
            if(client.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public boolean friend(List<String> names, Client friend){
        boolean exists = false;
        for(String name : names)
            for (Client client: clients) {
                if(client.getName().equals(name)){
                    client.friends.add(friend);
                    friend.friends.add(client);
                    exists = true;
                }
            }
        return exists;
    }

    public Client getClients(String name){
        for (Client client: clients) {
            if(client.getName().equals(name)){
                return client;
            }
        }
        return null;
    }

}
