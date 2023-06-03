

import Server.JServer;
import Server.PageNavigator;
import UI.MainFrame;
import Utils.ConnectionData;
import Utils.IEventSubscriber;

import java.io.Console;
import java.io.IOException;

public class Main
{
    private static JServer Server;

    private static MainFrame Frame;
    public static void main(String[] args)
    {
        Frame = new MainFrame();
        Frame.ConnectionStatusChanged.Subscribe((data, Sender) -> StartServer(data));
        Frame.WindowClosing.Subscribe((data, Sender) ->
        {
            if(Server!=null)
                Server.Close();
        });
    }

    private static void StartServer(ConnectionData data) throws IOException
    {
        if (Server == null)
            CreateServer(data.GetPort());

        if (data.GetPort() == Server.GetPort() && data.GetStatus() == true)
            Server.Start();
        else if (data.GetStatus() == true)
        {
            Server.NewEvent.UnSubscribe(Frame.GetLogger());
            Server.Close();

            CreateServer(data.GetPort());
            Server.Start();
        }
        else
            Server.Stop();
    }

    private static void CreateServer(int port)
    {
        Server = new JServer(port, new PageNavigator());
        Server.NewEvent.Subscribe(Frame.GetLogger());
    }

}