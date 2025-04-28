/*
 * @(#) SuaKhuyenMaiDialog.java 1.0 Nov 4, 2024
 * Copyright (c) 2024 IUH.
 * All rights reserved.
 */
package iuh.fit.gui.app.khuyenmai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;

import entity.KhuyenMai;
import iuh.fit.gui.app.EnvironmentVariable;
import service.KhuyenMaiService;
import service.VeService;

/**
 * @description:
 * @author: Thanh Trong
 * @date: Nov 4, 2024
 * @version: 1.0
 */

public class SuaKhuyenMaiDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JTextField txtTenKM;
	private JTextField txtNgayBD;
	private JTextField txtNgayKT;
	private JTextField txtTongTien;
	private JTextField txtPhanTram;
	private JButton btnThoat;
	private KhuyenMaiService kmDao;
	private QuanLyKhuyenMaiGUI quanLyKhuyenMaiGUI;
	private JButton btnSua;
	private KhuyenMai khuyenMai;
	private DateChooser chonNgayBD;
	private JButton nutChonNgayBD;
	private DateChooser chonNgayKT;
	private JButton nutChonNgayKT;

    public SuaKhuyenMaiDialog(KhuyenMai khuyenMai) throws MalformedURLException, NotBoundException, RemoteException {
        this.khuyenMai = khuyenMai;
        
        //frame
        setSize(700, 500);
        setTitle("Sửa khuyến mãi");
        setLocationRelativeTo(null);
        setResizable(false);
        
        //add component
        JPanel pnlNor = new JPanel();
        JLabel lblKhuyenMai = new JLabel("SỬA KHUYẾN MÃI");
        pnlNor.add(lblKhuyenMai);
        lblKhuyenMai.setAlignmentX(CENTER_ALIGNMENT);
        lblKhuyenMai.setFont(new Font(lblKhuyenMai.getFont().getFontName(), 1, 26));
        pnlNor.setLayout(new BoxLayout(pnlNor, BoxLayout.Y_AXIS));
        pnlNor.add(Box.createVerticalStrut(20));
        add(pnlNor, BorderLayout.NORTH);
        
        JPanel pnlCen = new JPanel();
        pnlCen.setLayout(new BoxLayout(pnlCen, BoxLayout.Y_AXIS));
        add(pnlCen);
        
        JLabel lblTenKM = new JLabel("Tên khuyến mãi");
        JLabel lblNgayBD = new JLabel("Ngày bắt đầu khuyến mãi ");
        JLabel lblNgayKT = new JLabel("Ngày kết thúc khuyến mãi");
        JLabel lblTongTien = new JLabel("Tổng tiền hóa đơn tối thiểu");
        JLabel lblPhanTram = new JLabel("Phần trăm khuyến mãi");
        
        Dimension labelSize = new Dimension(200, lblTongTien.getPreferredSize().height);
        lblTenKM.setPreferredSize(labelSize);
        lblNgayBD.setPreferredSize(labelSize);
        lblNgayKT.setPreferredSize(labelSize);
        lblTongTien.setPreferredSize(labelSize);
        lblPhanTram.setPreferredSize(labelSize);
        
        JPanel pnlRow1 = new JPanel();
        pnlRow1.add(Box.createHorizontalStrut(20));
        pnlRow1.setLayout(new BoxLayout(pnlRow1, BoxLayout.X_AXIS));
        pnlRow1.add(lblTenKM);
        txtTenKM = new JTextField(30);
        pnlRow1.add(txtTenKM);
        pnlRow1.add(Box.createHorizontalStrut(20));
        pnlCen.add(pnlRow1);
        pnlCen.add(Box.createVerticalStrut(20));
        
        JPanel pnlRow2 = new JPanel();
        pnlRow2.add(Box.createHorizontalStrut(20));
        pnlRow2.setLayout(new BoxLayout(pnlRow2, BoxLayout.X_AXIS));
        pnlRow2.add(lblNgayBD);
        txtNgayBD = new JTextField(20);
		chonNgayBD = new DateChooser();
		nutChonNgayBD = new JButton();
		pnlRow2.add(txtNgayBD);
		pnlRow2.add(nutChonNgayBD);
        pnlRow2.add(Box.createHorizontalStrut(20));
        pnlCen.add(pnlRow2);
        pnlCen.add(Box.createVerticalStrut(20));
        
        JPanel pnlRow3 = new JPanel();
        pnlRow3.add(Box.createHorizontalStrut(20));
        pnlRow3.setLayout(new BoxLayout(pnlRow3, BoxLayout.X_AXIS));
        pnlRow3.add(lblNgayKT);
        txtNgayKT = new JTextField(10);
		chonNgayKT = new DateChooser();
		nutChonNgayKT = new JButton();
		pnlRow3.add(txtNgayKT);
		pnlRow3.add(nutChonNgayKT);
        pnlRow3.add(Box.createHorizontalStrut(20));
        pnlCen.add(pnlRow3);
        pnlCen.add(Box.createVerticalStrut(20));
        
        JPanel pnlRow4 = new JPanel();
        pnlRow4.add(Box.createHorizontalStrut(20));
        pnlRow4.setLayout(new BoxLayout(pnlRow4, BoxLayout.X_AXIS));
        pnlRow4.add(lblTongTien);
        txtTongTien = new JTextField(20);
        pnlRow4.add(txtTongTien);
        pnlRow4.add(Box.createHorizontalStrut(20));
        pnlCen.add(pnlRow4);
        pnlCen.add(Box.createVerticalStrut(20));
        
        JPanel pnlRow5 = new JPanel();
        pnlRow5.add(Box.createHorizontalStrut(20));
        pnlRow5.setLayout(new BoxLayout(pnlRow5, BoxLayout.X_AXIS));
        pnlRow5.add(lblPhanTram);
        txtPhanTram = new JTextField(20);
        pnlRow5.add(txtPhanTram);
        pnlRow5.add(Box.createHorizontalStrut(20));
        pnlCen.add(pnlRow5);
        pnlCen.add(Box.createVerticalStrut(20));
        
        JPanel pnlRow6 = new JPanel();
        pnlRow6.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnSua = new JButton("Sửa");
        btnSua.setBackground(Color.decode("#273167"));
        btnSua.setForeground(Color.white);
        pnlRow6.add(btnSua);
        btnThoat = new JButton("Hủy bỏ");
        btnThoat.setBackground(Color.decode("#ff4f4f"));
        btnThoat.setForeground(Color.white);
        pnlRow6.add(btnThoat);
        pnlCen.add(pnlRow6);
        
		FlatSVGIcon icon = new FlatSVGIcon("images/svg/calendar.svg", 18, 18);
		nutChonNgayBD.setIcon(icon);
		nutChonNgayBD.addActionListener(e -> {
			chonNgayBD.showPopup();
		});
		chonNgayBD.setTextRefernce(txtNgayBD);
		chonNgayBD.addEventDateChooser(new EventDateChooser() {
			@Override
			public void dateSelected(SelectedAction action, SelectedDate date) {
				System.out.println(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
				if (action.getAction() == SelectedAction.DAY_SELECTED) {
					chonNgayBD.hidePopup();
				}
			}
		});
		nutChonNgayKT.setIcon(icon);
		nutChonNgayKT.addActionListener(e -> {
			chonNgayKT.showPopup();
		});
		chonNgayKT.setTextRefernce(txtNgayKT);
		chonNgayKT.addEventDateChooser(new EventDateChooser() {
			@Override
			public void dateSelected(SelectedAction action, SelectedDate date) {
				System.out.println(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
				if (action.getAction() == SelectedAction.DAY_SELECTED) {
					chonNgayKT.hidePopup();
				}
			}
		});
        
		txtTenKM.addActionListener(this);
		txtNgayBD.addActionListener(this);
		txtNgayKT.addActionListener(this);
		txtTongTien.addActionListener(this);
		txtPhanTram.addActionListener(this);
        
        btnSua.addActionListener(this);
        btnThoat.addActionListener(this);
        
        kmDao = (KhuyenMaiService) Naming.lookup("rmi://"+ EnvironmentVariable.IP.getValue()+":"+Integer.parseInt(EnvironmentVariable.PORT_SERVER.getValue())+"/khuyenMaiService");

        loadKhuyenMaiData();
    }

	private void loadKhuyenMaiData() {
		if (khuyenMai != null) {
            txtTenKM.setText(khuyenMai.getTenKhuyenMai());
            txtNgayBD.setText(khuyenMai.getNgayBatDau().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            txtNgayKT.setText(khuyenMai.getNgayKetThuc().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            txtTongTien.setText(String.valueOf(khuyenMai.getTongHoaDonToiThieu()));
            txtPhanTram.setText(String.valueOf(khuyenMai.getPhanTramKhuyenMai() * 100));
        }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(btnSua) || o.equals(txtTenKM)|| o.equals(txtNgayBD)|| o.equals(txtNgayKT)|| o.equals(txtTongTien) || o.equals(txtPhanTram)) {
            try {
                capNhatKhuyenMai();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }

        }
		if(o.equals(btnThoat)) {
			UIManager.put("OptionPane.yesButtonText", "Có");
	        UIManager.put("OptionPane.noButtonText", "Không");
	        UIManager.put("Button.background", Color.decode("#273167"));
	        UIManager.put("Button.foreground", Color.WHITE);
			if(JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát? \nMọi thay đổi sẽ không được lưu.",
					"Chú ý", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_NO_OPTION) {
				this.dispose();
			}
			UIManager.put("Button.background", Color.white);
			UIManager.put("Button.foreground", Color.black);
		}
		
	}
	private void capNhatKhuyenMai() throws RemoteException {
	    if (kiemTraDieuKien()) {
	    	System.out.println(khuyenMai);
	        if (kmDao.update(khuyenMai)) {
	            JOptionPane.showMessageDialog(this, "Sửa khuyến mãi thành công!", "Thông báo", JOptionPane.PLAIN_MESSAGE);
	            this.dispose();
	        } else {
	            JOptionPane.showMessageDialog(this, "Sửa khuyến mãi không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
	
	private boolean kiemTraDieuKien() {
		String ngayBD = txtNgayBD.getText();
		String ngayKT = txtNgayKT.getText();
		double phanTram;
		double tongTien;
		
		String ten = txtTenKM.getText();
		if(!ten.matches("[aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTu UùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ0-9/-_ ]+")) {
			JOptionPane.showMessageDialog(this, "Tên khuyến mãi có thể chứa kí tự thường, in hoa hoặc ký tự số");
			txtTenKM.selectAll();
			txtTenKM.requestFocus();
			return false;
		}
		
		try {
			tongTien = Double.parseDouble(txtTongTien.getText());
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Tổng tiền tối thiếu của hóa đơn phải là 1 số");
			txtTongTien.selectAll();
			txtTongTien.requestFocus();
			return false;
		}
		tongTien = Double.parseDouble(txtTongTien.getText());
		if(tongTien < 0) {
			JOptionPane.showMessageDialog(this, "Tổng tiền tối thiểu phải lớn hơn hoặc bằng 0");
			txtTongTien.requestFocus();
			txtTongTien.selectAll();
			return false;
		}
		
		try {
			phanTram = Double.parseDouble(txtPhanTram.getText());
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Phần trăm giảm giá phải là một số");
			txtPhanTram.selectAll();
			txtPhanTram.requestFocus();
			return false;
		}
		phanTram = Double.parseDouble(txtPhanTram.getText());
		if(phanTram < 0 || phanTram > 100) {
			JOptionPane.showMessageDialog(this, "Phần trăm giảm giá phải lớn hơn 0 và bé hơn hoặc bằng 100");
			txtPhanTram.requestFocus();
			txtPhanTram.selectAll();
			return false;
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate ngayBDLocalDate = LocalDate.parse(ngayBD, formatter);
		
		LocalDate ngayKTLocalDate = LocalDate.parse(ngayKT, formatter);
		
		khuyenMai.setTenKhuyenMai(ten);
		khuyenMai.setNgayBatDau(ngayBDLocalDate);
		khuyenMai.setNgayKetThuc(ngayKTLocalDate);
		khuyenMai.setTongHoaDonToiThieu(tongTien);
		khuyenMai.setPhanTramKhuyenMai(phanTram/100);
		return true;
	}
	
	/**
	 * @param quanLyKhuyenMaiGUI the quanLyKhuyenMaiGUI to set
	 */
	public void setQuanLyKhuyenMaiGUI(QuanLyKhuyenMaiGUI quanLyKhuyenMaiGUI) {
		this.quanLyKhuyenMaiGUI = quanLyKhuyenMaiGUI;
	}
}
