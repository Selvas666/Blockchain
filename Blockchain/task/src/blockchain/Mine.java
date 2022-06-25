package blockchain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

public class Mine implements Runnable {

    private final BlockChain blockChain;
    private final long timestamp;
    private final int id;
    private final String prevHash;
    private final int n;
    private final LocalTime start;
    private final int creatorNo;
    private final ArrayList<String> chat;

    public Mine(BlockChain blockChain, long timestamp, int id, String prevHash, int n, LocalTime start, int creatorNo, ArrayList <String> chat) {
        this.blockChain = blockChain;
        this.timestamp = timestamp;
        this.id = id;
        this.prevHash = prevHash;
        this.n = n;
        this.start = start;
        this.creatorNo = creatorNo;
        this.chat = chat;
    }

    @Override
    public void run() {
         StringBuilder hashSource;
         String currHash;
         Random r = new Random();
         int maybeMagic;
         do {
             hashSource = new StringBuilder();
             hashSource.append(timestamp);
             hashSource.append(id);
             hashSource.append(prevHash);
             hashSource.append(n);
             hashSource.append(creatorNo);
             hashSource.append(chat.toString());
             maybeMagic = r.nextInt();
             hashSource.append(maybeMagic);
             currHash = StringUtil.applySha256(hashSource.toString());
         } while (!BlockChain.validateHash(currHash, n));
         int magicNo = maybeMagic;
         LocalTime end = LocalTime.now();
         int genTime = end.toSecondOfDay() - start.toSecondOfDay();
         blockChain.acceptBlock(id, new Block(timestamp, id, magicNo, currHash, prevHash, n, genTime, creatorNo, chat));
    }
}
