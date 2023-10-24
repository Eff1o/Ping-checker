import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PingChecker {
    private static final int NUM_THREADS = 3; // Liczba wątków
    private static final String[] SERVERS = {"google.com", "facebook.com", "stackoverflow.com"};

    private ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);

    public static void main(String[] args) {
        PingChecker pingChecker = new PingChecker();
        pingChecker.startPingThreads();
    }

    private void startPingThreads() {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (String server : SERVERS) {
            executor.execute(() -> {
                try {
                    int count = counter.get();
                    counter.set(count + 1);

                    InetAddress address = InetAddress.getByName(server);
                    long startTime = System.currentTimeMillis();
                    boolean isReachable = address.isReachable(5000); // 5000 ms timeout
                    long endTime = System.currentTimeMillis();

                    count = counter.get();
                    counter.set(count - 1);

                    long pingTime = endTime - startTime;
                    System.out.println(server + " - isReachable: " + isReachable + ", ping: " + pingTime + "ms");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
    }
}
