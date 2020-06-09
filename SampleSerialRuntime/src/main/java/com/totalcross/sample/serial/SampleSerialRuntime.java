package com.totalcross.sample.serial;

import totalcross.ui.MainWindow;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.KeyListener;
import totalcross.ui.gfx.Color;
import totalcross.ui.Edit;
import totalcross.ui.ListBox;
import java.io.IOException;
import totalcross.io.LineReader;
import totalcross.io.Stream;
import totalcross.sys.Settings;
import totalcross.sys.SpecialKeys;
import java.io.OutputStream;
//import totalcross.io.device.PortConnector;

public class SampleSerialRuntime extends MainWindow {

    OutputStream Runexec;
    //PortConnector pc;
    ListBox Output;

    public SampleSerialRuntime() {
        setUIStyle(Settings.MATERIAL_UI);
    }

    @Override
    public void initUI() {

        Edit Input = new Edit();
        add(Input, LEFT, BOTTOM, FILL, PREFERRED);

        Output = new ListBox();
        Output.setBackForeColors(Color.BLACK, Color.WHITE);
        add(Output, LEFT, TOP, FILL, FIT);

        new Thread () {

            @Override
            public void run() {
                try {
                    Process Runexec2 = Runtime.getRuntime().exec("cat /dev/ttyUSB0 9600\n");
                    LineReader lineReader = new LineReader(Stream.asStream(Runexec2.getInputStream()));
                    //pc = new PortConnector(PortConnector.USB, 9600);
                    //LineReader lineReader = new LineReader(pc);
                    String input;
                    while (true) {
                        if ((input = lineReader.readLine()) != null) {
                            Output.add(input);
                            Output.selectLast();
                            Output.repaintNow();
                        }
                    }
                } catch (IOException ioe) {
                  //catch (totalcross.io.IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }.start();
        

        try{
            Runexec = Runtime.getRuntime().exec("sh").getOutputStream();
        }catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        Input.addKeyListener(new KeyListener() {

            @Override
            public void specialkeyPressed(KeyEvent e) {
                if (e.key == SpecialKeys.ENTER) {
                    String s = Input.getText();
                    Input.clear();
                    try {
                        Runexec.write(("echo \"" + s + "\" > /dev/ttyUSB0 9600\n").getBytes());
                        //pc.writeBytes(s);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void actionkeyPressed(KeyEvent e) {
            }
        });

    }
}
