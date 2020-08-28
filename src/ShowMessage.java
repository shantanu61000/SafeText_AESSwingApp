import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class ShowMessage {

	JFrame frmShowMessage;
	private JPasswordField passwordField;
	private JTextArea textArea;
	private JButton btnBack;
	private JScrollPane scrollPane;
	private JLabel lblTitle;
	private JTextField textField;
	private JLabel error;
	private JLabel lblYourMessage;

	/**
	 * Launch the application.
	 */


	/**
	 * Create the application.
	 */
	public ShowMessage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmShowMessage = new JFrame();
		frmShowMessage.setTitle("Safe Text : Show Message");
		frmShowMessage.getContentPane().setBackground(Color.DARK_GRAY);
		frmShowMessage.setBounds(100, 100, 450, 422);
		frmShowMessage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmShowMessage.getContentPane().setLayout(null);
		
		JLabel lblPassword = new JLabel("Password: ");
		lblPassword.setForeground(new Color(224, 255, 255));
		lblPassword.setFont(new Font("Ubuntu", Font.BOLD, 16));
		lblPassword.setBounds(70, 117, 89, 35);
		frmShowMessage.getContentPane().add(lblPassword);
		
		JButton btnNewButton = new JButton("Show");
		btnNewButton.setFont(new Font("Ubuntu", Font.BOLD, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				error.setText("");
				textArea.setText("");
				//disable textarea so that user cannot edit it
				textArea.disable();
				String pwd = passwordField.getText();
				String title = textField.getText();
				aesUtils aes = new aesUtils();
				try {
					Database db = new Database();
					Connection con = db.getConn();
					//check if any messages exist with the entered title
					String query = "Select * from msg where msgTitle = ?";
					PreparedStatement pst =  con.prepareStatement(query);
					pst.setString(1, title);
					ResultSet rs = pst.executeQuery();
					if(rs.next()) {
						//Declaring a hashmap to  add value to because decrypt function expects a hashmap
						HashMap<String,String> map = new HashMap<String, String>();
						map.put("password",pwd);
						map.put("iv",rs.getString("iv"));
						map.put("message", rs.getString("message"));
						map.put("salt",rs.getString("salt"));
						textArea.enable();
						textArea.append(aes.decrypt(map));
						con.close();
					}
					else {
						error.setText("No message found with this title");
						con.close();
					}
				} 
				catch (Exception e) {
					//if the algorith failed to decrypt the text with the provided password it means the password entered was wrong
					//in this case decrypt function throw's an exception 
					error.setText("Wrong password. Please try again");
					//e.printStackTrace();
					
				}
			}
		});
		btnNewButton.setBounds(188, 174, 89, 25);
		frmShowMessage.getContentPane().add(btnNewButton);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Ubuntu", Font.BOLD, 14));
		passwordField.setBounds(187, 126, 178, 19);
		frmShowMessage.getContentPane().add(passwordField);
		
		JTextPane txtpnEnterYourMessage = new JTextPane();
		txtpnEnterYourMessage.setEditable(false);
		txtpnEnterYourMessage.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		txtpnEnterYourMessage.setForeground(Color.WHITE);
		txtpnEnterYourMessage.setBackground(Color.DARK_GRAY);
		txtpnEnterYourMessage.setText("Enter your message title and password and click show to see your message");
		txtpnEnterYourMessage.setBounds(92, 25, 273, 48);
		frmShowMessage.getContentPane().add(txtpnEnterYourMessage);
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				error.setText("");
				textArea.setText("");
				frmShowMessage.dispose();
				Home home = new Home();
				home.frmSafeText.setVisible(true);
			}
		});
		btnBack.setForeground(Color.WHITE);
		btnBack.setBackground(Color.DARK_GRAY);
		btnBack.setBounds(289, 175, 76, 25);
		frmShowMessage.getContentPane().add(btnBack);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(70, 280, 295, 73);
		frmShowMessage.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setEnabled(false);
		scrollPane.setViewportView(textArea);
		textArea.setFont(new Font("Ubuntu", Font.BOLD, 16));
		textArea.setLineWrap(true);
		
		lblTitle = new JLabel("Title:");
		lblTitle.setFont(new Font("Ubuntu", Font.BOLD, 16));
		lblTitle.setForeground(new Color(224, 255, 255));
		lblTitle.setBounds(70, 90, 70, 15);
		frmShowMessage.getContentPane().add(lblTitle);
		
		textField = new JTextField();
		textField.setFont(new Font("Ubuntu", Font.BOLD, 14));
		textField.setBounds(187, 85, 178, 19);
		frmShowMessage.getContentPane().add(textField);
		textField.setColumns(10);
		
		error = new JLabel("");
		error.setFont(new Font("Ubuntu", Font.PLAIN, 12));
		error.setHorizontalAlignment(SwingConstants.CENTER);
		error.setForeground(new Color(240, 248, 255));
		error.setBounds(12, 211, 416, 25);
		frmShowMessage.getContentPane().add(error);
		
		lblYourMessage = new JLabel("Your message:");
		lblYourMessage.setForeground(new Color(224, 255, 255));
		lblYourMessage.setBounds(70, 248, 132, 25);
		frmShowMessage.getContentPane().add(lblYourMessage);
		frmShowMessage.setLocationRelativeTo(null);
	}
}
