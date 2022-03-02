package com.miniontoby.ModUpdater;

import java.awt.event.*;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
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
//import javax.swing.plaf.metal;

public class UpdaterGui extends JFrame {
	public static UpdaterGui instance;
	private JTabbedPane contentPane;
	private CheckModPanel CM;
	private InstallModPanel IM;
	private UpdateAllModsPanel UAM;
	private static String pidValue;
	private static String versionValue;
	private static File locationValue;
	private static String didSubmit = null;

	public static String getSubmit() { return didSubmit; }
	public static String getPid() { return pidValue; }
	private static void setPid(String pid) { pidValue = pid; }
	public static String getVersion() { return versionValue; }
	private static void setVersion(String version) { versionValue = version; }
	public static File getFolder() { return locationValue; }
	private static void setFolder(File location) { locationValue = location; }

	private static class T implements Runnable {
		private UpdaterGui e;

		public T(UpdaterGui e) {
			this.e = e;
		}

		@Override
		public void run() {
			while (true) {
				if (e.isVisible()) {
					if (CheckModPanel.getSubmit()){
						setPid(CheckModPanel.getPid());
						setVersion(CheckModPanel.getVersion());
						didSubmit = "checkMod";
						e.setVisible(false);
						break;
					} else if (InstallModPanel.getSubmit()) {
						setPid(InstallModPanel.getPid());
						setVersion(InstallModPanel.getVersion());
						setFolder(InstallModPanel.getFolder());
						didSubmit = "installMod";
						e.setVisible(false);
						break;
					} else if (UpdateAllModsPanel.getSubmit()) {
						setFolder(UpdateAllModsPanel.getFolder());
						didSubmit = "updateAllMods";
						e.setVisible(false);
						break;
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						System.out.println("Error");
						break;
					}
				} else {
					System.exit(1);
				}
			}
		}
	}

	public UpdaterGui(){
		contentPane = new JTabbedPane();
		CM = new CheckModPanel();
		IM = new InstallModPanel();
		UAM = new UpdateAllModsPanel();

		contentPane.addTab("Check Mod", CM);
		contentPane.addTab("Install Mod", IM);
		contentPane.addTab("Update All Mods", UAM);

//		UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("icon.png")));
		setSize(450, 200);
		setTitle("Mod Updater GUI");
		setVisible(true);
	}

	public static boolean main(String s[]) throws Exception {
		UpdaterGui frame = new UpdaterGui();
		Thread thread = new Thread(new T(frame));
		thread.start();

/*		try {
			ImageIcon icon = new ImageIcon(new URL("https://media.forgecdn.net/avatars/130/458/636460205549127215.png"));
			frame.setIconImage(icon.getImage());
		} catch (Exception e) {
		} */

		while (thread.isAlive()) {
			if (!thread.isAlive()) return true;
		}
		return true;
	}
}



class CheckModPanel extends JPanel {
	public static CheckModPanel instance;
	private JLabel pidLabel;
	private JLabel versionLabel;
	private JTextField pidField;
	private JTextField versionField;
	private JButton submit;
	private static String pidValue;
	private static String versionValue;
	private static boolean didSubmit = false;

	public static boolean getSubmit() { return didSubmit; }
	private void setSubmit() { didSubmit = true; }
	public static String getPid() { return pidValue; }
	private void setPid(String pid) { this.pidValue = pid; }
	public static String getVersion() { return versionValue; }
	private void setVersion(String version) { this.versionValue = version; }

	public CheckModPanel() {
		pidLabel = new JLabel("Project ID:");
		pidField = new JTextField("");
		pidField.setBounds(0, 0, 10, 20);
		pidField.setColumns(10);

		versionLabel = new JLabel("Version:");
		versionField = new JTextField("");
		versionField.setBounds(0, 0, 10, 20);
		versionField.setColumns(10);

		submit = new JButton("Submit");

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pidLabel).addComponent(versionLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pidField).addComponent(versionField))
			).addComponent(submit)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pidLabel).addComponent(pidField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(versionLabel).addComponent(versionField))
			).addComponent(submit)
		);

		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(pidField.getText().isEmpty()||versionField.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Data Missing");
				else {
					setSubmit();
					setPid(pidField.getText());
					setVersion(versionField.getText());
				}
			}
		});
	}
}

class InstallModPanel extends JPanel implements ActionListener {
	public static InstallModPanel instance;
	private JLabel pidLabel;
	private JLabel versionLabel;
	private JLabel locationLabel;
	private JTextField pidField;
	private JTextField versionField;
	private JButton submit;
	private JButton locationSelect;
	private static String pidValue;
	private static String versionValue;
	private static File locationValue;
	private boolean locationSet = false;
	private static boolean didSubmit = false;

	public static boolean getSubmit() { return didSubmit; }
	private void setSubmit() { didSubmit = true; }
	public static String getPid() { return pidValue; }
	private void setPid(String pid) { this.pidValue = pid; }
	public static String getVersion() { return versionValue; }
	private void setVersion(String version) { this.versionValue = version; }
	public static File getFolder() { return locationValue; }
	private void setFolder(File location) { this.locationValue = location; this.locationSet = true; }

	public InstallModPanel() {
		pidLabel = new JLabel("Project ID:");
		pidField = new JTextField("");
//		pidField.setBounds(128, 28, 86, 20);
		pidField.setColumns(10);

		versionLabel = new JLabel("Version:");
		versionField = new JTextField("");
//		versionField.setBounds(128, 28, 86, 20);
		versionField.setColumns(10);

		locationLabel = new JLabel("Select the .minecraft folder:");
		locationSelect = new JButton("Select");
		locationSelect.addActionListener(this);

		submit = new JButton("Submit");

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pidLabel).addComponent(versionLabel).addComponent(locationLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pidField).addComponent(versionField).addComponent(locationSelect))
			).addComponent(submit)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pidLabel).addComponent(pidField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(versionLabel).addComponent(versionField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(locationLabel).addComponent(locationSelect))
			).addComponent(submit)
		);

		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(pidField.getText().isEmpty()||(versionField.getText().isEmpty()||!locationSet))
					JOptionPane.showMessageDialog(null, "Data Missing");
				else {
					setSubmit();
					setPid(pidField.getText());
					setVersion(versionField.getText());
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		chooser.setDialogTitle("Select the .minecraft folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) setFolder(chooser.getSelectedFile());
	}
}

class UpdateAllModsPanel extends JPanel implements ActionListener {
	public static UpdateAllModsPanel instance;
	private JLabel locationLabel;
	private JButton locationSelect;
	private JButton submit;
	private static File locationValue;
	private boolean locationSet = false;
	private static boolean didSubmit = false;

	public static boolean getSubmit() { return didSubmit; }
	private void setSubmit() { didSubmit = true; }
	public static File getFolder() { return locationValue; }
	private void setFolder(File location) { this.locationValue = location; this.locationSet = true; }

	public UpdateAllModsPanel() {
		locationLabel = new JLabel("Select the .minecraft folder:");
		locationSelect = new JButton("Select");
		locationSelect.addActionListener(this);

		submit = new JButton("Submit");

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(locationLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(locationSelect))
			).addComponent(submit)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(locationLabel).addComponent(locationSelect))
			).addComponent(submit)
		);

		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!locationSet)
					JOptionPane.showMessageDialog(null, "Folder location missing!");
				else {
					setSubmit();
				}
			}
		});
	}
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		chooser.setDialogTitle("Select the .minecraft folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) setFolder(chooser.getSelectedFile());
	}
}
