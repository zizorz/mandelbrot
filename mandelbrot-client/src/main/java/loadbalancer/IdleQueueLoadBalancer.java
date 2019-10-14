package loadbalancer;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

public class IdleQueueLoadBalancer implements LoadBalancer {

    volatile private BlockingQueue<Server> idleProcessors;
    private int capacity;

    @Override
    public void addServers(List<Server> servers) {
        this.capacity = servers.stream().parallel().mapToInt(Server::getNrOfProcessors).sum();
        idleProcessors = new ArrayBlockingQueue<>(this.capacity);
        for (var server : servers) {
            for (int i = 0; i < server.getNrOfProcessors(); i++) {
                idleProcessors.add(server);
            }
        }
        System.out.println("Added " + servers.size() + " server(s) with " + this.capacity + " processor(s)");
    }

    @Override
    public <T> T sendGetRequest(String path, Function<byte[], T> mapper) {
        T result = null;
        try {
            var server = idleProcessors.take();
            result = server.get(path, mapper);
            idleProcessors.add(server);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int getNrOfResources() {
        return this.capacity;
    }
}
