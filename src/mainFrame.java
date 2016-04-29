import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class mainFrame extends JFrame {
	private JPanel controlPanel;
	static JPanel backGroundPanel;
	private JButton addFishButton;
	private JButton addTurtleButton;
	private JButton removeButton;
	private JButton removeAllButton;
	private JLabel conditionLabel;
	private int taskChoose = 0;
	ExecutorService executor = Executors.newCachedThreadPool();
	LinkedList<Fish> lkListF = new LinkedList<Fish>();
	LinkedList<Turtle> lkListT = new LinkedList<Turtle>();
	private int fishCount = 0;
	private int turtleCount = 0;

	public mainFrame() {
		super("水族箱");
		GridBagLayout gridBag = new GridBagLayout(); // 新增物件及配置版面
		GridBagConstraints c = new GridBagConstraints();
		controlPanel = new JPanel(gridBag);
		addFishButton = new JButton("新增魚");
		addTurtleButton = new JButton("新增烏龜");
		removeButton = new JButton("移除選取");
		removeAllButton = new JButton("移除全部");
		conditionLabel = new JLabel("歡迎來到水族箱");
		conditionLabel.setBackground(Color.lightGray);
		conditionLabel.setForeground(Color.black);
		conditionLabel.setOpaque(true);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 2, 2, 2);
		c.weightx = 1.0;
		controlPanel.add(addFishButton, c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		controlPanel.add(removeButton, c);
		c.gridwidth = GridBagConstraints.RELATIVE;
		controlPanel.add(addTurtleButton, c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		controlPanel.add(removeAllButton, c);
		c.ipady = 15;
		controlPanel.add(conditionLabel, c);

		ButtonHandler buttonHandler = new ButtonHandler(); // 註冊按鈕監聽者
		addFishButton.addActionListener(buttonHandler);
		addTurtleButton.addActionListener(buttonHandler);
		removeButton.addActionListener(buttonHandler);
		removeAllButton.addActionListener(buttonHandler);
		add(controlPanel, BorderLayout.NORTH);

		backGroundPanel = new GaPanel(); // 新增與設定好背景,使其持續更新畫面
		backGroundPanel.setBackground(Color.cyan);
		MouseHandler mouseHandler = new MouseHandler();
		backGroundPanel.addMouseListener(mouseHandler);
		add(backGroundPanel, BorderLayout.CENTER);
		executor.execute((Runnable) backGroundPanel);

	}

	private class MouseHandler extends MouseAdapter {
		public void mouseClicked(MouseEvent event) {
			if (taskChoose == 1) { // 當功能為新增魚
				Fish fish = new Fish(event.getX(), event.getY());
				if ((event.getX() > 0) // 隨機產生的魚大小不超過版面才加入畫面中
						&& (event.getY() > 0)
						&& (event.getX() + fish.getSize() < backGroundPanel
								.getWidth())
						&& (event.getY() + fish.getSize() < backGroundPanel
								.getHeight())) {
					fishCount++;
					lkListF.add(fish);
					executor.execute(fish);
					conditionLabel.setText(String.format(
							"目前功能:新增魚          魚數量:%-10d烏龜數量%d", fishCount,
							turtleCount));
				}
			} else if (taskChoose == 2) { // 當功能為新增烏龜
				Turtle turtle = new Turtle(event.getX(), event.getY());
				if ((event.getX() > 0) // 隨機產生的烏龜大小不超過版面才加入畫面中
						&& (event.getY() > 0)
						&& (event.getX() + turtle.getSize() < backGroundPanel
								.getWidth())
						&& (event.getY() + turtle.getSize() < backGroundPanel
								.getHeight())) {
					turtleCount++;
					lkListT.add(turtle);
					executor.execute(turtle);
					conditionLabel.setText(String.format(
							"目前功能:新增烏龜      魚數量:%-10d烏龜數量%d", fishCount,
							turtleCount));
				}
			} else if (taskChoose == 3) { // 當功能為移除一隻
				boolean hasRemove = false; // 新增變數,當烏龜跟魚疊在一起時已移除烏龜就不會移除到魚
				for (int i = turtleCount - 1; i >= 0; i--) { // index較後面的會顯示在上層,故由後面找回來
					if (lkListT.get(i).isSelected(event.getX(), event.getY())) {
						lkListT.remove(i);
						turtleCount--;
						hasRemove = true;
						conditionLabel.setText(String.format(
								"目前功能:移除              魚數量:%-10d烏龜數量%d",
								fishCount, turtleCount));
						break; // 已移除一隻就離開
					}
				}
				if (!hasRemove) { // 判斷是否有移除烏龜,詳見105行
					for (int i = fishCount - 1; i >= 0; i--) {
						if (lkListF.get(i).isSelected(event.getX(),
								event.getY())) {
							lkListF.remove(i);
							fishCount--;
							conditionLabel.setText(String.format(
									"目前功能:移除              魚數量:%-10d烏龜數量%d",
									fishCount, turtleCount));
							break; // 已移除一隻就離開
						}
					}
				}
			}
		}
	}

	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource().equals(addFishButton)) {
				taskChoose = 1;
				conditionLabel.setText(String.format(
						"目前功能:新增魚          魚數量:%-10d烏龜數量%d", fishCount,
						turtleCount));
			} else if (event.getSource().equals(addTurtleButton)) {
				taskChoose = 2;
				conditionLabel.setText(String.format(
						"目前功能:新增烏龜      魚數量:%-10d烏龜數量%d", fishCount,
						turtleCount));
			} else if (event.getSource().equals(removeButton)) {
				taskChoose = 3;
				conditionLabel.setText(String.format(
						"目前功能:移除              魚數量:%-10d烏龜數量%d", fishCount,
						turtleCount));
			} else if (event.getSource().equals(removeAllButton)) {
				taskChoose = 0;
				fishCount = 0;
				turtleCount = 0;
				lkListF = new LinkedList<Fish>();
				lkListT = new LinkedList<Turtle>();
				conditionLabel.setText("歡迎來到水族箱");
			}
		}
	}

	private class GaPanel extends JPanel implements Runnable {
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			for (int i = 0; i < fishCount; i++) {// 畫魚
				g.drawImage(lkListF.get(i).getImage(), lkListF.get(i).getX(),
						lkListF.get(i).getY(), this);
			}
			for (int i = 0; i < turtleCount; i++) {// 畫烏龜
				g.drawImage(lkListT.get(i).getImage(), lkListT.get(i).getX(),
						lkListT.get(i).getY(), this);
			}
		}
	}
}
