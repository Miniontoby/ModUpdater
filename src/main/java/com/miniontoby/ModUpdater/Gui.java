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
import javax.swing.SwingUtilities;
//import javax.swing.plaf.metal;

public class Gui extends JFrame {
	public static Gui instance;
	private JTabbedPane contentPane;
	private CheckModPanel CM;
	private InstallModPanel IM;
	private UpdateAllModsPanel UAM;
	private InstallModpackPanel IMP;
	private static String pidValue;
	private static String versionValue;
	private static String modLoaderValue;
	private static File locationValue;
	private static String didSubmit = null;

	public static String getSubmit() { return didSubmit; }
	public static String getPid() { return pidValue; }
	private static void setPid(String pid) { pidValue = pid; }
	public static String getVersion() { return versionValue; }
	private static void setVersion(String version) { versionValue = version; }
	public static String getModLoader() { return modLoaderValue; }
	private static void setModLoader(String modLoader) { modLoaderValue = modLoader; }
	public static File getFolder() { return locationValue; }
	private static void setFolder(File location) { locationValue = location; }

	private static class T implements Runnable {
		private Gui e;

		public T(Gui e) {
			this.e = e;
		}

		@Override
		public void run() {
			while (true) {
				if (e.isVisible()) {
					if (CheckModPanel.getSubmit()){
						setPid(CheckModPanel.getPid());
						setVersion(CheckModPanel.getVersion());
						setModLoader(CheckModPanel.getModLoader());
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
					} else if (InstallModpackPanel.getSubmit()) {
						setFolder(InstallModpackPanel.getFolder());
						didSubmit = "installModpack";
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

	public Gui(){
		contentPane = new JTabbedPane();
		CM = new CheckModPanel();
		IM = new InstallModPanel();
		UAM = new UpdateAllModsPanel();
		IMP = new InstallModpackPanel();

		contentPane.addTab("Check Mod", CM);
		contentPane.addTab("Install Mod", IM);
		contentPane.addTab("Update All Mods", UAM);
		contentPane.addTab("Install Modpack", IMP);

//		UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("icon.png")));
		setSize(450, 200);
		setTitle("Mod Updater GUI");
		setVisible(true);
	}

	public static boolean main(String s[]) throws Exception {
		Gui frame = new Gui();
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
	private JLabel modLoaderLabel;
	private JTextField pidField;
	private JTextField versionField;
	private JTextField modLoaderField;
	private JButton submit;
	private static String pidValue;
	private static String versionValue;
	private static String modLoaderValue;
	private static boolean didSubmit = false;

	public static boolean getSubmit() { return didSubmit; }
	private void setSubmit() { didSubmit = true; }
	public static String getPid() { return pidValue; }
	private void setPid(String pid) { this.pidValue = pid; }
	public static String getVersion() { return versionValue; }
	private void setVersion(String version) { this.versionValue = version; }
	public static String getModLoader() { return modLoaderValue; }
	private void setModLoader(String modLoader) { this.modLoaderValue = modLoader; }

	public CheckModPanel() {
		pidLabel = new JLabel("Project ID:");
		pidField = new JTextField("");
		pidField.setBounds(0, 0, 10, 20);
		pidField.setColumns(10);

		versionLabel = new JLabel("Version:");
		versionField = new JTextField("");
		versionField.setBounds(0, 0, 10, 20);
		versionField.setColumns(10);

		modLoaderLabel = new JLabel("ModLoader:");
		modLoaderField = new JTextField("");
		modLoaderField.setColumns(10);

		submit = new JButton("Submit");

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pidLabel).addComponent(versionLabel).addComponent(modLoaderLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pidField).addComponent(versionField).addComponent(modLoaderField))
			).addComponent(submit)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pidLabel).addComponent(pidField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(versionLabel).addComponent(versionField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(modLoaderLabel).addComponent(modLoaderField))
			).addComponent(submit)
		);

		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(pidField.getText().isEmpty()||versionField.getText().isEmpty()||modLoaderField.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Data Missing");
				else {
					setPid(pidField.getText());
					setVersion(versionField.getText());
					setModLoader(modLoaderField.getText());
					setSubmit();
				}
			}
		});
	}
}

class InstallModPanel extends JPanel implements ActionListener {
	public static InstallModPanel instance;
	private JLabel pidLabel;
	private JLabel versionLabel;
	private JLabel modLoaderLabel;
	private JLabel locationLabel;
	private JTextField pidField;
	private JTextField versionField;
	private JTextField modLoaderField;
	private JButton submit;
	private JButton locationSelect;
	private static String pidValue;
	private static String versionValue;
	private static String modLoaderValue;
	private static File locationValue;
	private boolean locationSet = false;
	private static boolean didSubmit = false;

	public static boolean getSubmit() { return didSubmit; }
	private void setSubmit() { didSubmit = true; }
	public static String getPid() { return pidValue; }
	private void setPid(String pid) { this.pidValue = pid; }
	public static String getVersion() { return versionValue; }
	private void setVersion(String version) { this.versionValue = version; }
	public static String getModLoader() { return modLoaderValue; }
	private void setModLoader(String modLoader) { this.modLoaderValue = modLoader; }
	public static File getFolder() { return locationValue; }
	private void setFolder(File location) { this.locationValue = location; this.locationSet = true; }

	public InstallModPanel() {
		pidLabel = new JLabel("Project ID:");
		pidField = new JTextField("");
		pidField.setColumns(10);

		versionLabel = new JLabel("Version:");
		versionField = new JTextField("");
		versionField.setColumns(10);

		modLoaderLabel = new JLabel("ModLoader:");
		modLoaderField = new JTextField("");
		modLoaderField.setColumns(10);

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
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pidLabel).addComponent(versionLabel).addComponent(modLoaderLabel).addComponent(locationLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pidField).addComponent(versionField).addComponent(modLoaderField).addComponent(locationSelect))
			).addComponent(submit)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pidLabel).addComponent(pidField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(versionLabel).addComponent(versionField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(modLoaderLabel).addComponent(modLoaderField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(locationLabel).addComponent(locationSelect))
			).addComponent(submit)
		);

		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(pidField.getText().isEmpty()||(versionField.getText().isEmpty()||!locationSet)||modLoaderField.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Data Missing");
				else {
					setPid(pidField.getText());
					setVersion(versionField.getText());
					setModLoader(modLoaderField.getText());
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
class InstallModpackPanel extends JPanel implements ActionListener {
	public static InstallModpackPanel instance;
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

	public InstallModpackPanel() {
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
		chooser.setDialogTitle("Select the .minecraft folder (With manifest.json inside)");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) setFolder(chooser.getSelectedFile());
	}
}

