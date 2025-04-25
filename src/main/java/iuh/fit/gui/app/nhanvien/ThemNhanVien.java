package iuh.fit.gui.app.nhanvien;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
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
import dao.TaiKhoanDAO;
import entity.NhanVien;
import entity.TaiKhoan;

public class ThemNhanVien extends JFrame {
    private JTextField txtFullName, txtEmail, txtPhone;
    private JComboBox<String> cboEmployeeRole;
    private JButton btnSave;
    private JRadioButton rbtnMale, rbtnFemale;
    private JDateChooser birthDateChooser, startDateChooser;

    public ThemNhanVien() {
        setupUI();
    }

    private void setupUI() {
        setTitle("Thêm Nhân Viên");
        setSize(920, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setLayout(null); // Use null layout for custom positioning

        setupFormPanel();
    }

    private void setupFormPanel() {
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
        addPersonalInfoComponents(leftPanel);

        // Add components to the right panel (employee info)
        addEmployeeInfoComponents(rightPanel);

        // Button to save employee
        btnSave = new JButton("Lưu");
        btnSave.setBounds(350, 520, 150, 50);
        btnSave.setBackground(new Color(70, 130, 180));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Arial", Font.BOLD, 16));
        btnSave.addActionListener(this::saveEmployee);

        // Add panels to the form panel
        formPanel.add(leftPanel);
        formPanel.add(rightPanel);
        formPanel.add(btnSave);

        // Add form panel to the content pane
        getContentPane().add(formPanel);
    }

    private void addPersonalInfoComponents(JPanel panel) {
        txtFullName = createTextField(150, 40, 250, 40);
        birthDateChooser = createDatePicker(150, 90);
        startDateChooser = createDatePicker(150, 140);

        panel.add(createLabel("Họ và tên:", 30, 40));
        panel.add(txtFullName);
        panel.add(createLabel("Ngày sinh:", 30, 90));
        panel.add(birthDateChooser);
        panel.add(createLabel("Ngày bắt đầu làm:", 30, 140));
        panel.add(startDateChooser);

        // Gender radio buttons setup
        setupGenderRadioButtons(panel);
    }

    private void addEmployeeInfoComponents(JPanel panel) {
        // Email and Phone fields
        txtEmail = createTextField(150, 40, 200, 40);
        txtPhone = createTextField(150, 90, 200, 40);

        // Vai trò combo box
        cboEmployeeRole = new JComboBox<>(new String[] { "Nhân viên", "Quản lý" });
        cboEmployeeRole.setBounds(150, 140, 200, 40);

        panel.add(createLabel("Email:", 30, 40));
        panel.add(txtEmail);
        panel.add(createLabel("SĐT:", 30, 90));
        panel.add(txtPhone);
        panel.add(createLabel("Vai trò:", 30, 140));
        panel.add(cboEmployeeRole);
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        return textField;
    }

    private JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 120, 40); // Adjusted width to fit the label text
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private JDateChooser createDatePicker(int x, int y) {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setBounds(x, y, 250, 40);
        dateChooser.setDateFormatString("dd/MM/yyyy");
        return dateChooser;
    }

    private void setupGenderRadioButtons(JPanel formPanel) {
        rbtnMale = new JRadioButton("Nam");
        rbtnFemale = new JRadioButton("Nữ");

        rbtnMale.setBounds(150, 190, 70, 40); // Adjusted size for visibility
        rbtnFemale.setBounds(220, 190, 70, 40); // Adjusted size for visibility

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(rbtnMale);
        genderGroup.add(rbtnFemale);

        formPanel.add(rbtnMale);
        formPanel.add(rbtnFemale);
    }

    private void saveEmployee(ActionEvent event) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String fullName = txtFullName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String birthDateStr = getDateString(birthDateChooser, dateFormat);
        String startDateStr = getDateString(startDateChooser, dateFormat);
        String vaiTro = (String) cboEmployeeRole.getSelectedItem();
        boolean gioiTinh = rbtnMale.isSelected();

        if (!validateInputs(fullName, email, phone, birthDateStr, startDateStr)) {
            return;
        }

        LocalDate ngaySinh = LocalDate.parse(birthDateStr);
        LocalDate ngayBatDauLam = LocalDate.parse(startDateStr);

        // Tạo tài khoản trước
        TaiKhoanDAO tkDao = new TaiKhoanDAO(TaiKhoan.class);
        TaiKhoan tk = new TaiKhoan();
        tk.setTaiKhoan(phone); // Sử dụng số điện thoại làm tài khoản
        tk.setMatKhau("12345"); // Mật khẩu mặc định, có thể thay đổi sau

        boolean isOke = tkDao.add(tk); // Thêm tài khoản vào cơ sở dữ liệu
        if (!isOke) {
            JOptionPane.showMessageDialog(this, "Tạo tài khoản thất bại!");
            return;
        }

        // Nếu tài khoản tạo thành công, tiếp tục tạo nhân viên
        NhanVien nv = new NhanVien();
        nv.setHoTen(fullName);
        nv.setGioiTinh(gioiTinh);
        nv.setEmail(email);
        nv.setSoDienThoai(phone);
        nv.setVaiTro(vaiTro);
        nv.setNgaySinh(ngaySinh);
        nv.setNgayBatDauLam(ngayBatDauLam);
        TaiKhoan taiKhoan = tkDao.getTaiKhoanTheoUsername(phone);
        nv.setTaiKhoan(taiKhoan);

        NhanVienDAO dao = new NhanVienDAO(NhanVien.class);
        boolean isEmployeeCreated = dao.add(nv); // Thêm nhân viên vào cơ sở dữ liệu

        if (isEmployeeCreated) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            clearFields(); // Xóa các trường nhập liệu
        } else {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại!");
        }
    }

    private String getDateString(JDateChooser dateChooser, SimpleDateFormat dateFormat) {
        return dateChooser.getDate() != null ? dateFormat.format(dateChooser.getDate()) : "";
    }

    private boolean validateInputs(String fullName, String email, String phone, String birthDateStr,
            String startDateStr) {
        if (!Pattern.matches("^[\\p{L} ]+$", fullName)) {
            JOptionPane.showMessageDialog(this, "Tên không hợp lệ!");
            return false;
        }

        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!");
            return false;
        }

        if (!Pattern.matches("^\\d{10}$", phone)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!");
            return false;
        }

        if (birthDateStr.isEmpty() || startDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!");
            return false;
        }

        return true;
    }

    private void clearFields() {
        txtFullName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        birthDateChooser.setDate(null);
        startDateChooser.setDate(null);
        rbtnMale.setSelected(false);
        rbtnFemale.setSelected(false);
        cboEmployeeRole.setSelectedIndex(0);
    }
}
