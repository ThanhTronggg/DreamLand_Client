/*
 * @(#) ChonSanPhamDialog.java 1.0 Nov 11, 2024
 * Copyright (c) 2024 IUH.
 * All rights reserved.
 */
package iuh.fit.gui.app.lichchieu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

import com.formdev.flatlaf.FlatClientProperties;

import dao.SanPhamDAO;
import entity.ChiTietHoaDon;
import entity.Ghe;
import entity.LichChieu;
import entity.NhanVien;
import entity.SanPham;
import entity.Ve;
import service.SanPhamService;
import service.VeService;

/**
 * @description:
 * @author: Thanh Trong
 * @date: Nov 11, 2024
 * @version: 1.0
 */

public class ChonSanPhamDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel pnlChinh, pnlTrai, pnlPhai, pnlDanhMuc, pnlSanPham, pnlThanhToan, pnlSanPhamDaChon, pnlTongTien, pnlTiepTuc;
    private JButton btnTatCa, btnDoAn, btnThucUong, btnXoaTatCa, btnTiepTuc;
    private JLabel lblThanhToan, lblTongTien, lblGiaTriTongTien;
    private ArrayList<ChiTietHoaDon> danhSachChiTietDatHang;
    private SanPhamService sanPhamDAO;
    private boolean sanPhamDaTonTai;
    private NhanVien nhanVienHienTai;
    private ChonGheDialog chonGheDialog;
    private String kieuNhomButton, kieuButtonBinhThuong;
	private GridBagConstraints gbc;

	public ChonSanPhamDialog(ArrayList<Ghe> danhSachGheDaChon, LichChieu lichChieu) throws MalformedURLException, NotBoundException, RemoteException {

        danhSachChiTietDatHang = new ArrayList<ChiTietHoaDon>();
        sanPhamDAO = (SanPhamService) Naming.lookup("rmi://XXXXXX:9090/sanPhamService");
        
        pnlChinh = new JPanel(new BorderLayout());
        
        taoPanelTrai();
        taoPanelPhai(danhSachGheDaChon, lichChieu);

        pnlChinh.add(pnlTrai, BorderLayout.CENTER);
        pnlChinh.add(pnlPhai, BorderLayout.EAST);

        add(pnlChinh);
        setSize(1300, 800);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(ChonSanPhamDialog.this,
                        "Bạn có chắc chắn muốn hủy các thay đổi?", "Cảnh báo", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                } else {
                    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    private void taoPanelTrai() throws RemoteException {
        pnlTrai = new JPanel(new BorderLayout());
        pnlTrai.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.gray));

        pnlDanhMuc = new JPanel();
        btnTatCa = taoButtonDanhMuc("Tất cả");
        btnDoAn = taoButtonDanhMuc("Đồ ăn");
        btnThucUong = taoButtonDanhMuc("Thức uống");

        pnlDanhMuc.add(btnTatCa);
        pnlDanhMuc.add(btnDoAn);
        pnlDanhMuc.add(btnThucUong);

        pnlTrai.add(pnlDanhMuc, BorderLayout.NORTH);

        pnlSanPham = new JPanel();
        pnlSanPham.setLayout(new GridBagLayout());
        JScrollPane scrSanPham = new JScrollPane(pnlSanPham);
        scrSanPham.setBorder(null);

        gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        pnlTrai.add(scrSanPham, BorderLayout.CENTER);

        hienThiSanPham((ArrayList<SanPham>) sanPhamDAO.getAll());
    }

    private void taoPanelPhai(ArrayList<Ghe> danhSachGheDaChon, LichChieu lichChieu) {
        pnlPhai = new JPanel(new BorderLayout());
        pnlPhai.setPreferredSize(new Dimension(300, 800));

        pnlThanhToan = new JPanel();
        
        btnXoaTatCa = new JButton("Xóa tất cả");
        btnXoaTatCa.addActionListener(e -> xoaTatCaSanPhamDaChon());

        pnlThanhToan.add(btnXoaTatCa);

        pnlPhai.add(pnlThanhToan, BorderLayout.NORTH);

        pnlSanPhamDaChon = new JPanel();
        pnlSanPhamDaChon.setLayout(new GridLayout(20, 0, 5, 5));
        JScrollPane scrSanPhamDaChon = new JScrollPane(pnlSanPhamDaChon);
        scrSanPhamDaChon.setBorder(BorderFactory.createEmptyBorder());

        pnlPhai.add(scrSanPhamDaChon, BorderLayout.CENTER);
        


        pnlTiepTuc = new JPanel();
        pnlTiepTuc.setLayout(new BoxLayout(pnlTiepTuc, BoxLayout.Y_AXIS));
        pnlTongTien = new JPanel();
        lblTongTien = new JLabel("Tổng tiền: ");
//        lblTongTien.setFont(new Font(lblTongTien.getFont().getFontName(), 1, 30));
        lblTongTien.putClientProperty(FlatClientProperties.STYLE, "font:$h5.font;foreground:$danger;");
        
        lblGiaTriTongTien = new JLabel("0 VND");
        lblGiaTriTongTien.putClientProperty(FlatClientProperties.STYLE, "font:$h5.font;foreground:$danger;");
        pnlTongTien.add(lblTongTien);
        pnlTongTien.add(lblGiaTriTongTien);
        pnlTiepTuc.add(pnlTongTien);
        
        btnTiepTuc = new JButton("Tiếp tục");
        btnTiepTuc.setAlignmentX(CENTER_ALIGNMENT);
        btnTiepTuc.addActionListener(e -> moCuaSoThanhToan(danhSachGheDaChon, lichChieu));
        pnlTiepTuc.add(btnTiepTuc);
        pnlPhai.add(pnlTiepTuc, BorderLayout.SOUTH);
    }

    private void hienThiSanPham(ArrayList<SanPham> danhSachSanPham) {
        pnlSanPham.removeAll();
        int cols = 3;
        int i = 0;
        for (SanPham sanPham : danhSachSanPham) {
            if (sanPham.getSoLuong() > 0) {
                JButton btnSanPham = taoButtonSanPham(sanPham);
                int j = i % cols;
                int row = i / cols;
                gbc.gridx = j;
                gbc.gridy = row;
                pnlSanPham.add(btnSanPham, gbc);
                i++;
            }
        }
        revalidate();
        repaint();
    }

    private JButton taoButtonSanPham(SanPham sanPham) {
        JButton btnSanPham = new JButton();
        btnSanPham.setPreferredSize(new Dimension(300, 280));
        btnSanPham.setLayout(new BoxLayout(btnSanPham, BoxLayout.Y_AXIS));
       
        ImageIcon icon = new ImageIcon(sanPham.getAnh());
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(180, 180, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImg);

        JLabel lblHinhAnh = new JLabel(resizedIcon);
        lblHinhAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTenSanPham = new JLabel(sanPham.getTenSanPham());
        lblTenSanPham.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblTenSanPham.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font;");
        JLabel lblSoLuong = new JLabel("Số lượng: " + sanPham.getSoLuong());
        lblSoLuong.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblGia = new JLabel("Giá: " + sanPham.getGiaBan() + " VND");
        lblGia.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblGia.putClientProperty(FlatClientProperties.STYLE, "font:$h5.font;foreground:$primary;");

        btnSanPham.add(lblHinhAnh);
        btnSanPham.add(lblTenSanPham);
        btnSanPham.add(lblSoLuong);
        btnSanPham.add(lblGia);

        btnSanPham.putClientProperty(FlatClientProperties.STYLE, "default.background:$white;");
        btnSanPham.addActionListener(e -> themSanPhamDaChon(sanPham));
        
        return btnSanPham;
    }


    private void themSanPhamDaChon(SanPham sanPham) {
        sanPhamDaTonTai = false;
        for (ChiTietHoaDon chiTietDatHang : danhSachChiTietDatHang) {
            if (chiTietDatHang.getSanPham().equals(sanPham)) {
                chiTietDatHang.setSoLuong(chiTietDatHang.getSoLuong() + 1);
                sanPhamDaTonTai = true;
                hienThiSanPhamDaChon();
                break;
            }
        }
        if (!sanPhamDaTonTai) {
            danhSachChiTietDatHang.add(new ChiTietHoaDon(1, null, sanPham));
            hienThiSanPhamDaChon();
        }
    }

    private void hienThiSanPhamDaChon() {
        pnlSanPhamDaChon.removeAll();
        double tongTien = 0;
        for (ChiTietHoaDon chiTiet : danhSachChiTietDatHang) {
            tongTien += chiTiet.getSanPham().getGiaBan() * chiTiet.getSoLuong();
            pnlSanPhamDaChon.add(taoCardSanPhamDaChon(chiTiet));
        }
        lblGiaTriTongTien.setText(new DecimalFormat("#").format(tongTien)+" VND");
        revalidate();
        repaint();
    }

    private JPanel taoCardSanPhamDaChon(ChiTietHoaDon chiTiet) {
        JPanel pnlCardSanPham = new JPanel(new BorderLayout());
		ImageIcon icon = new ImageIcon(chiTiet.getSanPham().getAnh());
		Image img = icon.getImage();
		Image resizedImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImg);
        JLabel lblHinhAnhSanPham = new JLabel(resizedIcon);
        JPanel pnlThongTin = new JPanel();
        pnlThongTin.setLayout(new BoxLayout(pnlThongTin, BoxLayout.Y_AXIS));
        JLabel lblTenSanPham = new JLabel(chiTiet.getSanPham().getTenSanPham());
        pnlThongTin.add(lblTenSanPham);
        pnlThongTin.add(new JLabel("Giá: " + chiTiet.getSanPham().getGiaBan() + "VND"));

        JSpinner spnSoLuong = new JSpinner(new SpinnerNumberModel(chiTiet.getSoLuong(), 1, chiTiet.getSanPham().getSoLuong(), 1));
        spnSoLuong.setOpaque(true);
        spnSoLuong.setBorder(null);
        spnSoLuong.setBackground(new Color(250,250,250));
        spnSoLuong.addChangeListener(e -> {
            chiTiet.setSoLuong((int) spnSoLuong.getValue());
            hienThiSanPhamDaChon();
        });
        
        JPanel pnlXoa = new JPanel();

        JButton btnXoa = new JButton("Xóa");
