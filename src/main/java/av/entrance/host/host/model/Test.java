package av.entrance.host.host.model;

import java.util.List;

public class Test {
    private String testName;
    private List<Question> questions;

    public Test() {
    }

    public Test(String testName, List<Question> questions) {
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
}
