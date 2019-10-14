package loadbalancer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.util.function.Function;

public class MandelbrotServer implements Server {

    private HttpClient httpClient;
    private String host;
    private int nrOfProcessors;

    public MandelbrotServer(HttpClient httpClient, String host) {
        this.httpClient = httpClient;
        this.host = host;
    }

    @Override
    public <T> T get(String path, Function<byte[], T> mapper) {
        HttpRequest request = HttpRequest.newBuilder().uri(constructUri(path)).build();
        byte[] bytes = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body)
                .join();
        return mapper.apply(bytes);
    }

    @Override
    public int getNrOfProcessors() {
        if (nrOfProcessors == 0) {
            this.nrOfProcessors = get("processors", (bytes -> {
                ByteBuffer.allocate(bytes.length);
                return ByteBuffer.wrap(bytes).getInt();
            }));
        }
        return nrOfProcessors;
    }

    private URI constructUri(String path) {
        return URI.create("http://" + this.host + "/" + path);
    }
}
