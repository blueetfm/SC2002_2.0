/**
 * The {@code Menu} interface defines the structure for menu-related operations.
 * It provides a method to display the menu options to the user.
 * Any class implementing this interface is expected to provide an implementation
 * for showing a menu.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Views;

import java.util.*;


public interface Menu {

    /**
     * Displays the menu options to the user.
     * The implementation of this method will define how the menu is presented,
     * including the options available for the user to select from.
     */
    public abstract void showMenu();
}
