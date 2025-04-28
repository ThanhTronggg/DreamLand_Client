/*
 * @(#) ThongKeKhachHang.java 1.0 Nov 5, 2024
 * Copyright (c) 2024 IUH.
 * All rights reserved.
 */
package iuh.fit.gui.app.thongke;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import iuh.fit.gui.app.EnvironmentVariable;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.VerticalAlignment;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import dao.KhachHangDAO;
import entity.KhachHangThongKe;
import entity.SanPhamThongKe;
import net.miginfocom.swing.MigLayout;
import service.DoanhThuService;
import service.KhachHangThongKeService;

/**
 * @description:
 * @author: Thanh Trong
 * @date: Nov 5, 2024
 * @version: 1.0
 */

public class ThongKeKhachHang extends JPanel implements ActionListener {

    private KhachHangThongKeService kh_dao;
	private JComboBox<String> cboLoai;
	private DefaultCategoryDataset lineDataset;
	private DefaultCategoryDataset barDataset;
	private JPanel pnlCharts;
	private JComboBox<String> cboLoai2;
	private JComboBox<String> cboThangNam;
	private JButton btnExcel;
	private String pathFile;
	public static final int COLUMN_INDEX_MAKH = 0;
    public static final int COLUMN_INDEX_TENKH = 1;
    public static final int COLUMN_INDEX_TONGTIEN = 2;

    public ThongKeKhachHang() throws MalformedURLException, NotBoundException, RemoteException {
    	setLayout(new BorderLayout());

        kh_dao = (KhachHangThongKeService) Naming.lookup("rmi://"+ EnvironmentVariable.IP.getValue()+":"+Integer.parseInt(EnvironmentVariable.PORT_SERVER.getValue())+"/khachHangThongKeService");

        cboLoai = new JComboBox<String>();
        cboLoai.addItem("Theo từng tháng");
        cboLoai.addItem("Theo từng quý");
        cboLoai.addItem("Theo từng năm");
        cboLoai.addActionListener(this);

        cboLoai2 = new JComboBox<String>();
        cboLoai2.addItem("Theo năm");
        cboLoai2.addItem("Theo tháng");

        cboThangNam = new JComboBox<String>();
        btnExcel = new JButton("Xuất báo cáo excel");

        lineDataset = kh_dao.getSoLuongKhachHangPhanBietTheoThang();

        cboLoai.addActionListener(this);
        cboLoai2.addActionListener(this);
        cboThangNam.addActionListener(this);
        btnExcel.addActionListener(this);

        loadCbo();
        hienThi();
    }

    private void hienThi() throws RemoteException {
        if (pnlCharts != null) {
            remove(pnlCharts);
        }

        JPanel pnlTitle = new JPanel(new MigLayout("", "[][]push[]", ""));
        JLabel lblTitle = new JLabel("Số lượng khách hàng");
        lblTitle.setFont(new Font(lblTitle.getFont().getFontName(), Font.BOLD, 20));

        pnlTitle.add(lblTitle);
        pnlTitle.add(cboLoai);

        JFreeChart lineChart = createLineChart(lineDataset);
        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        lineChartPanel.setPreferredSize(new Dimension(200, 330));

        String selected = (String) cboThangNam.getSelectedItem();
        if (selected != null && selected.contains("/")) {
            String[] parts = selected.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);

            barDataset = kh_dao.getTop5KhachHangTheoChiTieu(month, year);

        } else if (selected != null) {
            int year = Integer.parseInt(selected);

            barDataset = kh_dao.getTop5KhachHangTheoChiTieu(year);
        }

        JFreeChart barChart = createBarChart(barDataset);
        ChartPanel barChartPanel = new ChartPanel(barChart);
        barChartPanel.setPreferredSize(new Dimension(200, 400));


        JPanel pnlTitle2 = new JPanel(new MigLayout("", "[]push[][][]", ""));
        JLabel lblTitle2 = new JLabel("Top các khách hàng xem phim nhiều nhất" );
        lblTitle2.setFont(new Font(lblTitle2.getFont().getFontName(), Font.BOLD, 20));


        pnlTitle2.add(lblTitle2);
        pnlTitle2.add(btnExcel);
        pnlTitle2.add(cboLoai2);
        pnlTitle2.add(cboThangNam);

        pnlCharts = new JPanel();
        pnlCharts.setLayout(new BoxLayout(pnlCharts, BoxLayout.Y_AXIS));

        pnlCharts.add(pnlTitle);
        pnlCharts.add(lineChartPanel);
        pnlCharts.add(pnlTitle2);
        pnlCharts.add(barChartPanel);

        add(pnlCharts, BorderLayout.CENTER);

