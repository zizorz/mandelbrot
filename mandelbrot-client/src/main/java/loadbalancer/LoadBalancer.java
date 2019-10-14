package loadbalancer;

import java.util.List;
import java.util.function.Function;

public interface LoadBalancer {

    void addServers(List<Server> servers);

    <T> T sendGetRequest(String path, Function<byte[], T> mapper);

    int getNrOfResources();

}
