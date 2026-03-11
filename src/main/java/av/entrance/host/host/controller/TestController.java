package av.entrance.host.host.controller;

import av.entrance.host.host.model.*;
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
                        .getReference(test.getClientId()).child("tests")
                        .push();

        ref.setValueAsync(test);

        return ResponseEntity.ok("Test created with key: " + ref.getKey());
    }

    @PostMapping("/deleteTest")
    public ResponseEntity<String> deleteTest(@RequestBody Test test) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(test.getClientId()).child("tests").child(test.getTestId());

        ref.removeValueAsync();

        return ResponseEntity.ok("Test deleted.");
    }

    @GetMapping("/get_tests")
    public List<Test> getAllTests(@RequestParam String clientId) throws InterruptedException {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(clientId).child("tests");

        List<Test> result = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot testSnap : snapshot.getChildren()) {
                    String testId = testSnap.getKey();
                    String testName = testSnap.child("testName").getValue(String.class);
                    int duration = testSnap.child("duration").getValue(Integer.class);

                    List<Question> questions = new ArrayList<>();
                    for (DataSnapshot qSnap : testSnap.child("questions").getChildren()) {
                        Question q = qSnap.getValue(Question.class);
                        if (q != null) questions.add(q);
                        else System.out.println("⚠️ Question null for key: " + qSnap.getKey());
                    }

                    List<Image> imageKeys = new ArrayList<>();
                    for (DataSnapshot imgSnap : testSnap.child("imageKeys").getChildren()) {
                        Image img = imgSnap.getValue(Image.class);
                        if (img != null) imageKeys.add(img);
                        else System.out.println("⚠️ Image null for key: " + imgSnap.getKey());
                    }

                    result.add(new Test(testId, testName, questions, imageKeys, duration));
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
    public ResponseEntity<String> submitResponse(@RequestParam String clientId, @RequestBody List<SubmitResponse> responses) {
        for (SubmitResponse response : responses) {
            DatabaseReference ref =
                    FirebaseDatabase.getInstance()
                            .getReference(clientId).child("responses").child(response.testId).child(response.date).child(response.userId)
                            .push();

            ref.setValueAsync(response.responses);
        }

        return ResponseEntity.ok("Responses submitted...");
    }

    @GetMapping("/get_results")
    public List<SubmitResponse> getTestResults(@RequestParam String testId, @RequestParam String clientId) throws InterruptedException {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(clientId).child("responses").child(testId);

        List<SubmitResponse> result = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dateTestSnap : snapshot.getChildren()) {
                    String testDate = dateTestSnap.getKey();

                    for (DataSnapshot userSnap : dateTestSnap.getChildren()) {
                        String username = userSnap.getKey();

                        List<Response> responses = new ArrayList<>();
                        for (DataSnapshot pushSnap : userSnap.getChildren()) {
                            for (DataSnapshot qSnap : pushSnap.getChildren()) {
                                Response res = qSnap.getValue(Response.class);
                                if (res != null) responses.add(res);
                                else System.out.println("⚠️ Question null for key: " + qSnap.getKey());
                            }
                        }
                        result.add(new SubmitResponse(testId, username, testDate, responses));
                    }

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
        System.out.println("Returning Responses: " + result.size());
        return result;
    }
}
