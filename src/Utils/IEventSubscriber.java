package Utils;

import java.io.IOException;

public interface IEventSubscriber<T>
{
    public void EventRaised(T data,Object Sender) throws IOException;
}
