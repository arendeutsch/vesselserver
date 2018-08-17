package aegirdynamics.com.joystickserver.NMEA;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import gnu.io.UnsupportedCommOperationException;
import net.sf.marineapi.nmea.event.SentenceEvent;
import net.sf.marineapi.nmea.event.SentenceListener;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.sentence.SentenceValidator;
import org.lwjgl.Sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

public class NMEAController implements SentenceListener {

    public NMEAController() {
        try {
            initalizeSerialPort();
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readingPaused() {
        System.out.println("-- Paused --");
    }

    @Override
    public void readingStarted() {
        System.out.println("-- Started --");
    }

    @Override
    public void readingStopped() {
        System.out.println("-- Stopped --");
    }

    @Override
    public void sentenceRead(SentenceEvent event) {
        // receive each sentence from the port
        System.out.println(event.getSentence());
    }

    private SerialPort getSerialPort() throws PortInUseException, UnsupportedCommOperationException {
        try {
            Enumeration<?> e = CommPortIdentifier.getPortIdentifiers();

            while (e.hasMoreElements()) {
                CommPortIdentifier id = (CommPortIdentifier) e.nextElement();
                if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    SerialPort sp = (SerialPort) id.open("GPSNMEA", 30);
                    sp.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                    try {
                        InputStream is = sp.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader buf = new BufferedReader(isr);

                        System.out.println("Scanning port " + sp.getName());

                        for (int i = 0; i < 5; i++) {
                            try {
                                String data = buf.readLine();
                                if(SentenceValidator.isValid(data)) {
                                    System.out.println("NMEA data found!");
                                    return sp;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        is.close();
                        isr.close();
                        buf.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            }
            System.out.println("NMEA data was not found...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void initalizeSerialPort() throws PortInUseException, UnsupportedCommOperationException, IOException {

        SerialPort sp = getSerialPort();
        if (sp != null) {
            InputStream is = sp.getInputStream();
            SentenceReader sr = new SentenceReader(is);
            sr.addSentenceListener(this);
            sr.start();

        }
    }
}
