import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class LexicalAnalyser {
    private FileReader fileReader;
    private int startState;
    private int currentState;

    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("Please input the path of the file to be analysied:");
                File file = new File(new Scanner(System.in).nextLine());
                FileReader fileReader = new FileReader(file);
//                LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(fileReader, );
//                lexicalAnalyser.getToken();
            } catch (FileNotFoundException e) {
                System.out.println("File input invalid.");
            } catch (IOException e) {
                System.out.println("An I/O error happened.");
            }
        }
    }

    public LexicalAnalyser(FileReader fileReader, int startState) {
        this.fileReader = fileReader;
        this.startState = startState;
        this.currentState = startState;
    }

    public void getToken() throws IOException {
        int c;
        while ((c = fileReader.read()) != -1) {
            switch (currentState) {
                case 1375995437:
                    if (c == 'a') {
                        currentState = 1338841523;
                    } else if (c == 'b') {
                        currentState = 929776179;
                    } else if (c == 'c') {
                        currentState = 802581203;
                    } else if (c == 'f') {
                        currentState = 1561408618;
                    } else if (c == 'v') {
                        currentState = 2050404090;
                    } else if (c == 'g') {
                        currentState = 388043093;
                    } else if (c == 'h') {
                        currentState = 188576144;
                    } else {
                        System.out.print("<random1> "; currentState = startState;
                    } break;
                case 1338841523:
                    if (c == 'a') {
                        currentState = 1608230649;
                    } else if (c == 'b') {
                        currentState = 929776179;
                    } else if (c == 'c') {
                        currentState = 802581203;
                    } else if (c == 'v') {
                        currentState = 2050404090;
                    } else if (c == 'g') {
                        currentState = 388043093;
                    } else if (c == 'h') {
                        currentState = 188576144;
                    } else {
                        System.out.print("<random0> "; currentState = startState;
                    } break;
                case 1608230649:
                    if (c == 'a') {
                        currentState = 1608230649;
                    } else if (c == 'b') {
                        currentState = 929776179;
                    } else if (c == 'c') {
                        currentState = 802581203;
                    } else if (c == 'v') {
                        currentState = 2050404090;
                    } else if (c == 'g') {
                        currentState = 388043093;
                    } else if (c == 'h') {
                        currentState = 188576144;
                    } else {
                        System.out.print("<random1> "; currentState = startState;
                    } break;
                case 929776179:
                    if (c == 'a') {
                        currentState = 1608230649;
                    } else if (c == 'b') {
                        currentState = 929776179;
                    } else if (c == 'c') {
                        currentState = 802581203;
                    } else if (c == 'v') {
                        currentState = 2050404090;
                    } else if (c == 'g') {
                        currentState = 388043093;
                    } else if (c == 'h') {
                        currentState = 188576144;
                    } else {
                        System.out.print("<random1> "; currentState = startState;
                    } break;
                case 802581203:
                    if (c == 'a') {
                        currentState = 1608230649;
                    } else if (c == 'b') {
                        currentState = 929776179;
                    } else if (c == 'c') {
                        currentState = 802581203;
                    } else if (c == 'v') {
                        currentState = 2050404090;
                    } else if (c == 'g') {
                        currentState = 388043093;
                    } else if (c == 'h') {
                        currentState = 188576144;
                    } else {
                        System.out.print("<random1> "; currentState = startState;
                    } break;
                case 2050404090:
                    if (c == 'a') {
                        currentState = 1608230649;
                    } else if (c == 'b') {
                        currentState = 929776179;
                    } else if (c == 'c') {
                        currentState = 802581203;
                    } else if (c == 'v') {
                        currentState = 2050404090;
                    } else if (c == 'g') {
                        currentState = 388043093;
                    } else if (c == 'h') {
                        currentState = 188576144;
                    } else {
                        System.out.print("<random1> "; currentState = startState;
                    } break;
                case 388043093:
                    if (c == 'a') {
                        currentState = 1608230649;
                    } else if (c == 'b') {
                        currentState = 929776179;
                    } else if (c == 'c') {
                        currentState = 802581203;
                    } else if (c == 'v') {
                        currentState = 2050404090;
                    } else if (c == 'g') {
                        currentState = 388043093;
                    } else if (c == 'h') {
                        currentState = 188576144;
                    } else {
                        System.out.print("<random1> "; currentState = startState;
                    } break;
                case 188576144:
                    if (c == 'a') {
                        currentState = 1608230649;
                    } else if (c == 'b') {
                        currentState = 929776179;
                    } else if (c == 'c') {
                        currentState = 802581203;
                    } else if (c == 'v') {
                        currentState = 2050404090;
                    } else if (c == 'g') {
                        currentState = 388043093;
                    } else if (c == 'h') {
                        currentState = 188576144;
                    } else {
                        System.out.print("<random1> "; currentState = startState;
                    } break;
                case 1561408618: {
                    System.out.print("<random1> "; currentState = startState;
                } break;
            }
        }
    }

    private void fail() {

    }
}
