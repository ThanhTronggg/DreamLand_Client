package iuh.fit.gui.app;

import java.awt.*;
import java.time.LocalTime;

import javax.swing.*;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import entity.NhanVien;
import iuh.fit.gui.app.form.LoginForm;
import iuh.fit.gui.app.form.MainForm;
import iuh.fit.gui.app.form.LoginForm;
import iuh.fit.gui.app.form.MainForm;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialLiteTheme;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	private static Main app;
	private final LoginForm loginForm;
	private MainForm mainForm;

	private Main() {
		initComponents();
		loginForm = new LoginForm();
		setContentPane(loginForm);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		app = this;
		
	}

	public static Main getInstance() {
		return app;
	}

	public MainForm getMainForm() {
		return mainForm;
	}

	public LoginForm getLoginForm() {
		return loginForm;
	}

	public void createMainForm(NhanVien employee) {
		mainForm = new MainForm(employee);
	}

	public static void showMainForm(Component component) {
		component.applyComponentOrientation(app.getComponentOrientation());
		app.mainForm.showForm(component);
	}

	public static void setSelectedMenu(int index, int subIndex) {
		app.mainForm.setSelectedMenu(index, subIndex);
	}

	public static void logout() {
		int confirm = JOptionPane.showConfirmDialog(null, "Bạn có muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			app.loginForm.resetLogin();
			FlatAnimatedLafChange.showSnapshot();
			app.setContentPane(app.loginForm);
			app.loginForm.applyComponentOrientation(app.getComponentOrientation());
			SwingUtilities.updateComponentTreeUI(app.loginForm);
			FlatAnimatedLafChange.hideSnapshotWithAnimation();
		}
	}

	private void initComponents() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setUndecorated(false);
		setResizable(false);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 719, Short.MAX_VALUE));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 521, Short.MAX_VALUE));

		pack();
	}

	public static void main(String args[]) {
		FlatRobotoFont.install();
		FlatLaf.registerCustomDefaultsSource("theme");
		UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
		FlatMacLightLaf.setup();
//		SwingUtilities.invokeLater(() -> new Main().setVisible(true));
		SwingUtilities.invokeLater(() -> {
	        // Tạo instance của Main
	        Main mainApp = new Main();
	        mainApp.setVisible(false); // Không hiển thị JFrame chính ngay lúc đầu

	        // Hiển thị màn hình splash thông qua LoginForm
			LoginForm.getInstance().showSplashScreen(() -> {
				// Khi splash kết thúc, hiển thị JFrame chính
	            mainApp.setVisible(true);
	            mainApp.setContentPane(mainApp.getLoginForm());
	            mainApp.revalidate();
	            mainApp.repaint();
	        });
	    });
	}

	private static void showSplashScreen(Runnable onSplashFinished) {
		JWindow splashScreen = new JWindow();
		splashScreen.getContentPane().setBackground(new Color(191, 185, 165));

		JLabel imageLabel = new JLabel(new ImageIcon("images/png/logo_by_cahn.jpeg"), SwingConstants.CENTER);
		splashScreen.getContentPane().add(imageLabel, BorderLayout.CENTER);

		ImageIcon originalIcon = (ImageIcon) imageLabel.getIcon();
		Image originalImage = originalIcon.getImage();
		int newWidth = 400;
		int newHeight = 400;
		Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);
		imageLabel.setIcon(scaledIcon);

		JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		splashScreen.getContentPane().add(progressBar, BorderLayout.SOUTH);

		splashScreen.setBounds(450, 150, 400, 400);
		splashScreen.setLocationRelativeTo(null);

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				Thread.sleep(2000); // Hiển thị splash 3 giây
				return null;
			}

			@Override
			protected void done() {
				splashScreen.setVisible(false);
				splashScreen.dispose();
				SwingUtilities.invokeLater(onSplashFinished);
			}
		};

		new Thread(() -> {
			int value = 0;
			while (value <= 100) {
				progressBar.setValue(value);
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				value++;
			}
		}).start();

		splashScreen.setVisible(true);
		worker.execute();
	}

}
