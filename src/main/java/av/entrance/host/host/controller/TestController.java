package av.entrance.host.host.controller;

import av.entrance.host.host.model.Question;
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
    public List<Test> getAllTests() throws Exception {

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("tests");

        System.out.println("Fetching tests...");

        List<Test> result = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Snapshot exists: " + snapshot.exists());
                System.out.println("Children count: " + snapshot.getChildrenCount());

                for (DataSnapshot testSnap : snapshot.getChildren()) {

                    System.out.println("Test key: " + testSnap.getKey());

                    String testName =
                            testSnap.child("testName").getValue(String.class);

                    List<Question> questions = new ArrayList<>();
                    for (DataSnapshot qSnap :
                            testSnap.child("questions").getChildren()) {

                        Question q = qSnap.getValue(Question.class);
                        questions.add(q);
                    }

                    result.add(
                            new Test(testSnap.getKey(), testName, questions)
                    );
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
}
