import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AppController {
    Scanner sc = new Scanner(System.in);
    String info[][]; // 책 데이터
    String customer[][]; // 사람 데이터
    int bookNum;
    int customerNum;
    int isbn;
    int cno;
    int rentalOrder;
    int reservationOrder;
    Random random = new Random();

    AppController(){
        info = null;
        customer = null;
        isbn = 1000;
        cno = 100;
        rentalOrder = 0;
        reservationOrder = 0;
        bookNum = 0;
        customerNum = 0;
    }
    AppController(int isbn_, int cno_){
        info = null;
        customer = null;
        isbn = isbn_;
        cno = cno_;
        rentalOrder = 0;
        reservationOrder = 0;
        bookNum = 0;
        customerNum = 0;
    }

    void addBookInfo() throws IOException {
        System.out.println("\n----------- BOOKINFO -----------");
        if ( this.info == null){ // 4가지 링크 모두 생성
            System.out.println("현재 생성된 책 데이터 없음 바로 생성");

        }
        else{
            System.out.println("현재 생성된 책 데이터가 있음\n 추가 생성 - 1 \n 취소 - any key ");
            System.out.print("Choice : ");
            int m = sc.nextInt();
            if (m != 1) return;
        }
        System.out.println("\n[Choice] 1. BEST  2. NEW  3. FREE  4.RECOMMEND");
        System.out.print("Category : ");
        int link = sc.nextInt();
        String url = null;
        switch ( link ){
            case 1: // BEST
                url =  "http://digital.kyobobook.co.kr/digital/publicview/publicViewBest.ink?tabType=EBOOK&tabSrnb=12";
                break;
            case 2: // NEW
                url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewFree.ink?tabType=EBOOK&tabSrnb=12";
                break;
            case 3: // FREE
                url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewNew.ink?tabType=EBOOK&tabSrnb=12R";
                break;
            case 4:  // RECOMMEND
                url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewMDRecommend.ink?tabType=";
                break;
            default :
                while( link < 0 || link > 4) {
                    System.out.println("[ERROR] 1~4 중 다시 입력하시오.") ;
                    link = sc.nextInt();
                }
        }
        Document doc = Jsoup.connect(url).get();

        Elements titles = doc.select("div[class =\"title\"]");
        Elements publish = doc.select("div[class=\"info1\"]");
        String[][] books = new String[titles.size()][4];


        // 제목 저장
        int count = 0;
        for (Element e : titles.select("strong")){
            books[count][1] = e.text();
            count ++;
        }
        // 작가 , 출판사 저장
        count = 0;
        for(Element e : publish) {
            Elements author = e.select(">span[class=\"n1\"]");
            String pusblihser = "3";
            if ( link != 1 ) pusblihser = "2";
            Elements publiser = e.select(">span[class=\"n"+pusblihser+"\"]");
            books[count][2] = author.text();
            books[count][3] = publiser.text();
            count++;
        }

        // SQL QUERY 생성
        System.out.println("You need this");
        System.out.println("alter table ebook modify(publisher varchar2(50)) ;");
        System.out.println("alter table authors modify(author varchar2(50)) ;\n\n");
        for ( int i = 0 ; i < books.length; i ++){
            books[i][0] = String.valueOf(isbn);
            System.out.println("INSERT INTO EBOOK VALUES("+books[i][0]+",'" +books[i][1]+"', '"+books[i][3]+"',NULL,NULL,0,NULL,NULL);");
            System.out.println("Insert INTO AUTHORS VALUES ("+books[i][0]+",'"+books[i][2]+"');");
            isbn++;
        }
        bookNum += count;
        if ( this.info == null) this.info = books;
        else this.info = Stream.concat(Arrays.stream(info),Arrays.stream(books)).toArray(String[][]::new);


    }

    public void addCustomerInfo()throws IOException {
        System.out.println("\n----------- CUSTOMER -----------");
        System.out.print("[Input] 추가할 고객 인원 ( 1 ~ 100 ) : ");
        int n = sc.nextInt();
        while ( n < 1 || n > 100){
            System.out.print("[ERROR] 1 ~ 100 에서 재입력하시오 : ");
            n = sc.nextInt();
        }
        final int customer_size = n;
        String nameurl = "https://m.blog.naver.com/spring1a/221212019473";
        Document namedoc = Jsoup.connect(nameurl).get();
        Elements name = namedoc.select("tr.se-tr>td:nth-child(1)>div>p>span");
        String[][] person = new String[customer_size][4];
        Random random = new Random();
        int count = 0;
        for (Element e : name){
            if ( random.nextInt(2) != 1){ // 순회하면서 1/2 확률로 넣는다..
                continue;
            }
            else{
                person[count][1] = e.text();
                count++;
            }
            if ( count == person.length) break; // 꽉차면 종료
        }
        customerNum +=count;

        String homepage[] = {"gmail.com","naver.com","hanmail.net","o.cnu.ac.kr","daum.net"};

        for ( int i = 0 ; i < customer_size; i ++){
            person[i][0] = String.valueOf(cno);
            cno++;
            int emailExist = random.nextInt(2);

            if (emailExist == 1){
                String firstemail = "";
                for ( int q = 0 ; q < 5; q ++){
                    firstemail+= String.valueOf((char)('a'+ random.nextInt(26)));
                }
                person[i][3] = "'"+firstemail+String.valueOf(random.nextInt(500))+"@"+homepage[random.nextInt(homepage.length)]+"'";
            }
            else{
                person[i][3] = "NULL";
            }
            person[i][2] = String.valueOf(random.nextInt(10000)) +String.valueOf('a'+random.nextInt(64));
        }
        // SQL QUERY 생성
        for( int i = 0 ; i < customer_size ; i++){
            System.out.println("INSERT INTO CUSTOMER VALUES("+person[i][0]+", '"+person[i][1]+"' , '"+person[i][2]+"',"+person[i][3]+");");
        }
        if ( this.customer == null) this.customer = person;
        else this.customer = Stream.concat(Arrays.stream(customer),Arrays.stream(person)).toArray(String[][]::new);
        //test

    }
    void createRentalHistory(){
        System.out.println("\n----------- PREVIOUSRENTAL -----------");
        System.out.print("[Input] N회의 대여 이력 \n N : ");
        final int rental = sc.nextInt();
        for ( int i = 0 ; i < rental ; i ++){
            String RentalIsbn = info[random.nextInt(info.length)][0];
            String RentalCno = customer[random.nextInt(customer.length)][0];
            String year = String.valueOf(random.nextInt(3)+2019);
            String month = String.valueOf(random.nextInt(12)+1);
            String date = String.valueOf(random.nextInt(15)+1);

            if ( month.length() < 2) month= "0" + month;

            if ( date.length() < 2) date = "0"+date;

            String returndate = String.valueOf(Integer.parseInt(date) + random.nextInt(5)+1);
            if ( returndate.length() < 2) returndate = "0"+returndate;

            rentalOrder++;

            // SQL QUERY 생성
            System.out.println("INSERT INTO PREVIOUS VALUES("+RentalIsbn+",to_date('"+year+"-"+month+"-"+date+"','yyyy-mm-dd'),to_date('"+year+"-"+month+"-"+returndate+"','yyyy-mm-dd'),"+RentalCno+");");

        }
    }
    void createResevation(){
        System.out.println("\n----------- RESERVATION -----------");
        System.out.print("[Input] N 개의 책 각 M 번의 예약 \n N : ");
        final int reservatedbook = sc.nextInt();
        System.out.print("M : " );
        final int repeat = sc.nextInt();
        for ( int i = 0 ; i < reservatedbook ; i ++){
            String ReserIsbn = info[random.nextInt(info.length)][0];
            String ReserCno = customer[random.nextInt(customer.length)][0];
            for (int j = 0 ; j < repeat; j ++){
                String year = String.valueOf(random.nextInt(3)+2019);
                String month = String.valueOf(random.nextInt(12)+1);
                String date = String.valueOf(random.nextInt(30)+1);

                if ( month.length() < 2) month= "0" + month;
                if ( date.length() < 2)  date = "0"+date;

                reservationOrder++;

                // SQL QUERY 생성
                System.out.println("INSERT INTO RESERVATION VALUES("+ReserIsbn+","+ReserCno+",to_date('"+year+"-"+month+"-"+date+"','yyyy-mm-dd');");
            }
        }
    }

    public int menu(){
        System.out.println("\n[Menu] \n 1 - 책 데이터 생성 \n 2 - 사람 데이터 생성 \n 3 - 대여기록 생성 \n 4 - 예약기록 생성 \n 5 - 종료");
        System.out.print("[Do] : ");
        int m = sc.nextInt();
        while( m < 1 || m > 5 ){
            System.out.println("\n[ERROR] 0~5 중 다시 입력하시오.");
            System.out.print("[Do] : ");
            m = sc.nextInt();
        }
        return m;
    }
    public void result(){
        System.out.print("총 " + this.bookNum + "개의 책을 생성");
        if (this.bookNum > 0 ){
            System.out.print((" ("+this.info[0][0]+" ~ " + this.info[info.length-1][0]+")"));
        }
        System.out.print("\n총 " + this.customerNum + "명의 고객을 생성");
        if ( this.customerNum > 0){
            System.out.print((" ("+this.customer[0][0]+" ~ " + this.customer[customer.length-1][0]+")"));
        }
        System.out.println("\n총 " + this.rentalOrder +"개의 대여 이력을 생성");
        System.out.println("총 " + this.reservationOrder +"개의 예약 이력을 생성");
    }
    void run() throws IOException {
        boolean running = true;
        while (running){
            switch ( menu() ) {
                case 1:
                    addBookInfo();
                    break;
                case 2:
                    addCustomerInfo();
                    break;
                case 3:
                    if(info == null || customer == null) System.out.println("\n[ERROR] 기본 엔터티 책 또는 고객이 존재하지 않습니다.");
                    else createRentalHistory();

                    break;
                case 4:
                    if(info == null || customer == null) System.out.println("\n[ERROR] 기본 엔터티 책 또는 고객이 존재하지 않습니다.");
                    else createResevation();

                    break;
                case 5:
                    running = false;
                    break;
            }
        }
        this.result();

    }
}
