package iuh.fit.gui.app.phim;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.toedter.calendar.JDateChooser; 
import entity.Phim;
import iuh.fit.gui.app.EnvironmentVariable;
import service.PhimService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Calendar;

public class ThemPhimDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JLabel lblMaPhim; 
    private JTextField txtTenPhim, txtTheLoai, txtDaoDien, txtThoiLuong, txtNgonNgu, txtQuocGia, txtGiaThau, txtDoanPhimGioiThieu, txtTomTat;
    private JComboBox<String> cbTrangThai;
    private JDateChooser dateChooserNgayCongChieu, dateChooserNgayBatDau;
    private JButton btnThem, btnThoat, btnChonAnh;
    private JLabel lblHinhChon;
    private String pathAnh;
    private PhimService phimDao;
    private Phim phim;

    public ThemPhimDialog() throws MalformedURLException, NotBoundException, RemoteException {
        phimDao = (PhimService) Naming.lookup("rmi://"+ EnvironmentVariable.IP.getValue()+":"+Integer.parseInt(EnvironmentVariable.PORT_SERVER.getValue())+"/phimService");

        setTitle("Thêm Phim");
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(38, 49, 104));
        JLabel lblHeader = new JLabel("THÊM PHIM");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 26));
        lblHeader.setForeground(Color.WHITE);
        pnlHeader.add(lblHeader);
        add(pnlHeader, BorderLayout.NORTH);

        // Center panel with form
        JPanel pnlCen = new JPanel();
        pnlCen.setLayout(new GridBagLayout());
        pnlCen.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Thêm Thông Tin Phim"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Left panel for first set of inputs
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Thông tin phim"));
        String[] leftLabels = {
            "Mã phim", "Tên phim", "Thể loại", "Đạo diễn", "Ngày công chiếu", "Ảnh"
        };

        // Initialize components for left panel
        lblMaPhim = new JLabel(); 
        txtTenPhim = new JTextField(60);
        txtTheLoai = new JTextField(60);
        txtDaoDien = new JTextField(60);
        dateChooserNgayCongChieu = new JDateChooser();

        // Add components to left panel
        for (int i = 0; i < leftLabels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            leftPanel.add(new JLabel(leftLabels[i]), gbc);

            gbc.gridx = 1;
            if (i == 0) { 
                leftPanel.add(lblMaPhim, gbc);
            } else if (i == 4) { 
                leftPanel.add(dateChooserNgayCongChieu, gbc);
            } else if (i == 5) { 
                lblHinhChon = new JLabel();
                lblHinhChon.setPreferredSize(new Dimension(150, 150));
                lblHinhChon.setHorizontalAlignment(JLabel.CENTER);
                leftPanel.add(lblHinhChon, gbc);

                btnChonAnh = new JButton("Chọn ảnh");
                btnChonAnh.addActionListener(e -> chonAnh());
                gbc.gridy++;
                leftPanel.add(btnChonAnh, gbc);
            } else {
                JTextField textField = new JTextField(25);
                switch (i) {
                    case 1: {
                        textField = txtTenPhim;
                        break;
                    }
                    case 2: {
                        textField = txtTheLoai;
                        break;
                    }
                    case 3: {
                        textField = txtDaoDien;
                        break;
                    }
                }
                leftPanel.add(textField, gbc);
            }
        }

        // Right panel for the remaining inputs
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khác"));
        String[] rightLabels = {
            "Thời lượng (phút)", "Ngôn ngữ", "Quốc gia", "Trạng thái", "Ngày bắt đầu", "Giá thầu", "Đoạn phim giới thiệu", "Tóm tắt"
        };

        // Initialize components for right panel
        txtThoiLuong = new JTextField(40);
        txtNgonNgu = new JTextField(40);
        txtQuocGia = new JTextField(40);
        String[] trangThaiOptions = {"Đã phát hành", "Chưa phát hành"};
        cbTrangThai = new JComboBox<>(trangThaiOptions);
        dateChooserNgayBatDau = new JDateChooser();
        txtGiaThau = new JTextField(40);
        txtDoanPhimGioiThieu = new JTextField(25);
        txtTomTat = new JTextField(40);

        // Add components to right panel
        for (int i = 0; i < rightLabels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            rightPanel.add(new JLabel(rightLabels[i]), gbc);

            gbc.gridx = 1;
            if (i == 3) { 
                rightPanel.add(cbTrangThai, gbc);
            } else if (i == 4) { 
                rightPanel.add(dateChooserNgayBatDau, gbc);
            } else {
                JTextField textField = new JTextField(25);
                switch (i) {
                    case 0: {
                        textField = txtThoiLuong;
                        break;
                    }
                    case 1: {
                        textField = txtNgonNgu;
                        break;
                    }
                    case 2: {
                        textField = txtQuocGia;
                        break;
                    }
                    case 5: {
                        textField = txtGiaThau;
                        break;
                    }
                    case 6: {
                        textField = txtDoanPhimGioiThieu;
                        break;
                    }
                    case 7: {
                        textField = txtTomTat;
                        break;
                    }
                }
                rightPanel.add(textField, gbc);
            }
        }

        // Combine both panels
        JPanel combinedPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        combinedPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        combinedPanel.add(rightPanel, gbc);

        add(combinedPanel, BorderLayout.CENTER);

        // Action buttons
        JPanel pnlButtons = new JPanel();
        btnThem = new JButton("Thêm");
        btnThem.setBackground(Color.decode("#3f9daf"));
        btnThem.setForeground(Color.white);
        btnThem.addActionListener(this);
        pnlButtons.add(btnThem);

        btnThoat = new JButton("Hủy bỏ");
        btnThoat.setBackground(Color.decode("#ff4f4f"));
        btnThoat.setForeground(Color.white);
        btnThoat.addActionListener(this);
        pnlButtons.add(btnThoat);

        add(pnlButtons, BorderLayout.SOUTH);

        setMaPhim(); // Lấy mã phim mới khi dialog xuất hiện
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThem) {
            try {
                them();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == btnThoat) {
            int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát? \nMọi thay đổi sẽ không được lưu.",
                    "Chú ý", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        }
    }
