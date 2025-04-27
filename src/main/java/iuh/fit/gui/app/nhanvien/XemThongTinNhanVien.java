package iuh.fit.gui.app.nhanvien;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entity.NhanVien;

public class XemThongTinNhanVien extends JDialog {

    private static final long serialVersionUID = 1L;

    public XemThongTinNhanVien(NhanVien nhanVien) {
        setTitle("Chi Tiết Nhân Viên");
        setSize(800, 600); // Tăng kích thước cửa sổ lên 800x600
        setLocationRelativeTo(null);

        // Panel chính chứa các phần tử
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 2 cột, khoảng cách 10 giữa các ô
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Khoảng cách xung quanh
        add(mainPanel);

        // Thêm label và giá trị vào panel
        addLabel(mainPanel, "Mã NV:", String.valueOf(nhanVien.getMaNhanVien()));
        addLabel(mainPanel, "Họ và Tên:", nhanVien.getHoTen());
        addLabel(mainPanel, "Giới Tính:", nhanVien.isGioiTinh() ? "Nam" : "Nữ");
        addLabel(mainPanel, "Ngày Sinh:", nhanVien.getNgaySinh().toString());
        addLabel(mainPanel, "SĐT:", nhanVien.getSoDienThoai());
        addLabel(mainPanel, "Email:", nhanVien.getEmail());
        addLabel(mainPanel, "Ngày Bắt Đầu Làm:", nhanVien.getNgayBatDauLam().toString());
        addLabel(mainPanel, "Vai Trò:", nhanVien.getVaiTro());
        addLabel(mainPanel, "Trạng Thái:", nhanVien.getTrangThai());

        // Thêm tài khoản nếu có
        if (nhanVien.getTaiKhoan() != null) {
            addLabel(mainPanel, "Tài Khoản:", nhanVien.getTaiKhoan().toString());
        }
    }

    // Hàm phụ trợ để thêm các label vào GridLayout
    private void addLabel(JPanel panel, String labelText, String valueText) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 16)); // Dùng font chữ dễ đọc
        label.setForeground(new Color(50, 50, 50)); // Màu chữ tối để dễ nhìn
        panel.add(label); // Thêm label vào cột 1

        JLabel valueLabel = new JLabel(valueText);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Font chữ rõ ràng
        valueLabel.setForeground(new Color(0, 0, 0)); // Màu chữ cho giá trị
        valueLabel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1)); // Viền mỏng cho giá trị
        valueLabel.setBackground(new Color(245, 245, 245)); // Nền sáng cho các giá trị
        valueLabel.setOpaque(true); // Đảm bảo nền được hiển thị
        panel.add(valueLabel); // Thêm giá trị vào cột 2
    }
}
