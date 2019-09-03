import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        if (args.length < 3 ) {
            System.out.println("Error of count arguments");
            return;
        }
        boolean desc = false;
        OrderTest orderTest = null;
        String out_file_name = null;
        List<String> in_list = new ArrayList<>();

        for (int f = 0; f < args.length; f++){
            switch (args[f]){
                case "-i":
                    orderTest = new OrderTest(true);
                    break;
                case "-s":
                    orderTest = new OrderTest();
                    break;
                case "-d":
                    desc = true;
                    break;
                case "-a":
                    break;
                default:if (f < 1) {
                    System.out.println("Input Error");
                    return;
                }
                else if (out_file_name == null) out_file_name = args[f];
                else in_list.add(args[f]);
            }
        }
        if (orderTest == null) {
            System.out.println("Input Error");
            return;
        }
        orderTest.setOrderByDesc(desc);
        orderTest.setOut(out_file_name);
        orderTest.setIn(in_list);
        orderTest.MergeSort();
    }
}
