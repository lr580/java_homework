package ui;

import java.awt.*;
import javax.swing.*;
import plugin.*;

public class Root extends JFrame {
    public Root() {
        if (Init.isInitialized()) {
        } else {
        }
    }

    public static void main(String[] args) {
        new Root();
    }
}
