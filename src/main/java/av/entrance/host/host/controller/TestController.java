package av.entrance.host.host.controller;

import av.entrance.host.host.model.Question;
import av.entrance.host.host.model.Response;
import av.entrance.host.host.model.SubmitResponse;
import av.entrance.host.host.model.Test;
import com.google.firebase.database.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @PostMapping("/create")
    public ResponseEntity<String> createTest(@RequestBody Test test) {

        DatabaseReference ref =
                FirebaseDatabase.getInstance()
                        .getReference("tests")
                        .push();

        ref.setValueAsync(test);

        return ResponseEntity.ok("Test created with key: " + ref.getKey());
    }

    @GetMapping("/get_tests")
    public List<Test> getAllTests() throws InterruptedException {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("tests");

        List<Test> result = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot testSnap : snapshot.getChildren()) {
                    String testId = testSnap.getKey();
                    String testName = testSnap.child("testName").getValue(String.class);

                    List<Question> questions = new ArrayList<>();
                    for (DataSnapshot qSnap : testSnap.child("questions").getChildren()) {
                        Question q = qSnap.getValue(Question.class);
                        if (q != null) questions.add(q);
                        else System.out.println("⚠️ Question null for key: " + qSnap.getKey());
                    }

                    result.add(new Test(testId, testName, questions));
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Firebase error: " + error.getMessage());
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);
        System.out.println("Returning tests: " + result.size());
        return result;
    }

    @PostMapping("/submitResponse")
    public ResponseEntity<String> submitResponse(@RequestBody SubmitResponse response) {
        DatabaseReference ref =
                FirebaseDatabase.getInstance()
                        .getReference("responses").child(response.date).child(response.testId).child(response.userId)
                        .push();

        ref.setValueAsync(response.responses);

        return ResponseEntity.ok("Responses submitted with key: " + ref.getKey());
    }

}
