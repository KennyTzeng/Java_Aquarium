import java.awt.Image;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Turtle implements Runnable {
	private int x, y;
	private int turtleSize;
	private int turtleDirection;
	private int turtleSpeed;
	private static Random rand = new Random();
	private ImageIcon icon;
	private Image leftImg;
	private Image rightImg;
	private Timer timer = new Timer();

	public Turtle(int x, int y) {// 隨機產生烏龜的大小,方向,速度並設定好烏龜的位置,大小,方向,速度及左右向圖案
		this.x = x;
		this.y = y;
		turtleSize = rand.nextInt(60) + 40;
		turtleDirection = rand.nextInt(2) * 180;
		turtleSpeed = rand.nextInt(3) + 2;
		icon = new ImageIcon(getClass().getResource("w.png"));
		rightImg = icon.getImage().getScaledInstance(turtleSize, turtleSize,
				Image.SCALE_DEFAULT);
		icon = new ImageIcon(getClass().getResource("w2.png"));
		leftImg = icon.getImage().getScaledInstance(turtleSize, turtleSize,
				Image.SCALE_DEFAULT);
	}

	public void run() {

		timer.schedule(new timerTask(), 5000, 5000);// 固定執行隨機重設方向及速度
		while (true) {

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				JOptionPane.showMessageDialog(null, "Thread sleeping error.");
			}
			move();// 呼叫移動方法
		}
	}

	public void move() {
		if (y + turtleSize <= mainFrame.backGroundPanel.getHeight()) {// 烏龜在落地之前都是向下墜
			y = y + turtleSpeed;
		} else {// 落地後根據方向向左或右前進,算出下一個位置
			x = (int) (x + turtleSpeed
					* Math.cos(Math.toRadians(turtleDirection)));
		}
		isCollision();// 呼叫判斷碰撞處理方法
	}

	public void isCollision() {// 判斷烏龜撞到左或右面,使其反向回去
		if (x <= 0) {
			turtleDirection = 0;
		}

		if (x + turtleSize >= mainFrame.backGroundPanel.getWidth()) {
			turtleDirection = 180;
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSize() {
		return turtleSize;
	}

	public Image getImage() {
		if (turtleDirection == 0) {
			return rightImg;
		} else {
			return leftImg;
		}
	}

	public boolean isSelected(int appointX, int appointY) {// 判斷滑鼠點擊位置是否在烏龜身上
		if ((appointX > x && appointX < x + turtleSize)
				&& (appointY > y && appointY < y + turtleSize)) {
			return true;
		} else {
			return false;
		}
	}

	public class timerTask extends TimerTask {// 重設烏龜方向及速度
		public void run() {
			turtleDirection = rand.nextInt(2) * 180;
			turtleSpeed = rand.nextInt(3) + 2;
		}
	}
}
