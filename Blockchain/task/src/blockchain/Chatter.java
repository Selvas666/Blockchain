package blockchain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Chatter {
    private BlockChain bc;
    private long life;
    private long interval;

    public ExecutorService getExecutor() {
        return executor;
    }

    private final ExecutorService executor;


    public Chatter(BlockChain bc, long life, long interval) {
        this.bc = bc;
        this.life = life;
        this.interval = interval;
        this.executor =  Executors.newSingleThreadExecutor();
        executor.submit(new Chat(bc, interval, life));
    }

}
