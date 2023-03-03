package com.miniontoby.ModUpdater;

import java.awt.event.*;
import java.awt.Toolkit;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URI;
import java.util.*;
import javax.swing.filechooser.*;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment.*;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class SettingsMenu extends JFrame {
	public static Settings configProps = new Settings();
	private URI getAPIKeyURL = URI.create("https://console.curseforge.com/");
	private static String getAPIKeyURLStatic = "https://console.curseforge.com/";

	private JLabel labelInstructions = new JLabel("Get API Key at");
	private SwingLink link = new SwingLink(getAPIKeyURL.toString(), getAPIKeyURL.toString());
	private JLabel labelApiKey = new JLabel("API Key: ");

	private JTextField textApiKey = new JTextField(40);

	private JButton buttonSave = new JButton("Save");
	public Boolean isSaved = false;

        private static class T implements Runnable {
                private SettingsMenu e;

                public T(SettingsMenu e) {
                        this.e = e;
                }

                @Override
                public void run() {
                        while (true) {
                                if (e.isVisible()) {
					if (e.isSaved) {
						break;
					}
                                        try {
                                                Thread.sleep(100);
                                        } catch (InterruptedException e1) {
                                                System.out.println("Error");
                                                break;
                                        }
                                } else {
					break;
                                }
			}
		}
	}
	public SettingsMenu(String filename) {
		super("Settings Menu");

		try {
			configProps.loadFromFile(filename);
			isSaved = true;
			return;
		} catch (java.io.FileNotFoundException ex) {
			JOptionPane.showMessageDialog(this, "The config.properties file does not exist, please use this form to set.");
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "The config.properties file does not exist, please use this form to set.");
		}

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 10, 5, 10);
		constraints.anchor = GridBagConstraints.WEST;

		add(labelInstructions, constraints);
		constraints.gridx = 1;
		add(link, constraints);
		constraints.gridy += 1;

		constraints.gridx = 0;
		add(labelApiKey, constraints);
		constraints.gridx = 1;
		add(textApiKey, constraints);
		constraints.gridy += 1;

		/*
		constraints.gridx = 0;
		add(labelPort, constraints);
		constraints.gridx = 1;
		add(textPort, constraints);
		*/
		constraints.gridy += 1;

		constraints.gridx = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		add(buttonSave, constraints);

		buttonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					configProps.setProp("api_key", textApiKey.getText());

					configProps.saveToFile();
					JOptionPane.showMessageDialog(SettingsMenu.this,
							"Properties were saved successfully!");
					setVisible(false);
					isSaved = true;
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(SettingsMenu.this,
							"Error saving properties file: " + ex.getMessage());
				}
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);

		setVisible(true);
	}

        public static boolean main(String[] args) {
		try {
			SettingsMenu frame = new SettingsMenu(args[0]);
	                Thread thread = new Thread(new T(frame));
	                thread.start();

			while (!frame.isSaved && thread.isAlive()) {
				if (frame.isSaved || !thread.isAlive()) return true;
			}
		} catch (java.awt.HeadlessException e) {
			configProps = new Settings();
			try {
				configProps.loadFromFile(args[0]);
			} catch (IOException ex) {
				System.out.println("Error: " + ex.getMessage() + "\n\nThis may be because you don't have a settings file setup. Make one now:");
				Scanner in = new Scanner(System.in);

				System.out.print("API Key (get from " + getAPIKeyURLStatic + "): ");
				String key = in.nextLine();
				if (key == null || key == "") return false;
				configProps.setProp("api_key", key);

				try {
					configProps.saveToFile();
				} catch (IOException ex2) {
					ex2.printStackTrace();
					return false;
				}
			}
		}
                return true;
        }
}

class Settings extends Properties {
	private static File configFile;
	private static Properties ps = new Properties();
	public static void setProp(String key, String value) {
		ps.setProperty(key, value);
	}
	public static String getProp(String key) {
		return ps.getProperty(key);
	}
	public static void loadFromFile(String filename) throws IOException {
		configFile = new File(filename);
                InputStream inputStream = new FileInputStream(configFile);
		ps = new Properties();
		ps.load(inputStream);
		inputStream.close();
        }
	public static void saveToFile() throws IOException {
		if (!configFile.exists() && !configFile.getParentFile().mkdirs() && !configFile.createNewFile()) throw new IOException("Cannot make config folder!");
		OutputStream outputStream = new FileOutputStream(configFile);
		ps.store(outputStream, "setttings");
		outputStream.close();
	}
}
