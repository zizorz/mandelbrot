package loadbalancer;

import java.util.function.Function;

public interface Server {

    <T> T get(String path, Function<byte[], T> mapper);

    int getNrOfProcessors();

}
