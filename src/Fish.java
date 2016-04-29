import java.awt.Image;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Fish implements Runnable {
	private int x, y;
	private int fishKind;
	private int fishSize;
	private int fishDirection;
	private int fishSpeed;
	private static Random rand = new Random();
	private ImageIcon icon;
	private Image leftImg;
	private Image rightImg;
	private Timer timer = new Timer();

	public Fish(int x, int y) {// 隨機產生魚的種類,大小,方向,速度,並設定好座標位置,種類,大小,方向,速度及左右向照片
		this.x = x;
		this.y = y;
		fishSize = rand.nextInt(60) + 40;
		fishKind = rand.nextInt(3) * 2;
		fishDirection = rand.nextInt(360);
		fishSpeed = rand.nextInt(5) + 2;
		icon = new ImageIcon(getClass().getResource(fishKind + 1 + ".png"));
		rightImg = icon.getImage().getScaledInstance(fishSize, fishSize,
				Image.SCALE_DEFAULT);
		icon = new ImageIcon(getClass().getResource(fishKind + 2 + ".png"));
		leftImg = icon.getImage().getScaledInstance(fishSize, fishSize,
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

		x = (int) (x + fishSpeed * Math.cos(Math.toRadians(fishDirection)));// 根據三角函數算出x及y方向速度並算出下一個位置
		y = (int) (y - fishSpeed * Math.sin(Math.toRadians(fishDirection)));
		isCollision();// 呼叫判斷碰撞處理方法
	}

	public void isCollision() { // 判斷是否撞到四面,並根據碰撞來的方向算出新的反彈方向
		if (x <= 0) {
			if (fishDirection > 90 && fishDirection <= 180) {
				fishDirection = 180 - fishDirection;
			} else if (fishDirection > 180 && fishDirection < 270) {
				fishDirection = 540 - fishDirection;
			}
		}
		if (y <= 0) {
			if (fishDirection > 0 && fishDirection < 180) {
				fishDirection = 360 - fishDirection;
			}
		}
		if (x + fishSize >= mainFrame.backGroundPanel.getWidth()) {
			if (fishDirection >= 0 && fishDirection < 90) {
				fishDirection = 180 - fishDirection;
			} else if (fishDirection > 270 && fishDirection < 360) {
				fishDirection = 540 - fishDirection;
			}
		}
		if (y + fishSize >= mainFrame.backGroundPanel.getHeight()) {
			if (fishDirection > 180 && fishDirection < 360) {
				fishDirection = 360 - fishDirection;
			}
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSize() {
		return fishSize;
	}

	public Image getImage() {
		if (fishDirection > 90 && fishDirection <= 270) {
			return leftImg;
		} else {
			return rightImg;
		}
	}

	public boolean isSelected(int appointX, int appointY) { // 判斷滑鼠點擊位置是否在魚身上
		if ((appointX > x && appointX < x + fishSize)
				&& (appointY > y && appointY < y + fishSize)) {
			return true;
		} else {
			return false;
		}
	}

	public class timerTask extends TimerTask {// 重設魚方向及速度
		public void run() {
			fishDirection = rand.nextInt(360);
			fishSpeed = rand.nextInt(5) + 2;
		}
	}
}
