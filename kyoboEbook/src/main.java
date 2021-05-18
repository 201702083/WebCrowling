import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;

public class main {
    public static void main(String[] args) throws IOException {

//   BEST
        String url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewBest.ink?tabType=EBOOK&tabSrnb=12";
        url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewNew.ink?tabType=EBOOK&tabSrnb=";
//   NEW
//     String url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewFree.ink?tabType=EBOOK&tabSrnb=12";
//   FREE     String url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewNew.ink?tabType=EBOOK&tabSrnb=12R";
//   RECOMMEND String url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewMDRecommend.ink?tabType=";
        // 페이지 골라서 링크 복붙해서 실행
        Document doc = Jsoup.connect(url).get();

        Elements titles = doc.select("div[class =\"title\"]");
        Elements publish = doc.select("div[class=\"info1\"]");
        int isbn = 1020;
        String info[][] = new String[titles.size()][3];

        // 제목 저장
        int count = 0;
        for (Element e : titles.select("strong")){
            info[count][0] = e.text();
            count ++;
        }
        // 작가 저장
        count = 0;
        for(Element e : publish.select(">span[class=\"n1\"]")) {
            info[count][1] = e.text();
            count++;
        }

        // 출판사 저장
        count = 0 ;
        for (Element e : publish.select(">span[class=\"n3\"]")){
            info[count][2] = e.text();
            count ++;
        }

        // Ebook , Author 추가
        System.out.println("You need this");
        System.out.println("alter table ebook modify(publisher varchar2(50)) ;");
        System.out.println("alter table authors modify(author varchar2(50)) ;");
        for ( int i = 0 ; i < info.length; i ++){
            System.out.println("INSERT INTO EBOOK VALUES("+isbn+",'" +info[i][0]+"', '"+info[i][2]+"',NULL,NULL,0,NULL,NULL);");
            System.out.println("Insert INTO AUTHORS VALUES ("+isbn+",'"+info[i][1]+"');");
            isbn++;
        }

        /* 이름 생성 기능
        final int customer_size = 5;
        String nameurl = "https://baby-name.kr/annalRanking/2020/";
        Document namedoc = Jsoup.connect(nameurl).get();
        Elements name = namedoc.select("table#tlb-rank-box");
        Elements name1 = name.select("tbody > tr > td>a");
        Elements name2 = name1.select("span");
        String names[] = new String[customer_size];
        Random random = new Random();
        count = 0;
        System.out.println("name1\n"+name1);
        System.out.println("name2\n"+name2);

        for (Element e : name2){
            int a;
            System.out.println(random.nextInt(5));
            if ( (a=random.nextInt(5)) != 3){
                System.out.println(a);
                continue;
            }
            else{
                names[count] = e.text();
                count++;
            }
        }

        for ( int i = 0 ; i < customer_size; i ++){
            System.out.println(names[i]);
        }
    */
    }
}
