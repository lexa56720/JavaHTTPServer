package Test;

import Server.PageReader;
import Server.ResponseData;
import fi.iki.elonen.NanoHTTPD;

import java.util.*;

public class TestPage implements IPageProvider
{

    private int VariantsCount = 5;

    private List<String> Words = new LinkedList<>();

    private Map<String, String> Questions = new HashMap<>();

    private static Random Random=new Random();


    private void UpdateData()
    {
        Words = Arrays.stream(QuestionReader.ReadWords()).toList();
        Questions = QuestionReader.ReadQuestions();
    }
    private String GetHtmlQuestion(int index)
    {
        var questionWord = Questions.keySet().toArray()[index];
        var question = new StringBuilder();
        question.append("<div><p1>"+questionWord+"</p1><br/>"+"\n");

        var rightIndex = Random.nextInt(VariantsCount);

        var banList = new String[VariantsCount];
        for (int i = 0; i < VariantsCount; i++)
        {
            String variant;
            if (i == rightIndex)
                variant = Questions.values().toArray(new String[0])[index];
            else
                variant = TakeUniqueWord(banList);
            banList[i] = variant;
            question.append("<input type=\"radio\" name=\""+questionWord+"\" value=\""+variant+"\" required=\"required\" /> "+variant+"\n");
        }
        question.append("</div>");
        return question.toString();
    }

    private String TakeUniqueWord(String[] banList)
    {
        var freeWords =  Words.stream().filter(s-> !Arrays.stream(banList).toList().contains(s)).toArray();
        return (String)freeWords[Random.nextInt(freeWords.length)];
    }

    private String GenerateTest()
    {
        UpdateData();
        var result = new StringBuilder();
        for (int i = 0; i < Questions.values().size(); i++)
            result.append(GetHtmlQuestion(i)+"\n");
        return result.toString();
    }
    @Override public ResponseData GetPage(NanoHTTPD.IHTTPSession request)
    {
        var page = PageReader.ReadPage("Test.html");
        return new ResponseData(page.replace("{QUESTIONS}", GenerateTest()), null);
    }
}
