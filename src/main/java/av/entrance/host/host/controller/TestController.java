package av.entrance.host.host.controller;

import av.entrance.host.host.model.Test;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
                .getReference("tests");

        List<Test> list = new ArrayList<>();

        CountDownLatch latch = new CountDownLatch(1);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Test test = child.getValue(Test.class);
                    list.add(test);
                    System.out.println(list);
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);

        return list;
    }

    @GetMapping("/firebase-check")
    public String check() {
        return "Apps = " + FirebaseApp.getApps().size();
    }
}
