package KALE.dataProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FindTrainMaxIndex {
    // 找到数据集中最大的实体编号，方便传参
    private static BufferedReader reader;

    public static void main(String[] args) {
        String path = "D:\\Program Files\\JavaProject\\KALE\\untitled\\src\\main\\java\\KALE\\datasets\\wn18\\test.txt";
        try {
            reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(path)), StandardCharsets.UTF_8));
            String line = "";
            int maxId = 0;
            while ((line = reader.readLine()) != null) {
                for (String s : line.split("\t")) {
                    maxId=Math.max(Integer.parseInt(s),maxId);
                }
            }
            System.out.println(maxId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
