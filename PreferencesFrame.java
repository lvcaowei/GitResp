package preferences;

import sun.util.resources.cldr.ar.CalendarData_ar_JO;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private Preferences root = Preferences.userRoot();
    private Preferences node = root.node("/preferences");

    public PreferencesFrame()
    {
        int left = node.getInt("left",0);
        int top = node.getInt("top",0);
        int width = node.getInt("width",DEFAULT_WIDTH);
        int height = node.getInt("height",DEFAULT_HEIGHT);
        setBounds(left, top, width, height);

        String title = node.get("title", "");
        if(title.equals(""))
            title = JOptionPane.showInputDialog("please supply a fram title");
        if(title == null) title = "";
        setTitle(title);

        final JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("File");
        menu.add(menu);

        JMenuItem exportItem = new JMenuItem("exoort preferences");
        menu.add(exportItem);
        exportItem.addActionListener(event->{
            if (chooser.showSaveDialog(PreferencesFrame.this)==JFileChooser.APPROVE_OPTION)
            {
                try {
                    savePreferences();
                    OutputStream out = new FileOutputStream(chooser.getSelectedFile());
                    node.exportSubtree(out);
                    out.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        JMenuItem importItem = new JMenuItem("Import preferences");
        menu.add(importItem);
        importItem.addActionListener(evect->{
            if (chooser.showOpenDialog(PreferencesFrame.this)==JFileChooser.APPROVE_OPTION)
            {
                try {
                    InputStream in = new FileInputStream(chooser.getSelectedFile());
                    Preferences.importPreferences(in);
                    in.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        JMenuItem exit = new JMenuItem("exit");
        menu.add(exit);
        exit.addActionListener(event->{
            savePreferences();
            System.exit(0);
        });

    }

    public void savePreferences(){
        node.putInt("left",getX());
        node.putInt("top",getY());
        node.putInt("width",getWidth());
        node.putInt("height",getHeight());
        node.put("title",getTitle());
    }



}
