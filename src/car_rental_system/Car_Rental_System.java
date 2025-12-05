/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package car_rental_system;

/**
 *
 * @author NewVision
 */
import javax.swing.UIManager;
public class Car_Rental_System {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // عشان الشكل يبقى زي الويندوز أو الماك
        // عشان الشكل يبقى حلو زي الويندوز
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // دي اللي هتفتح شاشة اللوجن
        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });    }
    
}
