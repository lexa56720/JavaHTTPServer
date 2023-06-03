package UI;

import Utils.IEventSubscriber;
import fi.iki.elonen.NanoHTTPD;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger extends JPanel implements IEventSubscriber<NanoHTTPD.IHTTPSession>
{
    private JScrollPane pane;
    private JTextArea Log=new JTextArea();
    public Logger()
    {
        super();
        setLayout(new BorderLayout());
        pane=new JScrollPane(Log);
        pane.revalidate();
        add(pane,BorderLayout.CENTER);
    }

    @Override public void EventRaised(NanoHTTPD.IHTTPSession data,Object sender)
    {
        String line="["+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"] "
                +data.getRemoteIpAddress()+": "+data.getUri()+" "+data.getMethod().toString();
        NewLogLine(line);
    }

    public void NewLogLine(String line)
    {
        Log.append(line+"\n");
        pane.revalidate();
        pane.updateUI();
    }

}
