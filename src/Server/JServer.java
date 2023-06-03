package Server;

import Utils.Event;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class JServer extends NanoHTTPD
{
    public Boolean IsRunning;

    public Event<IHTTPSession> NewEvent=new Event<>();
    private IResponseProvider ResponseProvider;

    public int GetPort()
    {
        return port;
    }
    private int port;
    public JServer(int port, IResponseProvider responseProvider)
    {
        super(port);
        ResponseProvider = responseProvider;
        this.port=port;
    }


    public void Start() throws IOException
    {
        IsRunning = true;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    public void Stop()
    {
        IsRunning = false;
        stop();
    }

    @Override
    public Response serve(IHTTPSession session)
    {
        NewEvent.NotifyAll(session,this);
        var response = ResponseProvider.GetResponse(session);
        var responseObject = newFixedLengthResponse(Response.Status.OK, GetType(session), response.GetResponse());

        var c=new CookieHandler(session.getHeaders());
        if (response.GetCookies()!=null && response.GetCookies().Length() > 0)
            responseObject.addHeader("Set-Cookie", response.GetCookies().ToString());
        return responseObject;
    }

    private String GetType(IHTTPSession session)
    {
        var acceptHeader = session.getHeaders().get("accept");
        if (acceptHeader.contains("text/html"))
            return "text/html";
        else if (acceptHeader.contains("text/css"))
            return "text/css";
        return "text/html";
    }

    public void Close()
    {
        if (IsRunning)
            Stop();
        closeAllConnections();
    }
}
