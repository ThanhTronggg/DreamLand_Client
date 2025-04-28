package iuh.fit.gui.app.lichchieu;

import java.awt.*; 
import java.awt.event.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;

import iuh.fit.gui.app.EnvironmentVariable;
import net.miginfocom.swing.MigLayout;
import dao.PhimDAO;
import entity.LichChieu;
import entity.NhanVien;
import entity.Phim;
import iuh.fit.gui.app.khuyenmai.SuaKhuyenMaiDialog;
import iuh.fit.gui.app.khuyenmai.ThemKhuyenMaiDialog;
import service.LichChieuService;

public class QuanLyLichChieuGUI extends JPanel implements ActionListener {
    
    private static final long serialVersionUID = 1L;
	private static final JFrame QuanLyLichChieuGUI = null;
    private JButton btnThem;
    private DefaultTableModel tableModel;
    private JTable table;
    private JPanel pnlChiTiet;
    private ArrayList<LichChieu> dsLichChieu;
    private LichChieuService lichChieuDAO;
    private PhimDAO phimDAO;
    
    private JLabel lblTenPhim;
    private JLabel lblTheLoai;
    private JLabel lblThoiLuong;
    private JLabel lblDaoDien;
	private DateChooser chonNgay;
	private JTextField txtNgay;
	private JButton nutChonNgay;
	private JPanel pnlLichChieu;
	private JPanel pnlRow41;
	private JPanel pnlRow42;
	private Component pnlRow43;
	private JPanel pnlRow44;
	private JScrollPane scrollPane;
	private NhanVien nhanVienHienTai;
    
