package se.gustav.databasProjekt.ui;

public class MenuHandler {
    private boolean running = true;
    private final MenuChoiceHandler MENU_CHOICE_HANDLER;

    public MenuHandler(MenuChoiceHandler menuChoiceHandler) {
        this.MENU_CHOICE_HANDLER = menuChoiceHandler;
    }

    public boolean isRunning() {
        return running;
    }

    public void printOptions() {
        System.out.println("""
        Välj ett menyval: 
        1. Skapa ny arbetsroll
        2. Ta bort arbetsroll 
        3. Visa alla arbetsroller 
        4. Visa en arbetsroll
        5. Uppdatera arbetsroll
        6. Logga in och se din roll
        7. Skapa ny anställd
        8. Visa alla anställda 
        9. Visa en anställd 
        10. Radera anställd
        11. Avsluta
        """);
    }

    public void handleMenuChoice(int choice) {
        switch (choice) {
            case 1 -> MENU_CHOICE_HANDLER.handleCreateWorkRole();
            case 2 -> MENU_CHOICE_HANDLER.handleDeleteWorkRole();
            case 3 -> MENU_CHOICE_HANDLER.handleShowAllWorkRoles();
            case 4 -> MENU_CHOICE_HANDLER.handleShowWorkRole();
            case 5 -> MENU_CHOICE_HANDLER.handleUpdateWorkRole();
            case 6 -> MENU_CHOICE_HANDLER.handleLoginEmployee();
            case 7 -> MENU_CHOICE_HANDLER.handleCreateEmployee();
            case 8 -> MENU_CHOICE_HANDLER.handleShowAllEmployees();
            case 9 -> MENU_CHOICE_HANDLER.handleShowEmployee();
            case 10 -> MENU_CHOICE_HANDLER.handleDeleteEmployee();
            case 11 -> {
                System.out.println("Programmet avslutas!");
                running = false;
            }
        }
    }

}
