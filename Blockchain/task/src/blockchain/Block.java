package blockchain;

import java.io.Serializable;
import java.util.ArrayList;

class Block implements Serializable {

    private static final long serialVersionUID = 1L;
    private final long timestamp;
    private final int id;
    private final int magicNo;
    private final String myHash;
    private final String prevHash;
    private final int n;
    private final int genTime;
    private final int creatorNo;


    private final ArrayList <String> chat;

    public Block(long timestamp, int id, int magicNo, String myHash, String prevHash, int n, int genTime, int creatorNo, ArrayList<String> chat) {
        this.timestamp = timestamp;
        this.id = id;
        this.magicNo = magicNo;
        this.myHash = myHash;
        this.prevHash = prevHash;
        this.n = n;
        this.genTime = genTime;
        this.creatorNo = creatorNo;
        this.chat = chat;
    }

    public int getN() {
        return n;
    }
    public long getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }

    public String getMyHash() {
        return myHash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public int getMagicNo() {
        return magicNo;
    }

    public int getGenTime() { return genTime; }

    public int getCreatorNo() { return creatorNo; }

    public ArrayList<String> getChat() {
        return chat;
    }






//    public Block(int id, String prevHash, int n) {
//        LocalTime start = LocalTime.now();
//         this.timestamp = new Date().getTime();
//         this.id = id;
//         this.prevHash = prevHash;
//         this.n = n;
//         StringBuilder hashSource;
//         String currHash;
//         Random r =new Random();
//         int maybeMagic;
//         do {
//             hashSource = new StringBuilder();
//             hashSource.append(timestamp);
//             hashSource.append(id);
//             hashSource.append(prevHash);
//             hashSource.append(n);
//             maybeMagic = r.nextInt();
//             hashSource.append(maybeMagic);
//             currHash = StringUtil.applySha256(hashSource.toString());
//         } while (!validateHash(currHash, n));
//         this.magicNo = maybeMagic;
//         this.myHash = currHash;
//         LocalTime end = LocalTime.now();
//         this.genTime = end.toSecondOfDay() - start.toSecondOfDay();
//    }

//    public boolean validateHash (int n){
//        if (n == 0) return true;
//        String nSubString = myHash.substring(0, n+1);
//        for (char c : nSubString.toCharArray()){
//            if (c != '0') return false;
//        }
//        return true;
//    }



    @Override
    public String toString() {

        StringBuilder chatLog = new StringBuilder();
        if (chat.isEmpty()) chatLog.append(" no messages");
        else this.chat.stream().map(c -> chatLog.append("\n" + c));

        return "Block:" +
                "\nCreated by miner # " + creatorNo +
                "\nMiner #" + creatorNo + " gets 100 VC" +
                "\nId: " + id +
                "\nTimestamp: " + timestamp +
                "\nMagic number: " + magicNo +
                "\nHash of the previous block:\n" + prevHash +
                "\nHash of the block:\n" + myHash +
                "\nBlock data:" + chatLog.toString() +
                "\nBlock was generating for " + genTime + " seconds";
    }
}
