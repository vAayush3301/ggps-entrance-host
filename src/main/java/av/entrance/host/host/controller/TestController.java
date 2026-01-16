package av.entrance.host.host.controller;

import av.entrance.host.host.model.Question;
import av.entrance.host.host.model.Test;
import com.google.firebase.FirebaseApp;
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

        List<Test> tests = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot testSnap : snapshot.getChildren()) {

                    String testId = testSnap.getKey();
                    String testName = testSnap.child("testName")
                            .getValue(String.class);

                    List<Question> questions = new ArrayList<>();
                    for (DataSnapshot qSnap :
                            testSnap.child("questions").getChildren()) {

                        Question q = qSnap.getValue(Question.class);
                        questions.add(q);
                    }

                    tests.add(new Test(testId, testName, questions));
                }

                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        return tests;
    }
}