    public QuanLyLichChieuGUI(NhanVien nv) throws MalformedURLException, NotBoundException, RemoteException {
    	setLayout(new BorderLayout());
		
    	pnlLichChieu = new JPanel();
		lichChieuDAO = (LichChieuService) Naming.lookup("rmi://"+ EnvironmentVariable.IP.getValue()+":"+Integer.parseInt(EnvironmentVariable.PORT_SERVER.getValue())+"/lichChieuService");
    	this.nhanVienHienTai = nv;
//    	System.out.println("QLLCGUI " + nhanVienHienTai);
    	
    	scrollPane = new JScrollPane(pnlLichChieu);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    	
    	JPanel pnlTitle = new JPanel();
		JLabel lblTitle = new JLabel("THÔNG TIN LỊCH CHIẾU");
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		lblTitle.setFont(new Font(lblTitle.getFont().getFontName(), 1, 40));
		pnlTitle.add(lblTitle);
		add(pnlTitle, BorderLayout.NORTH);
		
		
		JPanel pnlNor = new JPanel();
		pnlNor.setLayout(new BoxLayout(pnlNor, BoxLayout.Y_AXIS));
		add(pnlNor, BorderLayout.SOUTH);
		
		JPanel pnlRow1 = new JPanel();
		pnlRow1.setLayout(new MigLayout("", "[][]push[][]", ""));
		pnlNor.add(pnlRow1);
		
		txtNgay = new JTextField(10);
		chonNgay = new DateChooser();
		nutChonNgay = new JButton();
		txtNgay.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,  "Tìm theo mã");
		txtNgay.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, new FlatSVGIcon("images/svg/search.svg", 0.35f));
		nutChonNgay.setIcon(new FlatSVGIcon("images/svg/calendar.svg", 18, 18));
		nutChonNgay.addActionListener(e -> {
			chonNgay.showPopup();
		});
		chonNgay.setTextRefernce(txtNgay);
		chonNgay.addEventDateChooser(new EventDateChooser() {
			@Override
			public void dateSelected(SelectedAction action, SelectedDate date) {
				if (action.getAction() == SelectedAction.DAY_SELECTED) {
					chonNgay.hidePopup();
				}
			}
		});
		pnlRow1.add(txtNgay, "w 200!");
		pnlRow1.add(nutChonNgay);
		
		btnThem = new JButton("Thêm mới");
		btnThem.setIcon(new FlatSVGIcon("images/svg/add.svg", 20, 20));
		pnlRow1.add(btnThem);
		
        
        String[] header = {"Mã lịch chiếu", "Mã phòng", "Mã phim", "Giờ bắt đầu", "Giờ kết thúc", "Giá một ghế"};
        tableModel = new DefaultTableModel(header, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(table);
//        pnlLeft.add(scroll, BorderLayout.CENTER);
        
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
        table.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
        table.getTableHeader().setDefaultRenderer(getAlignmentCellRender(table.getTableHeader().getDefaultRenderer(), true));
        table.setDefaultRenderer(Object.class, getAlignmentCellRender(table.getDefaultRenderer(Object.class), false));
        
        pnlChiTiet = new JPanel();
        pnlChiTiet.setLayout(new MigLayout("wrap 2", "[][grow]", "[]10[]10[]10[]10[]"));
        pnlChiTiet.setBorder(BorderFactory.createTitledBorder("Chi tiết phim"));
        
        pnlChiTiet.add(new JLabel("Tên Phim:"), "right");
        lblTenPhim = new JLabel("");
        pnlChiTiet.add(lblTenPhim, "grow");
        
        pnlChiTiet.add(new JLabel("Thể Loại:"), "right");
        lblTheLoai = new JLabel("");
        pnlChiTiet.add(lblTheLoai, "grow");
        
        pnlChiTiet.add(new JLabel("Thời Lượng:"), "right");
        lblThoiLuong = new JLabel("");
        pnlChiTiet.add(lblThoiLuong, "grow");
        
        pnlChiTiet.add(new JLabel("Đạo Diễn:"), "right");
        lblDaoDien = new JLabel("");
        pnlChiTiet.add(lblDaoDien, "grow");
          
        txtNgay.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
                try {
                    loadLichChieu();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
			
			@Override
			public void insertUpdate(DocumentEvent e) {
                try {
                    loadLichChieu();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
			
			@Override
			public void changedUpdate(DocumentEvent e) {
                try {
                    loadLichChieu();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
		});
        btnThem.addActionListener(this);
        loadLichChieu();
    }

    public void loadLichChieu() throws RemoteException {
        pnlLichChieu.removeAll();
        pnlLichChieu.setLayout(new GridLayout(0, 3, 10, 10));

        String ngay = txtNgay.getText();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate ngayLocalDate;
        
        try {
            ngayLocalDate = LocalDate.parse(ngay, dateFormatter);
        } catch (DateTimeParseException e) {
            return;
        }

        dsLichChieu = lichChieuDAO.getLichChieuTheoNgay(ngayLocalDate);
        
        Map<String, JPanel> listPhim = new HashMap<>();
        Map<String, JPanel> listGioChieu = new HashMap<>();

        for (LichChieu lc : dsLichChieu) {
        	JPanel movieScheduleCard;
        	JPanel pnlRow4 = new JPanel();
        	pnlRow4.setLayout(new GridLayout(2, 2, 10, 10));
        	if(listPhim.containsKey(lc.getPhim().getMaPhim())) {
        		movieScheduleCard = listPhim.get(lc.getPhim().getMaPhim());
        		pnlRow4 = listGioChieu.get(lc.getPhim().getMaPhim());
        		DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");
        		JButton screeningButton = new JButton(lc.getGioBatDau().format(hourFormatter) + " ~ " + lc.getGioKetThuc().format(hourFormatter));
        		pnlRow4.add(screeningButton);
        		screeningButton.addActionListener(e -> {
                    ChonGheDialog chonGhediaLog = null;
                    try {
                        chonGhediaLog = new ChonGheDialog(lc);
                    } catch (MalformedURLException ex) {
                        throw new RuntimeException(ex);
                    } catch (NotBoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    chonGhediaLog.pack();
        			chonGhediaLog.setModal(true);
        			chonGhediaLog.setVisible(true);
        			chonGhediaLog.setQuanLyLichChieuGUI(this); 
        			chonGhediaLog.setNhanVienHienTai(nhanVienHienTai);
        		});
        		movieScheduleCard.add(pnlRow4);
        	}
        	else {
        		movieScheduleCard = new JPanel();
        		movieScheduleCard.setLayout(new BoxLayout(movieScheduleCard, BoxLayout.Y_AXIS));
//        		movieScheduleCard.setPreferredSize(new Dimension(277, 370)); 
        		movieScheduleCard.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        		
        		JPanel pnlRow1 = new JPanel();
        		JLabel lblTenPhim = new JLabel(lc.getPhim().getTenPhim());
        		lblTenPhim.setAlignmentX(CENTER_ALIGNMENT);
        		pnlRow1.add(lblTenPhim);
        		movieScheduleCard.add(pnlRow1);
        		
        		// Xử lý sự kiện cho nút "View details"
//            viewDetailButton.addActionListener(e -> {
//                MovieDetailDialog movieDetailDialog = new MovieDetailDialog(lc.getPhim());
//                movieDetailDialog.setModal(true);
//                movieDetailDialog.setVisible(true);
//            });
        		
        		ImageIcon icon = new ImageIcon(lc.getPhim().getAnh());
        		if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
        			icon = new ImageIcon("images/movie-poster-not-found.jpg");
        		}
        		Image img = icon.getImage();
        		Image resizedImg = img.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
        		JLabel movieImage = new JLabel(new ImageIcon(resizedImg));
        		
        		
        		JPanel pnlRow2 = new JPanel();
        		pnlRow2.add(movieImage);
        		
        		JPanel pnlRow3 = new JPanel();
        		JButton trailerButton = new JButton("Trailer");
        		trailerButton.addActionListener(e -> {
        			// Hành động khi nhấn nút "Trailer"
        		});
        		pnlRow3.add(trailerButton);
        		trailerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        		JButton viewDetailButton = new JButton("Thông tin chi tiết");
        		pnlRow3.add(viewDetailButton);

        		DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");
        		JButton screeningButton = new JButton(lc.getGioBatDau().format(hourFormatter) + " ~ " + lc.getGioKetThuc().format(hourFormatter));
        		pnlRow4.add(screeningButton);
        		screeningButton.addActionListener(e -> {
                    ChonGheDialog chonGhediaLog = null;
                    try {
                        chonGhediaLog = new ChonGheDialog(lc);
                    } catch (MalformedURLException ex) {
                        throw new RuntimeException(ex);
                    } catch (NotBoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    chonGhediaLog.setNhanVienHienTai(nhanVienHienTai);
        			chonGhediaLog.setQuanLyLichChieuGUI(this);        			
        			System.out.println("QLLCGUI " + nhanVienHienTai);
        			chonGhediaLog.pack();
        			chonGhediaLog.setModal(true);
        			chonGhediaLog.setVisible(true);
        		});
        		
        		movieScheduleCard.add(pnlRow2);
        		movieScheduleCard.add(pnlRow3);
        		movieScheduleCard.add(pnlRow4);
        		
        		pnlLichChieu.add(movieScheduleCard);
        		listGioChieu.put(String.valueOf(lc.getPhim().getMaPhim()), pnlRow4);
        		listPhim.put(String.valueOf(lc.getPhim().getMaPhim()), movieScheduleCard);
        	}
        }
        int requiredPanels = 6 - pnlLichChieu.getComponentCount();
        for (int i = 0; i < requiredPanels; i++) {
            pnlLichChieu.add(new JPanel());
        }
        revalidate();
        repaint();
    }


	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(btnThem)) {
            try {
                them();
            } catch (Exception ex) {
				ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
	}
	
	private void them() throws MalformedURLException, NotBoundException, RemoteException {
		ThemLichChieuDialog themLC = new ThemLichChieuDialog(QuanLyLichChieuGUI);
		themLC.setModal(true);
		themLC.setVisible(true);
	}
	
	@SuppressWarnings("serial")
	private TableCellRenderer getAlignmentCellRender(TableCellRenderer oldRender, boolean header) {
	    return new DefaultTableCellRenderer() {
	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	                                                      boolean hasFocus, int row, int column) {
	            Component com = oldRender.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	             if (com instanceof JLabel) {
	                 JLabel label = (JLabel) com;
	                 label.setHorizontalAlignment(SwingConstants.CENTER);
	             }
	            return com;
	        }
	    };
	}
   
}