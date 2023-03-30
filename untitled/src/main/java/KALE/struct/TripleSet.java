package KALE.struct;

import KALE.utils.StringSplitter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class TripleSet {
    private int iNumberOfEntities;
    private int iNumberOfRelations;
    private int iNumberOfTriples;
    // 用列表存储所有的triple
    public ArrayList<Triple> pTriple = null;
    public HashMap<String, Boolean> pTripleStr = null;

    public TripleSet() {
        this.pTripleStr = new HashMap<>();
    }

    public TripleSet(int iEntities, int iRelations) throws Exception {
        this.iNumberOfEntities = iEntities;
        this.iNumberOfRelations = iRelations;
    }

    public int entities() {
        return this.iNumberOfEntities;
    }

    public int relations() {
        return this.iNumberOfRelations;
    }

    public int triples() {
        return this.iNumberOfTriples;
    }

    public HashMap<String, Boolean> tripleSet() {
        return this.pTripleStr;
    }

    // 根据triple列表里的ID取出对应的triple
    public Triple get(int iID) throws Exception {
        if (iID >= 0 && iID < this.iNumberOfTriples) {
            return this.pTriple.get(iID);
        } else {
            throw new Exception("getTriple error in TripleSet: ID out of range");
        }
    }

    // 将三元组读取到内存中， 存储到列表ArrayList<Triple> pTriple中
    public void load(String fnInput) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(fnInput)), StandardCharsets.UTF_8));
        this.pTriple = new ArrayList<>();
        String line = "";

        while ((line = reader.readLine()) != null) {
            String[] tokens = StringSplitter.RemoveEmptyEntries(StringSplitter.split("\t ", line));
            if (tokens.length != 3) {
                throw new Exception("一条不完整的三元组:load error in TripleSet: data format incorrect");
            }

            int iHead = Integer.parseInt(tokens[0]);
            int iTail = Integer.parseInt(tokens[2]);
            int iRelation = Integer.parseInt(tokens[1]);
            if (iHead >= 0 && iHead < this.iNumberOfEntities) {
                if (iTail >= 0 && iTail < this.iNumberOfEntities) {
                    if (iRelation >= 0 && iRelation < this.iNumberOfRelations) {
                        this.pTriple.add(new Triple(iHead, iRelation, iTail));
                        continue;
                    }

                    throw new Exception("load error in TripleSet: relation ID out of range");
                }

                throw new Exception("load error in TripleSet: tail entity ID out of range");
            }

            throw new Exception("load error in TripleSet: head entity ID out of range");
        }

        this.iNumberOfTriples = this.pTriple.size();
        reader.close();
    }

    // 记录输入文件中的每一行内容是否重复pTripleStr.put(line.trim(), true);
    public void loadStr(String fnInput) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(fnInput)), StandardCharsets.UTF_8));
        String line = "";
        while ((line = reader.readLine()) != null) {
            this.pTripleStr.put(line.trim(), true);
        }
        reader.close();
    }

    // 加载文件中的1000行，做快速测试
    public void subload(String fnInput) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(fnInput)), StandardCharsets.UTF_8));
        this.pTriple = new ArrayList<>();
        String line = "";
        int count = 0;// 记录文件的行数

        while ((line = reader.readLine()) != null) {
            ++count;
            String[] tokens = StringSplitter.RemoveEmptyEntries(StringSplitter.split("\t ", line));
            if (tokens.length != 3) {
                throw new Exception("load error in TripleSet: data format incorrect");
            }

            int iHead = Integer.parseInt(tokens[0]);
            int iTail = Integer.parseInt(tokens[2]);
            int iRelation = Integer.parseInt(tokens[1]);
            if (iHead >= 0 && iHead < this.iNumberOfEntities) {
                if (iTail >= 0 && iTail < this.iNumberOfEntities) {
                    if (iRelation >= 0 && iRelation < this.iNumberOfRelations) {
                        this.pTriple.add(new Triple(iHead, iRelation, iTail));
                        if (count != 1000) {
                            continue;
                        }
                        break;
                    }

                    throw new Exception("load error in TripleSet: relation ID out of range");
                }

                throw new Exception("load error in TripleSet: tail entity ID out of range");
            }

            throw new Exception("load error in TripleSet: head entity ID out of range");
        }
        this.iNumberOfTriples = this.pTriple.size();
        reader.close();
    }

    // 打乱顺序
    public void randomShuffle() {
        TreeMap<Double, Triple> tmpMap = new TreeMap<>();
        int i, j, k;
        for (int iID = 0; iID < this.iNumberOfTriples; ++iID) {
            i = this.pTriple.get(iID).head();
            j = this.pTriple.get(iID).tail();
            k = this.pTriple.get(iID).relation();
            tmpMap.put(Math.random(), new Triple(i, k, j));
        }

        this.pTriple = new ArrayList<>();

        for (double dRand : tmpMap.keySet()) {
            Triple trip = tmpMap.get(dRand);
            this.pTriple.add(new Triple(trip.head(), trip.relation(),trip.tail()));
        }

        this.iNumberOfTriples = this.pTriple.size();
        tmpMap.clear();
    }
}
