import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * Created by renhongqiang on 2019-07-02 18:32
 */
public class TestReadFile {
    public static void main(String[] args) throws IOException {
        String smallFilePath = "/Users/renhongqiang/Downloads/work-doc/2000W/demo.txt";
        File file = new File(smallFilePath);


        RandomAccessFile rAccessFile = new RandomAccessFile(file, "r");

        MappedByteBuffer mapBuffer = rAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());

        int bufferSize = 10;
        byte[] buffer = new byte[10];

        for (int offset = 0; offset < file.length(); offset += bufferSize) {
            int readLength;
            if (offset + bufferSize <= file.length()) {
                readLength = bufferSize;
            } else {
                readLength = (int) (file.length() - offset);
            }
            mapBuffer.get(buffer, 0, readLength);
            System.out.println(new String(buffer, StandardCharsets.UTF_8));
        }


    }
}
