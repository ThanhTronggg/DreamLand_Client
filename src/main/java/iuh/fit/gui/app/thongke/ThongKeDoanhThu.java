///*
// * @(#) ThongKeDoanhThu.java 1.0 Nov 7, 2024
// * Copyright (c) 2024 IUH.
// * All rights reserved.
// */
//package iuh.fit.gui.app.thongke;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Desktop;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.text.DecimalFormat;
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//import javax.swing.AbstractButton;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JFileChooser;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.SwingConstants;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableCellRenderer;
//
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.BorderStyle;
//import org.apache.poi.ss.usermodel.BuiltinFormats;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
//import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
//import org.jfree.chart.plot.CategoryPlot;
//import org.jfree.chart.plot.PiePlot;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.chart.renderer.category.LineAndShapeRenderer;
//import org.jfree.chart.title.LegendTitle;
//import org.jfree.chart.ui.HorizontalAlignment;
//import org.jfree.chart.ui.RectangleEdge;
//import org.jfree.chart.ui.VerticalAlignment;
//import org.jfree.data.category.CategoryDataset;
//import org.jfree.data.category.DefaultCategoryDataset;
//import org.jfree.data.general.DefaultPieDataset;
//
//import com.formdev.flatlaf.FlatClientProperties;
//
////import dao.DoanhThuDAO;
////import entity.DoanhThu;
////import entity.KhachHangThongKe;
//import net.miginfocom.swing.MigLayout;
//
///**
// * @description:
// * @author: Thanh Trong
// * @date: Nov 7, 2024
// * @version: 1.0
// */
//
//public class ThongKeDoanhThu extends JPanel implements ActionListener {
//	private JComboBox<String> cboLoai;
//	private JComboBox<String> cboThangNam;
//	private DefaultTableModel tableModel;
//	private JTable table;
//	private JPanel pnlCharts;
//	private DefaultPieDataset pieDataset;
//	private DoanhThuDAO doanhThuDAO;
//	private DefaultTableModel tableModelRight;
//	private JTable tableRight;
//	private JButton btnExcel;
//	private String pathFile;
//	public static final int COLUMN_INDEX_TONGTIENVE = 0;
//    public static final int COLUMN_INDEX_TONGTIENSP = 1;
//    public static final int COLUMN_INDEX_TONGTIEN = 2;
//
//	public ThongKeDoanhThu() {
//
//		setLayout(new BorderLayout());
//
//		doanhThuDAO = new DoanhThuDAO();
//
//		JPanel pnlTitle = new JPanel();
//        pnlTitle.setLayout(new MigLayout("", "[]push[][]", ""));
//        JLabel lblTitle = new JLabel("Thống kê doanh thu");
//        lblTitle.setFont(new Font(lblTitle.getFont().getFontName(), 1, 20));
//        cboLoai = new JComboBox<String>();
//        cboLoai.addItem("Theo năm");
//        cboLoai.addItem("Theo tháng");
//        cboThangNam = new JComboBox<String>();
//        btnExcel = new JButton("Xuất báo cáo excel");
//        pnlTitle.add(lblTitle);
//        pnlTitle.add(btnExcel);
//        pnlTitle.add(cboLoai);
//        pnlTitle.add(cboThangNam);
//        add(pnlTitle, BorderLayout.NORTH);
//
//        String[] headerLeft = {"Tổng tiền bán đồ ăn & uống", "Tổng tiền bán vé", "Tổng doanh thu"};
//        tableModel = new DefaultTableModel(headerLeft, 0);
//        table = new JTable(tableModel);
//        table.setRowHeight(25);
//        JScrollPane scrollLeft = new JScrollPane(table);
//
////        String[] headerRight = {"Tổng tiền mua sản phẩm", "Tổng tiền thầu phim", "Tổng chi phí"};
////        tableModelRight = new DefaultTableModel(headerRight, 0);
////        tableRight = new JTable(tableModelRight);
////        tableRight.setRowHeight(25);
////        JScrollPane scrollRight = new JScrollPane(tableRight);
//
//        JPanel pnlTables = new JPanel();
////        pnlTables.setLayout(new GridLayout(1, 2));
//        pnlTables.setLayout(new GridLayout(1, 1));
//        pnlTables.add(scrollLeft);
////        pnlTables.add(scrollRight);
//
//        add(pnlTables, BorderLayout.CENTER);
//
//        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
//        table.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
//
//        table.getTableHeader()
//                .setDefaultRenderer(getAlignmentCellRender(table.getTableHeader().getDefaultRenderer(), true));
//        table.setDefaultRenderer(Object.class,
//                getAlignmentCellRender(table.getDefaultRenderer(Object.class), false));
//
////        tableRight.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
////        tableRight.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
////
////        tableRight.getTableHeader()
////                .setDefaultRenderer(getAlignmentCellRender(tableRight.getTableHeader().getDefaultRenderer(), true));
////        tableRight.setDefaultRenderer(Object.class,
////                getAlignmentCellRender(tableRight.getDefaultRenderer(Object.class), false));
//
//        cboLoai.addActionListener(this);
//        cboThangNam.addActionListener(this);
//        btnExcel.addActionListener(this);
//        loadCbo();
//        loadTableData();
//        hienThiBieuDo();
//	}
//
//	private void hienThiBieuDo() {
//        if (pnlCharts != null) {
//            remove(pnlCharts);
//        }
//        pnlCharts = new JPanel();
//        pnlCharts.setLayout(new BorderLayout());
//
//        String selected = (String) cboThangNam.getSelectedItem();
//        if (selected != null && selected.contains("/")) {
//            String[] parts = selected.split("/");
//            int month = Integer.parseInt(parts[0]);
//            int year = Integer.parseInt(parts[1]);
//
//            pieDataset = doanhThuDAO.getThongKeDoanhThuTheoThangBD(month, year);
//            System.out.println(pieDataset.getValue("Tổng tiền bán vé"));
//        } else if (selected != null) {
//            int year = Integer.parseInt(selected);
//
//            pieDataset = doanhThuDAO.getThongKeDoanhThuTheoNamBD(year);
//            System.out.println(pieDataset.getValue("Tổng tiền bán vé"));
//        }
//
//        JFreeChart pieChart = ChartFactory.createPieChart(
//                "Biểu đồ thống kê doanh thu", pieDataset, true, true, false
//            );
//
//        pieChart.setBackgroundPaint(Color.decode("#fafafa"));
//        pieChart.setBorderPaint(null);
//
//        PiePlot plot = (PiePlot) pieChart.getPlot();
//        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
//                "{0}: {1} ({2})", new DecimalFormat("#,##0"), new DecimalFormat("0.0%")
//        ));
//        plot.setLabelBackgroundPaint(Color.WHITE);
//        plot.setLabelFont(new Font("Arial", Font.PLAIN, 12));
//        plot.setLabelPaint(Color.BLACK);
//
//        LegendTitle legend = pieChart.getLegend();
//        legend.setPosition(RectangleEdge.RIGHT);
//        legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
//        legend.setVerticalAlignment(VerticalAlignment.TOP);
//        legend.setBackgroundPaint(Color.decode("#fafafa"));
//        ChartPanel chartPanel = new ChartPanel(pieChart);
//        chartPanel.setPreferredSize(new Dimension(800, 600));
//
//        pnlCharts.add(chartPanel);
//        add(pnlCharts, BorderLayout.SOUTH);
//
//        revalidate();
//        repaint();
//    }
//
//	private void loadTableData() {
//        tableModel.setRowCount(0);
////        tableModelRight.setRowCount(0);
//        String selected = (String) cboThangNam.getSelectedItem();
//        if (selected != null && selected.contains("/")) {
//            String[] parts = selected.split("/");
//            int month = Integer.parseInt(parts[0]);
//            int year = Integer.parseInt(parts[1]);
//
//            ArrayList<DoanhThu> dsDoanhThu = doanhThuDAO.getThongKeDoanhThuTheoThang(month, year);
//
//            for (DoanhThu dt : dsDoanhThu) {
//            	String[] row = {Double.toString(dt.getTongTienBanSanPham()), Double.toString(dt.getTongTienBanVe()), Double.toString(dt.getTongDoanhThu())};
//                tableModel.addRow(row);
////                tableModelRight.addRow(row);
//            }
//        } else if (selected != null) {
//            int year = Integer.parseInt(selected);
//
//            ArrayList<DoanhThu> dsDoanhThu = doanhThuDAO.getThongKeDoanhThuTheoNam(year);
//
//            for (DoanhThu dt : dsDoanhThu) {
//            	String[] row = {Double.toString(dt.getTongTienBanSanPham()), Double.toString(dt.getTongTienBanVe()), Double.toString(dt.getTongDoanhThu())};
//                tableModel.addRow(row);
////                tableModelRight.addRow(row);
//            }
//        }
//	}
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		Object o = e.getSource();
//		if(o.equals(cboLoai)) {
//			loadCbo();
//			loadTableData();
//			hienThiBieuDo();
//		}
//		if(o.equals(cboThangNam)) {
//			loadTableData();
//			hienThiBieuDo();
//		}
//		if(o.equals(btnExcel)) {
//			String selected = (String) cboThangNam.getSelectedItem();
//			ArrayList<DoanhThu> doanhThu = null;
//			String title = "";
//			String titleExcel = "";
//	        if (selected != null && selected.contains("/")) {
//	            String[] parts = selected.split("/");
//	            int month = Integer.parseInt(parts[0]);
//	            int year = Integer.parseInt(parts[1]);
//
//	            title = "thang_" + month + "_nam_" +  year;
//
//	            titleExcel = "Báo cáo thống kê doanh thu tháng " + month + " năm " + year;
//
//	            doanhThu = doanhThuDAO.getThongKeDoanhThuTheoThang(month, year);
//	        } else if (selected != null) {
//	            int year = Integer.parseInt(selected);
//
//	            doanhThu = doanhThuDAO.getThongKeDoanhThuTheoNam(year);
//	            title = "nam_" +  year;
//	            titleExcel = "Báo cáo thống kê doanh thu năm " + year;
//	        }
////	        JFileChooser fileChooser = new JFileChooser();
////	        String excelFilePath = "data/excel/bao_cao_thong_ke_doanh_thu_" + title + ".xlsx";
////	        File defaultFile = new File(excelFilePath);
////	        fileChooser.setSelectedFile(defaultFile);
////	        FileNameExtensionFilter filter = new FileNameExtensionFilter("File excel (*.xlsx, *.xls)", "xlsx", "xls");
////	        fileChooser.setFileFilter(filter);
////	        int returnValue = fileChooser.showOpenDialog(this);
////	        if (returnValue == JFileChooser.APPROVE_OPTION) {
////	            pathFile = fileChooser.getSelectedFile().getAbsolutePath();
//	        pathFile = "data/excel/bao_cao_thong_ke_doanh_thu_" + title + ".xlsx";
//            try {
//            	writeExcel(doanhThu, pathFile, titleExcel);
//            	File file = new File(pathFile);
////            	JOptionPane.showMessageDialog(this, "File đã được lưu vào: " + pathFile);
//    	        if (Desktop.isDesktopSupported()) {
//   	             Desktop desktop = Desktop.getDesktop();
//   	             if (desktop.isSupported(Desktop.Action.OPEN)) {
//   	                desktop.open(file);
//   	             } else {
//   	            	JOptionPane.showMessageDialog(this, "Hệ thống không hỗ trợ mở file");
//   	             }
//   	          } else {
//   	             System.out.println("Desktop API không được hỗ trợ trên hệ thống này");
//   	          }
//            } catch (IOException e1) {
//            	e1.printStackTrace();
//            	JOptionPane.showMessageDialog(this, "Lỗi không thể xuất file");
//            }
////	        }
//		}
//
//	}
//
//	public static void writeExcel(ArrayList<DoanhThu> doanhThu, String excelFilePath, String title) throws IOException {
//        Workbook workbook = getWorkbook(excelFilePath);
//
//        Sheet sheet = workbook.createSheet("Thống kê doanh thu");
//
//        int rowIndex = 0;
//
//        writeHeader(sheet, rowIndex, title, workbook);
//
//        rowIndex += 7;
//        for (DoanhThu dt : doanhThu) {
//            Row row = sheet.createRow(rowIndex);
//            writeBook(dt, row, workbook, sheet);
//            rowIndex++;
//        }
//
//        // Auto resize column witdth
//        int numberOfColumn = sheet.getRow(rowIndex-1).getPhysicalNumberOfCells();
//        autosizeColumn(sheet, numberOfColumn);
//
//        createOutputFile(workbook, excelFilePath);
//        System.out.println("Xuất file excel thống kê thành công!!!");
//    }
//
//	private static Workbook getWorkbook(String excelFilePath) throws IOException {
//        Workbook workbook = null;
//        if (excelFilePath.endsWith("xlsx")) {
//            workbook = new XSSFWorkbook();
//        } else if (excelFilePath.endsWith("xls")) {
//            workbook = new HSSFWorkbook();
//        } else {
//            throw new IllegalArgumentException("Tệp được chỉ định không phải là tệp Excel.");
//        }
//
//        return workbook;
//    }
//
//	private static void writeHeader(Sheet sheet, int rowIndex, String title, Workbook workbook) {
//		CellStyle style = workbook.createCellStyle();
//		style.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
//		style.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
//		org.apache.poi.ss.usermodel.Font font2 = sheet.getWorkbook().createFont();
//		font2.setBold(true);
//        font2.setFontName("Times New Roman");
//        font2.setFontHeightInPoints((short) 16);
//        style.setFont(font2);
//        style.setBorderTop(BorderStyle.THIN);
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);
//
//		CellStyle styleHeader = workbook.createCellStyle();
//		styleHeader.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
//		styleHeader.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
//		org.apache.poi.ss.usermodel.Font font = sheet.getWorkbook().createFont();
//        font.setFontName("Times New Roman");
//        font.setBold(true);
//        font.setFontHeightInPoints((short) 20);
//		styleHeader.setFont(font);
//
//		CellStyle styleHeader2 = workbook.createCellStyle();
//		org.apache.poi.ss.usermodel.Font font3 = sheet.getWorkbook().createFont();
//        font3.setFontName("Times New Roman");
//        font3.setFontHeightInPoints((short) 16);
//		styleHeader2.setFont(font3);
//
//		CellStyle styleHeader3 = workbook.createCellStyle();
//		styleHeader3.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
//		styleHeader3.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
//		org.apache.poi.ss.usermodel.Font font4 = sheet.getWorkbook().createFont();
//        font4.setFontName("Times New Roman");
//        font4.setBold(true);
//        font4.setFontHeightInPoints((short) 16);
//		styleHeader3.setFont(font4);
//
//		Row row1 = sheet.createRow(rowIndex);
//		Cell cell1 = row1.createCell(0);
//		cell1.setCellValue(title);
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 2, 0, 2));
//		cell1.setCellStyle(styleHeader);
//
//		Row row2 = sheet.createRow(rowIndex+3);
//		Cell cell2 = row2.createCell(0);
//		cell2.setCellValue("Rạp chiếu phim DreamLand");
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex + 3, rowIndex +3, 0, 2));
//		cell2.setCellStyle(styleHeader2);
//
//		Row row3 = sheet.createRow(rowIndex+4);
//		Cell cell3 = row3.createCell(0);
//		cell3.setCellValue("12 Nguyễn Văn Bảo, Phường 4, Quận Gò Vấp, TP. Hồ Chí Minh");
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex + 4, rowIndex +4, 0, 2));
//		cell3.setCellStyle(styleHeader2);
//
//		Row row4 = sheet.createRow(rowIndex+5);
//		Cell cell4 = row4.createCell(0);
//		cell4.setCellValue("Thống kê doanh thu");
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex + 5, rowIndex +5, 0, 2));
//		cell4.setCellStyle(styleHeader3);
//
//        Row row = sheet.createRow(rowIndex+6);
//
//        Cell cell = row.createCell(COLUMN_INDEX_TONGTIENVE);
//        cell.setCellStyle(style);
//        cell.setCellValue("Tổng tiền bán vé");
//
//        cell = row.createCell(COLUMN_INDEX_TONGTIENSP);
//        cell.setCellStyle(style);
//        cell.setCellValue("Tổng tiền bán đồ ăn & uống");
//
//        cell = row.createCell(COLUMN_INDEX_TONGTIEN);
//        cell.setCellStyle(style);
//        cell.setCellValue("Tổng doanh thu");
//    }
//
//	private static void writeBook(DoanhThu dt, Row row, Workbook workbook, Sheet sheet) {
//
//		CellStyle style = workbook.createCellStyle();
//		style.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
//		org.apache.poi.ss.usermodel.Font font2 = sheet.getWorkbook().createFont();
//        font2.setFontName("Times New Roman");
//        font2.setFontHeightInPoints((short) 16);
//        short format = (short)BuiltinFormats.getBuiltinFormat("#,##0");
//        style.setDataFormat(format);
//        style.setFont(font2);
//        style.setBorderTop(BorderStyle.THIN);
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);
//
//        Cell cell = row.createCell(COLUMN_INDEX_TONGTIENVE);
//        cell.setCellStyle(style);
//        cell.setCellValue(dt.getTongTienBanVe());
//
//        cell = row.createCell(COLUMN_INDEX_TONGTIENSP);
//        cell.setCellStyle(style);
//        cell.setCellValue(dt.getTongTienBanSanPham());
//
//        cell = row.createCell(COLUMN_INDEX_TONGTIEN);
//        cell.setCellStyle(style);
//        cell.setCellValue(dt.getTongDoanhThu());
//    }
//
//    private static void autosizeColumn(Sheet sheet, int lastColumn) {
//        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
//            sheet.autoSizeColumn(columnIndex);
//            sheet.setColumnWidth(columnIndex, (int) (sheet.getColumnWidth(columnIndex) * 1.3));
//        }
//    }
//
//    private static void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {
//        try (OutputStream os = new FileOutputStream(excelFilePath)) {
//            workbook.write(os);
//        }
//    }
//
//	private void loadCbo() {
//		int namHienTai = LocalDate.now().getYear();
//		int thangHienTai = LocalDate.now().getMonthValue();
//		cboThangNam.removeAllItems();
//		if (cboLoai.getSelectedItem().equals("Theo tháng")) {
//			for (int month = thangHienTai; month >= 1; month--) {
//				cboThangNam.addItem(month + "/" + namHienTai);
//			}
//			for (int month = 12; month >= 1; month--) {
//				cboThangNam.addItem(month + "/" + (namHienTai-1));
//			}
//	    } else {
//	        for (int year = namHienTai; year >= namHienTai-10; year--) {
//	            cboThangNam.addItem(""+ year);
//	        }
//	    }
//
//	}
//
//	@SuppressWarnings("serial")
//	private TableCellRenderer getAlignmentCellRender(TableCellRenderer oldRender, boolean header) {
//	    return new DefaultTableCellRenderer() {
//	        @Override
//	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
//	                                                      boolean hasFocus, int row, int column) {
//	            Component com = oldRender.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//	             if (com instanceof JLabel) {
//	                 JLabel label = (JLabel) com;
//	                 label.setHorizontalAlignment(SwingConstants.CENTER);
//	             }
//	            return com;
//	        }
//	    };
//	}
//}
