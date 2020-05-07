package com.github.intrigus.ftd.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import net.freeutils.httpserver.HTTPServer;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainUI {

	private JFrame frame;

	private static int port = 8888;
	private JTextField txtTest;

	private URI targetUrl = java.net.URI.create("http://localhost:" + port + "/scratch/");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainUI window = new MainUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Scratch Hilfsserver");
		frame.getContentPane().setLayout(null);

		JButton btnScratchImBrowser = new JButton("Scratch im Browser öffnen");
		btnScratchImBrowser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					java.awt.Desktop.getDesktop().browse(targetUrl);
				} catch (IOException | UnsupportedOperationException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"Der Browser konnte leider nicht geöffnet werden.\nBitte kopiere die URL manuell in den Browser.",
							"Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnScratchImBrowser.setEnabled(false);
		btnScratchImBrowser.setBounds(80, 103, 236, 25);
		new ServerStartWorker(btnScratchImBrowser).execute();
		frame.getContentPane().add(btnScratchImBrowser);

		txtTest = new JTextField();
		txtTest.setHorizontalAlignment(SwingConstants.CENTER);
		txtTest.setText(targetUrl.toString());
		txtTest.setEditable(false);
		txtTest.setBounds(80, 138, 236, 19);
		frame.getContentPane().add(txtTest);
		txtTest.setColumns(10);
	}

	private static class ServerStartWorker extends SwingWorker<Void, Void> {

		private JButton buttonToActivate;

		ServerStartWorker(JButton buttonToActivate) {
			this.buttonToActivate = buttonToActivate;
		}

		@Override
		protected Void doInBackground() throws Exception {
			HTTPServer server = Server.getNewConfiguredServerInstance(port);
			server.start();
			return null;
		}

		@Override
		protected void done() {
			buttonToActivate.setEnabled(true);
		}

	}
}
