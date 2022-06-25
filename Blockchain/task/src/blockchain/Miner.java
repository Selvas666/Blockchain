package blockchain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Miner {

    private final int minerNo;

    public ExecutorService getExecutor() {
        return executor;
    }

    private final ExecutorService executor =  Executors.newSingleThreadExecutor();

    public Miner(int minerNo) {
        this.minerNo = minerNo;
    }

    public void acceptOrder (BlockChain blockChain, long timestamp, int id, String prevHash, int n, LocalTime start, ArrayList <String> chat){
        this.executor.submit(new Mine(blockChain, timestamp, id, prevHash, n, start, minerNo, chat));
    }
}
