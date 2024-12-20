package se.gustav.databasProjekt;

import se.gustav.databasProjekt.DAO.impl.EmployeeDAOImpl;
import se.gustav.databasProjekt.DAO.impl.WorkRoleDAOImpl;
import se.gustav.databasProjekt.ui.InputHandler;
import se.gustav.databasProjekt.ui.MenuChoiceHandler;
import se.gustav.databasProjekt.ui.MenuHandler;

public class Main {
    public static void main(String[] args) {
        InputHandler inputHandler = new InputHandler();
        MenuChoiceHandler menuChoiceHandler = new MenuChoiceHandler(
                inputHandler,
                new WorkRoleDAOImpl(),
                new EmployeeDAOImpl()
        );
        MenuHandler menuHandler = new MenuHandler(menuChoiceHandler);
        inputHandler.setMenuHandler(menuHandler);

        inputHandler.run();
    }
}