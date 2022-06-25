package blockchain;

import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        int n = 0;
//        System.out.println("Enter how many zeros the hash must start with: ");
//        try (Scanner scanner = new Scanner(System.in)){
//            n = scanner.nextInt();
//        }
        String filePath = "myBlockChain" + n + ".blockchain";
        BlockChain blockChain = BlockChain.initialize(n, filePath);
        Miner [] miners = new Miner[10];
        for (int i = 1; i <= 10; i++){
            miners[i-1] = new Miner(i);
        }
        blockChain.orderBlock(miners);
        Chatter [] chatters = new Chatter[3];
        for (int i = 0; i<3; i++){
            chatters[i] = new Chatter(blockChain, 30, 1);
        }
        for (int i = 0; i<4; i++){
            blockChain.orderBlock(miners);
        }
        for (int i = 0; i < 9; i ++){
            miners [i].getExecutor().shutdown();
        }
        Stream.of(chatters).forEach(ch -> ch.getExecutor().shutdown());
//        blockChain.validateBlockchain();
//        for (int i = 0; i < 5; i++){
//            System.out.printf("%s%n%n", blockChain.getBlocks().get(i).toString());
//        }
    }
}
