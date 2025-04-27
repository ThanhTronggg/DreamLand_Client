/*
 * @(#) ChonGheGUI.java 1.0 Nov 10, 2024
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
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.formdev.flatlaf.FlatClientProperties;

import dao.GheDAO;
import dao.VeDAO;
import entity.Ghe;
import entity.LichChieu;
import entity.NhanVien;
import entity.Ve;
import iuh.fit.gui.app.Main;
import service.GheService;
import service.LichChieuService;
import service.VeService;

/**
 * @description:
 * @author: Thanh Trong
 * @date: Nov 10, 2024
 * @version: 1.0
 */

public class ChonGheDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private GheService gheDAO;
    private ArrayList<Ghe> danhSachGheDaChon;
    private JLabel thoiGianChieu;
    private JLabel phong;
    private JLabel giaVe;
    private JTextArea ghe;
    private JLabel tongTien;
    private double tongTienDouble;
    private NhanVien nhanVienHienTai;
    private QuanLyLichChieuGUI quanLyLichChieuGUI;
	private VeService veDao;
	private JButton btnGheTrong;
	private JButton btnGheDaDat;
	private JPanel pnlKhungChinh;
	private JPanel pnlKhungTrai;
	private JPanel pnlKhungPhai;
	private JPanel pnlkhungGhe;
	private JPanel pnlGhiChu;
	private JButton btnTiepTuc;
	private ArrayList<Ve> danhSachVe;

    /**
	 * @param quanLyLichChieuGUI the quanLyLichChieuGUI to set
	 */
	public void setQuanLyLichChieuGUI(QuanLyLichChieuGUI quanLyLichChieuGUI) {
		this.quanLyLichChieuGUI = quanLyLichChieuGUI;
	}

	public ChonGheDialog(LichChieu lichChieu) throws MalformedURLException, NotBoundException, RemoteException {
		
		setLayout(new BorderLayout());
		
		btnGheTrong = new JButton();
		btnGheTrong.setBackground(Color.white);
	    btnGheDaDat = new JButton();
	    btnGheDaDat.setBackground(Color.decode("#e0e0e0"));
		
        this.setSize(1700, 1000);
        
        veDao = (VeService) Naming.lookup("rmi://XXXXXX:9090/veService");
        gheDAO = (GheService) Naming.lookup("rmi://XXXXXX:9090/gheService");
        pnlKhungChinh = new JPanel();
        pnlKhungChinh.setLayout(new BorderLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        pnlKhungChinh.setSize(800, 400);
        setLocationRelativeTo(null);

        pnlKhungTrai = new JPanel();
        pnlKhungTrai.setLayout(new BorderLayout());
        
        JPanel pnlManHinh = new JPanel();
        pnlManHinh.setLayout(new BoxLayout(pnlManHinh, BoxLayout.Y_AXIS));
        
		JPanel linePanel = new JPanel();
		linePanel.setBackground(new Color(255, 165, 0));
		linePanel.setPreferredSize(new Dimension(pnlKhungTrai.getWidth(), 5));
		pnlManHinh.add(linePanel);
		pnlManHinh.add(Box.createVerticalStrut(10));
        
        JLabel lblManHinh = new JLabel("Màn hình", SwingConstants.CENTER);
		lblManHinh.setFont(new Font(lblManHinh.getFont().getFontName(), 1, 30));
		lblManHinh.setBackground(Color.white);
		lblManHinh.setForeground(Color.decode("#cccccf"));
		lblManHinh.setAlignmentX(CENTER_ALIGNMENT);
		pnlManHinh.add(lblManHinh);
		pnlManHinh.add(Box.createVerticalStrut(10));
		pnlKhungTrai.add(pnlManHinh, BorderLayout.NORTH);
		
        pnlkhungGhe = new JPanel();
        pnlkhungGhe.setLayout(new GridBagLayout());
        pnlKhungTrai.add(pnlkhungGhe);


        danhSachGheDaChon = new ArrayList<Ghe>();
        danhSachVe = veDao.getVeTheoLichChieu(lichChieu);
        ArrayList<Ghe> danhSachGhe = gheDAO.getDanhSachGheTheoPhong(lichChieu.getPhong());
        int i = 0;
        for (Ghe ghe : danhSachGhe) {
            JButton nutGhe = taoNutGhe(ghe, lichChieu, danhSachVe);

            gbc.gridx = i % 16;
            gbc.gridy = i / 16;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(5, 5, 5, 5);
            if (ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế đôi Sweetbox")) {
            	gbc.insets = new Insets(5, 0, 0, 0);
                gbc.gridwidth = 2;
            }

            pnlkhungGhe.add(nutGhe, gbc);
            if (ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế đôi Sweetbox")) {
                i += 2;
            } else {
                i += 1;
            }
        }
        
        JPanel pnlGhiChu2 = new JPanel();
        pnlGhiChu2.setLayout(new BoxLayout(pnlGhiChu2, BoxLayout.Y_AXIS));
        pnlGhiChu2.add(Box.createVerticalStrut(30));
        pnlGhiChu = taoKhungGhiChu();
        pnlGhiChu2.add(pnlGhiChu);
        pnlGhiChu2.add(Box.createVerticalStrut(20));
        pnlKhungTrai.add(pnlGhiChu2, BorderLayout.SOUTH);


        pnlKhungPhai = new JPanel();
        pnlKhungPhai.setLayout(new BoxLayout(pnlKhungPhai, BoxLayout.Y_AXIS));

        JPanel thongTinPhim = taoKhungThongTinPhim(lichChieu);
        pnlKhungPhai.add(thongTinPhim);

        btnTiepTuc = new JButton("Tiếp tục");
        btnTiepTuc.putClientProperty(FlatClientProperties.STYLE, "arc:5;hoverBackground:$primary;hoverForeground:$clr-white");
        btnTiepTuc.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTiepTuc.addActionListener(e -> {
            try {
                xuLyNutTiepTuc(lichChieu);
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            } catch (NotBoundException ex) {
                throw new RuntimeException(ex);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });
        pnlKhungPhai.add(btnTiepTuc);
        pnlKhungPhai.add(Box.createVerticalStrut(20));
        
        JScrollPane scroll = new JScrollPane(pnlKhungPhai);
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        pnlKhungChinh.add(pnlKhungTrai, BorderLayout.CENTER);
        pnlKhungChinh.add(scroll, BorderLayout.EAST);
        add(pnlKhungChinh);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        pnlKhungChinh.revalidate();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resetGlassPane();
            }
        });
    }

	private JButton taoNutGhe(Ghe ghe, LichChieu lichChieu, ArrayList<Ve> danhSachVe) {
	    JButton nut;
	    boolean gheDaCoVe = false;
	    for (Ve ve : danhSachVe) {
	        if (String.valueOf(ghe.getMaGhe()).equals(String.valueOf(ve.getGhe().getMaGhe()))) {
	            gheDaCoVe = true;
	            break;
	        }
	    }

	    if (gheDaCoVe) {
	        nut = new JButton(ghe.getViTri());
	        nut.setOpaque(true);
	        nut.setFont(new Font(nut.getFont().getFontName(), nut.getFont().getStyle(), 10));
	        nut.setBackground(btnGheDaDat.getBackground());
//	        nut.setForeground(Color.white);
	        if(ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế đôi Sweetbox")) {
	        	nut.setPreferredSize(new Dimension(80, 40));
        	}
        	if(ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế VIP")) {
        		nut.setPreferredSize(new Dimension(40, 40));
        	}
        	if(ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế thường")) {
        		nut.setPreferredSize(new Dimension(40, 40));
        	}
	        nut.setEnabled(false);
	    } else {
	        nut = new JButton(ghe.getViTri());
	        nut.setFont(new Font(nut.getFont().getFontName(), nut.getFont().getStyle(), 10));
	        nut.setBackground(btnGheTrong.getBackground());
	        nut.setForeground(btnGheTrong.getForeground());
	        if (ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế thường")) {
	            nut.setPreferredSize(new Dimension(40, 40));
	            nut.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
	        }
	        if (ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế VIP")) {
	            nut.setPreferredSize(new Dimension(40, 40));
	            nut.setBorder(BorderFactory.createLineBorder(Color.decode("#ffe716"), 2, true));
	        }
	        if (ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế đôi Sweetbox")) {
	            nut.setPreferredSize(new Dimension(80, 40));
	            nut.setBorder(BorderFactory.createLineBorder(Color.decode("#ffc2e6"), 2, true));
	        }

	        boolean[] daChon = {false};
	        nut.addActionListener(e -> {
	            if (daChon[0]) {
	                danhSachGheDaChon.remove(ghe);
	                nut.setBackground(btnGheTrong.getBackground());
	                nut.setForeground(btnGheTrong.getForeground());
	                if (ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế đôi Sweetbox")) {
	                    nut.setBorder(BorderFactory.createLineBorder(Color.decode("#ffc2e6"), 2, true));
	                }
	                if (ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế thường")) {
	                    nut.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
	                }
	                if (ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế VIP")) {
	                    nut.setBorder(BorderFactory.createLineBorder(Color.decode("#ffe716"), 2, true));
	                }
	            } else {
	                danhSachGheDaChon.add(ghe);
	                nut.setBackground(Color.decode("#f58020"));
	                nut.setForeground(Color.white);
	                nut.setBorder(BorderFactory.createLineBorder(Color.decode("#f58020"), 2, true));
	            }
	            daChon[0] = !daChon[0];
	            capNhatChonGhe(lichChieu);
	        });
	    }
	    return nut;
	}


    private JPanel taoKhungGhiChu() {
        JPanel pnlGhiChu = new JPanel();
        pnlGhiChu.setLayout(new BoxLayout(pnlGhiChu, BoxLayout.X_AXIS));
        
        JLabel lblGheTrong = new JLabel("Ghế đang chọn");
        JButton btnTrong = new JButton();
        btnTrong.setPreferredSize(new Dimension(40, 40));
        btnTrong.setMinimumSize(new Dimension(40, 40));
        btnTrong.setMaximumSize(new Dimension(40, 40));
        btnTrong.setBackground(Color.decode("#f58020"));
        btnTrong.setAlignmentY(JButton.CENTER_ALIGNMENT);

        JLabel lblDaDat = new JLabel("Ghế đã bán");
        JButton btnDaDat = new JButton();
        btnDaDat.setPreferredSize(new Dimension(40, 40));
        btnDaDat.setMinimumSize(new Dimension(40, 40));
        btnDaDat.setMaximumSize(new Dimension(40, 40));
        btnDaDat.setOpaque(true);
        btnDaDat.setBackground(btnGheDaDat.getBackground());
        btnDaDat.setAlignmentY(JButton.CENTER_ALIGNMENT);

        JLabel lblGheThuong = new JLabel("Ghế thường");
        JButton btnGheThuong = new JButton();
        btnGheThuong.setPreferredSize(new Dimension(40, 40));
        btnGheThuong.setMinimumSize(new Dimension(40, 40));
        btnGheThuong.setMaximumSize(new Dimension(40, 40));
        btnGheThuong.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        btnGheThuong.setBackground(btnGheTrong.getBackground());
        btnGheThuong.setAlignmentY(JButton.CENTER_ALIGNMENT);
        
        JLabel lblGheVIP = new JLabel("Ghế VIP");
        JButton btnGheVIP = new JButton();
        btnGheVIP.setPreferredSize(new Dimension(40, 40));
        btnGheVIP.setMinimumSize(new Dimension(40, 40));
        btnGheVIP.setMaximumSize(new Dimension(40, 40));
        btnGheVIP.setBorder(BorderFactory.createLineBorder(Color.decode("#f3cb52"), 2, true));
        btnGheVIP.setBackground(btnGheTrong.getBackground());
        btnGheVIP.setAlignmentY(JButton.CENTER_ALIGNMENT);
        
        JLabel lblGheDoi = new JLabel("Ghế đôi SweetBox");
        JButton btnGheDoi = new JButton();
        btnGheDoi.setPreferredSize(new Dimension(80, 40));
        btnGheDoi.setMinimumSize(new Dimension(80, 40));
        btnGheDoi.setMaximumSize(new Dimension(80, 40));
        btnGheDoi.setBorder(BorderFactory.createLineBorder(Color.decode("#ffc2e6"), 2, true));
        btnGheDoi.setBackground(btnGheTrong.getBackground());
        btnGheDoi.setAlignmentY(JButton.CENTER_ALIGNMENT);

        pnlGhiChu.add(Box.createHorizontalStrut(20));
        pnlGhiChu.add(lblGheTrong);
        pnlGhiChu.add(Box.createHorizontalStrut(5));
        pnlGhiChu.add(btnTrong);
        pnlGhiChu.add(Box.createHorizontalStrut(20));
        pnlGhiChu.add(lblDaDat);
        pnlGhiChu.add(Box.createHorizontalStrut(5));
        pnlGhiChu.add(btnDaDat);
        pnlGhiChu.add(Box.createHorizontalStrut(50));
        pnlGhiChu.add(lblGheThuong);
        pnlGhiChu.add(Box.createHorizontalStrut(5));
        pnlGhiChu.add(btnGheThuong);
        pnlGhiChu.add(Box.createHorizontalStrut(20));
        pnlGhiChu.add(lblGheVIP);
        pnlGhiChu.add(Box.createHorizontalStrut(5));
        pnlGhiChu.add(btnGheVIP);
        pnlGhiChu.add(Box.createHorizontalStrut(20));
        pnlGhiChu.add(lblGheDoi);
        pnlGhiChu.add(Box.createHorizontalStrut(5));
        pnlGhiChu.add(btnGheDoi);
        
        return pnlGhiChu;
    }


    private JPanel taoKhungThongTinPhim(LichChieu lichChieu) {
        JPanel pnlPhim = new JPanel();
        pnlPhim.setLayout(new BoxLayout(pnlPhim, BoxLayout.Y_AXIS));
        pnlPhim.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ImageIcon icon = new ImageIcon(lichChieu.getPhim().getAnh());
        Image img = icon.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH);
        JLabel lblAnh = new JLabel(new ImageIcon(img));
        lblAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlPhim.add(lblAnh);

        JPanel pnlTenPhim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTenPhim.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel lblTenPhim = new JLabel(lichChieu.getPhim().getTenPhim());
        lblTenPhim.setAlignmentX(CENTER_ALIGNMENT);
        lblTenPhim.setFont(new Font(lblTenPhim.getFont().getFontName(), 1, 20));
        pnlTenPhim.add(lblTenPhim);
        
        JPanel pnlNgayChieu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlNgayChieu.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        JLabel ngayChieu = new JLabel(lichChieu.getGioBatDau().format(dateFormatter));
        pnlNgayChieu.add(new JLabel("Ngày chiếu: "));
        pnlNgayChieu.add(ngayChieu);

        JPanel pnlThoiGianChieu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlThoiGianChieu.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        thoiGianChieu = new JLabel(lichChieu.getGioBatDau().format(DateTimeFormatter.ofPattern("HH:mm")));
        pnlThoiGianChieu.add(new JLabel("Giờ chiếu: "));
        pnlThoiGianChieu.add(thoiGianChieu);

        JPanel pnlPhong = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlPhong.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        phong = new JLabel(lichChieu.getPhong().getTenPhong());
        pnlPhong.add(new JLabel("Phòng: "));
        pnlPhong.add(phong);

        JPanel pnlGiaVe = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlGiaVe.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        giaVe = new JLabel(lichChieu.getGiaMotGhe() + " VND");
        pnlGiaVe.add(new JLabel("Giá vé: "));
        pnlGiaVe.add(giaVe);

        JPanel pnlGhe = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlGhe.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        ghe = new JTextArea(1, 10);
        ghe.setLineWrap(true);
        ghe.setWrapStyleWord(true);
        ghe.setEditable(false);
        ghe.setOpaque(false);
        pnlGhe.add(new JLabel("Ghế: "));
        pnlGhe.add(ghe);

        JPanel pnlTongTien = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTongTien.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        tongTien = new JLabel("0 VND");
        pnlTongTien.add(new JLabel("Tổng: "));
        pnlTongTien.add(tongTien);

        pnlPhim.add(pnlTenPhim);
        pnlPhim.add(pnlNgayChieu);
        pnlPhim.add(pnlThoiGianChieu);
        pnlPhim.add(pnlPhong);
        pnlPhim.add(pnlGiaVe);
        pnlPhim.add(pnlGhe);
        pnlPhim.add(pnlTongTien);

        return pnlPhim;
    }


    private void capNhatChonGhe(LichChieu lichChieu) {
        StringBuilder chuoiGhe = new StringBuilder();
        tongTienDouble = 0;
        for (Ghe ghe : danhSachGheDaChon) {
        	if(ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế đôi Sweetbox")) {
        		tongTienDouble += lichChieu.getGiaMotGhe()*2;
        	}
        	if(ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế VIP")) {
        		tongTienDouble += lichChieu.getGiaMotGhe()*1.5;
        	}
        	if(ghe.getLoaiGhe().getTenLoaiGhe().equals("Ghế thường")) {
        		tongTienDouble += lichChieu.getGiaMotGhe();
        	}
            chuoiGhe.append(ghe.getViTri()).append(", ");
        }
        if (chuoiGhe.length() > 0) {
            chuoiGhe.setLength(chuoiGhe.length() - 2);
        }
        ghe.setText(chuoiGhe.toString());
        tongTien.setText(new DecimalFormat("#").format(tongTienDouble)+ " VND");
    }

    private void xuLyNutTiepTuc(LichChieu lichChieu) throws MalformedURLException, NotBoundException, RemoteException {
        if (!danhSachGheDaChon.isEmpty()) {
            ChonSanPhamDialog dialogSanPham = new ChonSanPhamDialog(danhSachGheDaChon, lichChieu);
            dialogSanPham.setNhanVienHienTai(nhanVienHienTai);
//            System.out.println(nhanVienHienTai);
            dialogSanPham.setChonGheDialog(this);
            dialogSanPham.setModal(true);
            dialogSanPham.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Hãy chọn ít nhất một ghế để tiếp tục!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void resetGlassPane() {
        JPanel glassPaneMacDinh = (JPanel) Main.getInstance().getGlassPane();
        glassPaneMacDinh.removeAll();
        Main.getInstance().setGlassPane(glassPaneMacDinh);
        glassPaneMacDinh.setVisible(false);
    }

    public void setNhanVienHienTai(NhanVien nhanVien) {
        this.nhanVienHienTai = nhanVien;
    }
}
