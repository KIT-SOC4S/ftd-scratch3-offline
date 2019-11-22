package com.github.intrigus.ftd.ui;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

import net.freeutils.httpserver.HTTPServer;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class MainUI {

	private JFrame frame;

	private static int port = 8888;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
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
			public void actionPerformed(ActionEvent e) {
				try {
					java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://localhost:" + port + "/scratch/"));
				} catch (IOException e1) {
					// TODO add fitting error dialog using JOptionPane
					// allow the user to manually copy and paste the url
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnScratchImBrowser.setEnabled(false);
		btnScratchImBrowser.setBounds(80, 103, 236, 25);
		new ServerStartWorker(btnScratchImBrowser).execute();
		frame.getContentPane().add(btnScratchImBrowser);
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
