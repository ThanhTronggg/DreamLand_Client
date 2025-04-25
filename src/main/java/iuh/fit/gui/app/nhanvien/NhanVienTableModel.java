package iuh.fit.gui.app.nhanvien;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.NhanVien;

public class NhanVienTableModel extends AbstractTableModel {

    private List<NhanVien> nhanVienList;
    private final String[] columnNames = { "Mã NV", "Họ và Tên", "SĐT", "Vai Trò", "Trạng Thái" };

    public NhanVienTableModel(List<NhanVien> nhanVienList) {
        this.nhanVienList = nhanVienList;
    }

    public List<NhanVien> getNhanVienList() {
        return nhanVienList;
    }

    @Override
    public int getRowCount() {
        return nhanVienList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        NhanVien nhanVien = nhanVienList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return nhanVien.getMaNhanVien();
            case 1:
                return nhanVien.getHoTen();
            case 2:
                return nhanVien.getSoDienThoai();
            case 3:
                return nhanVien.getVaiTro();
            case 4:
                return nhanVien.getTrangThai();
            default:
                return null;
        }
    }

    public void setNhanVienList(List<NhanVien> nhanVienList) {
        this.nhanVienList = nhanVienList;
        fireTableDataChanged();
    }
}
