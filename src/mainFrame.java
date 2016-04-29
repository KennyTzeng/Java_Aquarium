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
		super("���ڽc");
		GridBagLayout gridBag = new GridBagLayout(); // �s�W����ΰt�m����
		GridBagConstraints c = new GridBagConstraints();
		controlPanel = new JPanel(gridBag);
		addFishButton = new JButton("�s�W��");
		addTurtleButton = new JButton("�s�W�Q�t");
		removeButton = new JButton("�������");
		removeAllButton = new JButton("��������");
		conditionLabel = new JLabel("�w��Ө���ڽc");
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

		ButtonHandler buttonHandler = new ButtonHandler(); // ���U���s��ť��
		addFishButton.addActionListener(buttonHandler);
		addTurtleButton.addActionListener(buttonHandler);
		removeButton.addActionListener(buttonHandler);
		removeAllButton.addActionListener(buttonHandler);
		add(controlPanel, BorderLayout.NORTH);

		backGroundPanel = new GaPanel(); // �s�W�P�]�w�n�I��,�Ϩ�����s�e��
		backGroundPanel.setBackground(Color.cyan);
		MouseHandler mouseHandler = new MouseHandler();
		backGroundPanel.addMouseListener(mouseHandler);
		add(backGroundPanel, BorderLayout.CENTER);
		executor.execute((Runnable) backGroundPanel);

	}

	private class MouseHandler extends MouseAdapter {
		public void mouseClicked(MouseEvent event) {
			if (taskChoose == 1) { // ��\�ର�s�W��
				Fish fish = new Fish(event.getX(), event.getY());
				if ((event.getX() > 0) // �H�����ͪ����j�p���W�L�����~�[�J�e����
						&& (event.getY() > 0)
						&& (event.getX() + fish.getSize() < backGroundPanel
								.getWidth())
						&& (event.getY() + fish.getSize() < backGroundPanel
								.getHeight())) {
					fishCount++;
					lkListF.add(fish);
					executor.execute(fish);
					conditionLabel.setText(String.format(
							"�ثe�\��:�s�W��          ���ƶq:%-10d�Q�t�ƶq%d", fishCount,
							turtleCount));
				}
			} else if (taskChoose == 2) { // ��\�ର�s�W�Q�t
				Turtle turtle = new Turtle(event.getX(), event.getY());
				if ((event.getX() > 0) // �H�����ͪ��Q�t�j�p���W�L�����~�[�J�e����
						&& (event.getY() > 0)
						&& (event.getX() + turtle.getSize() < backGroundPanel
								.getWidth())
						&& (event.getY() + turtle.getSize() < backGroundPanel
								.getHeight())) {
					turtleCount++;
					lkListT.add(turtle);
					executor.execute(turtle);
					conditionLabel.setText(String.format(
							"�ثe�\��:�s�W�Q�t      ���ƶq:%-10d�Q�t�ƶq%d", fishCount,
							turtleCount));
				}
			} else if (taskChoose == 3) { // ��\�ର�����@��
				boolean hasRemove = false; // �s�W�ܼ�,��Q�t���|�b�@�_�ɤw�����Q�t�N���|�����쳽
				for (int i = turtleCount - 1; i >= 0; i--) { // index���᭱���|��ܦb�W�h,�G�ѫ᭱��^��
					if (lkListT.get(i).isSelected(event.getX(), event.getY())) {
						lkListT.remove(i);
						turtleCount--;
						hasRemove = true;
						conditionLabel.setText(String.format(
								"�ثe�\��:����              ���ƶq:%-10d�Q�t�ƶq%d",
								fishCount, turtleCount));
						break; // �w�����@���N���}
					}
				}
				if (!hasRemove) { // �P�_�O�_�������Q�t,�Ԩ�105��
					for (int i = fishCount - 1; i >= 0; i--) {
						if (lkListF.get(i).isSelected(event.getX(),
								event.getY())) {
							lkListF.remove(i);
							fishCount--;
							conditionLabel.setText(String.format(
									"�ثe�\��:����              ���ƶq:%-10d�Q�t�ƶq%d",
									fishCount, turtleCount));
							break; // �w�����@���N���}
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
						"�ثe�\��:�s�W��          ���ƶq:%-10d�Q�t�ƶq%d", fishCount,
						turtleCount));
			} else if (event.getSource().equals(addTurtleButton)) {
				taskChoose = 2;
				conditionLabel.setText(String.format(
						"�ثe�\��:�s�W�Q�t      ���ƶq:%-10d�Q�t�ƶq%d", fishCount,
						turtleCount));
			} else if (event.getSource().equals(removeButton)) {
				taskChoose = 3;
				conditionLabel.setText(String.format(
						"�ثe�\��:����              ���ƶq:%-10d�Q�t�ƶq%d", fishCount,
						turtleCount));
			} else if (event.getSource().equals(removeAllButton)) {
				taskChoose = 0;
				fishCount = 0;
				turtleCount = 0;
				lkListF = new LinkedList<Fish>();
				lkListT = new LinkedList<Turtle>();
				conditionLabel.setText("�w��Ө���ڽc");
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

			for (int i = 0; i < fishCount; i++) {// �e��
				g.drawImage(lkListF.get(i).getImage(), lkListF.get(i).getX(),
						lkListF.get(i).getY(), this);
			}
			for (int i = 0; i < turtleCount; i++) {// �e�Q�t
				g.drawImage(lkListT.get(i).getImage(), lkListT.get(i).getX(),
						lkListT.get(i).getY(), this);
			}
		}
	}
}
