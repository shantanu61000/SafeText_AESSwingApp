import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class AddMessage implements KeyListener{

	JFrame frmAddMessage;
	private JLabel lblPassword;
	private JButton btnSaveMessage;
	private JTextArea textArea;
	private JButton btnBack;
	private JPasswordField passwordField;
	private JTextField textField;
	private JLabel label;
	private JLabel error;

	/**
	 * Launch the application.
	 */


	/**
	 * Create the application.
	 */
	public AddMessage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAddMessage = new JFrame();
		frmAddMessage.getContentPane().setFont(new Font("Ubuntu", Font.BOLD, 14));
		frmAddMessage.getContentPane().setForeground(new Color(224, 255, 255));
		frmAddMessage.setTitle("Safe Text: Add Message");
		frmAddMessage.getContentPane().setBackground(Color.DARK_GRAY);
		frmAddMessage.getContentPane().setLayout(null);
		
		JLabel lblMessage = new JLabel("Message: ");
		lblMessage.setFont(new Font("Ubuntu", Font.BOLD, 16));
		lblMessage.setForeground(new Color(224, 255, 255));
		lblMessage.setBounds(71, 95, 91, 29);
		frmAddMessage.getContentPane().add(lblMessage);
		
		lblPassword = new JLabel("Password: ");
		lblPassword.setFont(new Font("Ubuntu", Font.BOLD, 16));
		lblPassword.setForeground(new Color(224, 255, 255));
		lblPassword.setBounds(71, 179, 101, 29);
		frmAddMessage.getContentPane().add(lblPassword);
		
		btnSaveMessage = new JButton("Save");
		btnSaveMessage.setFont(new Font("Ubuntu", Font.BOLD, 16));
		btnSaveMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//clear the error message if any . This is so that the error still doesn't appear when the inputs are correct
				error.setText("");
				
				//get the values entered by the user
				String msg = textArea.getText();
				String pwd = passwordField.getText();	
				String title = textField.getText(); 
				
				//check if any input field is blank
				if(msg.equals("") || pwd.equals("") || title.equals("")) {
					error.setText("None of the fields can be blank");
				}
				
				//No input fields are blank
				else {
					//check if msg is less then 1000 char
					if(msg.length() <= 1000) {
						//check if password is within the range
						if(pwd.length()<=15 && pwd.length() >=3) {
							aesUtils aes = new aesUtils();
							try {
								Database db = new Database();
								Connection con = db.getConn();
								
								//prepeared statement query to check if the entered title is already there in database
								String query = "Select * from msg where msgTitle = ?";
								PreparedStatement pst = con.prepareStatement(query);
								pst.setString(1, title);
								ResultSet rs = pst.executeQuery();
								if(rs.next()) {
									error.setText("Title already exists. Please enter unique title.");;
									con.close();
								}
								else {
									//means password is unique
									var map = aes.encrypt(pwd,msg);
									
									//prepared statement query to input the values returned by encrypt method
									query = "Insert into msg(message,iv,salt,msgTitle) values (?,?,?,?)";
									PreparedStatement pst2 =  con.prepareStatement(query);				
									pst2.setString(1, map.get("message"));
									pst2.setString(2, map.get("iv"));
									pst2.setString(3, map.get("salt"));
									pst2.setString(4, title);
									int temp = pst2.executeUpdate();
									con.close();
									if (temp > 0)
										error.setText("Message saved successfuly.");
									else
										error.setText("Falied to save message");
									}						
									
							} 
							catch(Exception e){
								e.printStackTrace();
							}
						}
						else {
							error.setText("Password should be between 3-15 characters");
						}
					}
					else {
						error.setText("Message cannot be of more then 1000 charecters");
					}
				}
			}
		});
		btnSaveMessage.setBounds(190, 254, 84, 25);
		frmAddMessage.getContentPane().add(btnSaveMessage);
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				error.setText("");
				frmAddMessage.dispose();
				Home home = new Home();
				home.frmSafeText.setVisible(true);
			}
		});
		btnBack.setBackground(Color.DARK_GRAY);
		btnBack.setForeground(Color.WHITE);
		btnBack.setBounds(295, 254, 67, 25);
		frmAddMessage.getContentPane().add(btnBack);		
		passwordField = new JPasswordField();
		passwordField.setBounds(190, 185, 172, 19);
		frmAddMessage.getContentPane().add(passwordField);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(190, 100, 172, 57);
		frmAddMessage.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Ubuntu", Font.BOLD, 14));
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true);
		textArea.addKeyListener((KeyListener) this);
		JLabel lblTitle = new JLabel("Title:");
		lblTitle.setFont(new Font("Ubuntu", Font.BOLD, 16));
		lblTitle.setForeground(new Color(224, 255, 255));
		lblTitle.setBounds(69, 46, 70, 15);
		frmAddMessage.getContentPane().add(lblTitle);
		
		textField = new JTextField();
		textField.setFont(new Font("Ubuntu", Font.BOLD, 16));
		textField.setBounds(190, 45, 172, 19);
		frmAddMessage.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("/ 1000");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 10));
		lblNewLabel.setForeground(new Color(224, 255, 255));
		lblNewLabel.setBounds(324, 155, 38, 29);
		frmAddMessage.getContentPane().add(lblNewLabel);
		
		label = new JLabel("");
		label.setForeground(new Color(224, 255, 255));
		label.setFont(new Font("Dialog", Font.BOLD, 10));
		label.setBounds(293, 155, 32, 29);
		frmAddMessage.getContentPane().add(label);
		
		error = new JLabel("");
		error.setFont(new Font("Ubuntu", Font.PLAIN, 12));
		error.setHorizontalAlignment(SwingConstants.CENTER);
		error.setForeground(new Color(240, 248, 255));
		error.setBounds(12, 216, 406, 26);
		frmAddMessage.getContentPane().add(error);
		frmAddMessage.setBounds(100, 100, 450, 357);
		frmAddMessage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAddMessage.setLocationRelativeTo(null);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		if(arg0.getSource()==textArea) {
			label.setText(Integer.toString(textArea.getText().length()+1));
		}
		
	}
}
