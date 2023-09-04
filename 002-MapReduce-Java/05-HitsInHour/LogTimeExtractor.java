import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogTimeExtractor {
    public static void main(String[] args) {
        // The log string
        String logString = "tlorek.interart.com - - [25/Jul/1995:15:59:31 -0400] \"GET /images/ksclogo-medium.gif HTTP/1.0\" 304 0";
        String value = logString;
        // // Define a regular expression pattern for extracting the time
        // String regex = "\\[(\\d{2}/[A-Za-z]{3}/\\d{4}:\\d{2}:\\d{2}:\\d{2} [+-]\\d{4})\\]";

        // // Create a Pattern object
        // Pattern pattern = Pattern.compile(regex);

        // // Create a Matcher object
        // Matcher matcher = pattern.matcher(logString);

        // // Check if the pattern matches and extract the time
        // if (matcher.find()) {
        //     String time = matcher.group(1);
        //     System.out.println("Extracted Time: " + time);
        // } else {
        //     System.out.println("Time not found in the log string.");
        // }


        String words[] = value.toString().strip().split("\\s+");
        String date = words[3].strip().substring(1);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, inputFormatter);

        int hour = dateTime.getHour();
        System.out.println(hour);
        
    }
}
