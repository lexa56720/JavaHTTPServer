package Server;

import Test.IPageProvider;
import Test.ResultPage;
import Test.TestPage;
import fi.iki.elonen.NanoHTTPD;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class PageNavigator implements IResponseProvider
{

    private Map<String, IPageProvider> Pages = new HashMap<String, IPageProvider>()
    {{
        put("/test",new TestPage());
        put("/result",new ResultPage());
    }};


    public ResponseData GetResponse(NanoHTTPD.IHTTPSession session)
    {


        if (session.getUri().equals("/css/style.css"))
            return new ResponseData(PageReader.GetStyles(),null);

        if(Pages.get(session.getUri())!=null)
            return Pages.get(session.getUri()).GetPage(session);

        if (session.getUri().equals("/"))
            return new ResponseData(PageReader.ReadPage("Index.html"),null);


        return new ResponseData("",null);
    }
}
