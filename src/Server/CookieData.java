package Server;

import java.util.HashMap;
import java.util.Map;

public class CookieData
{
    private Map<String, String> Cookies = new HashMap<>();
    private String Name;


    public CookieData()
    {
    }
    public CookieData(Map<String, String> cookies, String name)
    {
        Name = name;
        if (cookies != null)
            Cookies = new HashMap<>(cookies);
    }
    public CookieData(String cookies)
    {
        if (cookies != null)
        {
            cookies = cookies.substring(1, cookies.length() - 1);
            var pairs = cookies.split("\\|");
            for (var pair : pairs)
            {
                Cookies.put(pair.split("->")[0], pair.split("->")[1]);
            }
        }
    }

    public String GetCookie(String name)
    {
        return Cookies.get(name);
    }

    public String ToString()
    {
        var builder = new StringBuilder();
        var keys = Cookies.keySet().toArray(new String[0]);
        var values = Cookies.values().toArray(new String[0]);

        builder.append(Name+"=");
        builder.append("\"");
        for (int i = 0; i < Cookies.size(); i++)
        {
            builder.append(keys[i] + "->" + values[i]);
            if (i < Cookies.size() - 1)
                builder.append("|");
        }
        builder.append("\"");
        return builder.toString();
    }

    public int Length()
    {
        return Cookies.size();
    }

}
