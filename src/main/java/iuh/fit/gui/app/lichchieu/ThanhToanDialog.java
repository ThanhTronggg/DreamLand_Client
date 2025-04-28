/*
 * @(#) ThanhToanDialog.java 1.0 Nov 12, 2024
 * Copyright (c) 2024 IUH.
 * All rights reserved.
 */
package iuh.fit.gui.app.lichchieu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.formdev.flatlaf.FlatClientProperties;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.KhuyenMaiDAO;
import dao.SanPhamDAO;
import dao.VeDAO;
import entity.*;
import iuh.fit.gui.app.EnvironmentVariable;
import iuh.fit.gui.other.TaoHoaDon;
import iuh.fit.gui.other.TaoVe;
import service.*;

/**
 * @description:
 * @author: Thanh Trong
 * @date: Nov 12, 2024
 * @version: 1.0
 */

public class ThanhToanDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtSoDienThoai;
    private JTextField txtTen;
    private JTextField txtEmail;

    private JButton btnThanhToan;

    private JTextArea txtGhiChu;

    private KhachHangService khachHangDAO;
    private SanPhamService sanPhamDAO;
    private NhanVien nhanVienHienTai;
    private ChiTietHoaDonService chiTietDonHangDAO;
    private ChonSanPhamDialog chonSanPhamDialog;

	private KhuyenMaiService khuyenMaiDAO;

	private JLabel lblLoiTen;

	private JLabel lblLoiSoDienThoai;

	private JLabel lblLoiEmail;

	private VeService veDao;

	private HoaDonService hoaDonDAO;

    public ThanhToanDialog(ArrayList<Ghe> danhSachGheDaChon, ArrayList<ChiTietHoaDon> danhSachChiTietSanPham, LichChieu lichChieu) throws MalformedURLException, NotBoundException, RemoteException {

        khachHangDAO = (KhachHangService) Naming.lookup("rmi://"+EnvironmentVariable.IP.getValue()+":"+Integer.parseInt(EnvironmentVariable.PORT_SERVER.getValue())+"/khachHangService");
        sanPhamDAO = (SanPhamService) Naming.lookup("rmi://"+EnvironmentVariable.IP.getValue()+":"+Integer.parseInt(EnvironmentVariable.PORT_SERVER.getValue())+"/sanPhamService");
        chiTietDonHangDAO = (ChiTietHoaDonService) Naming.lookup("rmi://"+EnvironmentVariable.IP.getValue()+":"+Integer.parseInt(EnvironmentVariable.PORT_SERVER.getValue())+"/chiTietHoaDonService");
        veDao = (VeService) Naming.lookup("rmi://"+EnvironmentVariable.IP.getValue()+":"+Integer.parseInt(EnvironmentVariable.PORT_SERVER.getValue())+"/veService");
        hoaDonDAO = (HoaDonService) Naming.lookup("rmi://"+EnvironmentVariable.IP.getValue()+":"+Integer.parseInt(EnvironmentVariable.PORT_SERVER.getValue())+"/hoaDonService");
        khuyenMaiDAO = (KhuyenMaiService) Naming.lookup("rmi://"+ EnvironmentVariable.IP.getValue()+":"+Integer.parseInt(EnvironmentVariable.PORT_SERVER.getValue())+"/khuyenMaiService");
        IdGeneratorService idGeneratorService = (IdGeneratorService) Naming.lookup("rmi://localhost:9090/idGeneratorService");
        DecimalFormat df = new DecimalFormat("#,##0.00");
        JPanel pnlChinh = new JPanel(new BorderLayout());


        JPanel pnlKhachHang = new JPanel();
        pnlKhachHang.setLayout(new BoxLayout(pnlKhachHang, BoxLayout.Y_AXIS));
        pnlChinh.add(pnlKhachHang, BorderLayout.NORTH);
        
        JLabel lblTieuDeKhachHang = new JLabel("THÔNG TIN KHÁCH HÀNG");
        lblTieuDeKhachHang.setAlignmentX(CENTER_ALIGNMENT);
        lblTieuDeKhachHang.setFont(new Font("Arial", Font.BOLD, 30));
        pnlKhachHang.add(lblTieuDeKhachHang);
        pnlKhachHang.add(Box.createVerticalStrut(10));
        
        JPanel pnlFormKhachHang = new JPanel();
        pnlFormKhachHang.setLayout(new BoxLayout(pnlFormKhachHang, BoxLayout.Y_AXIS));

        // Số điện thoại
        JPanel pnlSDT = new JPanel();
        pnlSDT.setLayout(new BoxLayout(pnlSDT, BoxLayout.X_AXIS));
        JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
        txtSoDienThoai = new JTextField(20);
        JPanel pnlLoiSDT = new JPanel();
        pnlLoiSDT.add(Box.createHorizontalStrut(106));
        pnlLoiSDT.setLayout(new FlowLayout(5, 5, FlowLayout.LEFT));
        lblLoiSoDienThoai = new JLabel("*");
        lblLoiSoDienThoai.setForeground(Color.RED);
        
        pnlSDT.add(lblSoDienThoai);
        pnlSDT.add(Box.createHorizontalStrut(18));
        pnlSDT.add(txtSoDienThoai);
        pnlSDT.add(Box.createHorizontalStrut(20));
        pnlLoiSDT.add(lblLoiSoDienThoai);

        // Họ và tên
        JPanel pnlTen = new JPanel();
        pnlTen.setLayout(new BoxLayout(pnlTen, BoxLayout.X_AXIS));
        JLabel lblTen = new JLabel("Họ và tên:");
        txtTen = new JTextField(20);
        JPanel pnlLoiTen = new JPanel();
        pnlLoiTen.add(Box.createHorizontalStrut(106));
        pnlLoiTen.setLayout(new FlowLayout(5, 5, FlowLayout.LEFT));
        lblLoiTen = new JLabel("*");
        lblLoiTen.setForeground(Color.RED);

        pnlTen.add(lblTen);
        pnlTen.add(Box.createHorizontalStrut(42));
        pnlTen.add(txtTen);
        pnlTen.add(Box.createHorizontalStrut(20));
        pnlLoiTen.add(lblLoiTen);

        // Email
        JPanel pnlEmail = new JPanel();
        pnlEmail.setLayout(new BoxLayout(pnlEmail, BoxLayout.X_AXIS));
        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField(20);
        JPanel pnlLoiEmail = new JPanel();
        pnlLoiEmail.add(Box.createHorizontalStrut(106));
        pnlLoiEmail.setLayout(new FlowLayout(5, 5, FlowLayout.LEFT));
        lblLoiEmail = new JLabel(" ");
        lblLoiEmail.setForeground(Color.RED);

        pnlEmail.add(lblEmail);
        pnlEmail.add(Box.createHorizontalStrut(71));
        pnlEmail.add(txtEmail);
        pnlEmail.add(Box.createHorizontalStrut(20));
        pnlLoiEmail.add(lblLoiEmail);

        pnlFormKhachHang.add(pnlSDT);
        pnlFormKhachHang.add(pnlLoiSDT);
        pnlFormKhachHang.add(Box.createVerticalStrut(10));
        pnlFormKhachHang.add(pnlTen);
        pnlFormKhachHang.add(pnlLoiTen);
        pnlFormKhachHang.add(Box.createVerticalStrut(10));
        pnlFormKhachHang.add(pnlEmail);
        pnlFormKhachHang.add(pnlLoiEmail);
        pnlKhachHang.add(pnlFormKhachHang);
        
        txtSoDienThoai.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {
                try {
                    if (!validateSDT()) {
return;
}
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
            @Override public void removeUpdate(DocumentEvent e) {
                try {
                    if (!validateSDT()) {
return;
}
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
            @Override public void changedUpdate(DocumentEvent e) {
                try {
                    if (!validateSDT()) {
                        return;
}
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        
        txtTen.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {
            	if (!validateTen()) {
                    return;
                }
            }
            @Override public void removeUpdate(DocumentEvent e) {
            	if (!validateTen()) {
                    return;
                }
            }
            @Override public void changedUpdate(DocumentEvent e) {
            	if (!validateTen()) {
                    return;
                }
            }
        });
        
        txtEmail.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {
            	if (!validateEmail()) {
                    return;
                }
            }
            @Override public void removeUpdate(DocumentEvent e) {
            	if (!validateEmail()) {
                    return;
                }
            }
            @Override public void changedUpdate(DocumentEvent e) {
            	if (!validateEmail()) {
                    return;
                }
            }
        });
        JLabel lblThongTinHoaDon = new JLabel("THÔNG TIN HÓA ĐƠN");
        lblThongTinHoaDon.setAlignmentX(CENTER_ALIGNMENT);
        lblThongTinHoaDon.setFont(new Font("Arial", Font.BOLD, 30));
        pnlKhachHang.add(lblThongTinHoaDon);
        pnlKhachHang.add(Box.createVerticalStrut(20));
        
        JPanel pnlHoaDon = new JPanel(new GridLayout(0, 3, 5, 5));
        pnlChinh.add(pnlHoaDon);
        
        JPanel pnlPhim = new JPanel(new BorderLayout());
        pnlHoaDon.add(pnlPhim);
        pnlPhim.setBorder(BorderFactory.createMatteBorder(0,  0,  0, 2, Color.gray));
        
        JPanel pnlChiTiet = new JPanel();
        pnlChiTiet.setLayout(new BoxLayout(pnlChiTiet, BoxLayout.Y_AXIS));
        JLabel lblTitle1 = new JLabel("Phim");
        lblTitle1.setFont(new Font(lblTitle1.getFont().getFontName(), 1, 20));
        lblTitle1.setAlignmentX(CENTER_ALIGNMENT);
        pnlChiTiet.add(lblTitle1);
        pnlPhim.add(pnlChiTiet, BorderLayout.NORTH);
        
        ImageIcon icon = new ImageIcon(lichChieu.getPhim().getAnh());
        Image img = icon.getImage().getScaledInstance(100, -1, Image.SCALE_SMOOTH);
        JLabel lblAnh = new JLabel(new ImageIcon(img));
        lblAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlChiTiet.add(lblAnh);
        
        JPanel pnlTenPhim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTenPhim.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel lblTenPhim = new JLabel(lichChieu.getPhim().getTenPhim());
        lblTenPhim.setAlignmentX(CENTER_ALIGNMENT);
        lblTenPhim.setFont(new Font(lblTenPhim.getFont().getFontName(), 1, 20));
        pnlTenPhim.add(lblTenPhim);
        pnlChiTiet.add(pnlTenPhim);
        
        JPanel pnlNgayChieu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlNgayChieu.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        JLabel ngayChieu = new JLabel(lichChieu.getGioBatDau().format(dateFormatter));
        pnlNgayChieu.add(new JLabel("Ngày chiếu: "));
        pnlNgayChieu.add(ngayChieu);
        pnlChiTiet.add(pnlNgayChieu);

        JPanel pnlThoiGianChieu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlThoiGianChieu.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel thoiGianChieu = new JLabel(lichChieu.getGioBatDau().format(DateTimeFormatter.ofPattern("HH:mm")));
        pnlThoiGianChieu.add(new JLabel("Giờ chiếu: "));
        pnlThoiGianChieu.add(thoiGianChieu);
        pnlChiTiet.add(pnlThoiGianChieu);

        JPanel pnlPhong = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlPhong.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel lblphong = new JLabel(lichChieu.getPhong().getTenPhong());
        pnlPhong.add(new JLabel("Phòng: "));
        pnlPhong.add(lblphong);
        pnlChiTiet.add(pnlPhong);

        JPanel pnlGiaVe = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlGiaVe.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel lblGiaVe = new JLabel(df.format(lichChieu.getGiaMotGhe()) + " VND");
        pnlGiaVe.add(new JLabel("Giá vé: "));
        pnlGiaVe.add(lblGiaVe);
        pnlChiTiet.add(pnlGiaVe);

        JPanel pnlGhe = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlGhe.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        String dsGhe = "";
        for (Ghe ghe : danhSachGheDaChon) {
        	dsGhe = dsGhe.concat(ghe.getViTri()).concat(", ");
        }
        if(dsGhe.length() > 0) {
        	dsGhe = dsGhe.substring(0, dsGhe.length()-2);
//        	System.out.println(dsGhe);
        }
        JLabel lblGhe = new JLabel(dsGhe);
        pnlGhe.add(new JLabel("Ghế: " ));
        pnlGhe.add(lblGhe);
        pnlChiTiet.add(pnlGhe);

        JPanel pnlTongTienPhim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        double tongTienVe = 0;
        for (Ghe ghe : danhSachGheDaChon) {
        	if(ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế đôi Sweetbox")) {
        		tongTienVe += lichChieu.getGiaMotGhe()*2;
        	}
        	if(ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế VIP")) {
        		tongTienVe += lichChieu.getGiaMotGhe()*1.5;
        	}
        	if(ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế thường")) {
        		tongTienVe += lichChieu.getGiaMotGhe();
        	}
        }
        JLabel lblTongTienPhim = new JLabel(df.format(tongTienVe) + " VND");
        pnlTongTienPhim.add(new JLabel("Tổng: "));
        pnlTongTienPhim.add(lblTongTienPhim);
        pnlPhim.add(pnlTongTienPhim, BorderLayout.SOUTH);
        
        JPanel pnlSanPham = new JPanel(new BorderLayout());
        pnlSanPham.setBorder(BorderFactory.createMatteBorder(0, 0,  0, 2, Color.gray));
        JScrollPane scroll = new JScrollPane(pnlSanPham);
        scroll.setBorder(null);
        pnlHoaDon.add(scroll);
        
        JPanel pnlDanhSachSP = new JPanel();
        pnlDanhSachSP.setLayout(new BoxLayout(pnlDanhSachSP, BoxLayout.Y_AXIS));
        JLabel lblTitle2 = new JLabel("Đồ ăn & uống");
        lblTitle2.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle2.setFont(new Font(lblTitle2.getFont().getFontName(), Font.BOLD, 20));
        pnlDanhSachSP.add(lblTitle2);
        for (ChiTietHoaDon ct : danhSachChiTietSanPham) {
        	pnlDanhSachSP.add(Box.createVerticalStrut(10));
        	JPanel pnlTemp3 = new JPanel(new BorderLayout());
        	ImageIcon icon2 = new ImageIcon(ct.getSanPham().getAnh());
    		Image img2 = icon2.getImage();
    		Image resizedImg = img2.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    		ImageIcon resizedIcon = new ImageIcon(resizedImg);
            JLabel lblHinhAnhSanPham = new JLabel(resizedIcon);
            pnlTemp3.add(lblHinhAnhSanPham, BorderLayout.WEST);
        	
        	JPanel pnlTemp1 = new JPanel(new BorderLayout());
        	JLabel lblTenSanPham = new JLabel(ct.getSanPham().getTenSanPham());
        	lblTenSanPham.setFont(new Font(lblTenSanPham.getFont().getFontName(), 1, lblTenSanPham.getFont().getSize()));
        	pnlTemp1.add(lblTenSanPham, BorderLayout.WEST);
        	
        	JLabel lblTong = new JLabel(ct.getSoLuong() + " x "+ ct.getSanPham().getGiaBan() + " VND");
        	pnlTemp1.add(lblTong, BorderLayout.EAST);
        	
        	pnlDanhSachSP.add(pnlTemp3);
        	pnlDanhSachSP.add(pnlTemp1);
        	pnlDanhSachSP.add(Box.createVerticalStrut(10));
        }
        pnlSanPham.add(pnlDanhSachSP, BorderLayout.NORTH);
        
        JPanel pnlTongTienSP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTongTienSP;
        double tongTienSP = 0f;
        for (ChiTietHoaDon ct : danhSachChiTietSanPham) {
        	tongTienSP += ct.getSoLuong()*ct.getSanPham().getGiaBan();
        }
        lblTongTienSP = new JLabel(df.format(tongTienSP) + " VND");
        pnlTongTienSP.add(new JLabel("Tổng: "));
        pnlTongTienSP.add(lblTongTienSP);
        pnlSanPham.add(pnlTongTienSP, BorderLayout.SOUTH);
        
        JPanel pnlThanhToan = new JPanel(new BorderLayout());
        pnlHoaDon.add(pnlThanhToan);
        
        JPanel pnlDanhSachThanhToan = new JPanel();
        pnlDanhSachThanhToan.setLayout(new BoxLayout(pnlDanhSachThanhToan, BoxLayout.Y_AXIS));
        JLabel lblTitle3 = new JLabel("Thanh toán");
        lblTitle3.setAlignmentX(CENTER_ALIGNMENT);
        lblTitle3.setFont(new Font(lblTitle3.getFont().getFontName(), 1, 20));
        pnlDanhSachThanhToan.add(lblTitle3);
        pnlThanhToan.add(pnlDanhSachThanhToan, BorderLayout.NORTH);
        
        JPanel pnlTongTienPhim2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTongTienPhim2 = new JLabel(df.format(tongTienVe) + " VND");
        pnlTongTienPhim2.add(new JLabel("Tổng tiền vé: "));
        pnlTongTienPhim2.add(lblTongTienPhim2);
        pnlDanhSachThanhToan.add(pnlTongTienPhim2);
        
        JPanel pnlTongTienSP2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTongTienSP2 = new JLabel(df.format(tongTienSP) + " VND");
        pnlTongTienSP2.add(new JLabel("Tổng tiền đồ ăn & uống: "));
        pnlTongTienSP2.add(lblTongTienSP2);
        pnlDanhSachThanhToan.add(pnlTongTienSP2);
        
        double VAT = 0.1;
        JPanel pnlVAT = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblVAT = new JLabel(df.format((tongTienSP + tongTienVe)*VAT) + " VND");
        pnlVAT.add(new JLabel("VAT: "));
        pnlVAT.add(lblVAT);
        pnlDanhSachThanhToan.add(pnlVAT);
        
        KhuyenMai km1 = khuyenMaiDAO.getKhuyenMaiConHanTheoTongTienToiThieu(tongTienSP + tongTienVe);
        JLabel lblKhuyenMai;
        double phanTram = 0;
        if(km1 != null) {
        	
        	phanTram = km1.getPhanTramKhuyenMai();
        }
        JPanel pnlKhuyenMai = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblKhuyenMai = new JLabel(df.format(phanTram*100) + "%");
        pnlKhuyenMai.add(new JLabel("Khuyến mãi: "));
        pnlKhuyenMai.add(lblKhuyenMai);
        pnlDanhSachThanhToan.add(pnlKhuyenMai);
        
        JPanel pnlTongTien = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTongTien = new JLabel(df.format(((tongTienSP + tongTienVe)*VAT + (tongTienSP + tongTienVe))* (1- phanTram)) + " VND");
        JLabel lblTongtien2 = new JLabel("Tổng tiền: ");
//        JLabel lblTemp = new JLabel("-------------------------------------------------------------------------------------------");
        lblTongTien.putClientProperty(FlatClientProperties.STYLE, "font:$h5.font;foreground:$danger;");
        lblTongtien2.putClientProperty(FlatClientProperties.STYLE, "font:$h5.font;foreground:$danger;");
//        pnlTongTien.add(lblTemp);
        pnlTongTien.add(lblTongtien2);
        pnlTongTien.add(lblTongTien);
        pnlDanhSachThanhToan.add(pnlTongTien);
        
        JPanel pnlBtnThanhToan = new JPanel();
        pnlBtnThanhToan.setLayout(new BoxLayout(pnlBtnThanhToan, BoxLayout.Y_AXIS));
        btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setBackground(new Color(39, 49, 103));
        btnThanhToan.setPreferredSize(new Dimension(150, 100));
        btnThanhToan.setFont(new Font(btnThanhToan.getFont().getFontName(), 1, 30));
        btnThanhToan.setForeground(Color.white);
        btnThanhToan.setAlignmentX(CENTER_ALIGNMENT);
        pnlBtnThanhToan.add(btnThanhToan);
        pnlBtnThanhToan.add(Box.createVerticalStrut(20));
        btnThanhToan.addActionListener(e -> {
            try {
                // Kiểm tra dữ liệu đầu vào
                if (!validateSDT() || !validateTen() || !validateEmail()) {
                    return;
                }

                // Kiểm tra hoặc tạo KhachHang
                boolean tonTaiKhachHang = khachHangDAO.kiemTraSoDienThoaiTonTai(txtSoDienThoai.getText().trim());
                KhachHang khachHang;
                String maKhachHang;

                if (tonTaiKhachHang) {
                    // Lấy KhachHang hiện có (managed entity)
                    khachHang = khachHangDAO.timKhachHangTheoSoDienThoai(txtSoDienThoai.getText().trim());
                    // Cập nhật thông tin nếu cần
                    khachHangDAO.capNhatTenVaEmailKhachHangTheoSoDienThoai(
                            txtSoDienThoai.getText().trim(),
                            txtTen.getText().trim(),
                            txtEmail.getText().trim()
                    );
                    maKhachHang = khachHang.getMaKhachHang();
                } else {
                    // Tạo và lưu KhachHang mới
                    maKhachHang = idGeneratorService.getNextId("KhachHang");
                    khachHang = new KhachHang();
                    khachHang.setMaKhachHang(maKhachHang);
                    khachHang.setSoDienThoai(txtSoDienThoai.getText().trim());
                    khachHang.setTenKhachHang(txtTen.getText().trim());
                    khachHang.setEmail(txtEmail.getText().trim());
                    khachHangDAO.add(khachHang); // Lưu KhachHang
                    // Lấy lại managed entity để đảm bảo KhachHang đã được lưu
                    khachHang = khachHangDAO.timKhachHangTheoSoDienThoai(txtSoDienThoai.getText().trim());
                }

                // Cập nhật số lượng sản phẩm
                for (ChiTietHoaDon ct : danhSachChiTietSanPham) {
                    sanPhamDAO.giamSoLuongSanPham(String.valueOf(ct.getSanPham().getMaSanPham()), ct.getSoLuong());
                }

                // Tạo HoaDon
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(idGeneratorService.getNextId("HoaDon"));
                hd.setNgayDat(LocalDate.from(LocalDateTime.now()));
                hd.setSoGhe(danhSachGheDaChon.size());
                hd.setGhiChu(" ");
                hd.setKhachHang(khachHang); // KhachHang đã là managed entity
                hd.setNhanVien(nhanVienHienTai);
                hd.setKhuyenMai(km1);
                hd.setVAT(VAT);

                // Tạo danh sách ChiTietHoaDon
                Set<ChiTietHoaDon> danhSachCTHD = new HashSet<>();
                for (ChiTietHoaDon ct : danhSachChiTietSanPham) {
                    ct.setHoaDon(hd);
                    SanPham sanPham = sanPhamDAO.findById(ct.getSanPham().getMaSanPham()); // Đảm bảo SanPham là managed entity
                    ct.setSanPham(sanPham);
                    ct.setThanhTien(ct.getSoLuong() * sanPham.getGiaBan());
                    danhSachCTHD.add(ct);
                }
                hd.setDanhSachChiTietHD(danhSachCTHD);

                // Tạo danh sách Ve
                Set<Ve> danhSachVeMoi = new HashSet<>();
                for (Ghe ghe : danhSachGheDaChon) {
                    Ve ve = new Ve();
                    ve.setMaVe(idGeneratorService.getNextId("Ve"));
                    ve.setGhe(ghe);
                    ve.setHoaDon(hd);
                    ve.setNgayPhatHanh(LocalDate.now());
                    ve.setLichChieu(lichChieu);
                    danhSachVeMoi.add(ve);
                }
                hd.setDanhSachVe(danhSachVeMoi);
                hd.setTongTien(danhSachChiTietSanPham, danhSachGheDaChon, lichChieu);

                // Lưu HoaDon
                hoaDonDAO.add(hd);

                // Lưu ChiTietHoaDon
                for (ChiTietHoaDon ct : danhSachCTHD) {
                    ChiTietHoaDonPK pk = new ChiTietHoaDonPK(hd, ct.getSanPham());
                    ct.setId(pk);
                    chiTietDonHangDAO.add(ct);
                }

                // Lưu Ve
                for (Ve ve : danhSachVeMoi) {
                    veDao.add(ve);
                }

                // Thông báo thành công và tạo tài liệu
                JOptionPane.showMessageDialog(this, "Đơn hàng đã được thêm thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                for (Ve ve : danhSachVeMoi) {
                    TaoVe.taoVe(ve);
                }

                ArrayList<Ve> danhSachVe = new ArrayList<>();

                for (Ve ve: danhSachVeMoi) {
                    danhSachVe.add(ve);
                }
                TaoHoaDon.taoHD(danhSachChiTietSanPham, danhSachVe, hd);

                // Đóng dialog
                this.dispose();
                chonSanPhamDialog.dispose();
                chonSanPhamDialog.disposeChonGheDialog();

            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }
        });
        pnlThanhToan.add(pnlBtnThanhToan, BorderLayout.SOUTH);

        add(pnlChinh);
        setSize(1300, 800);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private boolean validateTen() {
        String ten = txtTen.getText().trim();
        if (ten.isEmpty()) {
            lblLoiTen.setText("Tên không được để trống");
            txtTen.requestFocus();
            return false;
        }
        if (!ten.matches("[aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTu UùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ0-9/-_ ]+")) {
            lblLoiTen.setText("Tên không chứa ký tự không hợp lệ");
            return false;
        }
        lblLoiTen.setText("*");
        return true;
    }

    
    private boolean validateEmail() {
        String email = txtEmail.getText().trim();
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            lblLoiEmail.setText("Email phải đúng định dạng");
            return false;
        }
        lblLoiEmail.setText("");
        return true;
    }

    
    private boolean validateSDT() throws RemoteException {
        String soDienThoai = txtSoDienThoai.getText().trim();
        if (soDienThoai.isEmpty()) {
            lblLoiSoDienThoai.setText("Số điện thoại không được để trống");
            txtSoDienThoai.requestFocus();
            return false;
        }
        if (!soDienThoai.matches("\\d{10}")) {
            lblLoiSoDienThoai.setText("Số điện thoại phải có 10 chữ số");
            return false;
        }
        lblLoiSoDienThoai.setText("*");
        KhachHang khachHang = khachHangDAO.timKhachHangTheoSoDienThoai(soDienThoai);
        if (khachHang != null) {
            txtTen.setText(khachHang.getTenKhachHang());
            txtEmail.setText(khachHang.getEmail());
        }
        return true;
    }


	/**
	 * @param nhanVienHienTai the nhanVienHienTai to set
	 */
	public void setNhanVienHienTai(NhanVien nhanVienHienTai) {
		this.nhanVienHienTai = nhanVienHienTai;
	}

	/**
	 * @param chonSanPhamDialog the chonSanPhamDialog to set
	 */
	public void setChonSanPhamDialog(ChonSanPhamDialog chonSanPhamDialog) {
		this.chonSanPhamDialog = chonSanPhamDialog;
	}

}
