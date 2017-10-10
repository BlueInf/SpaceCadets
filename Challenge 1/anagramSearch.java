
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


public class anagramSearch {

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {

            String input=br.readLine(); //String that contains the email id
            input=input.replace(" ","+");
            String webAdress="https://new.wordsmith.org/anagram/anagram.cgi?anagram="+input; //the new String
            //String that creates an url object from the web adress
            URL myURL = new URL(webAdress);
            URLConnection uc;
            uc=myURL.openConnection();
            //In order to not get 403, we use add request Property
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            String tmpLine = "";
            while ((inputLine = in.readLine()) != null ) {
                if(inputLine.contains("<div class=\"p402_premium\">")) {
                    while ((inputLine = in.readLine()) != null){
                        if(inputLine.contains("Displaying all:")){
                            while ((inputLine = in.readLine()) != null){
                                if(inputLine.contains("</div")) return;
                                tmpLine=inputLine.replaceAll("(<br>|</b>)","");
                                //System.out.println("This is the original line -->"+inputLine);
                                //System.out.println("This must be the anagrams "+tmpLine);
                                System.out.println(tmpLine);
                            }
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//Krasimir Marinov updated- 10/09.2017
