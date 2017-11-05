public class TestCase {
    public void test(){
        int i = 1;
        char c = 'a';
        double d = 1.0;

        if (i == 1){
            i = i + 1;
        }else {
            i = i - 1;
        }

        for (int j = 0; j < 4; j++) {
            i = i + 1;
        }
    }
}