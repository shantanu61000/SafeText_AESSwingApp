import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.UIManager;
import java.awt.Color;

public class Home {

	JFrame frmSafeText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home window = new Home();
					window.frmSafeText.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Home() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSafeText = new JFrame();
		frmSafeText.setTitle("Safe Text : Home");
		frmSafeText.getContentPane().setBackground(Color.DARK_GRAY);
		frmSafeText.setBounds(100, 100, 450, 300);
		frmSafeText.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSafeText.getContentPane().setLayout(null);
		
		JLabel lblSafeText = new JLabel("Safe Text");
		lblSafeText.setForeground(new Color(224, 255, 255));
		lblSafeText.setFont(new Font("Ubuntu", Font.BOLD, 26));
		lblSafeText.setBounds(164, 32, 130, 37);
		frmSafeText.getContentPane().add(lblSafeText);
		
		JButton btnAddMessage = new JButton("Add Message");
		btnAddMessage.setBackground(UIManager.getColor("ColorChooser.background"));
		btnAddMessage.setFont(new Font("Ubuntu", Font.BOLD, 15));
		btnAddMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmSafeText.dispose();
				AddMessage addMessage = new AddMessage();
				addMessage.frmAddMessage.setVisible(true);
			}
		});
		btnAddMessage.setBounds(149, 107, 158, 40);
		frmSafeText.getContentPane().add(btnAddMessage);
		
		JButton btnShowMessage = new JButton("Show Message");
		btnShowMessage.setFont(new Font("Ubuntu", Font.BOLD, 15));
		btnShowMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmSafeText.dispose();
				ShowMessage showMessage = new ShowMessage();
				showMessage.frmShowMessage.setVisible(true);
			}
		});
		btnShowMessage.setBounds(149, 159, 158, 40);
		frmSafeText.getContentPane().add(btnShowMessage);
		frmSafeText.setLocationRelativeTo(null);
	}

}
