import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class OrderTest <T> {
    private int MAX_LENGTH_BUFFER = 10240;

    private int iter_count = 0;
    private boolean ORDER_D = false;
    private String out = "";
    private List<String> in = new ArrayList<>();
    private boolean isInt = false;

    public OrderTest(boolean isInt) {
        this.isInt = isInt;
    }

    public OrderTest() {}

    public void setOrderByDesc(boolean desc){
        this.ORDER_D = desc;
    }

    public void setIn(List<String> in) {
        this.in = in;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public void MergeSort() {
        if (this.out.isEmpty()) {System.out.println("Output file name error"); return;}
        if (this.in.size() == 0) {System.out.println("Input file name error"); return;}
        String filename1, filename2;
        Iterator<String> iterator = in.iterator();
        filename1 = iterator.next();
        do {
            File file1 = null;
            File file2 = null;
            if (iterator.hasNext()) filename2 = iterator.next();
            else filename2 = null;
            if (filename1 != null) {
                file1 = new File(filename1);
                if (!file1.exists()){
                    System.out.println("File not found: " + filename1);
                    return;
                }
            }

            if (filename2 != null) {
                file2 = new File(filename2);
                if (!file2.exists()){
                    System.out.println("File not found: " + filename2);
                    return;
                }
            } else {
                File file_out = new File(out);
                if (!file1.renameTo(file_out)) {
                    System.out.println("Error creating result file");
                } else System.out.println("The merge sort result was successfully saved in a file:" + out);
                break;
            }
            try {
                filename1 = Sort(file1, file2);
                if (iter_count > 1) {
                    if (file1 != null )file1.delete();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return;
            }
        } while (true);
    }

    private String Sort(File file1, File file2) throws IOException {

        StringBuilder tmpFileName = new StringBuilder();
        tmpFileName.append(out)
                .append(".tmp")
                .append(iter_count++);

        List<String> buffer1 = new ArrayList<>();
        List<String> buffer2 = new ArrayList<>();

        List <String> resultbuffer = new ArrayList<>();

        Scanner fs1 = new Scanner(new FileReader(file1));
        Scanner fs2 = new Scanner(new FileReader(file2));

        fillBuffer(fs1, buffer1);
        fillBuffer(fs2, buffer2);

        int i1 = 0;
        int i2 = 0;
        int compare;
        while (i1 < buffer1.size() || i2 < buffer2.size()){

            if (i1 == buffer1.size()) {
                fillBuffer(fs1, buffer1);
                i1 = 0;
            }else if (i2 == buffer2.size()) {
                fillBuffer(fs2, buffer2);
                i2 = 0;
            }
            if (buffer1.size() != 0 && buffer2.size() != 0) {
                //Если указан тип Int, то конвертируем в Integer и сравниваем
                if (isInt){
                    compare = Integer.valueOf(buffer1.get(i1)).compareTo(Integer.valueOf(buffer2.get(i2)));
                } else compare = buffer1.get(i1).compareTo(buffer2.get(i2));

                //Делаем сортировку по убыванию
                compare = ((ORDER_D) ? -compare: compare);

                if (compare <= 0) {
                    resultbuffer.add(buffer1.get(i1++));
                } else {
                    resultbuffer.add(buffer2.get(i2++));
                }

            } else if (buffer1.size() != 0) {
                resultbuffer.add(buffer1.get(i1++));
            } else if (buffer2.size() != 0) {
                resultbuffer.add(buffer2.get(i2++));
            }

            //при заполнении результирующего буффера и при достижении конца обоих файлов
            //записать во временный  файл результата
            if ((buffer1.size() == i1 && buffer2.size() == i2) || resultbuffer.size() == MAX_LENGTH_BUFFER) {
                    File file = new File(tmpFileName.toString());
                    if (!file.exists()) file.createNewFile();
                    FileWriter fw = new FileWriter(file, true);
                for (String s : resultbuffer) {
                    fw.write(s + "\n");
                }
                    resultbuffer.clear();
                    fw.close();
                }
        }
        fs1.close();
        fs2.close();
        return tmpFileName.toString();
    }

    private void fillBuffer(Scanner scanner, List<String> buffer){
        buffer.clear();
        while (scanner.hasNext() && buffer.size() < MAX_LENGTH_BUFFER) {
                buffer.add(scanner.nextLine());
        }
    }

}
