package av.entrance.host.host.model;

import java.util.List;

public class Test {
    private String testId;
    private String testName;
    private List<Question> questions;

    public Test() {
    }

    public Test(String testId, String testName, List<Question> questions) {
        this.testId = testId;
        this.testName = testName;
        this.questions = questions;
    }

    public String getTestName() {
        return testName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }
}
