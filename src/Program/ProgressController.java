/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Program;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author zhouheng
 */
public class ProgressController implements ActionListener{
    private JProgressBar progressBar;
    private Timer timer;

    public ProgressController(JProgressBar progressBar) {
        this.progressBar = progressBar;
        progressBar.setValue(0);
        timer = new Timer(100, this);
        timer.start();
    }
    
    public void stop() {
        if (timer.isRunning()) {
            this.timer.stop();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //int value = progressBar.getValue();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int value = progressBar.getValue();
                if (value < 40) {
                    value += 2;
                    progressBar.setValue(value);
                } else if (value >= 40 && value < 70) {
                    value += 1;
                    progressBar.setValue(value);
                } else if (value >= 70) {
                    timer.stop();
                }
            }
        });
//        if (value < 40) {
//            value += 2;
//            progressBar.setValue(value);
//        } else if (value >= 40 && value < 70) {
//            value += 1;
//            progressBar.setValue(value);
//        } else if (value >= 70){
//            timer.stop();
//        }
    }
}
