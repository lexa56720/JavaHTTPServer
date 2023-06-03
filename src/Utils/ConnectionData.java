package Utils;

public class ConnectionData
{
    private int Port;

    private boolean Status;

    public ConnectionData(int port, boolean status)
    {
        Port = port;
        Status = status;
    }

    public int GetPort()
    {
        return Port;
    }

    public boolean GetStatus()
    {
        return Status;
    }

}
