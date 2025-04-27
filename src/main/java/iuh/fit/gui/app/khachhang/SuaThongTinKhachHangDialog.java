package iuh.fit.gui.app.khachhang;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.*;
import entity.KhachHang;
import service.KhachHangService;

public class SuaThongTinKhachHangDialog extends JDialog implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    private JTextField txtTenKH;
    private JTextField txtSoDT;
    private JTextField txtEmail;
    private JButton btnThoat;
    private JButton btnSua;
    private KhachHangService khachHangDao;
    private KhachHang khachHang;

    public SuaThongTinKhachHangDialog(KhachHang khachHang) throws MalformedURLException, NotBoundException, RemoteException {
        this.khachHang = khachHang;
        khachHangDao = (KhachHangService) Naming.lookup("rmi://XXXXXX:9090/khachHangService");
        // Frame settings
        setSize(700, 500);
        setTitle("Sửa thông tin khách hàng");
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel for header
        JPanel pnlNor = new JPanel();
        JLabel lblKhachHang = new JLabel("SỬA THÔNG TIN KHÁCH HÀNG");
        lblKhachHang.setFont(new Font(lblKhachHang.getFont().getFontName(), Font.BOLD, 26));
        pnlNor.add(lblKhachHang);
        pnlNor.setLayout(new BoxLayout(pnlNor, BoxLayout.Y_AXIS));
        pnlNor.add(Box.createVerticalStrut(20));
        add(pnlNor, BorderLayout.NORTH);
        
        // Panel for input fields
        JPanel pnlCen = new JPanel();
        pnlCen.setLayout(new BoxLayout(pnlCen, BoxLayout.Y_AXIS));
        add(pnlCen);
        
        // Input fields
        addInputField(pnlCen, "Tên khách hàng", txtTenKH = new JTextField(30));
        addInputField(pnlCen, "Số điện thoại", txtSoDT = new JTextField(20));
        addInputField(pnlCen, "Email", txtEmail = new JTextField(20));
        
        // Buttons
        JPanel pnlRow5 = new JPanel();
        pnlRow5.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnSua = new JButton("Sửa");
        btnSua.setBackground(Color.decode("#3f9daf"));
        btnSua.setForeground(Color.white);
        pnlRow5.add(btnSua);
        
        btnThoat = new JButton("Hủy bỏ");
        btnThoat.setBackground(Color.decode("#ff4f4f"));
        btnThoat.setForeground(Color.white);
        pnlRow5.add(btnThoat);
        
        pnlCen.add(pnlRow5);
        
        btnSua.addActionListener(this);
        btnThoat.addActionListener(this);
        
        
        loadKhachHangData();
    }

    private void addInputField(JPanel panel, String labelText, JTextField textField) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.add(Box.createHorizontalStrut(20));
        JLabel label = new JLabel(labelText);
        rowPanel.add(label);
        rowPanel.add(textField);
        rowPanel.add(Box.createHorizontalStrut(20));
        panel.add(rowPanel);
        panel.add(Box.createVerticalStrut(20));
    }

    private void loadKhachHangData() {
        if (khachHang != null) {
            txtTenKH.setText(khachHang.getTenKhachHang());
            txtSoDT.setText(khachHang.getSoDienThoai());
            txtEmail.setText(khachHang.getEmail());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(btnSua)) {
            if (kiemTraDieuKien()) {
                try {
					if (khachHangDao.update(khachHang)) {
					    JOptionPane.showMessageDialog(this, "Cập nhật thông tin khách hàng thành công!");
					    dispose();
					} else {
					    JOptionPane.showMessageDialog(this, "Cập nhật thất bại! Vui lòng thử lại.");
					}
				} catch (HeadlessException | RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        } else if (source.equals(btnThoat)) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn thoát? \nMọi thay đổi sẽ không được lưu.",
                "Chú ý", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        }
    }

    private boolean kiemTraDieuKien() {
        String ten = txtTenKH.getText().trim();
        String soDT = txtSoDT.getText().trim();
        String email = txtEmail.getText().trim();
        
        // Check if name is valid
        if (!ten.matches("[a-zA-Z\\s]+")) {
            showError("Tên khách hàng chỉ có thể chứa chữ và khoảng trắng", txtTenKH);
            return false;
        }
        
        // Check if phone number is valid
        if (!soDT.matches("^[0-9]{10,11}$")) {
            showError("Số điện thoại không hợp lệ!", txtSoDT);
            return false;
        }
        
        // Check if email is valid
        if (!email.matches("^[\\w-]+(?:\\.[\\w-]+)*@[\\w-]+(?:\\.[\\w-]+)*$")) {
            showError("Email không hợp lệ!", txtEmail);
            return false;
        }
        
        // All validations passed, update customer info
        khachHang.setTenKhachHang(ten);
        khachHang.setSoDienThoai(soDT);
        khachHang.setEmail(email);
        return true;
    }

    private void showError(String message, JTextField field) {
        JOptionPane.showMessageDialog(this, message);
        field.selectAll();
        field.requestFocus();
    }

    public void setQuanLyKhachHangGUI(QuanLyKhachHangGUI quanLyKhachHangGUI) {
        // This method can be used if you want to refresh data in the main GUI after editing
    }
}