package iuh.fit.gui.app;

public enum EnvironmentVariable {
    PORT_SERVER("1883"),
    IP("172.28.100.3");

    // Biến instance để lưu giá trị của hằng số
    private String value;

    // Constructor để khởi tạo giá trị cho các hằng số
    EnvironmentVariable(String value) {
        this.value = value;
    }

    // Phương thức getter để truy xuất giá trị của hằng số
    public String getValue() {
        return value;
    }
}
