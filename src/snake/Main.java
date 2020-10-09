package snake;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import static snake.Algorithms.*;

public class Main {

    public static JFrame f;
    private static Random random;
    public static int height, width;
    public static Node[][] board;
    public static Node snakeHead;
    public static ArrayList<Node> snakeTail;
    private static int snakeLength;
    public static Node food;
    public static ArrayList<Node> path;
    private static boolean horizontal = true;
    private static boolean vertical = true;
    public static HashSet<Node> visited;
    private static int temp = 1;

    //you can actually tweak this variables :

    // frame's width and height
    private static final int frameWidthAndHeight = 800;

    //size of node in grid
    public static int rectSize = 20;

    // 0 = player movement
    // 1 = BFS or A* pathfinding
    // 2 = hamiltonian cycle
    private static final int mode = 2;
    // 1 = BFS , 2 = A*
    private static final int algo = 1;

    //when using PathFinding set this true to see path snake following.
    public static boolean showPath = false;

    //set delay to control speed of algorithm
    private static final long delay = 1;


    private static double totalNodes;

    public static void main(String[] args) {
        f = new JFrame();
        random = new Random();
        f.setSize(frameWidthAndHeight, frameWidthAndHeight);
        totalNodes = Math.pow(frameWidthAndHeight / rectSize, 2);
        board = new Node[f.getHeight() / rectSize][f.getWidth() / rectSize];
        height = board.length;
        width = board[0].length;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = new Node(i, j);
            }
        }

        f.setSize(f.getWidth(), f.getHeight() + 37);


        snakeHead = new Node(0, 0);
        snakeLength = 1;
        snakeTail = new ArrayList<>();
        visited = new HashSet<>();
        path = new ArrayList<>();
        food = new Node(random.nextInt(height), random.nextInt(width));

        f.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'w': {
                        if (vertical) {
                            vertical = false;
                            horizontal = true;
                            snakeHead.setYs(-1);
                            snakeHead.setXs(0);
                        }
                        break;
                    }
                    case 's': {
                        if (vertical) {
                            vertical = false;
                            horizontal = true;
                            snakeHead.setYs(1);
                            snakeHead.setXs(0);
                        }
                        break;
                    }
                    case 'a': {
                        if (horizontal) {
                            horizontal = false;
                            vertical = true;
                            snakeHead.setYs(0);
                            snakeHead.setXs(-1);
                        }
                        break;
                    }
                    case 'd': {
                        if (horizontal) {
                            horizontal = false;
                            vertical = true;
                            snakeHead.setYs(0);
                            snakeHead.setXs(1);
                        }
                        break;
                    }
                    case 'q': {
                        new Thread(Main::startGame).start();
                        break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        Canvas canvas = new Canvas();
        f.add(canvas);
       
        if (mode == 2) {
            new Thread(Main::generateCycle).start();
        }
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("Snake");
        f.setVisible(true);
    }

    private static void generateCycle() {
        Node start = board[0][0];
        dfs(start, visited);
    }


    private static void dfs(Node node, HashSet<Node> visited) {

        if (visited.contains(node))
            return;

        visited.add(node);
        node.setIndex(temp);
        temp++;
        // Delay();
        f.repaint();
        int x = node.getX();
        int y = node.getY();
        if (x < width - 1) {
            if (board[x + 1][y].getParent() == null) {
                board[x + 1][y].setParent(node);
            }
            dfs(board[x + 1][y], visited);
        }
        if (y < height - 1) {
            if (board[x][y + 1].getParent() == null) {
                board[x][y + 1].setParent(node);
            }
            dfs(board[x][y + 1], visited);
        }


        if (y >= 1) {
            if (board[x][y - 1].getParent() == null) {
                board[x][y - 1].setParent(node);
            }
            dfs(board[x][y - 1], visited);
        }
        if (x >= 1) {
            if (board[x - 1][y].getParent() == null) {
                board[x - 1][y].setParent(node);
            }
            dfs(board[x - 1][y], visited);
        }


    }

    private static void startGame() {
        while (true) {
            if (snakeLength == temp - 1) {
                System.out.println("Game Over");
                System.out.println("Score = " + snakeLength);
                break;
            }
            try {
                switch (mode) {
                    case 0 -> movement();

                    case 1 -> pathFindingAi();

                    case 2 -> hamiltonianPath();

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Game Over");
                System.out.println("Score = " + snakeLength);
                break;
            }
        }
    }

    private static void hamiltonianPath() {

        Node node = getNextNode();
        snakeHead.setX(node.getX());
        snakeHead.setY(node.getY());

        if (snakeTail.size() >= snakeLength) snakeTail.remove(0);

        snakeTail.add(board[snakeHead.getX()][snakeHead.getY()]);

        Delay();

        f.repaint();

        if (equal(snakeHead, food)) {
            snakeLength++;
            generateFood();
        }
    }

    private static Node getNextNode() {
        int i = board[snakeHead.getX()][snakeHead.getY()].getIndex();
        int j = board[food.getX()][food.getY()].getIndex();
        var children = getChildren(snakeHead);

        if (i == totalNodes) {
            return board[0][0];
        }
        Node next = null;

        if (j < i) {
            // children.sort(Comparator.comparingInt(Node::getIndex));
            for (var child : children) {
                if (child.getIndex() < snakeTail.get(0).getIndex() && next == null && child.getIndex() < j) {
                    next = child;
                } else if (next != null && child.getIndex() < snakeTail.get(0).getIndex() && next.getIndex() > child.getIndex() && child.getIndex() < j)
                    next = child;
            }
        }

        if (next != null)
            return next;


        for (var child : children) {
            if (child.getIndex() < j && child.getIndex() > i) {
                if(next == null)
                    next = child;

                else if (!snakeTail.isEmpty() && child.getIndex() < snakeTail.get(0).getIndex() && next.getIndex() > child.getIndex()) {
                    next = child;
                }
            }
        }


        if (next != null)
            return next;

        for(var a : board){
            for(var b : a){
                if(i + 1 == b.getIndex())
                    return b;
            }
        }

            return null;
        }

    private static void pathFindingAi() throws snakeHeadTrapException {
             path = new ArrayList<>();
             if(algo == 1)
                 path = BFS(snakeHead, food);
             else
                 path = Astar(snakeHead,food);

            if(path.isEmpty()) {

                ArrayList<Node> result = getChildren(snakeHead);
                if(result.size() == 0)
                    throw new snakeHeadTrapException();
                else {
                    snakeHead.setX(result.get(0).getX());
                    snakeHead.setY(result.get(0).getY());
                }
                while (snakeTail.size() >= snakeLength) snakeTail.remove(0);

                snakeTail.add(board[snakeHead.getX()][snakeHead.getY()]);

                Delay();

                f.repaint();

            }
            else {
                while (!path.isEmpty()){

                    Node node = path.get(0);

                    snakeHead.setX(node.getX());
                    snakeHead.setY(node.getY());

                    if (snakeTail.size() >= snakeLength) snakeTail.remove(0);

                    snakeTail.add(board[snakeHead.getX()][snakeHead.getY()]);
                    Delay();
                    path.remove(0);
                    f.repaint();
                }
                snakeLength++;
                generateFood();
            }
    }

    private static void movement() throws snakeHeadTrapException {

        snakeHead.setX(snakeHead.getX() + snakeHead.getXs());
        snakeHead.setY(snakeHead.getY() + snakeHead.getYs());

        if(snakeHead.getX() >= width)
            snakeHead.setX(0);

        if(snakeHead.getY() >= height)
            snakeHead.setY(0);

        if(snakeHead.getX() <= -1)
            snakeHead.setX(width - 1);

        if(snakeHead.getY() <= - 1)
            snakeHead.setY(height - 1);

        if(snakeHead.getX() == food.getX() && snakeHead.getY() == food.getY()) {
            snakeLength++;
            generateFood();
        }

        while (snakeTail.size() >= snakeLength) snakeTail.remove(0);

        Delay();

        if(snakeTail.contains(board[snakeHead.getX()][snakeHead.getY()])) throw new snakeHeadTrapException();

        snakeTail.add(board[snakeHead.getX()][snakeHead.getY()]);

        f.repaint();
    }

    private static void generateFood() {

        int x = random.nextInt(width);
        int y = random.nextInt(height);

        while (snakeTail.contains(board[x][y]) || x == snakeHead.getX() && y == snakeHead.getY()){
             x = random.nextInt(width);
             y = random.nextInt(height);
        }

        food.setX(x);
        food.setY(y);
    }

    private static void Delay(){
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