//        btnXoa.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnXoa.addActionListener(e -> {
            danhSachChiTietDatHang.remove(chiTiet);
            hienThiSanPhamDaChon();
        });
        pnlXoa.add(btnXoa);
        pnlCardSanPham.add(lblHinhAnhSanPham, BorderLayout.WEST);
        pnlCardSanPham.add(pnlThongTin, BorderLayout.CENTER);
        pnlCardSanPham.add(spnSoLuong, BorderLayout.EAST);
        pnlCardSanPham.add(pnlXoa, BorderLayout.SOUTH);

        return pnlCardSanPham;
    }

    private void moCuaSoThanhToan(ArrayList<Ghe> danhSachGheDaChon, LichChieu lichChieu) {
        new Thread(() -> {
            ThanhToanDialog checkoutDialog = null;
            try {
                checkoutDialog = new ThanhToanDialog(danhSachGheDaChon, danhSachChiTietDatHang, lichChieu);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            checkoutDialog.setNhanVienHienTai(nhanVienHienTai);
//            System.out.println(nhanVienHienTai);
            checkoutDialog.setChonSanPhamDialog(this);
            checkoutDialog.setModal(true);
            checkoutDialog.setVisible(true);
        }).start();
    }

    private void xoaTatCaSanPhamDaChon() {
        danhSachChiTietDatHang.clear();
        hienThiSanPhamDaChon();
    }

    private JButton taoButtonDanhMuc(String ten) {
        JButton btn = new JButton(ten);
        btn.putClientProperty(FlatClientProperties.STYLE, ten.equals("Tất cả") ? kieuNhomButton : kieuButtonBinhThuong);
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<SanPham> sanPhamList;
        if (e.getSource() == btnTatCa) {
            try {
                sanPhamList = (ArrayList<SanPham>) sanPhamDAO.getAll();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == btnDoAn) {
            try {
                sanPhamList = sanPhamDAO.getSanPhamTheoLoaiSP("Thức ăn");
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            try {
                sanPhamList = sanPhamDAO.getSanPhamTheoLoaiSP("Nước uống");
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
        hienThiSanPham(sanPhamList);
    }

	/**
	 * @param nhanVienHienTai the nhanVienHienTai to set
	 */
	public void setNhanVienHienTai(NhanVien nhanVienHienTai) {
		this.nhanVienHienTai = nhanVienHienTai;
	}

	/**
	 * @param chonGheDialog the chonGheDialog to set
	 */
	public void setChonGheDialog(ChonGheDialog chonGheDialog) {
		this.chonGheDialog = chonGheDialog;
	}
	
	public void disposeChonGheDialog() {
		chonGheDialog.dispose();
	}
    
}
