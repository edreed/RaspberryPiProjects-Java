package LEDToggleSwitch;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Main {
    public static void main(String[] args) {

        System.out.print("Initializing state...");

        GpioController gpio = GpioFactory.getInstance();

        GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "LED");

        led.low();
        led.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        GpioPinDigitalInput button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, "Button", PinPullResistance.PULL_UP);

        button.setShutdownOptions(true);
        button.addListener(new GpioPinListenerDigital(){
        
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println("Pin 01 state is " + event.getState());
                if (event.getState() == PinState.HIGH) {
                    led.toggle();
                }
            }
        });

        System.out.println("[DONE]");
        System.out.println("Press control-C when done.");

        while(true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
