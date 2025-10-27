package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * A view for previewing and printing a bill.
 */
public class PrintOrderView extends JFrame {

    private JTextArea txtBill;
    private JButton btnPrint;
    private JButton btnClose;

    public PrintOrderView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Xem trước Hóa đơn");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null); // Center the frame
        
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // Text area for the bill
        txtBill = new JTextArea();
        txtBill.setEditable(false);
        // Use a monospaced font for better column alignment
        txtBill.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtBill);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPrint = new JButton("In Hóa Đơn");
        btnClose = new JButton("Đóng");
        buttonPanel.add(btnPrint);
        buttonPanel.add(btnClose);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Getters for components so the controller can access them
    public JTextArea getTxtBill() {
        return txtBill;
    }

    public JButton getBtnPrint() {
        return btnPrint;
    }

    public JButton getBtnClose() {
        return btnClose;
    }
}
