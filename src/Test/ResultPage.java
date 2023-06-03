package Test;

import Server.CookieData;
import Server.PageReader;
import Server.ResponseData;
import fi.iki.elonen.NanoHTTPD;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResultPage implements IPageProvider
{
    private Map<String, String> Questions = new HashMap<>();


    private String GetResult(String question, String userAnswer)
    {
        if (Questions.get(question).equals(userAnswer))
            return "<p class=\"right\">" + question + " - Your answer: " + userAnswer + ", true</p>";
        else
            return "<p class=\"wrong\">" + question + " - Your answer: " + userAnswer + ", but true is " + Questions.get(question) + "</p>";
    }
    private String GenerateResults(Map<String, String> inputs)
    {
        Questions = QuestionReader.ReadQuestions();
        var result = new StringBuilder();
        for (var key : inputs.keySet())
            result.append(GetResult(key, inputs.get(key)) + "\n");
        return result.toString();
    }

    private float GetRightPercent(Map<String, String> inputs)
    {
        int rightCount = 0;
        var keys = inputs.keySet().toArray(new String[0]);
        var values = inputs.values().toArray(new String[0]);

        for (int i = 0; i < inputs.size(); i++)
            if (Questions.get(keys[i]).equals(values[i]))
                rightCount++;
        return (float) rightCount / inputs.size();
    }
    private Map<String, String> ContentParse(NanoHTTPD.IHTTPSession request)
    {
        var result = new HashMap<String, String>();
        String content = "";
        try
        {
            if (request.getMethod() == NanoHTTPD.Method.GET)
                content = URLDecoder.decode(request.getQueryParameterString(), "UTF-8");
            else
            {
                Map<String, String> files = new HashMap<>();
                request.parseBody(files);
                content = URLDecoder.decode(request.getQueryParameterString(), "UTF-8");
            }
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (NanoHTTPD.ResponseException e)
        {
            throw new RuntimeException(e);
        }

        var questionAnswers = content.split("&");
        for (var questionAnswer : questionAnswers)
        {
            var pair = questionAnswer.split("=");
            if (pair.length == 2)
                result.put(pair[0], pair[1]);
        }
        return result;
    }

    private int UpdateTries()
    {
        var path = "Resources/Test/TryCount.txt";

        if (new File(path).exists())
        {
            try
            {
                var fileContent = Files.readAllLines(Paths.get(path));
                if (fileContent.size() > 0)
                {
                    var tries = Integer.parseInt(fileContent.get(0));
                    Files.writeString(Paths.get(path), String.valueOf(tries + 1));
                    return tries;
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        }
        try
        {
            new File(path).createNewFile();
            Files.writeString(Paths.get(path), String.valueOf(1));
            return 0;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Override public ResponseData GetPage(NanoHTTPD.IHTTPSession request)
    {
        var content = ContentParse(request);
        var answerResults = GenerateResults(content);
        var accuracy = GetRightPercent(content);
        var page = PageReader.ReadPage("Result.html");

        var cookies = new CookieData(request.getCookies().read("info"));
        var userTries = 0;
        var lastDate = "";


        if (cookies.GetCookie("last_date") != null)
        {
            lastDate = cookies.GetCookie("last_date");
        }
        if (cookies.GetCookie("try_count") != null)
        {
            userTries = Integer.parseInt(cookies.GetCookie("try_count"));
        }


        page = page.replace("{RESULTS}", answerResults)
                .replace("{RATE}", String.valueOf(accuracy))
                .replace("{TRY_COUNT}", String.valueOf(UpdateTries()))
                .replace("{USER_TRY_COUNT}", String.valueOf(userTries))
                .replace("{date}", lastDate);


        int finalUserTries = userTries;
        var date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        var cookie = new HashMap<String, String>()
        {{
            put("try_count", String.valueOf(finalUserTries + 1));
            put("last_date", String.valueOf(date));
        }};
        return new ResponseData(page, new CookieData(cookie, "info"));
    }
}
