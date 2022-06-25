package blockchain;

import java.util.concurrent.TimeUnit;

public class Chat implements Runnable {
    BlockChain bc;
    long interval;
    long life;

    public Chat(BlockChain bc, long interval, long life) {
        this.bc = bc;
        this.interval = interval;
        this.life = life;
    }

    @Override
    public void run () {
        try {
            for (long i = 0; i <= life; i++) {
                bc.msgChat(String.valueOf(Math.random()));
                TimeUnit.MILLISECONDS.sleep(interval * 100);
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
