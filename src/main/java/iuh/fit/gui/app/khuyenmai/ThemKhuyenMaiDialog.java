/*
 * @(#) ThemKhuyenMaiDialog.java 1.0 Nov 1, 2024
 * Copyright (c) 2024 IUH.
 * All rights reserved.
 */
package iuh.fit.gui.app.khuyenmai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialLiteTheme;

/**
 * @description:
 * @author: Thanh Trong
 * @date: Nov 1, 2024
 * @version: 1.0
 */

public class ThemKhuyenMaiDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JTextField txtTenKM;
	private JTextField txtNgayBD;
	private JTextField txtNgayKT;
	private JTextField txtTongTien;
	private JTextField txtPhanTram;
	private JButton btnThem;
	private JButton btnThoat;
	private KhuyenMaiDAO kmDao;
	private QuanLyKhuyenMaiGUI quanLyKhuyenMaiGUI;
	private DateChooser ngayBDChooser;
	private JButton nutChonNgayBD;
	private DateChooser chonNgayBD;
	private DateChooser chonNgayKT;
	private JButton nutChonNgayKT;

	public ThemKhuyenMaiDialog() {
		
		//frame
		setSize(700, 470);
		setTitle("Thêm khuyến mãi");
		setLocationRelativeTo(null);
		setResizable(false);
		
		//add component
		JPanel pnlNor = new JPanel();
		JLabel lblKhuyenMai = new JLabel("THÊM KHUYẾN MÃI");
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
		pnlRow6.setLayout( new FlowLayout(FlowLayout.RIGHT) );
		btnThem = new JButton("Thêm");
		btnThem.setBackground(Color.decode("#273167"));
		btnThem.setForeground(Color.white);
		pnlRow6.add(btnThem);
		btnThoat = new JButton("Hủy bỏ");
		btnThoat.setBackground(Color.decode("#ff4f4f"));
		btnThoat.setForeground(Color.white);
		pnlRow6.add(btnThoat);
		pnlCen.add(pnlRow6);
		
		kmDao = new KhuyenMaiDAO(KhuyenMai.class);
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
		
		btnThem.addActionListener(this);
		btnThoat.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(btnThem) || o.equals(txtTenKM)|| o.equals(txtNgayBD)|| o.equals(txtNgayKT)|| o.equals(txtTongTien) || o.equals(txtPhanTram)) {
			them();
			
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
	private void them() {
		KhuyenMai km1 = kiemTraDieuKien();
		if(kmDao.add(km1) ) {
			JOptionPane.showMessageDialog(this, "Thêm thành công!", "Thông báo", JOptionPane.PLAIN_MESSAGE);
			this.dispose();
		}
		else {
			JOptionPane.showMessageDialog(this, "Thêm không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);					
		}
	}
	
	private KhuyenMai kiemTraDieuKien() {
		String ngayBD = txtNgayBD.getText();
		String ngayKT = txtNgayKT.getText();
		double phanTram;
		double tongTien;
		
		String ten = txtTenKM.getText();
		if(!ten.matches("[aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTu UùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ0-9/-_ ]+")) {
			JOptionPane.showMessageDialog(this, "Tên khuyến mãi có thể chứa kí tự thường, in hoa hoặc ký tự số");
			txtTenKM.selectAll();
			txtTenKM.requestFocus();
			return null;
		}
		
		try {
			tongTien = Double.parseDouble(txtTongTien.getText());
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Tổng tiền tối thiếu của hóa đơn phải là 1 số");
			txtTongTien.selectAll();
			txtTongTien.requestFocus();
			return null;
		}
		tongTien = Double.parseDouble(txtTongTien.getText());
		if(tongTien < 0) {
			JOptionPane.showMessageDialog(this, "Tổng tiền tối thiểu phải lớn hơn hoặc bằng 0");
			txtTongTien.requestFocus();
			txtTongTien.selectAll();
			return null;
		}
		
		try {
			phanTram = Double.parseDouble(txtPhanTram.getText());
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Phần trăm giảm giá phải là một số");
			txtPhanTram.selectAll();
			txtPhanTram.requestFocus();
			return null;
		}
		phanTram = Double.parseDouble(txtPhanTram.getText());
		if(phanTram < 0 || phanTram > 100) {
			JOptionPane.showMessageDialog(this, "Phần trăm giảm giá phải lớn hơn 0 và bé hơn hoặc bằng 100");
			txtPhanTram.requestFocus();
			txtPhanTram.selectAll();
			return null;
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate ngayBDLocalDate = LocalDate.parse(ngayBD, formatter);
		
		LocalDate ngayKTLocalDate = LocalDate.parse(ngayKT, formatter);
		
		KhuyenMai km = new KhuyenMai(ten, ngayBDLocalDate, ngayKTLocalDate, tongTien, phanTram/100);
		return km;
	}

	
	/**
	 * @param quanLyKhuyenMaiGUI the quanLyKhuyenMaiGUI to set
	 */
	public void setQuanLyKhuyenMaiGUI(QuanLyKhuyenMaiGUI quanLyKhuyenMaiGUI) {
		this.quanLyKhuyenMaiGUI = quanLyKhuyenMaiGUI;
	}
	
}
