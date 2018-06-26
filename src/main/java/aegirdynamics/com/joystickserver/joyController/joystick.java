package aegirdynamics.com.joystickserver.joyController;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
public class joystick{

    static Controller controller;
    static private boolean pressed;

    public static void readCmd(){
        try {
            Controllers.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        Controllers.poll();

        controller = Controllers.getController(0);
        System.out.println("controller name: " + controller.getName());

        do {
            controller.poll();
            System.out.print("x: " + controller.getXAxisValue() + "   ");
            System.out.println("y: " + controller.getYAxisValue());
            pressed = controller.isButtonPressed(0);
        }while (!pressed);
    }

}
