package dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DbParamsDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextField db_url;
	private JPasswordField db_password;
	private JTextField db_user;
	private DialogCallBack dialogCallBack;
	
	public DbParamsDialog(DialogCallBack dialogCallBack) {
		this.dialogCallBack = dialogCallBack;
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);
		setTitle("数据库链接配置"); // 设置title
		setSize(500, 300); // 设置窗口大小
		
		// 设置窗口位置
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int Swing1x = 500;
		int Swing1y = 300;
		setBounds((screenSize.width - Swing1x) / 2, (screenSize.height - Swing1y) / 2 - 100, Swing1x, Swing1y);
		
		
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});
		
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});
		
		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});
		
		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}
	
	private void onOK() {
		this.dialogCallBack.callBck(this.db_url.getText(),this.db_user.getText(),String.valueOf(this.db_password.getPassword()));
		dispose();
	}
	
	private void onCancel() {
		// add your code here if necessary
		dispose();
	}
	
	public static void main(String[] args) {
		DbParamsDialog dialog = new DbParamsDialog(null);
		dialog.pack();
		dialog.setVisible(true);
		System.exit(0);
	}
}