//
    private void setMaPhim() throws RemoteException {
        lblMaPhim.setText(phimDao.getNextMaPhim());
    }



    private void chonAnh() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            pathAnh = fileChooser.getSelectedFile().getAbsolutePath();
            ImageIcon originalIcon = new ImageIcon(pathAnh);
            Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblHinhChon.setIcon(new ImageIcon(scaledImage));
            JOptionPane.showMessageDialog(this, "Chọn ảnh thành công: " + pathAnh);
        }
    }

    private void them() throws RemoteException {
        this.phim = kiemTraDieuKien();
        if (this.phim != null) {
            if (phimDao.add(this.phim)) { // Thêm phim vào cơ sở dữ liệu
                JOptionPane.showMessageDialog(this, "Thêm thành công!", "Thông báo", JOptionPane.PLAIN_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi thêm phim!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Phim kiemTraDieuKien() {
        String maPhim = lblMaPhim.getText(); 
        String tenPhim = txtTenPhim.getText().trim();
        String theLoai = txtTheLoai.getText().trim();
        String daoDien = txtDaoDien.getText().trim();
        String thoiLuongStr = txtThoiLuong.getText().trim();
        String ngonNgu = txtNgonNgu.getText().trim();
        String quocGia = txtQuocGia.getText().trim();
        String trangThai = (String) cbTrangThai.getSelectedItem();
        String giaThauStr = txtGiaThau.getText().trim();
        String doanPhimGioiThieu = txtDoanPhimGioiThieu.getText().trim();
        String tomTat = txtTomTat.getText().trim();

        if (tenPhim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên phim không được để trống");
            txtTenPhim.requestFocus();
            return null;
        }

        if (theLoai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Thể loại không được để trống");
            txtTheLoai.requestFocus();
            return null;
        }

        if (daoDien.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Đạo diễn không được để trống");
            txtDaoDien.requestFocus();
            return null;
        }

        int thoiLuong;
        try {
            thoiLuong = Integer.parseInt(thoiLuongStr);
            if (thoiLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Thời lượng phải là số nguyên dương");
                txtThoiLuong.requestFocus();
                return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Thời lượng phải là số nguyên dương");
            txtThoiLuong.requestFocus();
            return null;
        }

        LocalDate ngayCongChieu;
        try {
            ngayCongChieu = LocalDate.of(dateChooserNgayCongChieu.getCalendar().get(Calendar.YEAR),
                    dateChooserNgayCongChieu.getCalendar().get(Calendar.MONTH) + 1,
                    dateChooserNgayCongChieu.getCalendar().get(Calendar.DAY_OF_MONTH));
            if (ngayCongChieu.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Ngày công chiếu không thể trong quá khứ");
                dateChooserNgayCongChieu.requestFocus();
                return null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày công chiếu không hợp lệ");
            dateChooserNgayCongChieu.requestFocus();
            return null;
        }

        LocalDate ngayBatDau;
        try {
            ngayBatDau = LocalDate.of(dateChooserNgayBatDau.getCalendar().get(Calendar.YEAR),
                    dateChooserNgayBatDau.getCalendar().get(Calendar.MONTH) + 1,
                    dateChooserNgayBatDau.getCalendar().get(Calendar.DAY_OF_MONTH));
            if (ngayBatDau.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu không thể trong quá khứ");
                dateChooserNgayBatDau.requestFocus();
                return null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu không hợp lệ");
            dateChooserNgayBatDau.requestFocus();
            return null;
        }

        double giaThau;
        try {
            giaThau = Double.parseDouble(giaThauStr);
            if (giaThau < 0) {
                JOptionPane.showMessageDialog(this, "Giá thầu phải là số không âm");
                txtGiaThau.requestFocus();
                return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá thầu phải là số không âm");
            txtGiaThau.requestFocus();
            return null;
        }

//        phim = new Phim(maPhim, tenPhim, theLoai, daoDien, thoiLuong, ngayCongChieu, ngonNgu, quocGia, trangThai, ngayBatDau, giaThau, pathAnh, doanPhimGioiThieu, tomTat);
        phim = new Phim();
        phim.setMaPhim(maPhim);
        phim.setTenPhim(tenPhim);
        phim.setTheLoai(theLoai);
        phim.setDaoDien(daoDien);
        phim.setThoiLuong(thoiLuong);
        phim.setNgayCongChieu(ngayCongChieu);
        phim.setNgonNgu(ngonNgu);
        phim.setQuocGia(quocGia);
        phim.setTrangThai(trangThai);
        phim.setNgayBatDau(ngayBatDau);
        phim.setGiaThau(giaThau);
        phim.setAnh(pathAnh);
        phim.setTrailer(doanPhimGioiThieu);
        phim.setTomTat(tomTat);
        return phim;
    }

    public Phim getPhim() {
        return this.phim;
    }
}