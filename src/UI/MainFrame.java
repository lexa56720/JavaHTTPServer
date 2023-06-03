package UI;

import Utils.ConnectionData;
import Utils.Event;
import Utils.IEventSubscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class MainFrame
{
    public Event<String> WindowClosing = new Event<>();

    public Event<ConnectionData> ConnectionStatusChanged = new Event<>();

    public Logger GetLogger()
    {
        return Logger;
    }

    private ControlPanel ControlPanel = new ControlPanel();
    private Logger Logger = new Logger();
    private JFrame Frame = new JFrame();

    public MainFrame()
    {
        Frame.setTitle("Server");
        Frame.setLayout(new GridBagLayout());
        Frame.setSize(500, 500);
        Frame.setVisible(true);
        Frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                WindowClosing.NotifyAll("WindowClosing", this);
                System.exit(0);
            }
        });

        SetupControlPanel();
        SetupMainPart();

        ConnectionStatusChanged.Subscribe((data, Sender) -> Logger.NewLogLine(GetConnectionLog(data)));
        ControlPanel.ConnectEvent.Subscribe((data, Sender) -> ConnectionStatusChanged.NotifyAll(data, Sender));
    }

    private void SetupControlPanel()
    {
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.gridheight = 1;

        Frame.add(ControlPanel, c);
    }

    private void SetupMainPart()
    {
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 1;

        Frame.add(Logger, c);
    }

    private String GetConnectionLog(ConnectionData data)
    {
        var result = new String();
        if (data.GetStatus())
            result += "SERVER START AT ";
        else
            result += "SERVER SHUTTING DOWN AT ";
        result += data.GetPort();
        return result;
    }

}
