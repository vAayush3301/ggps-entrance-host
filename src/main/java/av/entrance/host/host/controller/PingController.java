package av.entrance.host.host.controller;

import av.entrance.host.host.HostApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

@RestController
public class PingController {
    @GetMapping("/ping")
    public String ping() {
        long uptimeSeconds = Duration.between(HostApplication.START_TIME, Instant.now()).getSeconds();
        long minutes = uptimeSeconds / 60;
        long seconds = uptimeSeconds % 60;

        int activeThreads = Thread.activeCount();

        return """
        <html>
        <head>
          <title>Server Info</title>
          <link rel="icon" href="https://ggps-club.onrender.com/imgs/logo.png" type="image/png">
          <style>
            body { font-family: Arial, sans-serif; background: #0d1117; color: #c9d1d9; text-align: center; padding: 40px; }
            .card { background: #161b22; border-radius: 10px; display: inline-block; padding: 30px; box-shadow: 0 0 15px #0d1117; }
            h1 { color: #58a6ff; }
            .stat { font-size: 1.2rem; margin: 10px 0; }
            .icon { width: 80px; height: 80px; margin-bottom: 10px; }
          </style>
        </head>
        <body>
          <div class='card'>
            <img src='https://ggps-club.onrender.com/imgs/logo.png' class='icon'>
            <h1>Server Status</h1>
            <div class='stat'>🕒 Uptime: %d min %d sec</div>
            <div class='stat'>🔗 Active Threads: %d</div>
            <div class='stat'>✅ Status: <span style='color: #3fb950;'>Online</span></div>
            <br/>
            <div class='stat'>Developed by: <a href="https://github.com/vAayush3301" target="_blank" style="text-decoration:none; color="#FFF">Aayush Vishwakarma</a></div>
          </div>
        </body>
        </html>
        """.formatted(minutes, seconds, activeThreads);
    }
}
