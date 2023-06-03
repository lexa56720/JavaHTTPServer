package Server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PageReader
{
    private static String BasePath="Resources/WebPages/";
    public static String GetStyles()
    {
        var path = Path.of(BasePath, "/css/style.css").toString();
        return ReadByPath(path);
    }

    public static String ReadPage(String path)
    {
        path = Path.of( BasePath, path).toString();
        return ReadByPath(path);
    }

    private static String ReadByPath(String path)
    {
        List<String> file = null;
        try
        {
            file = Files.readAllLines(Paths.get(path));
            return String.join("\n",file);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}