        revalidate();
        repaint();
    }



	private JFreeChart createLineChart(CategoryDataset dataset) {
        JFreeChart lineChart = ChartFactory.createLineChart(
                "",
                "",
                "",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        lineChart.setBackgroundPaint(Color.decode("#fafafa"));

        CategoryPlot plot = lineChart.getCategoryPlot();
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();

        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.decode("#38bdf8"));
        renderer.setSeriesShapesVisible(0, true);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.decode("#fafafa"));

        LegendTitle legend = lineChart.getLegend();
        legend.setPosition(RectangleEdge.RIGHT);
        legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(VerticalAlignment.TOP);
        legend.setBackgroundPaint(Color.decode("#fafafa"));

        return lineChart;
    }

    private JFreeChart createBarChart(CategoryDataset dataset) {
        JFreeChart barChart = ChartFactory.createBarChart(
                "",
                "",
                "",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        barChart.setBackgroundPaint(Color.decode("#fafafa"));

        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer();

        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.decode("#92dbd5"));
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.decode("#fafafa"));

        LegendTitle legend = barChart.getLegend();
        legend.setPosition(RectangleEdge.RIGHT);
        legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(VerticalAlignment.TOP);
        legend.setBackgroundPaint(Color.decode("#fafafa"));

        return barChart;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(cboLoai)) {
        	String loai = cboLoai.getSelectedItem().toString();
            if(loai.equals("Theo từng tháng")) {
                try {
                    lineDataset = kh_dao.getSoLuongKhachHangPhanBietTheoThang();
                    hienThi();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if(loai.equals("Theo từng quý")) {
                try {
                    lineDataset = kh_dao.getSoLuongKhachHangPhanBietTheoQuy();
                    hienThi();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if(loai.equals("Theo từng năm")) {
                try {
                    lineDataset = kh_dao.getSoLuongKhachHangPhanBietTheoNam();
                    hienThi();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if(o.equals(cboLoai2)) {
			loadCbo();
            try {
                hienThi();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(o.equals(cboThangNam)) {
            try {
                hienThi();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(o.equals(btnExcel)) {
			String selected = (String) cboThangNam.getSelectedItem();
			ArrayList<KhachHangThongKe> dsKH = null;
			String title = "";
			String titleExcel = "";
	        if (selected != null && selected.contains("/")) {
	            String[] parts = selected.split("/");
	            int month = Integer.parseInt(parts[0]);
	            int year = Integer.parseInt(parts[1]);

	            title = "thang_" + month + "_nam_" +  year;

	            titleExcel = "Báo cáo thống kê khách hàng tháng " + month + " năm " + year;

                try {
                    dsKH = kh_dao.getThongKeKhachHangTheoThang(month, year);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (selected != null) {
	            int year = Integer.parseInt(selected);

                try {
                    dsKH = kh_dao.getThongKeKhachHangTheoNam(year);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                title = "nam_" +  year;
	            titleExcel = "Báo cáo thống kê khách hàng năm " + year;
	        }
//	        JFileChooser fileChooser = new JFileChooser();
//	        String excelFilePath = "data/excel/bao_cao_thong_ke_khach_hang_" + title + ".xlsx";
//	        File defaultFile = new File(excelFilePath);
//	        fileChooser.setSelectedFile(defaultFile);
//	        FileNameExtensionFilter filter = new FileNameExtensionFilter("File excel (*.xlsx, *.xls)", "xlsx", "xls");
//	        fileChooser.setFileFilter(filter);
//	        int returnValue = fileChooser.showOpenDialog(this);
//	        if (returnValue == JFileChooser.APPROVE_OPTION) {
//	            pathFile = fileChooser.getSelectedFile().getAbsolutePath();
	        pathFile = "data/excel/bao_cao_thong_ke_khach_hang_" + title + ".xlsx";
	        try {
            	writeExcel(dsKH, pathFile, titleExcel);
            	File file = new File(pathFile);
//            	JOptionPane.showMessageDialog(this, "File đã được lưu vào: " + pathFile);
    	        if (Desktop.isDesktopSupported()) {
   	             Desktop desktop = Desktop.getDesktop();
   	             if (desktop.isSupported(Desktop.Action.OPEN)) {
   	                desktop.open(file);
   	             } else {
   	            	JOptionPane.showMessageDialog(this, "Hệ thống không hỗ trợ mở file");
   	             }
   	          } else {
   	             System.out.println("Desktop API không được hỗ trợ trên hệ thống này");
   	          }
            } catch (IOException e1) {
            	e1.printStackTrace();
            	JOptionPane.showMessageDialog(this, "Lỗi không thể xuất file");
            }
//	        }
		}
    }

    public static void writeExcel(ArrayList<KhachHangThongKe> dsKH, String excelFilePath, String title) throws IOException {
        Workbook workbook = getWorkbook(excelFilePath);

        Sheet sheet = workbook.createSheet("Thống kê khách hàng");

        int rowIndex = 0;

        writeHeader(sheet, rowIndex, title, workbook);

        rowIndex += 7;
        for (KhachHangThongKe khTK : dsKH) {
            Row row = sheet.createRow(rowIndex);
            writeBook(khTK, row, workbook, sheet);
            rowIndex++;
        }

        // Auto resize column witdth
        int numberOfColumn = sheet.getRow(rowIndex-1).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);

        createOutputFile(workbook, excelFilePath);
        System.out.println("Xuất file excel thống kê thành công!!!");
    }

	private static Workbook getWorkbook(String excelFilePath) throws IOException {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("Tệp được chỉ định không phải là tệp Excel.");
        }

        return workbook;
    }

	private static void writeHeader(Sheet sheet, int rowIndex, String title, Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		style.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		org.apache.poi.ss.usermodel.Font font2 = sheet.getWorkbook().createFont();
		font2.setBold(true);
        font2.setFontName("Times New Roman");
        font2.setFontHeightInPoints((short) 16);
        style.setFont(font2);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

		CellStyle styleHeader = workbook.createCellStyle();
		styleHeader.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		styleHeader.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		org.apache.poi.ss.usermodel.Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 20);
		styleHeader.setFont(font);

		CellStyle styleHeader2 = workbook.createCellStyle();
		org.apache.poi.ss.usermodel.Font font3 = sheet.getWorkbook().createFont();
        font3.setFontName("Times New Roman");
        font3.setFontHeightInPoints((short) 16);
		styleHeader2.setFont(font3);

		CellStyle styleHeader3 = workbook.createCellStyle();
		styleHeader3.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		styleHeader3.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		org.apache.poi.ss.usermodel.Font font4 = sheet.getWorkbook().createFont();
        font4.setFontName("Times New Roman");
        font4.setBold(true);
        font4.setFontHeightInPoints((short) 16);
		styleHeader3.setFont(font4);

		Row row1 = sheet.createRow(rowIndex);
		Cell cell1 = row1.createCell(0);
		cell1.setCellValue(title);
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 2, 0, 2));
		cell1.setCellStyle(styleHeader);

		Row row2 = sheet.createRow(rowIndex+3);
		Cell cell2 = row2.createCell(0);
		cell2.setCellValue("Rạp chiếu phim DreamLand");
		sheet.addMergedRegion(new CellRangeAddress(rowIndex + 3, rowIndex +3, 0, 2));
		cell2.setCellStyle(styleHeader2);

		Row row3 = sheet.createRow(rowIndex+4);
		Cell cell3 = row3.createCell(0);
		cell3.setCellValue("12 Nguyễn Văn Bảo, Phường 4, Quận Gò Vấp, TP. Hồ Chí Minh");
		sheet.addMergedRegion(new CellRangeAddress(rowIndex + 4, rowIndex +4, 0, 2));
		cell3.setCellStyle(styleHeader2);

		Row row4 = sheet.createRow(rowIndex+5);
		Cell cell4 = row4.createCell(0);
		cell4.setCellValue("Top 5 khách hàng xem phim nhiều nhất");
		sheet.addMergedRegion(new CellRangeAddress(rowIndex + 5, rowIndex +5, 0, 2));
		cell4.setCellStyle(styleHeader3);

        Row row = sheet.createRow(rowIndex+6);

        Cell cell = row.createCell(COLUMN_INDEX_MAKH);
        cell.setCellStyle(style);
        cell.setCellValue("Mã khách hàng");

        cell = row.createCell(COLUMN_INDEX_TENKH);
        cell.setCellStyle(style);
        cell.setCellValue("Tên khách hàng");

        cell = row.createCell(COLUMN_INDEX_TONGTIEN);
        cell.setCellStyle(style);
        cell.setCellValue("Tổng tiền chi");
    }

	private static void writeBook(KhachHangThongKe khTK, Row row, Workbook workbook, Sheet sheet) {

		CellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		org.apache.poi.ss.usermodel.Font font2 = sheet.getWorkbook().createFont();
        font2.setFontName("Times New Roman");
        font2.setFontHeightInPoints((short) 16);
        short format = (short)BuiltinFormats.getBuiltinFormat("#,##0");
        style.setDataFormat(format);
        style.setFont(font2);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        Cell cell = row.createCell(COLUMN_INDEX_MAKH);
        cell.setCellStyle(style);
        cell.setCellValue(khTK.getMaKhachHang());

        cell = row.createCell(COLUMN_INDEX_TENKH);
        cell.setCellStyle(style);
        cell.setCellValue(khTK.getTenKhachHang());

        cell = row.createCell(COLUMN_INDEX_TONGTIEN);
        cell.setCellStyle(style);
        cell.setCellValue(khTK.getTongTien());
    }

    private static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
            sheet.setColumnWidth(columnIndex, (int) (sheet.getColumnWidth(columnIndex) * 1.3));
        }
    }

    private static void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {
        try (OutputStream os = new FileOutputStream(excelFilePath)) {
            workbook.write(os);
        }
    }

    private void loadCbo() {
		int namHienTai = LocalDate.now().getYear();
		int thangHienTai = LocalDate.now().getMonthValue();
		cboThangNam.removeAllItems();
		if (cboLoai2.getSelectedItem().equals("Theo tháng")) {
			for (int month = thangHienTai; month >= 1; month--) {
				cboThangNam.addItem(month + "/" + namHienTai);
			}
			for (int month = 12; month >= 1; month--) {
				cboThangNam.addItem(month + "/" + (namHienTai-1));
			}
	    } else {
	        for (int year = namHienTai; year >= namHienTai-10; year--) {
	            cboThangNam.addItem(""+ year);
	        }
	    }

	}
}
