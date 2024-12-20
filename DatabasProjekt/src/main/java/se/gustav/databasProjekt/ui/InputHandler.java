package se.gustav.databasProjekt.ui;

import java.util.Scanner;


public class InputHandler {

    // Konstanter för menyval
    public static final String CREATE_WORK_ROLE = "skapa ny arbetsroll";
    public static final String DELETE_WORK_ROLE = "ta bort arbetsroll";
    public static final String SHOW_ALL_WORK_ROLES = "visa alla arbetsroller";
    public static final String SHOW_WORK_ROLE = "visa en arbetsroll";
    public static final String UPDATE_WORK_ROLE = "uppdatera arbetsroll";
    public static final String LOGIN_EMPLOYEE = "logga in och se din roll";
    public static final String CREATE_EMPLOYEE = "skapa ny anställd";
    public static final String DELETE_EMPLOYEE = "radera anställd";
    public static final String SHOW_ALL_EMPLOYEES = "visa alla anställda";
    public static final String SHOW_EMPLOYEE = "visa en anställd";
    public static final String EXIT = "avsluta";

    // Instansvariabler
    private static final Scanner SCANNER = new Scanner(System.in);
    private MenuHandler menuHandler;
    private boolean running = true;

    public InputHandler() {

    }

    public void setMenuHandler(MenuHandler menuHandler) {
        this.menuHandler = menuHandler;
    }

    public void run() {
        while (running) {
            menuHandler.printOptions();
            int choice = getMenuChoice();
            menuHandler.handleMenuChoice(choice);
            running = menuHandler.isRunning();
        }
        closeScanner();
    }

    public String getUserInputForMenuChoice() {

        System.out.print("Ditt val: ");
        String input = SCANNER.nextLine().trim().toLowerCase().replaceAll("\\s+", " ");

        while (input.isEmpty()) {
            System.out.println("Du måste göra ett val.");
            System.out.print("Ditt val: ");
            input = SCANNER.nextLine().trim().toLowerCase().replaceAll("\\s+", " ");
        }
        return input;
    }

    public String getUserInputForCreating() {

        String input = SCANNER.nextLine().trim().toLowerCase().replaceAll("\\s+", " ");

        while (input.isEmpty()) {
            System.out.println("Du måste göra ett val.");
            System.out.print("Ditt val: ");
            input = SCANNER.nextLine().trim().toLowerCase().replaceAll("\\s+", " ");
        }
        return input;
    }

    public int getMenuChoice() {
        while (true) {
            String input = getUserInputForMenuChoice();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 11) {
                    return choice;
                }
                System.out.println("Ogiltigt val. Använd siffror 1-11 eller skriv kommandot.");
            } catch (NumberFormatException e) {
                if (input.equals(CREATE_WORK_ROLE)) return 1;
                if (input.equals(DELETE_WORK_ROLE)) return 2;
                if (input.equals(SHOW_ALL_WORK_ROLES)) return 3;
                if (input.equals(SHOW_WORK_ROLE)) return 4;
                if (input.equals(UPDATE_WORK_ROLE)) return 5;
                if (input.equals(LOGIN_EMPLOYEE)) return 6;
                if (input.equals(CREATE_EMPLOYEE)) return 7;
                if (input.equals(SHOW_ALL_EMPLOYEES)) return 8;
                if (input.equals(SHOW_EMPLOYEE)) return 9;
                if (input.equals(DELETE_EMPLOYEE)) return 10;
                if (input.equals(EXIT)) return 11;
                System.out.println("Ogiltigt val. Använd siffror 1-11 eller skriv kommandot.");
            }
        }
    }

    public void closeScanner() {
        if (SCANNER != null) {
            SCANNER.close();
        }
    }


}