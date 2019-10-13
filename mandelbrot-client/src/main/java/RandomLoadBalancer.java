import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class RandomLoadBalancer implements LoadBalancer {

    private List<String> servers;
    private Random random;

    RandomLoadBalancer() {
        servers = new ArrayList<String>();
        random = new Random();
    }

    @Override
    public void addServer(String server) {
        this.servers.add(server);
    }

    @Override
    public String getServer() {
        var index = random.nextInt(servers.size());
        return servers.get(index);
    }
}
