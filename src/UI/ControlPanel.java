package UI;

import Utils.ConnectionData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel  extends JPanel implements ActionListener
{
    public Utils.Event<ConnectionData> ConnectEvent = new Utils.Event<>();
    private JButton ConnectButton;
    private JTextField PortArea;
    private void SetIsConnected(boolean value)
    {
        if (IsConnected != value)
        {
            PortArea.setEnabled(!value);

            if (value)
                ConnectButton.setText("Остановить");
            else
                ConnectButton.setText("Запустить");

            IsConnected = value;
        }
    }
    private boolean IsConnected;


    public ControlPanel()
    {
        super();
        setLayout(new GridBagLayout());
        LayoutSetup();
    }

    private void LayoutSetup()
    {
        var grid = GetGridBagConstraints();
        InternetSetup(grid);
    }

    private GridBagConstraints GetGridBagConstraints()
    {
        var grid = new GridBagConstraints();
        grid.gridwidth = 1;
        grid.gridheight = 2;
        grid.weightx = 0.3;
        grid.weighty = 1;
        grid.gridy = 0;
        grid.gridx = 0;
        grid.insets.top = 5;
        grid.insets.bottom = 5;
        grid.insets.right = 5;
        grid.insets.left = 5;
        grid.fill = GridBagConstraints.BOTH;
        return grid;
    }

    private void InternetSetup(GridBagConstraints grid)
    {
        grid.gridwidth = 2;
        ConnectButton = new JButton("Запустить");
        ConnectButton.setActionCommand("connection");
        ConnectButton.addActionListener(this);

        add(ConnectButton, grid);

        grid.gridy=0;
        grid.gridheight=1;
        grid.gridx = 2;
        grid.weightx = 1;
        grid.weighty = 0;
        var portLabel = new JTextArea("Порт");
        portLabel.setEditable(false);
        add(portLabel, grid);

        grid.gridy=1;
        PortArea = new JTextField("8080");
        add(PortArea, grid);
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "connection":
                SetIsConnected(!IsConnected);
                var port=Integer.parseInt(PortArea.getText());
                ConnectEvent.NotifyAll(new ConnectionData(port,IsConnected),this);
                break;
        }
    }
}
