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

	public Turtle(int x, int y) {// �H�����ͯQ�t���j�p,��V,�t�רó]�w�n�Q�t����m,�j�p,��V,�t�פΥ��k�V�Ϯ�
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

		timer.schedule(new timerTask(), 5000, 5000);// �T�w�����H�����]��V�γt��
		while (true) {

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				JOptionPane.showMessageDialog(null, "Thread sleeping error.");
			}
			move();// �I�s���ʤ�k
		}
	}

	public void move() {
		if (y + turtleSize <= mainFrame.backGroundPanel.getHeight()) {// �Q�t�b���a���e���O�V�U�Y
			y = y + turtleSpeed;
		} else {// ���a��ھڤ�V�V���Υk�e�i,��X�U�@�Ӧ�m
			x = (int) (x + turtleSpeed
					* Math.cos(Math.toRadians(turtleDirection)));
		}
		isCollision();// �I�s�P�_�I���B�z��k
	}

	public void isCollision() {// �P�_�Q�t���쥪�Υk��,�Ϩ�ϦV�^�h
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

	public boolean isSelected(int appointX, int appointY) {// �P�_�ƹ��I����m�O�_�b�Q�t���W
		if ((appointX > x && appointX < x + turtleSize)
				&& (appointY > y && appointY < y + turtleSize)) {
			return true;
		} else {
			return false;
		}
	}

	public class timerTask extends TimerTask {// ���]�Q�t��V�γt��
		public void run() {
			turtleDirection = rand.nextInt(2) * 180;
			turtleSpeed = rand.nextInt(3) + 2;
		}
	}
}
