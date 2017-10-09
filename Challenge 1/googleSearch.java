
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


public class googleSearch {

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {

            String input=br.readLine(); //String that contains the email id
            input=input.replace(" ","+");
            String webAdress="http://www.google.com/search?q="+input; //String that concatenates google's default url and our search string
            //System.out.println(webAdress);
            //String that creates an url object from the web adress
            URL myURL = new URL(webAdress);
            URLConnection uc;
            uc=myURL.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            String tmpLine = null;
            while ((inputLine = in.readLine()) != null ) {
                if(inputLine.contains("<h3 class=\"r\"><a href=\"")) {
                    tmpLine = inputLine;
                    tmpLine = tmpLine.substring(tmpLine.lastIndexOf("<h3 class=\"r\"><a href=\"")+30);
                    tmpLine=tmpLine.substring(0,tmpLine.indexOf("&amp"));
                    System.out.println(tmpLine);
                    break;
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
