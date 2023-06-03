package Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class QuestionReader
{
    public static String[] ReadWords()
    {
        try
        {
            return Files.readAllLines(Paths.get("Resources/Test/Words.txt")).toArray(new String[0]);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    public static Map<String, String> ReadQuestions()
    {
        var result = new HashMap<String, String>();
        try
        {
            var questions = Files.readAllLines(Paths.get("Resources/Test/Questions.txt"));
            for (var question : questions)
            {
                var questionAndAnswer = question.split(" ");
                result.put(questionAndAnswer[0], questionAndAnswer[1]);
            }
            return result;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
