package blockchain;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

class BlockChain implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<Block> blocks;
    private volatile int idCounter;
    private volatile int n;
    private final String filePath;
    private volatile ArrayList <String> chat;


    public BlockChain(int n, String filePath) {
        blocks = new ArrayList<>();
        this.n = n;
        this.filePath = filePath;
        this.idCounter = 1;
        this.chat = new ArrayList<>();
    }

    public synchronized void msgChat (String msg) {
        this.chat.add(msg);
    }

    public boolean validateBlockchain() {
        String curr;
        for (int i = 0; i < blocks.size(); i++) {
            StringBuilder builder = new StringBuilder();
            builder.append(blocks.get(i).getTimestamp());
            builder.append(blocks.get(i).getId());
            builder.append(blocks.get(i).getPrevHash());
            builder.append(blocks.get(i).getN());
            builder.append(blocks.get(i).getCreatorNo());
            builder.append(blocks.get(i).getChat().toString());
            builder.append(blocks.get(i).getMagicNo());
            curr = blocks.get(i).getMyHash();
            if (!(curr.equals(StringUtil.applySha256(builder.toString()))) && validateHash(blocks.get(i).getMyHash(), blocks.get(i).getN())) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateHash (String hash, int n){
        if (n == 0) return true;
        String nSubString = hash.substring(0, n);
        for (char c : nSubString.toCharArray()){
            if (c != '0') return false;
        }
        return true;
    }

    public void orderBlock (Miner[] miners){
        long timestamp = new Date().getTime();
        int orderId = idCounter;
        int orderN = n;
        LocalTime start = LocalTime.now();
        String prevHash = "0";
        ArrayList <String> orderChat = new ArrayList<>(chat);
        chat.clear();
        if (idCounter>1)  {
            prevHash = blocks.get(idCounter-2).getMyHash();
        }
        for (Miner m : miners){
            m.acceptOrder(this,  timestamp, orderId, prevHash, orderN, start, orderChat);
        }
        while (idCounter == orderId){

        }
    }

    public void acceptBlock (int id, Block newBlock) {

        final Block newAddition = new Block(newBlock.getTimestamp(),
                newBlock.getId(),
                newBlock.getMagicNo(),
                newBlock.getMyHash(),
                newBlock.getPrevHash(),
                newBlock.getN(),
                newBlock.getGenTime(),
                newBlock.getCreatorNo(),
                newBlock.getChat());

        if (blocks.size()>0) {
            if (!(newAddition.getPrevHash().equals(blocks.get(id - 2).getMyHash()))) return;
        }
        else if (!(newAddition.getPrevHash().equals("0"))) return;

        if (!(validateHash(newAddition.getMyHash(), n))) return;

        addBlock(newAddition);
    }
    private synchronized void addBlock (Block newAddition){
        if (idCounter == newAddition.getId()){
            blocks.add(newAddition);
            idCounter += 1;
            if (newAddition.getGenTime() < 15) this.n += 1;
            if (newAddition.getGenTime() > 15) this.n -= 1;
            System.out.println(newAddition.toString());
            if (newAddition.getN() < this.n){
                System.out.println("N was increased to " + this.n + "\n");
            }
            else if (newAddition.getN() == this.n){
                System.out.println("N stays the same\n");
            }
            else if (newAddition.getN() > this.n){
                System.out.println("N was decreased by 1\n");
            }
            saveToFile();
        }
    }

    private void saveToFile () {
        try (FileOutputStream fos = new FileOutputStream(filePath, true);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             AppendingObjectOutputStream oos = new AppendingObjectOutputStream(bos);) {
                oos.writeObject(blocks.get(blocks.size() - 1));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public boolean append() {
//        try {
//            try {
//                this.blocks.add(new Block(this.idCounter, this.blocks.get(this.blocks.size() - 1).getMyHash(), n));
//                this.idCounter += 1;
//                try (FileOutputStream fos = new FileOutputStream(filePath, true);
//                     BufferedOutputStream bos = new BufferedOutputStream(fos);
//                     AppendingObjectOutputStream oos = new AppendingObjectOutputStream(bos);) {
//                    oos.writeObject(blocks.get(blocks.size() - 1));
//                    oos.close();
//                    return true;
//                }
//            } catch (IndexOutOfBoundsException e) {
//                this.blocks.add(new Block(this.idCounter, "0", n));
//                this.idCounter += 1;
//                try (FileOutputStream fos = new FileOutputStream(filePath);
//                     BufferedOutputStream bos = new BufferedOutputStream(fos);
//                     ObjectOutputStream oos = new ObjectOutputStream(bos);){
//                    oos.writeObject(blocks.get(blocks.size() - 1));
//                    oos.close();
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }


    public static BlockChain initialize(int n, String filePath) {
        try {
            File maybeFile = new File(filePath);
            maybeFile.delete();
            if (!maybeFile.exists()) {
                maybeFile.createNewFile();
                return new BlockChain(n, filePath);
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
            Block block = (Block) ois.readObject();
            BlockChain readBlocks = new BlockChain(block.getN(), filePath);
            readBlocks.blocks.add(block);
            readBlocks.idCounter += 1;
            while (true) {
                try {
                    block = (Block) ois.readObject();
                    readBlocks.blocks.add(block);
                    readBlocks.idCounter += 1;
                } catch (EOFException e) {
                    e.printStackTrace();
                    ois.close();
                    break;
                }
            }
            if (readBlocks.validateBlockchain()) {
                return readBlocks;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return null;
    }



}
