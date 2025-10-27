package controllers;

import dao.OrderDao;
import dao.OrderItemDao;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import models.Order;
import models.OrderItem;
import views.PrintOrderView;

public class PrintOrderController {

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    DecimalFormat formatter = new DecimalFormat("###,###,###");

    /**
     * Fetches order data, formats it into a string, and displays it in a PrintOrderView.
     * @param id The ID of the order to print.
     * @throws Exception if the order cannot be found.
     */
    public void print(int id) throws Exception {
        OrderDao orderDao = new OrderDao();
        OrderItemDao orderItemDao = new OrderItemDao();
        Order order = orderDao.get(id);
        if (order == null) {
            throw new Exception("Không tìm thấy hóa đơn với ID: " + id);
        }
        ArrayList<OrderItem> orderItems = orderItemDao.getByIdOrder(id);

        // Generate the bill text
        String billText = generateBillText(order, orderItems);

        // Create and show the preview view
        PrintOrderView view = new PrintOrderView();
        view.getTxtBill().setText(billText);
        
        // Add event listeners
        view.getBtnClose().addActionListener(e -> view.dispose());
        view.getBtnPrint().addActionListener(e -> {
            try {
                // Use the JTextArea's built-in print functionality
                boolean didPrint = view.getTxtBill().print();
                if (didPrint) {
                    JOptionPane.showMessageDialog(view, "Đã gửi hóa đơn đến máy in.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Không thể in hóa đơn: " + ex.getMessage(), "Lỗi In", JOptionPane.ERROR_MESSAGE);
            }
        });

        view.setVisible(true);
    }

    /**
     * Generates a formatted string representation of the bill.
     * @param order The order object.
     * @param orderItems The list of items in the order.
     * @return A formatted string for the bill.
     */
    private String generateBillText(Order order, ArrayList<OrderItem> orderItems) {
        StringBuilder sb = new StringBuilder();

        sb.append("\t\tHÓA ĐƠN THANH TOÁN\n\n");
        sb.append("----------------------------------------------------------\n");
        sb.append(" Nhân viên: ").append(order.getEmployee().getName()).append("\n");
        sb.append(" Thời gian: ").append(dateFormat.format(new Date(order.getOrderDate().getTime()))).append("\n");
        sb.append("----------------------------------------------------------\n\n");

        sb.append(String.format(" %-25s %-8s %-12s %s\n", "Tên món", "SL", "Đơn giá", "Thành tiền"));
        sb.append("----------------------------------------------------------\n");
        for (OrderItem item : orderItems) {
            sb.append(String.format(" %-25s %-8d %-12s %s\n",
                    item.getFoodItem().getName(),
                    item.getQuantity(),
                    formatter.format(item.getFoodPrice()),
                    formatter.format(item.getAmount())
            ));
        }
        sb.append("----------------------------------------------------------\n\n");

        sb.append(String.format(" %-25s %25s VND\n", "Tổng tiền:", formatter.format(order.getTotalAmount())));
        sb.append(String.format(" %-25s %25s %%\n", "Giảm giá:", order.getDiscount()));
        sb.append(String.format(" %-25s %25s VND\n", "Phải thanh toán:", formatter.format(order.getFinalAmount())));
        sb.append(String.format(" %-25s %25s VND\n", "Đã thanh toán:", formatter.format(order.getPaidAmount())));
        
        long change = order.getPaidAmount() - order.getFinalAmount();
        sb.append(String.format(" %-25s %25s VND\n", "Tiền thừa:", formatter.format(change > 0 ? change : 0)));
        
        // This is the fix for the NullPointerException
        String payDateStr = (order.getPayDate() != null) ? dateFormat.format(new Date(order.getPayDate().getTime())) : "Chưa thanh toán";
        sb.append(String.format(" %-25s %25s\n", "Ngày thanh toán:", payDateStr));
        
        sb.append("\n\n\tCảm ơn quý khách! Hẹn gặp lại!\n");

        return sb.toString();
    }
}
