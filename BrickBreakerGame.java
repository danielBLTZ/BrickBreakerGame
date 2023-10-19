import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BrickBreakerGame extends JFrame implements ActionListener{
    private int ballX=250; //original ball coordinates
    private int ballY=350;
    private int ballXSpeed = 2; //ball speed on X axis
    private int ballYSpeed= -2; //ball speed on Y axis
    private int paddleX=200; //original paddle coordinates
    private final int paddleWidth=100;
    private final int paddleHeight=10;
    private boolean leftPressed=false;
    private boolean rightPressed=false;
    private Timer timer;
    private int score=0;
    private ArrayList<Bricks>bricks;
    public BrickBreakerGame(){
        setTitle("BRICK BREAKER");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        //init a timer
        timer=new Timer(5, this);
        timer.start();
        addKeyListener(new PaddleControl());
        bricks=new ArrayList<Bricks>();
        for(int i=0;i<8; i++){
            for(int j=0; j<5; j++){
                bricks.add(new Bricks(i*100, j*30));
            }
        }
        setVisible(true);
    }
    public void paint (Graphics g){
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 600); //background

        g.setColor(Color.BLUE);
        g.fillRect(paddleX, 550, paddleWidth, paddleHeight); //paddle(board)

        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, 20, 20);
        // ADD HERE BRICKS
        for(Bricks brick:bricks){
            if(!brick.isDestroyed()){
                g.setColor(Color.GREEN);
                g.fillRect(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
            }
        }
        //SCORE
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: "+score, 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ballX += ballXSpeed; //ball position actualization
        ballY += ballYSpeed;
        //detect collision with margins
        if(ballX <=0 || ballX>=780){
            ballXSpeed=-ballXSpeed;
        }
        if(ballY<=0){
            ballYSpeed=-ballYSpeed;
        }
        //detect collision with paddle
        if(ballY>=540 &&ballX>=paddleX&&ballX<=paddleX+paddleWidth){
            ballYSpeed=-ballYSpeed;
        }

        // ADD HERE COLLISION DETECTION WITH BRICKS AND GAME LOGIC
        for(Bricks brick:bricks){
            if(!brick.isDestroyed() && brick.intersects(ballX, ballY, 20, 20)){
                brick.setDestroyed(true);
                ballYSpeed=-ballYSpeed;
                score += 10;
            }
        }
        if(bricks.stream().allMatch(Bricks::isDestroyed)){
            JOptionPane.showMessageDialog(this, "FELICITARI! ATI CASTIGAT!");
            resetGame();
        }
        repaint();
    }
    private class PaddleControl extends KeyAdapter{

        public void keyPressed(KeyEvent e){
            int key=e.getKeyCode();
            if(key==KeyEvent.VK_LEFT){
                leftPressed=true;
                rightPressed=false;
            }
            if(key==KeyEvent.VK_RIGHT){
                rightPressed=true;
                leftPressed=false;
            }
        }
        public void keyReleased(KeyEvent e){
            leftPressed=false;
            rightPressed=false;
        }
    }
    public static void main(String [] args){
        new BrickBreakerGame();
    }
    private void resetGame(){
        ballX=250;
        ballY=350;
        ballXSpeed=2;
        ballYSpeed=-2;
        paddleX=200;
        score=0;

        for(Bricks brick:bricks){
            brick.setDestroyed(false);
        }
        repaint();
    }
    class Bricks{
        private int x;
        private int y;
        private boolean destroyed = false;
        public Bricks(int x, int y){
            this.x=x;
            this.y=y;
        }
        public boolean intersects(int ballX, int ballY, int ballWidth, int ballHeight){
            return x<ballX+ballWidth && x+100>ballX && y< ballY +ballHeight && y+30>ballY;
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }
        public int getWidth(){
            return 100;
        }
        public int getHeight(){
            return 30;
        }
        public boolean isDestroyed(){
            return destroyed;
        }
        public void setDestroyed(boolean destroyed){
            this.destroyed=destroyed;
        }
    }
}