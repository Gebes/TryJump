package eu.gebes.tryjump.desktop.map;

import eu.gebes.tryjump.Variables;
import eu.gebes.tryjump.desktop.GUI;
import eu.gebes.tryjump.desktop.game.StartApplication;
import eu.gebes.tryjump.desktop.settings.SettingsPane;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class MapNodes {
    private final StartApplication startApplication = new StartApplication();
    private final SettingsPane settingsPaneClass = new SettingsPane();
    private final MapManagment mapManagment = new MapManagment();
    private String[] maps = mapManagment.load();
    private String createName = new String();

    public JList selector(){
        DefaultListModel listModel = new DefaultListModel();
        String[] both;
        String tmpName = null;
        for (int i=0;i<maps.length;i++){
            both = maps[i].split(":");
            tmpName = both[0];
            String tmp;
            if(both[1].equals("10000")){
                tmp = both[0];
            }else{
                tmp = both[0] + " Sec: "+both[1];
            }
            listModel.add(i,tmp);
        }

        final String name =tmpName;

        final JList mapList = new JList(listModel);
        mapList.setFont(new java.awt.Font("Tahoma", 1, 28));


        mapList.setLayoutOrientation(JList.VERTICAL);
        mapList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        mapList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String[] tmp = mapList.getSelectedValue().toString().split(" ");
                    Variables.mapName = tmp[0];
                    startApplication.startGame(settingsPaneClass.getWidth(), settingsPaneClass.getHeight(), settingsPaneClass.getFov(), settingsPaneClass.isFullscreenBool(), settingsPaneClass.getVolume());
                    GUI.getFrame().dispose();
                }
            }
        });
        return mapList;
    }

    public JButton button(){
        JButton button = new JButton("CREATE");
        button.setSize(300,100);
        button.setPreferredSize(new Dimension(300, 70));

        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                nameDialog();
                Variables.create= true;
                Variables.mapName = createName;
                startApplication.startGame(settingsPaneClass.getWidth(), settingsPaneClass.getHeight(), settingsPaneClass.getFov(), settingsPaneClass.isFullscreenBool(), settingsPaneClass.getVolume());
                GUI.getFrame().dispose();
            }
        });
        return button;
    }

    private void nameDialog(){
        final JDialog dialog = new JDialog();
        JPanel pane = new JPanel();
        dialog.setTitle("name");
        dialog.setSize(200,150);
        dialog.setModal(true);
        pane.add(new JLabel("MAP NAME:"));
        final JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(180,40));
        final JButton button = new JButton("OK");
        pane.add(textField);
        pane.add(button);
        dialog.add(pane);
        centreWindow(dialog);

        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                createName= textField.getText();
                dialog.dispose();
            }
        });
        dialog.setVisible(true);

    }

    private void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}
