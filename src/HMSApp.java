/**
 * The {@code HMSApp} class is the entry point for the Hospital Management System (HMS) application.
 * It initializes the user interface by creating an instance of {@code UserMenu} and displaying it.
 * 
 * <p>This class does not contain any complex logic or business rules, but it serves as a controller 
 * that runs the application by showing the main user menu.
 * 
 * <p>Dependencies:
 * <ul>
 *     <li>{@code Views.UserMenu}</li>
 * </ul>
 * 
 * @see Views.UserMenu
 */

 import Views.*;
public class HMSApp {
  /**
   * The main method which serves as the entry point of the application.
   * It initializes the user menu and displays it to the user.
   * 
   * @param args Command-line arguments, which are not used in this application
   */
  public static void main(String[] args) {
      UserMenu userMenu = new UserMenu();
      userMenu.showMenu();
  }
}
