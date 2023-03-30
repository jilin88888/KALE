package KALE.dataProcess;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Filter {
    private static BufferedWriter ent;
    private static BufferedReader reader;
    // 过滤掉groundings文件中，实体编号不在实体字典中的rule
    public static void main(String[] args) {
        String path = "D:\\Program Files\\JavaProject\\KALE\\untitled\\src\\main\\java\\KALE\\datasets\\wn18\\groundings.txt";
        String path_out = "D:\\Program Files\\JavaProject\\KALE\\untitled\\src\\main\\java\\KALE\\datasets\\wn18\\groundings_filter.txt";
        try {
            ent = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(path_out)), StandardCharsets.UTF_8));
            reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(path)), StandardCharsets.UTF_8));
            String line = "";

            while ((line = reader.readLine()) != null) {
                String temp = line;
                // (37338	17	37848)	(37848	7	37338)
                line = line.replace("(","");
                line = line.replace(")","");
                // 37338	17	37848	37848	7	37338
                String[] split = line.split("\t");
                int head1 = Integer.parseInt(split[0]);
                int head2 = Integer.parseInt(split[3]);
                int tail1 = Integer.parseInt(split[2]);
                int tail2 = Integer.parseInt(split[5]);
                if (!(head1>40493||head2>40493||tail1>40493||tail2>40493)){
                    ent.write(temp+"\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                ent.close();
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
