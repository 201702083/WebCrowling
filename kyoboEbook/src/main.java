import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class main {
    public static void main(String[] args) throws IOException {

//   BEST
        String url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewBest.ink?tabType=EBOOK&tabSrnb=12";
//   NEW       url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewFree.ink?tabType=EBOOK&tabSrnb=12";
//   FREE      url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewNew.ink?tabType=EBOOK&tabSrnb=12R";
//   RECOMMEND url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewMDRecommend.ink?tabType=";
        // 페이지 골라서 링크 복붙해서 실행
        Document doc = Jsoup.connect(url).get();

        Elements titles = doc.select("div[class =\"title\"]");
        Elements publish = doc.select("div[class=\"info1\"]");
        int isbn = 1000;
        String info[][] = new String[titles.size()][4];

        // 제목 저장
        int count = 0;
        for (Element e : titles.select("strong")){
            info[count][1] = e.text();
            count ++;
        }
        // 작가 저장
        count = 0;
        for(Element e : publish.select(">span[class=\"n1\"]")) {
            info[count][2] = e.text();
            count++;
        }

        // 출판사 저장
        count = 0 ;
        for (Element e : publish.select(">span[class=\"n3\"]")){
            info[count][3] = e.text();
            count ++;
        }


        // Ebook , Author 추가
        System.out.println("You need this");
        System.out.println("alter table ebook modify(publisher varchar2(50)) ;");
        System.out.println("alter table authors modify(author varchar2(50)) ;\n\n");
        for ( int i = 0 ; i < info.length; i ++){
            info[i][0] = String.valueOf(isbn);
            System.out.println("INSERT INTO EBOOK VALUES("+info[i][0]+",'" +info[i][1]+"', '"+info[i][3]+"',NULL,NULL,0,NULL,NULL);");
            System.out.println("Insert INTO AUTHORS VALUES ("+info[i][0]+",'"+info[i][2]+"');");
            isbn++;
        }

//         이름 생성 기능
        System.out.println("\n----------- CUSTOMER -----------");
        final int customer_size = 15;
        int cno = 0;
        String nameurl = "https://m.blog.naver.com/spring1a/221212019473";
        Document namedoc = Jsoup.connect(nameurl).get();
        Elements malename = namedoc.select("tr.se-tr>td:nth-child(1)>div>p>span");
        String customer[][] = new String[customer_size][4];
        Random random = new Random();
        count = 0;

        for (Element e : malename){
            if ( random.nextInt(5) != 3){ // 순회하면서 1/5 확률로 넣는다..
                continue;
            }
            else{
                customer[count][1] = e.text();
                count++;
            }
            if ( count == customer.length) break; // 꽉차면 종료
        }
        String homepage[] = {"gmail.com","naver.com","hanmail.net","o.cnu.ac.kr","daum.net"};

        for ( int i = 0 ; i < customer_size; i ++){
            cno+= random.nextInt(800);
            for (int j = 0 ; j < customer_size ; j ++){
                while (String.valueOf(cno).equals(customer[j][0])){
                    cno+=1;
                }
            }
            customer[i][0] = String.valueOf(cno);
            cno = 0;
            int emailExist = random.nextInt(2);

            if (emailExist == 1){
                String firstemail = "";
                for ( int q = 0 ; q < 5; q ++){
                    firstemail+= String.valueOf((char)('a'+ random.nextInt(26)));
                }
                customer[i][3] = "'"+firstemail+String.valueOf(random.nextInt(500))+"@"+homepage[random.nextInt(homepage.length)]+"'";
            }
            else{
                customer[i][3] = "NULL";
            }
            customer[i][2] = String.valueOf(random.nextInt(10000)) +String.valueOf('a'+random.nextInt(64));
        }
        for( int i = 0 ; i < customer_size ; i++){
            System.out.println("INSERT INTO CUSTOMER VALUES("+customer[i][0]+", '"+customer[i][1]+"' , '"+customer[i][2]+"',"+customer[i][3]+");");
        }


        // reservation
        System.out.println("\n----------- RESERVATION -----------");
        final int reservatedbook = 3;
        final int repeat = 3;
        for ( int i = 0 ; i < reservatedbook ; i ++){
            String ReserIsbn = info[random.nextInt(info.length)][0];
            String ReserCno = customer[random.nextInt(customer.length)][0];
            for (int j = 0 ; j < repeat; j ++){
                String year = String.valueOf(random.nextInt(3)+2019);
                String month = String.valueOf(random.nextInt(12)+1);
                if ( month.length() < 2){
                     month= "0" + month;
                }
                String date = String.valueOf(random.nextInt(30)+1);
                if ( date.length() < 2){
                     date = "0"+date;
                }

                System.out.println("INSERT INTO RESERVATION VALUES("+ReserIsbn+","+ReserCno+",to_date('"+year+"-"+month+"-"+date+"','yyyy-mm-dd');");
            }
        }

        //PreviousRental
        System.out.println("\n----------- PREVIOUSRENTAL -----------");

        final int rental = 10;
        for ( int i = 0 ; i < rental ; i ++){
            String RentalIsbn = info[random.nextInt(info.length)][0];
            String RentalCno = customer[random.nextInt(customer.length)][0];
            String year = String.valueOf(random.nextInt(3)+2019);
            String month = String.valueOf(random.nextInt(12)+1);
            if ( month.length() < 2){
                month= "0" + month;
            }
            String date = String.valueOf(random.nextInt(15)+1);
            if ( date.length() < 2){
                date = "0"+date;
            }
            String returndate = String.valueOf(Integer.parseInt(date) + random.nextInt(5)+1);
            if ( returndate.length() < 2){
                returndate = "0"+returndate;
            }
            System.out.println("INSERT INTO PREVIOUS VALUES("+RentalIsbn+",to_date('"+year+"-"+month+"-"+date+"','yyyy-mm-dd'),to_date('"+year+"-"+month+"-"+returndate+"','yyyy-mm-dd'),"+RentalCno+");");

        }


    }
}
