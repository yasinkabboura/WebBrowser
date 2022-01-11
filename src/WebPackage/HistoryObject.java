package WebPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class HistoryObject implements Serializable {

    String url;


    @Override
    public String toString() {
        return "Url=" + url ;
    }


    public HistoryObject() {
    }

    public void addHist(String url, String fileName) {
        this.url = url;

        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName,true);
            fw.write(url);
            fw.write(System.lineSeparator());
        } catch (Exception e) {
            System.out.println(e);
        }finally{
            try {
                fw.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

    public ArrayList<HistoryObject> getAllHistory(String fileName){
        ArrayList<HistoryObject> ar = new ArrayList();
        BufferedReader br = null;
        try {

            br = new BufferedReader(new FileReader(fileName));
            while(true){
                HistoryObject ho = new HistoryObject();
                StringTokenizer token = new StringTokenizer(br.readLine());
                while(token.hasMoreTokens()){
                    ho.url = token.nextToken();
                }

                if(ho == null){
                    break;
                }

                ar.add(ho);

            }
        } catch (Exception e) {
            System.out.println("End of File");
        }finally{
            try {
                br.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return ar;
    }

}