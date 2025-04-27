package iuh.fit.gui.app.nhanvien;

import java.awt.Color;
import java.awt.Font;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import dao.NhanVienDAO;
import entity.NhanVien;
import service.NhanVienService;

public class CapNhatNhanVien extends JFrame {
	private JTextField txtFullName, txtEmail, txtPhone;
	private JComboBox<String> cboEmployeeRole;
	private JButton btnUpdate;
	private JRadioButton rbtnMale, rbtnFemale;
	private JDateChooser birthDateChooser, startDateChooser;
	private String hinhAnhNV; // Thông tin ảnh (bỏ qua trong trường hợp này)

	public CapNhatNhanVien(NhanVien nhanVien) {
		setupUI(nhanVien);
	}

	private void setupUI(NhanVien nhanVien) {
		setTitle("Cập Nhật Nhân Viên");
		setSize(920, 650);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);

		setupFormPanel(nhanVien);
	}

	private void setupFormPanel(NhanVien nhanVien) {
		JPanel formPanel = new JPanel();
		formPanel.setLayout(null);
		formPanel.setBackground(Color.WHITE);
		formPanel.setBounds(10, 10, 880, 570); // Set bounds for the form panel

		// Left panel for personal information
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(null);
		leftPanel.setBounds(10, 10, 480, 500); // 480px width for left panel
		leftPanel.setBackground(Color.WHITE);

		// Right panel for employee role, email, phone
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(null);
		rightPanel.setBounds(500, 10, 370, 500); // 370px width for right panel
		rightPanel.setBackground(Color.WHITE);

		// Add components to the left panel (personal info)
		addPersonalInfoComponents(leftPanel, nhanVien);

		// Add components to the right panel (employee info)
		addEmployeeInfoComponents(rightPanel, nhanVien);

		// Button to update employee
		btnUpdate = new JButton("Cập nhật");
		btnUpdate.setBounds(350, 520, 150, 50);
		btnUpdate.setBackground(new Color(70, 130, 180));
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setFont(new Font("Arial", Font.BOLD, 16));
		btnUpdate.addActionListener(event -> {
            try {
                updateEmployee(nhanVien);
            } catch (Exception e) {
				e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

		// Add panels to the form panel
		formPanel.add(leftPanel);
		formPanel.add(rightPanel);
		formPanel.add(btnUpdate);

		// Add form panel to the content pane
		getContentPane().add(formPanel);
	}

	private void addPersonalInfoComponents(JPanel panel, NhanVien nhanVien) {
		txtFullName = createTextField(150, 40, 250, 40, nhanVien.getHoTen());
		birthDateChooser = createDatePicker(150, 90, nhanVien.getNgaySinh());
		startDateChooser = createDatePicker(150, 140, nhanVien.getNgayBatDauLam());

		panel.add(createLabel("Họ và tên:", 30, 40));
		panel.add(txtFullName);
		panel.add(createLabel("Ngày sinh:", 30, 90));
		panel.add(birthDateChooser);
		panel.add(createLabel("Ngày bắt đầu làm:", 30, 140));
		panel.add(startDateChooser);

		setupGenderRadioButtons(panel, nhanVien.isGioiTinh());
	}

	private void addEmployeeInfoComponents(JPanel panel, NhanVien nhanVien) {
		txtEmail = createTextField(150, 40, 200, 40, nhanVien.getEmail());
		txtPhone = createTextField(150, 90, 200, 40, nhanVien.getSoDienThoai());

		cboEmployeeRole = new JComboBox<>(new String[] { "Nhân viên bán vé", "Nhân viên quản lý" });
		cboEmployeeRole.setBounds(150, 140, 200, 40);
		cboEmployeeRole.setSelectedItem(nhanVien.getVaiTro());

		panel.add(createLabel("Email:", 30, 40));
		panel.add(txtEmail);
		panel.add(createLabel("SĐT:", 30, 90));
		panel.add(txtPhone);
		panel.add(createLabel("Vai trò:", 30, 140));
		panel.add(cboEmployeeRole);
	}

	private JTextField createTextField(int x, int y, int width, int height, String text) {
		JTextField textField = new JTextField(text);
		textField.setBounds(x, y, width, height);
		textField.setFont(new Font("Arial", Font.PLAIN, 14));
		return textField;
	}

	private JLabel createLabel(String text, int x, int y) {
		JLabel label = new JLabel(text);
		label.setBounds(x, y, 120, 40);
		label.setFont(new Font("Arial", Font.PLAIN, 14));
		return label;
	}

	private JDateChooser createDatePicker(int x, int y, LocalDate date) {
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setBounds(x, y, 250, 40);
		dateChooser.setDateFormatString("dd/MM/yyyy");
		if (date != null) {
			dateChooser.setDate(java.sql.Date.valueOf(date));
		}
		return dateChooser;
	}

	private void setupGenderRadioButtons(JPanel formPanel, boolean gioiTinh) {
		rbtnMale = new JRadioButton("Nam");
		rbtnFemale = new JRadioButton("Nữ");

		rbtnMale.setBounds(150, 190, 70, 40);
		rbtnFemale.setBounds(220, 190, 70, 40);

		ButtonGroup genderGroup = new ButtonGroup();
		genderGroup.add(rbtnMale);
		genderGroup.add(rbtnFemale);

		formPanel.add(rbtnMale);
		formPanel.add(rbtnFemale);

		if (gioiTinh) {
			rbtnMale.setSelected(true);
		} else {
			rbtnFemale.setSelected(true);
		}
	}

	private void updateEmployee(NhanVien nhanVien) throws MalformedURLException, NotBoundException, RemoteException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String fullName = txtFullName.getText();
		String email = txtEmail.getText();
		String phone = txtPhone.getText();
		String birthDateStr = getDateString(birthDateChooser, dateFormat);
		String startDateStr = getDateString(startDateChooser, dateFormat);
		String vaiTro = (String) cboEmployeeRole.getSelectedItem();
		boolean gioiTinh = rbtnMale.isSelected();

		if (!validateInputs(fullName, email, phone, birthDateStr, startDateStr))
			return;

		nhanVien.setHoTen(fullName);
		nhanVien.setGioiTinh(gioiTinh);
		nhanVien.setEmail(email);
		nhanVien.setSoDienThoai(phone);
		nhanVien.setVaiTro(vaiTro);
		nhanVien.setNgaySinh(LocalDate.parse(birthDateStr));
		nhanVien.setNgayBatDauLam(LocalDate.parse(startDateStr));

		NhanVienService dao = (NhanVienService) Naming.lookup("rmi://XXXXXX:9090/nhanVienService");
		if (dao.update(nhanVien)) {
			JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!");
			dispose(); // Close the dialog

		} else {
			JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thất bại!");
		}
	}

	private String getDateString(JDateChooser dateChooser, SimpleDateFormat dateFormat) {
		return dateChooser.getDate() != null ? dateFormat.format(dateChooser.getDate()) : "";
	}

	private boolean validateInputs(String fullName, String email, String phone, String birthDateStr,
			String startDateStr) {
//		if (!Pattern.matches("^[\\p{L} ]+$", fullName)) {
//			JOptionPane.showMessageDialog(this, "Tên không hợp lệ!");
//			return false;
//		}

		if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
			JOptionPane.showMessageDialog(this, "Email không hợp lệ!");
			return false;
		}

		if (!Pattern.matches("^\\d{10}$", phone)) {
			JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!");
			return false;
		}

		if (birthDateStr.isEmpty() || startDateStr.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Ngày sinh và ngày bắt đầu làm không thể để trống!");
			return false;
		}

		return true;
	}
}
