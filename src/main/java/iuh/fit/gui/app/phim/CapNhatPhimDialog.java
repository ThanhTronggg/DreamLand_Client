package iuh.fit.gui.app.phim;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.toedter.calendar.JDateChooser;
import dao.PhimDAO;
import entity.Phim;
import service.NhanVienService;
import service.PhimService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class CapNhatPhimDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JTextField txtMaPhim, txtTenPhim, txtTheLoai, txtDaoDien, txtThoiLuong, txtNgonNgu, txtQuocGia, txtGiaThau, txtDoanPhimGioiThieu, txtTomTat;
    private JComboBox<String> cbTrangThai;
    private JDateChooser dateChooserNgayCongChieu, dateChooserNgayBatDau;
    private JButton btnCapNhat, btnThoat, btnChonAnh;
    private JLabel lblHinhChon;
    private String pathAnh;
    private PhimService phimDao;
    private Phim phim;

    public CapNhatPhimDialog(Phim phim) throws MalformedURLException, NotBoundException, RemoteException {
        
        this.phimDao = (PhimService) Naming.lookup("rmi://172.20.10.14:9090/phimService");
        setTitle("Cập Nhật Phim");
        setSize(980, 760);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(38, 49, 104));
        JLabel lblHeader = new JLabel("CẬP NHẬT PHIM");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 26));
        lblHeader.setForeground(Color.WHITE);
        pnlHeader.add(lblHeader);
        add(pnlHeader, BorderLayout.NORTH);

        // Center panel with form
        JPanel pnlCen = new JPanel(new GridBagLayout());
        pnlCen.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Cập Nhật Thông Tin Phim"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0; // Tăng chiều rộng cho các thành phần

        // Left panel for first set of inputs
        JPanel leftPanel = createLeftPanel(gbc);
        
        // Right panel for the remaining inputs
        JPanel rightPanel = createRightPanel(gbc);

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
        btnCapNhat = new JButton("Cập Nhật");
        btnCapNhat.setBackground(Color.decode("#3f9daf"));
        btnCapNhat.setForeground(Color.white);
        btnCapNhat.addActionListener(this);
        pnlButtons.add(btnCapNhat);

        btnThoat = new JButton("Hủy bỏ");
        btnThoat.setBackground(Color.decode("#ff4f4f"));
        btnThoat.setForeground(Color.white);
        btnThoat.addActionListener(this);
        pnlButtons.add(btnThoat);

        add(pnlButtons, BorderLayout.SOUTH);

        if (phim == null) {
            // Xử lý trường hợp phim là null, ví dụ: hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(this, "Không thể cập nhật phim vì dữ liệu không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.phim = phim; // Nhận phim hiện tại để cập nhật
        // Hiển thị thông tin đã có
        loadPhimData();
    }

    private JPanel createLeftPanel(GridBagConstraints gbc) {
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Thông tin phim"));
        String[] leftLabels = {
            "Mã phim", "Tên phim", "Thể loại", "Đạo diễn", "Ngày công chiếu", "Ảnh"
        };

        // Initialize components for left panel
        txtMaPhim = new JTextField(20);
        txtMaPhim.setEditable(false);
        txtTenPhim = new JTextField(20);
        txtTheLoai = new JTextField(20);
        txtDaoDien = new JTextField(20);
        dateChooserNgayCongChieu = new JDateChooser();
        lblHinhChon = new JLabel();
        lblHinhChon.setPreferredSize(new Dimension(150, 150));
        lblHinhChon.setHorizontalAlignment(JLabel.CENTER);
        btnChonAnh = new JButton("Chọn ảnh");
        btnChonAnh.addActionListener(e -> chonAnh());

        // Add components to left panel
        for (int i = 0; i < leftLabels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            leftPanel.add(new JLabel(leftLabels[i]), gbc);

            gbc.gridx = 1;
            if (i == 4) {
                leftPanel.add(dateChooserNgayCongChieu, gbc);
            } else if (i == 5) {
                leftPanel.add(lblHinhChon, gbc);
                gbc.gridy++;
                leftPanel.add(btnChonAnh, gbc);
            } else {
                JTextField textField = new JTextField(20);
                switch (i) {
                    case 0: {
                        textField = txtMaPhim;
                        break;
                    }
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
        return leftPanel;
    }

    private JPanel createRightPanel(GridBagConstraints gbc) {
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khác"));
        String[] rightLabels = {
            "Thời lượng (phút)", "Ngôn ngữ", "Quốc gia", "Trạng thái", "Ngày bắt đầu", "Giá thầu", "Đoạn phim giới thiệu", "Tóm tắt"
        };

        // Initialize components for right panel
        txtThoiLuong = new JTextField(20);
        txtNgonNgu = new JTextField(20);
        txtQuocGia = new JTextField(20);
        String[] trangThaiOptions = {"Đang chiếu", "Sắp chiếu"};
        cbTrangThai = new JComboBox<>(trangThaiOptions);
        dateChooserNgayBatDau = new JDateChooser();
        txtGiaThau = new JTextField(20);
        txtDoanPhimGioiThieu = new JTextField(20);
        txtTomTat = new JTextField(20);

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
                JTextField textField = new JTextField(20);
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
        return rightPanel;
    }
    private void loadPhimData() {
        // Set existing values for the fields from the phim object
    	// Sử dụng this.phim để tải dữ liệu phim
      //  int maPhim = this.phim.getMaPhim();
        txtMaPhim.setText(String.valueOf(phim.getMaPhim()));
        txtTenPhim.setText(phim.getTenPhim());
        txtTheLoai.setText(phim.getTheLoai());
        txtDaoDien.setText(phim.getDaoDien());
        dateChooserNgayCongChieu.setDate(java.sql.Date.valueOf(phim.getNgayCongChieu()));
        txtThoiLuong.setText(String.valueOf(phim.getThoiLuong()));
        txtNgonNgu.setText(phim.getNgonNgu());
        txtQuocGia.setText(phim.getQuocGia());
        cbTrangThai.setSelectedItem(phim.getTrangThai());
        dateChooserNgayBatDau.setDate(java.sql.Date.valueOf(phim.getNgayBatDau()));
        txtGiaThau.setText(String.valueOf(phim.getGiaThau()));
        txtDoanPhimGioiThieu.setText(phim.getTrailer());
        txtTomTat.setText(phim.getTomTat());
        lblHinhChon.setIcon(new ImageIcon(phim.getAnh())); // Hiển thị ảnh hiện tại
    }

    private void chonAnh() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            pathAnh = fileChooser.getSelectedFile().getAbsolutePath();
            ImageIcon originalIcon = new ImageIcon(pathAnh);
            
            // Thay đổi kích thước ảnh tại đây
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblHinhChon.setIcon(new ImageIcon(scaledImage));

            // Cập nhật kích thước JLabel nếu cần
            lblHinhChon.setPreferredSize(new Dimension(150, 150));
            lblHinhChon.revalidate(); // Cập nhật layout
            lblHinhChon.repaint(); // Vẽ lại JLabel

            JOptionPane.showMessageDialog(this, "Load ảnh thành công");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCapNhat) {
            capNhat();
        } else if (e.getSource() == btnThoat) {
            int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát? \nMọi thay đổi sẽ không được lưu.",
                    "Chú ý", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                this.dispose();
                JOptionPane.showMessageDialog(this, "Không có dữ liệu mới nào được cập nhật");
            }
        }
    }

    private void capNhat() {
        Phim phimCapNhat = kiemTraDieuKien();
        
        if (phimCapNhat != null) {
            try {
                if (phimDao.update(phimCapNhat)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.PLAIN_MESSAGE);
                    this.dispose(); // Đóng dialog nếu cập nhật thành công
                } else {
                   // JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật phim!", "Thông báo", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Thông báo", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Thông tin phim không hợp lệ, vui lòng kiểm tra lại.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    

    private Phim kiemTraDieuKien() {
        String maPhim = txtMaPhim.getText().trim();
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

        // Kiểm tra mã phim
        if (maPhim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã phim không được để trống");
            txtMaPhim.requestFocus();
            return null;
        }

        // Kiểm tra tên phim
        if (tenPhim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên phim không được để trống");
            txtTenPhim.requestFocus();
            return null;
        }

        // Kiểm tra thể loại
        if (theLoai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Thể loại không được để trống");
            txtTheLoai.requestFocus();
            return null;
        }

        // Kiểm tra đạo diễn
        if (daoDien.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Đạo diễn không được để trống");
            txtDaoDien.requestFocus();
            return null;
        }

        // Kiểm tra thời lượng
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

        // Kiểm tra ngày công chiếu
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

        // Kiểm tra ngày bắt đầu
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

        // Kiểm tra giá thầu
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
     // Kiểm tra ảnh (nếu có) đã được chọn hay chưa
        if (pathAnh == null || pathAnh.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ảnh cho phim!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // Tạo và trả về đối tượng Phim
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