package snake;

public class snakeHeadTrapException extends Exception {
    @Override
    public void printStackTrace() {
        System.out.println("NO Possible Way");
    }
}
