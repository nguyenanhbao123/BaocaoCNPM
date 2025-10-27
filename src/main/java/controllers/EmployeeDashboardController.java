/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.employee.InformationController;
import controllers.admin.OrderManagerController;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import main.SessionManager;
import models.Employee;
import utils.IconManager;
import views.EmployeeDashboardView;
import views.LoginView;
import views.admin.AboutView;
import views.admin.HomeView;
import views.admin.ManagerPaneView;
import views.admin.MenuItem;
import views.admin.OrderManagerView;
import views.employee.InformationView;

public class EmployeeDashboardController {

    private EmployeeDashboardView view;
    ManagerController orderManagerController = new OrderManagerController();
    InformationController informationController = new InformationController();
    ManagerPaneView orderManagerView = new OrderManagerView();
    HomeView homeView = new HomeView();
    AboutView aboutView = new AboutView();
    InformationView informationView = new InformationView();

    SideBarController sideBarController = new SideBarController();
    JPanel[] cards = {homeView, orderManagerView, aboutView, informationView};

    public EmployeeDashboardController(EmployeeDashboardView view) {
        this.view = view;
        sideBarController.setPanelSideBar(view.getPanelSideBar());
        view.setVisible(true);
        initMenu();
        addEvent();
        Employee session = SessionManager.getSession().getEmployee();
        if (session != null) {
            view.getLbName().setText(session.getName());
        }
        view.setCards(cards);
        view.setPanel(homeView);
    }

    public EmployeeDashboardView getView() {
        return view;
    }

    public void setView(EmployeeDashboardView view) {
        this.view = view;
        sideBarController.setPanelSideBar(view.getPanelSideBar());
    }

    private void initMenu() {
        IconManager im = new IconManager();
        MenuItem menuQLDDH = new MenuItem("QLDDH", im.getIcon("purchase_order_25px.png"), "Quản lý đơn đặt hàng");
        MenuItem menuTL = new MenuItem("TL", im.getIcon("settings_25px.png"), "Thiết lập");
        menuTL.addSubMenu(new MenuItem("TTCN", im.getIcon("about_25px.png"), "Thông tin cá nhân"));
        menuTL.addSubMenu(new MenuItem("TLGD", im.getIcon("contrast_25px.png"), "Giao diện"));
        menuTL.addSubMenu(new MenuItem("TT", im.getIcon("help_25px.png"), "About us"));
        sideBarController.addMenu(menuQLDDH, menuTL);
        sideBarController.addMenuEvent(this::onMenuChange);
    }

    private void addEvent() {
        view.getBtnLogout().addActionListener(evt -> {
            int confirm = JOptionPane.showConfirmDialog(view, "Bạn thực sự muốn đăng xuất?");
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            try {
                SessionManager.update();// Đẵng xuất
            } catch (SQLException ex) {
                view.showError(ex);
            }
            view.dispose();
            new LoginController(new LoginView());
        });
    }

    public void onMenuChange(MenuItem item) {
        switch (item.getId()) {
            case "QLDDH"://Đơn đặt hàng
                view.setPanel(orderManagerView);
                orderManagerController.setView(orderManagerView);
                orderManagerController.updateData();
                break;
            case "TT":
                view.setPanel(aboutView);
                break;
            case "TTCN": // Thống tin cá nhân
                view.setPanel(informationView);
                informationController.setView(informationView);
                break;
            default:
                view.setPanel(homeView);
        }
    }
}
