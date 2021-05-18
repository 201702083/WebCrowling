import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class main {
    public static void main(String[] args) throws IOException {

        String url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewBest.ink?tabType=EBOOK&tabSrnb=12";
        // 1위 ~ 20위 책을 보여주는 링크

        Document doc = Jsoup.connect(url).get();

        Elements titles = doc.select("div[class =\"title\"]");
        Elements publish = doc.select("div[class=\"info1\"]");
        int isbn = 1000;
        String info[][] = new String[titles.size()][3];
        int count = 0;
        for (Element e : titles.select("strong")){
            info[count][0] = e.text();
            count ++;
        }
        count = 0;
        for(Element e : publish.select(">span[class=\"n1\"]")) {
            info[count][1] = e.text();
            count++;
        }
        count = 0 ;
        for (Element e : publish.select(">span[class=\"n3\"]")){
            info[count][2] = e.text();
            count ++;
        }
        for ( int i = 0 ; i < info.length; i ++){
            System.out.println("INSERT INTO EBOOK ('"+isbn+"','" +info[i][0]+"', '"+info[i][1]+"','"+info[i][2]+"','NULL','0',NULL','NULL')");
            isbn++;
        }
    }
}
