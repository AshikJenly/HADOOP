
public class SplittingExample{
    public static void main(String[] args) {
        String s1 = "199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] \"GET /history/apollo/ HTTP/1.0\" 200 6245";
        String values[] = s1.split(" ");
        System.out.println(values[values.length - 4]);
    }
}
