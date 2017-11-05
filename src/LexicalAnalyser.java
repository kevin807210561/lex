import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LexicalAnalyser {
    private List<String> input;
    private int startState;
    private int currentState;
    private boolean isBegin;
    private int i;
    private int j;

    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("Please input the path of the file to be analysied:");
                BufferedReader fileReader = new BufferedReader(new FileReader(new Scanner(System.in).nextLine()));
                LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(fileReader.lines().collect(Collectors.toList()), 1956710488);
                lexicalAnalyser.getToken();
            } catch (FileNotFoundException e) {
                System.out.println("File input invalid.");
            } catch (IOException e) {
                System.out.println("An I/O error happened.");
            }
        }
    }

    public LexicalAnalyser(List<String> input, int startState) {
        this.input = input;
        for (int i = 0; i < input.size() - 1; i++) {
            this.input.set(i, this.input.get(i).concat("" + '\n'));
        }
        this.input.set(this.input.size() - 1, this.input.get(this.input.size() - 1).concat("" + '\0'));
        this.startState = startState;
        this.currentState = startState;
        this.isBegin = true;
        i = 0;
        j = 0;
    }

    public void getToken() throws IOException {
        int c;
        for (i = 0; i < input.size(); i++) {
            for (j = 0; j < input.get(i).length(); j++) {
                c = input.get(i).charAt(j);
                switch (currentState) {
                    case 1956710488:
                        if (c == 'a') {
                            currentState = 1413246829;
                            isBegin = false;
                        } else if (c == 'c') {
                            currentState = 603856241;
                            isBegin = false;
                        } else if (c == 'f') {
                            currentState = 682376643;
                            isBegin = false;
                        } else if (c == 'v') {
                            currentState = 854507466;
                            isBegin = false;
                        } else {
                            if (isBegin) fail();
                            else accept("random1");
                        }
                        break;
                    case 1413246829:
                        if (c == 'b') {
                            currentState = 334203599;
                            isBegin = false;
                        } else {
                            fail();
                        }
                        break;
                    case 334203599:
                        if (c == 'c') {
                            currentState = 1372082959;
                            isBegin = false;
                        } else {
                            fail();
                        }
                        break;
                    case 1372082959:
                        if (c == 'd') {
                            currentState = 1316061703;
                            isBegin = false;
                        } else {
                            fail();
                        }
                        break;
                    case 1316061703:
                        accept("random0");
                        break;
                    case 603856241:
                        if (c == 'c') {
                            currentState = 603856241;
                            isBegin = false;
                        } else if (c == 'v') {
                            currentState = 854507466;
                            isBegin = false;
                        } else {
                            accept("random1");
                        }
                        break;
                    case 854507466:
                        if (c == 'c') {
                            currentState = 603856241;
                            isBegin = false;
                        } else if (c == 'v') {
                            currentState = 854507466;
                            isBegin = false;
                        } else {
                            accept("random1");
                        }
                        break;
                    case 682376643:
                        accept("random1");
                        break;
                }
            }
        }
    }

    private void fail() throws IOException {
        System.out.println();
        System.out.print("An error occurred. Please check the content near \"" + input.get(i).substring(j, Math.min(j + 20, input.get(i).length())) + "\" at line " + (i + 1) + ".");
        System.out.println();
        System.exit(-1);
    }

    private void accept(String tokenName) throws IOException {
        System.out.println("<" + tokenName + "> ");
        currentState = startState;
        isBegin = true;
        if (i != input.size() - 1 || j != input.get(i).length() - 1) j--;
    }
}